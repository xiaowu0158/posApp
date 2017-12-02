package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.session.MeetingContainable;
import java.lang.reflect.Constructor;

public class ConsumerFactory{
  public static Consumer getConsumer(MeetingContainable paramMeetingContainable, String paramString) throws Exception{
	  /*String str1 = null;
    paramString = (paramString == null) || (paramString.length() == 0) ? "Consumer" : paramString;
    String str2 = paramString + ".csm";
    InputStream localInputStream = ResourceLocater.loadStream(str2);
    if ((localInputStream == null) && (!paramString.equals("Consumer"))) {
      localInputStream = ResourceLocater.loadStream("Consumer.csm");
    }
    if (localInputStream != null){
    	Properties  localObject1 = new Properties();
      try{
        ((Properties)localObject1).load(localInputStream);
        str1 = ((Properties)localObject1).getProperty("class");
      }catch (Throwable localThrowable1){
        try{
          localInputStream.close();
        }catch (Throwable localThrowable2) {}
      }finally{
        try{
          localInputStream.close();
        }catch (Throwable localThrowable3) {}
      }
    }
    if ((str1 == null) || (str1.length() == 0)) {
      str1 = "com.evangelsoft.econnect.client.SocketConsumer";
    }
    */
	 
    Object localObject1 = Class.forName("com.evangelsoft.econnect.client.SocketConsumer");
    Constructor localConstructor = ((Class)localObject1).getConstructor(new Class[] { MeetingContainable.class, String.class });
    return (Consumer)localConstructor.newInstance(new Object[] { paramMeetingContainable, paramString });
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.ConsumerFactory
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */