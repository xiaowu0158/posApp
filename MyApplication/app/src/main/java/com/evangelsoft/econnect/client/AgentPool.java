package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.intf.Registry;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.evangelsoft.econnect.session.Session;
import com.evangelsoft.econnect.util.Pool;
import com.evangelsoft.econnect.util.PooledObjectCleaner;
import com.evangelsoft.econnect.util.ResourceLocater;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

public class AgentPool{
  public static final String PM_HOST = "host";
  public static final String PM_PORT_NO = "port_no";
  public static final String PM_USER = "user";
  public static final String PM_CERTIFICATE = "certificate";
  public static final String PM_POOL = "pool";
  public static final String PM_LIFE_PERIOD = "life_period";
  public static final String PM_MAX_CONNECTIONS = "max_connections";
  private Consumer consumer;//this.C
  private Pool<ClientSession> sessionPool;//G;
  private Vector<ClientSession> sessionList= new Vector<ClientSession>();//this.E
  private int max_connections;//this.A;
  private String host = null;//this.I
  private int portNo = 0;//this.B
  private String user = null;//this.D
  private String certificate = null;//this.F
  private static ResourceBundle res = ResourceBundle.getBundle(AgentPool.class.getPackage().getName() + ".Res");//H.
  
  public AgentPool(String paramString)throws Exception{
    this(paramString, null);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public AgentPool(String paramString, Consumer paramConsumer)throws Exception{
	  Properties localProperties = new Properties();
	  String str1 = paramString + ".agt";
	  InputStream localInputStream = ResourceLocater.loadStream(str1);
	  if (localInputStream != null) {
		  try{
			  localProperties.load(localInputStream);
		  }catch (IOException localIOException){
			  System.out.println(localIOException.getMessage());
		  }
	  }
	  String str2 = localProperties.getProperty("host");
	  if ((str2 == null) || (str2.length() == 0)) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_MISSING_PARAMETER"), 
				  new Object[] { "host" }));
	  }
	  host = str2;
	  str2 = localProperties.getProperty("port_no");
	  if ((str2 == null) || (str2.length() == 0)) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_MISSING_PARAMETER"), 
				  new Object[] { "port_no" }));
	  }
	  try {
		  portNo = Integer.parseInt(str2);
	  }catch (NumberFormatException e){
		  throw new Exception(MessageFormat.format(res.getString("MSG_INVALID_PARAMETER"), 
				  new Object[] { str2, "port_no" }));
	  }
	  str2 = localProperties.getProperty("certificate");
	  if ((str2 == null) || (str2.length() == 0)) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_MISSING_PARAMETER"), 
				  new Object[] { "certificate" }));
	  }
	  certificate = str2;
	  user = localProperties.getProperty("user");
	  str2 = localProperties.getProperty("pool", "-1");
	  if ((str2 == null) || (str2.length() == 0)) {
		  throw new SQLException(MessageFormat.format(res.getString("MSG_MISSING_PARAMETER"), 
				  new Object[] { "pool" }));
	  }
	  int i;
	  try{
		  i = Integer.parseInt(str2);
	  }catch (NumberFormatException e){
		  throw new SQLException(MessageFormat.format(res.getString("MSG_INVALID_PARAMETER"), 
    			new Object[] { str2, "pool" }));
	  }
	  str2 = localProperties.getProperty("life_period", "-1");
	  if ((str2 == null) || (str2.length() == 0)) {
		  throw new SQLException(MessageFormat.format(res.getString("MSG_MISSING_PARAMETER"), new Object[] { "pool" }));
	  }
	  int j;
	  try{
		  j = Integer.parseInt(str2);
	  }catch (NumberFormatException e){
		  throw new SQLException(MessageFormat.format(res.getString("MSG_INVALID_PARAMETER"), new Object[] { str2, "life_period" }));
	  }
	  str2 = localProperties.getProperty("max_connections", "-1");
	  try{
		  max_connections = Integer.parseInt(str2);
	  }catch (NumberFormatException e){
		  throw new SQLException(MessageFormat.format(res.getString("MSG_INVALID_PARAMETER"), new Object[] { str2, "max_connections" }));
	  }
	  if (max_connections <= 0) {
		  max_connections = Integer.MAX_VALUE;
	  }
	  if (paramConsumer == null) {
		  paramConsumer = ConsumerFactory.getConsumer(null, paramString);
	  }
	  consumer = paramConsumer;
	  this.sessionPool = new Pool(i, j, new ClientSessionFactory(paramConsumer, host, portNo, user, certificate), 
			  new PooledObjectCleaner(){
		  			public void clean(Object paramAnonymousObject){
		  				try{
		  					Session localSession = (Session)paramAnonymousObject;
		  					if ((!localSession.getStatus().equals("CLOSING")) && (!localSession.getStatus().equals("CLOSED"))) {
		  						localSession.close(true);
		  					}
		  				}catch (Throwable e) {
		  				}
		  			}
	 });
	 paramConsumer.addListener(new ConsumerListener(){
    	public void processConsumerEvent(ClientSession paramAnonymousClientSession, int paramAnonymousInt){
    		if (paramAnonymousInt == 1){
    			sessionList.remove(paramAnonymousClientSession);
    			sessionPool.discard(paramAnonymousClientSession);
    		}
    	}
    });
  }
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public AgentPool(String paramString, Consumer paramConsumer,String localhost,String userCode)throws Exception{
	  
	  host = localhost;
	  portNo =4001;
	  certificate ="should be replaced by a long key";
	  user =userCode;
	  int pool=300;
	  int lifePeriod=10000;
	  max_connections=500;
	  if (paramConsumer == null) {
		  paramConsumer = ConsumerFactory.getConsumer(null, paramString);
	  }
	  consumer = paramConsumer;
	  this.sessionPool = new Pool(pool, lifePeriod, new ClientSessionFactory(paramConsumer, host, portNo, user, certificate), 
			  new PooledObjectCleaner(){
		  			public void clean(Object paramAnonymousObject){
		  				try{
		  					Session localSession = (Session)paramAnonymousObject;
		  					if ((!localSession.getStatus().equals("CLOSING")) && (!localSession.getStatus().equals("CLOSED"))) {
		  						localSession.close(true);
		  					}
		  				}catch (Throwable e) {
		  				}
		  			}
	 });
	 paramConsumer.addListener(new ConsumerListener(){
    	public void processConsumerEvent(ClientSession paramAnonymousClientSession, int paramAnonymousInt){
    		if (paramAnonymousInt == 1){
    			sessionList.remove(paramAnonymousClientSession);
    			sessionPool.discard(paramAnonymousClientSession);
    		}
    	}
    });
  }
  protected void finalize()throws Throwable{
	  this.sessionPool.close();
	  super.finalize();
  }
  
  public ClientSession getSession(HashMap<String, Object> paramHashMap, 
		  VariantHolder<HashMap<String, Object>> paramVariantHolder)throws Exception{
	  if (sessionList.size() >= max_connections) {
		  throw new SQLException(res.getString("MSG_SESSIONS_HAVE_BEEN_EXHAUSTED"));
	  }
	  ClientSession localClientSession = (ClientSession)this.sessionPool.pop();
	  VariantHolder<String> errMsg = new VariantHolder<String>();
	  if (paramHashMap != null) {
		  try{
			  Registry localRegistry = (Registry)new RMIProxy(localClientSession).newStub(Registry.class);
			  if (!localRegistry.bindUser(paramHashMap, paramVariantHolder, errMsg)) {
				  throw new Exception(errMsg.value);
			  }
			  localClientSession.signIn(paramHashMap, (HashMap)paramVariantHolder.value, errMsg);
			  Iterator localIterator = ((HashMap)paramVariantHolder.value).keySet().iterator();
			  while (localIterator.hasNext()){
				  String str = (String)localIterator.next();
				  localClientSession.getContext().registerTopic(str, ((HashMap)paramVariantHolder.value).get(str), 1);
				  consumer.registerEnv((String)localClientSession.getContext().getTopic("session_id", 1), str, 
						  ((HashMap)paramVariantHolder.value).get(str));
			  }
		  }catch (Throwable localThrowable){
			  localClientSession.close(true);
			  if ((localThrowable instanceof Exception)) {
				  throw ((Exception)localThrowable);
			  }
			  throw new Exception(localThrowable);
		  }
    }
    sessionList.add(localClientSession);
    return localClientSession;
  }
  
  public void returnSession(ClientSession paramClientSession){
	  sessionList.remove(paramClientSession);
	  if (paramClientSession.isSignIn()) {
		  try{
			  Registry localRegistry = (Registry)new RMIProxy(paramClientSession).newStub(Registry.class);
			  VariantHolder<String> errMsg = new VariantHolder<String>();
			  if (!localRegistry.unbindUser(null, errMsg)) {
				  throw new Exception(errMsg.value);
			  }
			  paramClientSession.signOut(null, errMsg);
			  this.sessionPool.push(paramClientSession);
		  }catch (Throwable e){
			  paramClientSession.close(true);
		  }
	  } else {
		  this.sessionPool.push(paramClientSession);
	  }
  }
  
  public int getActiveCount(){
	  return sessionList.size();
  }
  
  public int getPooledCount(){
	  return this.sessionPool.getSize();
  }
  
  public void clear(){
    this.sessionPool.clear();
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.AgentPool
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */