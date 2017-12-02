package com.example.wuhongjie.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity {

    private RelativeLayout cashier_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cashier_rl=(RelativeLayout) findViewById(R.id.cashier_rl);
        cashier_rl.setOnClickListener(new CashierClick());
    }
    private class CashierClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomepageActivity.this, CounterActivity.class);
            startActivityForResult(intent, 0);

        }
    }
}
