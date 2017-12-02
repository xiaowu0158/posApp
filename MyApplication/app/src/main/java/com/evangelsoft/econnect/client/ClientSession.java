package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.session.DataController;
import com.evangelsoft.econnect.session.MessageFrame;
import com.evangelsoft.econnect.session.Session;
import com.evangelsoft.econnect.session.SessionChannel;
import com.evangelsoft.econnect.session.SessionContainable;
import com.evangelsoft.econnect.util.ObjectDeserializer;
import com.evangelsoft.econnect.util.ObjectParserFactory;
import com.evangelsoft.econnect.util.StringUtilities;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ClientSession
  extends Session
{
  public ClientSession(SessionContainable paramSessionContainable, SessionChannel paramSessionChannel)
  {
    super(paramSessionContainable, paramSessionChannel);
  }
  
  protected void peerReady(MessageFrame paramMessageFrame)
    throws Exception
  {
    if ((paramMessageFrame.data == null) || (paramMessageFrame.data.length == 0)) {
      return;
    }
    HashMap localHashMap = null;
    if ((paramMessageFrame.data[0] == 123) && (paramMessageFrame.data[(paramMessageFrame.data.length - 1)] == 125))
    {
      localHashMap = new HashMap();
     String[] localObject1 = new String(paramMessageFrame.data, 1, paramMessageFrame.data.length - 2).split(";");
      for (String localObject2 : localObject1) {
        if (((String)localObject2).length() != 0)
        {
          int k = ((String)localObject2).indexOf('=');
          if (k > 0) {
            localHashMap.put(((String)localObject2).substring(0, k), ((String)localObject2).substring(k + 1));
          } else {
            localHashMap.put(localObject2, null);
          }
        }
      }
    }
    else
    {
    	ObjectDeserializer  localObject1 = ObjectParserFactory.getDeserializer(this.objectParserType);
      try
      {
        ((ObjectDeserializer)localObject1).prepare(paramMessageFrame.data);
     Object   localObject2 = ((ObjectDeserializer)localObject1).readObject();
        if ((localObject2 instanceof HashMap)) {
          localHashMap = (HashMap)localObject2;
        }
      }
      finally
      {
        ((ObjectDeserializer)localObject1).close();
      }
    }
    if (localHashMap != null)
    {
    	Iterator  localObject1 = localHashMap.keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
    	  String    localObject2 = (String)((Iterator)localObject1).next();
        String str;
        if (((String)localObject2).equals("object_parser_type"))
        {
          str = (String)localHashMap.get(localObject2);
          if ((str != null) && (str.length() > 0)) {
            this.objectParserType = str;
          }
        }
        else if (((String)localObject2).equals("session_id"))
        {
          this.sessionId = ((String)localHashMap.get(localObject2));
        }
        else if (((String)localObject2).equals("compress_arithmetic"))
        {
          getDataController().setCompressArithmetic((String)localHashMap.get(localObject2));
        }
        else if (((String)localObject2).equals("encrypt_arithmetic"))
        {
          str = (String)localHashMap.get(localObject2);
          String[] arrayOfString = str.split(":");
          if ((arrayOfString.length == 2) && (arrayOfString[0].equals("DES"))) {
            getDataController().setEncryptDes("DES", StringUtilities.decodeHexString(arrayOfString[1]));
          } else {
            getDataController().setEncryptDes("NONE", null);
          }
        }
      }
    }
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.ClientSession
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */