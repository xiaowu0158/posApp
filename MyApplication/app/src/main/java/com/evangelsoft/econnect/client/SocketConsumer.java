package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.session.MeetingContainable;
import com.evangelsoft.econnect.session.SessionChannel;
import com.evangelsoft.econnect.session.SessionReceivable;
import com.evangelsoft.econnect.session.SessionSendable;
import com.evangelsoft.econnect.session.SessionSocketChannel;
import com.evangelsoft.econnect.session.SessionSocketReceiver;
import com.evangelsoft.econnect.session.SessionSocketSender;
import java.util.Properties;

public class SocketConsumer
  extends Consumer
{
  public SocketConsumer(MeetingContainable paramMeetingContainable, String paramString)
  {
    super(paramMeetingContainable, paramString);
  }
  
  protected SessionChannel createChannel(Properties paramProperties)
    throws Exception
  {
    return new SessionSocketChannel(paramProperties);
  }
  
  protected SessionSendable createSender(int paramInt)
  {
    return new SessionSocketSender(paramInt);
  }
  
  protected SessionReceivable createReceiver(int paramInt)
  {
    return new SessionSocketReceiver(paramInt);
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.client.SocketConsumer
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */