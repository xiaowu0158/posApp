package com.example.wuhongjie.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.evangelsoft.econnect.dataformat.LocateOption;
import com.evangelsoft.econnect.dataformat.Record;
import com.example.wuhongjie.myapplication.adapter.AvlTppItemAdapter;
import com.example.wuhongjie.myapplication.util.RetailPromotionPolicyUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.youngor.modules.drp.counter.intf.RlbWeb;
import com.evangelsoft.crosslink.retail.document.intf.CashRegister;
import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.dataformat.RecordSetHelper;
import com.evangelsoft.econnect.dataformat.TransientRecordSet;
import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.example.wuhongjie.myapplication.adapter.RlbDtlItemAdapter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.id.content;


public class CounterActivity extends AppCompatActivity {
    private Handler mSearchHandler;
    private static int COUNT_INIT = 1901;
    private static int RLB_PROD_POST = 1902;
    private static int RLB_PROD_POST_INNER = 1802;
    private static int RLB_PROD_POST_CUST = 1702;
    private static int AVL_TPP_POST = 1903;
    public static final int REQUEST_VIP = 10;
    public static final int REQUEST_WAREH_STK = 11;
    private String deviceCashId = "";
    private ClientSession session = null;
    private BigDecimal shopId = null;
    private RecordSet shopRs;
    private RecordSet masterRs = new RecordSet();
    private RecordSet avlTppRs = new RecordSet();
    private RecordSet tppRs = new RecordSet();
    private RecordSet policyStructureRs = new RecordSet();
    private Record vipRc;
    private Record productRc;
    private KeyboardView keyboardView;
    private Keyboard k1;
    private EditText search_editer;
    private ListView rlbDtlListView;
    private RecordSet rlbDtlRs = new RecordSet();
    private RlbDtlItemAdapter rlbDtlListAdapter;
    private TextView totalQtyTextView;
    private TextView totalValTextView;
    private TextView totalDiscountTextView;
    private TextView totalFnlValTextView;
    private TextView custNameTextView;
    private TextView custBirthdayTextView;
    private TextView custPntValTextView;
    private Button search_btn;
    private StorageDataSet avlTppDataSet;
    private LinearLayout policyLinearLayout;
    private TextView policyTextView;
    private String policyMsg = "";
    private TextView ttlComDiscValTextView;
    private TextView ttlVipDiscValTextView;
    private TextView ttlSglDiscValTextView;
    private TextView counterTitleTextView;
    private PopupMenu popupMenu;
    //private boolean fromCounterPost=false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        //contentView=LayoutInflater.from(context).inflate(layoutRes, null, false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceName = Build.MODEL;
        deviceCashId = deviceName.replace(" ", "-").toUpperCase() + "-" + deviceId.toUpperCase();
        counterTitleTextView=(TextView) findViewById(R.id.counterTitleTextView);
        //setTitle(deviceId);
        counterTitleTextView.setText(deviceId);
        // setTitle("【S00743】萧山市心广场专卖店       【会计日期：2017-10-23】");
        initData();
        initView();

        checkCashReg(mSearchHandler);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // rlbDtlListAdapter.unregisterDataSetObserver(sumObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_avl_tpp) {
            System.out.println(avlTppRs.recordCount());
            AvlTppWindow avlTppWindow = new AvlTppWindow(CounterActivity.this, mSearchHandler, policyStructureRs, avlTppRs,
                    1600, 1000);

            avlTppWindow.showAsDropDown(keyboardView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIP) {
                Bundle bunde = data.getExtras();
                vipRc = (Record) bunde.get("Vip");
                String custName = "";
                try {
                    custName = new String(vipRc.getField("FULL_NAME").getBytes(), "GBK");
                    masterRs.getRecord(0).getField("CUST_ID").setNumber(vipRc.getField("CUST_ID").getNumber());
                    masterRs.getRecord(0).getField("CUST_OWNER_ID").setNumber(vipRc.getField("OWNER_ID").getNumber());
                    masterRs.post();
                    String point = vipRc.getField("PNT_AVL").getNumber().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    custNameTextView.setText(custName);
                    custPntValTextView.setText(point);
                    custBirthdayTextView.setText(sdf.format(vipRc.getField("BIRTHDAY").getDate()));
                    postCust(mSearchHandler);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



            }
            if (requestCode == REQUEST_WAREH_STK) {
                Bundle bunde = data.getExtras();
                productRc = (Record) bunde.get("WarehStk");
                search_editer.setText(productRc.getField("INTL_BC").getString());
                postRlbDtl(mSearchHandler, productRc.getField("INTL_BC").getString());


            }
        }
    }

    private void initData() {
        mSearchHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CounterActivity.this);
                    builder.setMessage(msg.obj.toString());
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CounterActivity.this.finish();
                        }
                    });
                    builder.create().show();

                }
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR_TOASE) {
                    Toast.makeText(CounterActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                if (msg.arg1 == COUNT_INIT) {
                    Date fsclDate = shopRs.getRecord(0).getField("FSCL_DATE").getDate();
                    String shopName = null;
                    try {
                        shopName = new String(shopRs.getRecord(0).getField("SHOP_NAME").getBytes(), "GBK");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    counterTitleTextView.setText("【" + shopRs.getRecord(0).getField("SHOP_NUM").getString() + "】" +
                            shopName + "   会计日期：" +
                            sdf.format(fsclDate));
                    //setTitle("【" + shopRs.getRecord(0).getField("SHOP_NUM").getString() + "】" +
                    //        shopName + "   会计日期：" +
                    //        sdf.format(fsclDate));
                    loadAvlTpp(mSearchHandler);
                }
                if (msg.arg1 == AVL_TPP_POST) {
                    Toast.makeText(CounterActivity.this, "促销加载完成",
                            Toast.LENGTH_SHORT).show();
                }
                if (msg.arg1 == RLB_PROD_POST) {
                    if(msg.arg2 == RLB_PROD_POST){
                        rlbDtlListAdapter = new RlbDtlItemAdapter(rlbDtlRs, avlTppRs, masterRs, tppRs, mSearchHandler,(YoungorApplication)getApplication(), CounterActivity.this);
                        //rlbDtlListAdapter.registerDataSetObserver(sumObserver);
                        rlbDtlListView.setAdapter(rlbDtlListAdapter);
                    }
                    if(msg.arg2 == RLB_PROD_POST_INNER){
                        rlbDtlRs=rlbDtlListAdapter.rlbDtlRs;
                        masterRs=rlbDtlListAdapter.masterRs;
                        tppRs=rlbDtlListAdapter.tppRs;
                       // rlbDtlListView.setAdapter(rlbDtlListAdapter);

                    }
                    if(msg.arg2 == RLB_PROD_POST_CUST){
                        rlbDtlListAdapter = new RlbDtlItemAdapter(rlbDtlRs, avlTppRs, masterRs, tppRs, mSearchHandler,(YoungorApplication)getApplication(), CounterActivity.this);
                        //rlbDtlListAdapter.registerDataSetObserver(sumObserver);
                        rlbDtlListView.setAdapter(rlbDtlListAdapter);

                    }

                    rlbDtlListAdapter.notifyDataSetChanged();
                    displayPolicy();
                    ttlComDiscValTextView.setText(masterRs.getRecord(0).getField("TTL_COM_DISC_VAL").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    ttlVipDiscValTextView.setText(masterRs.getRecord(0).getField("TTL_VIP_DISC_VAL").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    ttlSglDiscValTextView.setText(masterRs.getRecord(0).getField("TTL_SGL_DISC_VAL").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    //总数量
                    totalQtyTextView.setText(masterRs.getRecord(0).getField("TTL_QTY").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    //应收金额
                    totalFnlValTextView.setText(masterRs.getRecord(0).getField("TTL_VAL").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    //折前金额
                    totalValTextView.setText(masterRs.getRecord(0).getField("TTL_LST_VAL").getNumber().
                            setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    //整单折让
                    totalDiscountTextView.setText(masterRs.getRecord(0).getField("TTL_WHL_DISC_VAL").getNumber().
                            setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    if (policyMsg != null && policyMsg.length() > 0) {
                        policyLinearLayout.setVisibility(View.VISIBLE);
                        policyTextView.setText(policyMsg);
                    } else {
                        policyTextView.setText("");
                        policyLinearLayout.setVisibility(View.GONE);
                    }
                }
            }
        };
    }

    //判断收银机是否绑定
    private void checkCashReg(final Handler handler) {
        new Thread() {
            public void run() {
                Message msg = null;
                try {
                    session = ((YoungorApplication) getApplication()).getDxSession();
                    if (!((YoungorApplication) getApplication()).sessionLinked) {
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;
                        msg.obj = DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                        return;
                    }
                    CashRegister cashRegister = (CashRegister) new RMIProxy(session)
                            .newStub(CashRegister.class);
                    VariantHolder data = new VariantHolder();
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    data.value = new TransientRecordSet();
                    if (!cashRegister.get(deviceCashId, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    shopId = ((RecordSet) data.value).getRecord(0).getField("SHOP_ID").getNumber();
                    shopRs = (RecordSet) data.value;
                    msg = new Message();
                    msg.arg1 = COUNT_INIT;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    msg = new Message();
                    msg.arg1 = DxSessionHelper.SESSION_ERROR;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void loadAvlTpp(final Handler handler) {
        new Thread() {
            public void run() {
                Message msg = null;
                try {
                    session = ((YoungorApplication) getApplication()).getDxSession();
                    if (!((YoungorApplication) getApplication()).sessionLinked) {
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;
                        msg.obj = DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                        return;
                    }
                    RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                            .newStub(RlbWeb.class);
                    VariantHolder data = new VariantHolder();
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    data.value = new TransientRecordSet();
                    Object[] key = new Object[]{shopId, shopRs.getRecord(0).getField("FSCL_DATE").getDate()};
                    if (!rlbWeb.getPolicy(key, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    avlTppRs = (RecordSet) data.value;
                    data.value = new TransientRecordSet();
                    if (!rlbWeb.getPolicyStructure(data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    policyStructureRs = (RecordSet) data.value;
                    data.value = new TransientRecordSet[]{new TransientRecordSet(), new TransientRecordSet(), new TransientRecordSet()};
                    if (!rlbWeb.getRlbStructure(data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    masterRs = ((RecordSet[]) data.value)[0];
                    Record masterRc = masterRs.append();
                    masterRc.getField("SHOP_ID").setNumber(shopId);
                    Date d = new Date();
                    masterRc.getField("DOC_DATE").setDate(d);
                    masterRc.post();
                    msg = new Message();
                    msg.arg1 = AVL_TPP_POST;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    msg = new Message();
                    msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }

    private void postRlbDtl(final Handler handler, final String prodCode) {
        new Thread() {
            public void run() {
                Message msg = null;
                try {
                    session = ((YoungorApplication) getApplication()).getDxSession();
                    if (!((YoungorApplication) getApplication()).sessionLinked) {
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;
                        msg.obj = DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                        return;
                    }
                    RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                            .newStub(RlbWeb.class);
                    VariantHolder data = new VariantHolder();
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    Object[] key = new Object[]{shopId, prodCode};
                    data.value = new TransientRecordSet();
                    if (!rlbWeb.getProduct(key, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    RecordSet rs = (RecordSet) data.value;
                    if (rs.recordCount() <= 0) {
                        throw new Exception("商品【" + prodCode + "】无库存！");
                    }
                    int loc = -1;
                    if (rlbDtlRs.recordCount() <= 0) {
                        rlbDtlRs = rs;
                        loc = rlbDtlRs.recordCount() - 1;
                    } else {

                        loc = rlbDtlRs.locate(0, "PROD_CODE", rs.getRecord(0).getField("PROD_CODE").getString(),
                                LocateOption.CASE_INSENSITIVE);
                        if (loc < 0) {
                            BigDecimal lineNum = rlbDtlRs.getRecord(rlbDtlRs.recordCount() - 1).getField("LINE_NUM").getNumber().add(BigDecimal.ONE);
                            for (int i = 0; i < rs.recordCount(); i++) {
                                Record rc = rs.getRecord(i);
                                rc.getField("LINE_NUM").setNumber(lineNum.add(new BigDecimal(i)));
                                rc.post();
                            }
                            RecordSetHelper.appendFromRecordSet(rlbDtlRs, rs);
                            loc = rlbDtlRs.recordCount() - 1;
                        } else {
                            Record rc = rlbDtlRs.getRecord(loc);
                            rc.edit();
                            rc.getField("QTY").setNumber(rc.getField("QTY").getNumber().add(BigDecimal.ONE));
                            rc.post();
                        }
                    }
                    Object[] inputkey = new Object[]{rlbDtlRs, avlTppRs, masterRs, tppRs, loc};
                    data.value = new TransientRecordSet[]{new TransientRecordSet(), new TransientRecordSet(), new TransientRecordSet()};
                    if (!rlbWeb.executePolicy(inputkey, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    masterRs = ((RecordSet[]) data.value)[0];
                    rlbDtlRs = ((RecordSet[]) data.value)[1];
                    tppRs = ((RecordSet[]) data.value)[2];
                   // displayPolicy();
                    msg = new Message();
                    msg.arg1 = RLB_PROD_POST;
                    msg.arg2=RLB_PROD_POST;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = new Message();
                    msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
    private void postCust(final Handler handler){
        new Thread() {
            public void run(){
                Message msg = null;
                try{
                    session = ((YoungorApplication) getApplication()).getDxSession();
                    if (!((YoungorApplication) getApplication()).sessionLinked) {
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;
                        msg.obj = DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                        return;
                    }
                    RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                            .newStub(RlbWeb.class);
                    VariantHolder data = new VariantHolder();
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    Object[] inputkey = new Object[]{rlbDtlRs, avlTppRs, masterRs, tppRs};
                    data.value = new TransientRecordSet[]{new TransientRecordSet(), new TransientRecordSet(), new TransientRecordSet()};
                    if (!rlbWeb.executePolicyForCust(inputkey, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    masterRs = ((RecordSet[]) data.value)[0];
                    rlbDtlRs = ((RecordSet[]) data.value)[1];
                    tppRs = ((RecordSet[]) data.value)[2];
                    // displayPolicy();
                    msg = new Message();
                    msg.arg1 = RLB_PROD_POST;
                    msg.arg2=RLB_PROD_POST_CUST;
                    handler.sendMessage(msg);

                }catch (Exception e) {
                    e.printStackTrace();
                    msg = new Message();
                    msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    /*private void changeRlbDtl(final Handler handler){
        {
            new Thread() {
                public void run() {
                    Message msg = null;
                    try {
                        session = ((YoungorApplication) getApplication()).getDxSession();
                        if (!((YoungorApplication) getApplication()).sessionLinked) {
                            msg = new Message();
                            msg.arg1 = DxSessionHelper.SESSION_ERROR;
                            msg.obj = DxSessionHelper.errMsg;
                            handler.sendMessage(msg);
                            return;
                        }
                        RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                                .newStub(RlbWeb.class);
                        VariantHolder data = new VariantHolder();
                        VariantHolder<String> errMsg = new VariantHolder<String>();

                        Object[] inputkey=new Object[]{rlbDtlRs,avlTppRs,masterRs,tppRs,loc};
                        data.value = new TransientRecordSet[]{new TransientRecordSet(),new TransientRecordSet(),new TransientRecordSet()};
                        if(!rlbWeb.executePolicy(inputkey,data,errMsg)){
                            throw new Exception(errMsg.value);
                        }
                        masterRs=((RecordSet[]) data.value)[0];
                        rlbDtlRs=((RecordSet[]) data.value)[1];
                        tppRs=((RecordSet[]) data.value)[2];
                        displayPolicy();
                        msg = new Message();
                        msg.arg1 = RLB_PROD_POST;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                        msg.obj = e.getMessage();
                        handler.sendMessage(msg);
                    }
                }
            }.start();

        }

    }*/

    private void initView() {
        Column avlTppDataSetRetailerId = new Column();
        avlTppDataSetRetailerId.setModel("TPP.RETAILER_ID");
        Column avlTppDataSetTppNum = new Column();
        avlTppDataSetTppNum.setModel("TPP.TPP_NUM");
        avlTppDataSet = new StorageDataSet();
        avlTppDataSet.addColumn(avlTppDataSetRetailerId);
        //avlTppDataSet.setColumns(new Column[] { avlTppDataSetRetailerId});
        //  avlTppDataSet.open();
        totalQtyTextView = (TextView) findViewById(R.id.totalQtyTextView);
        totalValTextView = (TextView) findViewById(R.id.totalValTextView);
        totalDiscountTextView = (TextView) findViewById(R.id.totalDiscountTextView);
        totalFnlValTextView = (TextView) findViewById(R.id.totalFnlValTextView);
        ttlComDiscValTextView = (TextView) findViewById(R.id.ttlComDiscValTextView);
        ttlVipDiscValTextView = (TextView) findViewById(R.id.ttlVipDiscValTextView);
        ttlSglDiscValTextView = (TextView) findViewById(R.id.ttlSglDiscValTextView);

        policyLinearLayout = (LinearLayout) findViewById(R.id.policyLinearLayout);
        policyTextView = (TextView) findViewById(R.id.policyTextView);
        search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new ProdCodeSearchButtonClick());
        custNameTextView = (TextView) findViewById(R.id.custNameTextView);
        custBirthdayTextView = (TextView) findViewById(R.id.custBirthdayTextView);
        custPntValTextView = (TextView) findViewById(R.id.custPntValTextView);
        rlbDtlListView = (ListView) findViewById(R.id.rlbDtlListView);
        rlbDtlListAdapter = new RlbDtlItemAdapter(rlbDtlRs, avlTppRs, masterRs, tppRs,mSearchHandler,(YoungorApplication)getApplication(), CounterActivity.this);
        //rlbDtlListAdapter.registerDataSetObserver(sumObserver);
        rlbDtlListView.setAdapter(rlbDtlListAdapter);
        search_editer = (EditText) findViewById(R.id.search_editer);
        search_editer.setInputType(InputType.TYPE_NULL);
        k1 = new Keyboard(CounterActivity.this, R.xml.symbols);
        keyboardView = (KeyboardView) findViewById(R.id.countkeyboardView);

        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyListener);
        keyboardView.setVisibility(View.VISIBLE);

        popupMenu = new PopupMenu(this, findViewById(R.id.btn_more));
        menu = popupMenu.getMenu();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_counter, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenuItemSelection());
    }
    private class PopupMenuItemSelection implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_avl_tpp) {
                System.out.println(avlTppRs.recordCount());
                AvlTppWindow avlTppWindow = new AvlTppWindow(CounterActivity.this, mSearchHandler, policyStructureRs, avlTppRs,
                        1600, 1000);

                avlTppWindow.showAsDropDown(keyboardView);
            }
            return true;
        }
    }
    private void prepareData() {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Counter Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class ProdCodeSearchButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            intent.setClass(CounterActivity.this, WarehstkSearchActivity.class);
            intent.putExtra("shop", shopRs);
            startActivityForResult(intent, REQUEST_WAREH_STK);
        }
    }

    private KeyboardView.OnKeyboardActionListener keyListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = search_editer.getText();
            int start = search_editer.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_CANCEL) { // 清空
                editable.clear();
            } else if (primaryCode == Keyboard.KEYCODE_DONE) { // 提交
                postRlbDtl(mSearchHandler, editable.toString());
                editable.clear();
            } else if (primaryCode == -2) { // 顾客
                Intent intent = new Intent();
                intent.setClass(CounterActivity.this, VipSearchActivity.class);
                startActivityForResult(intent, REQUEST_VIP);
            } else if (primaryCode == -7) { // 结算
                Intent intent = new Intent();
                intent.setClass(CounterActivity.this, CashPayActivity.class);
                startActivityForResult(intent, 0);
            } else if (primaryCode == -6) { // 定额
                //CouponWindow couponWindow = new CouponWindow(CounterActivity.this,mSearchHandler,
                //        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                CouponWindow couponWindow = new CouponWindow(CounterActivity.this, mSearchHandler,
                        1000, 900);

                couponWindow.showAsDropDown(keyboardView);
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private void displayPolicy() {
        this.policyMsg = "";
        if (tppRs.recordCount() <= 0) {
            return;
        }
        String msg = "";
        for (int i = 0; i < tppRs.recordCount(); i++) {
            if (msg.length() > 0) {
                msg = msg + System.getProperty("line.separator");
            }

            msg = msg + RetailPromotionPolicyUtil.describeStatus(
                    tppRs.getRecord(i).getField("TPP_TYPE").getString(), tppRs.getRecord(i), 20);
        }
        this.policyMsg = msg;
    }
}