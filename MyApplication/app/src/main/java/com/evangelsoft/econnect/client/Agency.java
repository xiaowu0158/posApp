package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.util.LaunchPath;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Agency{
	private static Hashtable<String, AgentPool> agentPoolMap= new Hashtable<String, AgentPool>();
	private static ResourceBundle res = ResourceBundle.getBundle(Agency.class.getPackage().getName() + ".Res");
	/*static{
		ArrayList<String> agtList = new ArrayList<String>();
		String[] agtFileNameList = LaunchPath.listFileNames(".agt");
		String str;
		for (String agtFileName : agtFileNameList) {
			agtList.add(agtFileName.substring(0, agtFileName.length() - 4));
		}
		Iterator<String> localIterator = agtList.iterator();
		while (localIterator.hasNext()){
			str = (String)localIterator.next();
			try{
				agentPoolMap.put(str, new AgentPool(str));
			}catch (Throwable localThrowable){
				System.out.println("Failed to launch agent: " + str);
			}
		}
	}*/
	static{
		ArrayList<String> agtList = new ArrayList<String>();
		agtList.add("WEB");
		try{
			agentPoolMap.put("WEB", new AgentPool("WEB",null,"172.16.199.65","WEB@127-0-0-1"));
		}catch (Throwable localThrowable){
			System.out.println("Failed to launch agent: WEB" );
		}

	}
  
  public Agency() {
  }
  
  public static void clear(String paramString){
	  AgentPool agentPool = (AgentPool)agentPoolMap.get(paramString);
	  if (agentPool != null) {
		  synchronized (agentPool){
			  agentPool.clear();
		  }
	  }
  }
  
  public static void clear(){
    synchronized (agentPoolMap){
    	Iterator<String> localIterator = agentPoolMap.keySet().iterator();
    	while (localIterator.hasNext()){
    		String str = (String)localIterator.next();
    		clear(str);
    	}
    }
  }
  
  public static Hashtable<String, AgentPool> getPools(){
    return agentPoolMap;
  }
  
  public static AgentPool getPool(String paramString){
    return (AgentPool)agentPoolMap.get(paramString);
  }
  
  public static ClientSession getSession(String agtName, HashMap<String, Object> params, 
		  VariantHolder<HashMap<String, Object>> receipt)throws Exception{
	  AgentPool agentPool = (AgentPool)agentPoolMap.get(agtName);
	  if (agentPool == null) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_UNKNOWN_OR_INVALID_AGENT"), 
				  new Object[] { agtName }));
	  }
	  return agentPool.getSession(params, receipt);
  }
  
  public static ClientSession getSession(String paramString)throws Exception{
	  AgentPool localAgentPool = agentPoolMap.get(paramString);
	  if (localAgentPool == null) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_UNKNOWN_OR_INVALID_AGENT"), 
				  new Object[] { paramString }));
	  }
	  return localAgentPool.getSession(null, null);
  }
  
  public static void returnSession(String sessionName, ClientSession clientSession)throws Exception{
	  AgentPool agentPool = agentPoolMap.get(sessionName);
	  if (agentPool == null) {
		  throw new Exception(MessageFormat.format(res.getString("MSG_UNKNOWN_OR_INVALID_AGENT"), 
				  new Object[] { sessionName }));
	  }
	  agentPool.returnSession(clientSession);
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.Agency
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */