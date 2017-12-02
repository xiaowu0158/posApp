package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.intf.Registry;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.evangelsoft.econnect.session.Context;
import com.evangelsoft.econnect.session.SessionChannel;
import com.evangelsoft.econnect.util.ObjectFactory;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

public class ClientSessionFactory
  implements ObjectFactory<ClientSession>
{
  private Consumer L = null;
  private String M = null;
  private int K = 0;
  private String J = null;
  private String I = null;
  
  public ClientSessionFactory(Consumer paramConsumer, String paramString1, int paramInt, String paramString2, String paramString3)
  {
    this.L = paramConsumer;
    this.M = paramString1;
    this.K = paramInt;
    this.J = paramString2;
    this.I = paramString3;
  }
  
  @SuppressWarnings("unchecked")
public ClientSession getObjectInstance()
    throws Exception
  {
    String str = UUID.randomUUID().toString();
    Properties localProperties = new Properties();
    localProperties.setProperty("host", this.M);
    localProperties.setProperty("port", Integer.toString(this.K));
    localProperties.setProperty("application", ClientSessionFactory.class.getSimpleName());
    localProperties.setProperty("family", "interior");
    ClientSession localClientSession = this.L.connect(str, localProperties);
    localClientSession.getContext().registerTopic("session_id", str, 1);
    HashMap localHashMap = new HashMap();
    if ((this.J != null) && (this.J.length() > 0)) {
      localHashMap.put("user", this.J);
    } else {
      localHashMap.put("user", this.L.getName() + "@" + localClientSession.getChannel().getProperty("localAddress").toString().replace('.', '-'));
    }
    localHashMap.put("certificate", this.I);
    localHashMap.put("isServiceLink", Boolean.TRUE);
    VariantHolder localVariantHolder1 = new VariantHolder();
    VariantHolder localVariantHolder2 = new VariantHolder();
    Registry localRegistry = (Registry)new RMIProxy(localClientSession).newStub(Registry.class);
    try
    {
      if (!localRegistry.signIn(localHashMap, localVariantHolder1, localVariantHolder2)) {
        throw new Exception((String)localVariantHolder2.value);
      }
    }
    catch (Throwable localThrowable)
    {
      localClientSession.close(true);
      localClientSession = null;
      if ((localThrowable instanceof Exception)) {
        throw ((Exception)localThrowable);
      }
      throw new Exception(localThrowable);
    }
    return localClientSession;
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.ClientSessionFactory
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */