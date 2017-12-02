package com.evangelsoft.econnect.client;

import com.evangelsoft.econnect.session.AsynchroSessionHook;
import com.evangelsoft.econnect.session.JobContainable;
import com.evangelsoft.econnect.session.MeetingContainable;
import com.evangelsoft.econnect.session.SessionChannel;
import com.evangelsoft.econnect.session.SessionClerk;
import com.evangelsoft.econnect.session.SessionContainable;
import com.evangelsoft.econnect.session.SessionHandler;
import com.evangelsoft.econnect.session.SessionReceivable;
import com.evangelsoft.econnect.session.SessionSendable;
import com.evangelsoft.econnect.util.ExceptionFormat;
import com.evangelsoft.econnect.util.LaunchDirectory;
import com.evangelsoft.econnect.util.ObjectFactory;
import com.evangelsoft.econnect.util.Pool;
import com.evangelsoft.econnect.util.PooledObjectCleaner;
import com.evangelsoft.econnect.util.ResourceLocater;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public abstract class Consumer
  implements SessionContainable
{
  public static final String PM_CLASS = "class";
  public static final String PM_SENDER_BUFFER_SIZE = "sender_buffer_size";
  public static final String PM_SENDERS = "senders";
  public static final String PM_RECEIVER_BUFFER_SIZE = "receiver_buffer_size";
  public static final String PM_RECEIVERS = "receivers";
  public static final String PM_TRANSIENT_ZONE_CHECKING_INTERVAL = "transient_zone_checking_interval";
  public static final String PM_KEEP_ALIVE_INTERVAL = "keep_alive_interval";
  private SessionSendable[] a;
  private int i;
  private SessionReceivable[] g;
  private int h;
  private int checingIntVal;
  private int c = 0;
  private static String b = "@";
  private static Consumer e;
  private MeetingContainable T;
  private String j;
  private Hashtable<String, JobContainable> U = new Hashtable();
  private Hashtable<String, HashMap<String, Object>> V = new Hashtable();
  private Pool<SessionHandler> d;
  private Pool<AsynchroSessionHook> W;
  private Pool<SessionClerk> Z;
  private Hashtable<Thread, Date> X = new Hashtable();
  private Properties Y = new Properties();
  private Vector<ConsumerListener> f = new Vector();
  protected boolean handshakeRequired = true;
  protected RandomAccessFile logFile;
  
  public Consumer(MeetingContainable paramMeetingContainable, String paramString){
	  this.T = paramMeetingContainable;
	  this.j = paramString;
	  paramString = "Consumer";
	  String str =  "Consumer.csm";
	  Y.put("class", "com.evangelsoft.econnect.client.SocketConsumer");
	  Y.put("sender_buffer_size", "16384");
	  Y.put("senders", "1");
	  Y.put("receiver_buffer_size", "16384");
	  Y.put("receivers", "1");
	  Y.put("keep_alive_interval", "600000");
	  this.i = Integer.parseInt("16384");//sender_buffer_size
	  int k = 1;//senders
	  this.a = new SessionSendable[k];
	  for (int m = 0; m < this.a.length; m++) {
		  this.a[m] = createSender(this.i);
	  }
	  this.h = Integer.parseInt("16384");//receiver_buffer_size
	  int  m = 1;//receivers
	  this.g = new SessionReceivable[m];
	  for (int n = 0; n < this.g.length; n++) {
		  this.g[n] = createReceiver(this.h);
	  }
	  checingIntVal= 300000;
    
    this.c = Integer.parseInt(this.Y.getProperty("keep_alive_interval", "600000"));
    this.d = new Pool(-1, -1, new ObjectFactory(){
    		public SessionHandler getObjectInstance(){
    			return new SessionHandler();
    		}
    	}, null);
    this.W = new Pool(-1, -1, new ObjectFactory(){
    		public AsynchroSessionHook getObjectInstance(){
    			return new AsynchroSessionHook();
    		}
    	}, null);
    this.Z = new Pool(-1, -1, new ObjectFactory(){
    		public SessionClerk getObjectInstance(){
    			return new SessionClerk();
    		}
    	}, new PooledObjectCleaner(){
    		public void clean(Object paramAnonymousObject){
    			((SessionClerk)paramAnonymousObject).close(true);
    		}
    	});
    new Timer("Monitor", true).schedule(new TimerTask(){
      public void run(){
        try
        {
          Date localDate1 = new Date();
          Iterator localIterator =X.keySet().iterator();
          while (localIterator.hasNext())
          {
            Thread localThread = (Thread)localIterator.next();
            synchronized (localThread)
            {
              Date localDate2 = (Date)X.get(localThread);
              if ((localDate2 != null) && (localDate1.compareTo(localDate2) >= 0)){
                localThread.interrupt();
                X.remove(localThread);
              }
            }
          }
        }
        catch (Throwable localThrowable) {}
      }
    }, checingIntVal, checingIntVal);
    if (this.c > 0)
    {
      if (this.c < 1000) {
        this.c *= 1000;
      }
      new Timer("Reminder", true).scheduleAtFixedRate(new TimerTask()
      {
        public void run()
        {
          Hashtable localHashtable = (Hashtable)U.clone();
          Iterator localIterator = localHashtable.keySet().iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            JobContainable localJobContainable = (JobContainable)localHashtable.get(str);
            if ((localJobContainable instanceof ClientSession)) {
              ((ClientSession)localJobContainable).nop();
            }
          }
        }
      }, this.c, this.c);
    }
  }
  
  private static synchronized void H()
  {
    if (e == null) {
      try
      {
        e = ConsumerFactory.getConsumer(null, null);
      }
      catch (Throwable localThrowable)
      {
        throw new RuntimeException(ExceptionFormat.format(localThrowable));
      }
    }
  }
  
  public static Consumer getDefaultConsumer()
  {
    if (e == null) {
      H();
    }
    return e;
  }
  
  public static synchronized void clearDefaultConsumer()
  {
    if (e != null)
    {
      Object localObject;
      for (int k = 0; k < e.getSenders().length; k++)
      {
        localObject = e.getSenders()[k];
        ((SessionSendable)localObject).close();
      }
      for (int k = 0; k < e.getReceivers().length; k++)
      {
        localObject = e.getReceivers()[k];
        ((SessionReceivable)localObject).close();
      }
    }
    e = null;
  }
  
  protected abstract SessionChannel createChannel(Properties paramProperties)
    throws Exception;
  
  protected abstract SessionSendable createSender(int paramInt);
  
  protected abstract SessionReceivable createReceiver(int paramInt);
  
  public MeetingContainable getOwner()
  {
    return this.T;
  }
  
  public String getName()
  {
    return this.j;
  }
  
  public Properties getParameters()
  {
    return this.Y;
  }
  
  public Hashtable<String, JobContainable> getSessions()
  {
    return this.U;
  }
  
  public SessionReceivable[] getReceivers()
  {
    return this.g;
  }
  
  public SessionSendable[] getSenders()
  {
    return this.a;
  }
  
  public Pool<SessionHandler> getHandlerPool()
  {
    return this.d;
  }
  
  public Pool<AsynchroSessionHook> getHookPool()
  {
    return this.W;
  }
  
  public Pool<SessionClerk> getClerkPool()
  {
    return this.Z;
  }
  
  public Hashtable<Thread, Date> getTransientZone()
  {
    return this.X;
  }
  
  public void sessionClosed(JobContainable paramJobContainable)
  {
    synchronized (this.f)
    {
      for (int k = 0; k < this.f.size(); k++) {
        ((ConsumerListener)this.f.elementAt(k)).processConsumerEvent((ClientSession)paramJobContainable, 1);
      }
    }
    synchronized (this.U)
    {
      Enumeration localEnumeration = this.U.keys();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        if (((JobContainable)this.U.get(str)).equals(paramJobContainable))
        {
          this.U.remove(str);
          this.V.remove(str);
          break;
        }
      }
    }
  }
  
  public synchronized void log(String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      System.out.println("[" + DateFormat.getInstance().format(new Date()) + "] " + paramString);
    }
    if (this.logFile != null) {
      try
      {
        this.logFile.write(DateFormat.getInstance().format(new Date()).getBytes());
        this.logFile.writeBytes(System.getProperty("line.separator"));
        this.logFile.write((paramString == null ? "" : paramString).getBytes());
        this.logFile.writeBytes(System.getProperty("line.separator"));
      }
      catch (Throwable localThrowable)
      {
        System.out.println(localThrowable.getMessage());
      }
    }
  }
  
  public ClientSession connect(Properties paramProperties)
    throws IOException
  {
    return connect(b, paramProperties);
  }
  
  public ClientSession connect(String paramString, Properties paramProperties)
    throws IOException
  {
    ClientSession localClientSession;
    try
    {
      localClientSession = new ClientSession(this, createChannel(paramProperties));
    }
    catch (Throwable localThrowable)
    {
      throw new IOException(ExceptionFormat.format(localThrowable));
    }
    int k = -1;
    int m = Integer.MAX_VALUE;
    int i2;
    for (int n = 0; n < this.a.length; n++)
    {
      if (!this.a[n].isAlive())
      {
        this.a[n].close();
        this.a[n] = createSender(this.i);
      }
      i2 = this.a[n].getSessionCount();
      if (i2 < m)
      {
        k = n;
        m = i2;
      }
      if (m == 0) {
        break;
      }
    }
    if (k >= 0)
    {
      SessionSendable localSessionSendable = this.a[k];
      localSessionSendable.bindSession(localClientSession);
    }
    k = -1;
    m = Integer.MAX_VALUE;
    for (int i1 = 0; i1 < this.g.length; i1++)
    {
      if (!this.g[i1].isAlive())
      {
        this.g[i1].close();
        this.g[i1] = createReceiver(this.h);
      }
      i2 = this.g[i1].getSessionCount();
      if (i2 < m)
      {
        k = i1;
        m = i2;
      }
      if (m == 0) {
        break;
      }
    }
    if (k >= 0)
    {
    	SessionReceivable localObject1 = this.g[k];
      ((SessionReceivable)localObject1).bindSession(localClientSession);
    }
    Object[] localObject1 = { "application", "family" };
    HashMap localHashMap = new HashMap();
    Object localObject2;
    for (int i3 = 0; i3 < localObject1.length; i3++)
    {
      localObject2 = paramProperties.getProperty((String) localObject1[i3]);
      if ((localObject2 != null) && (((String)localObject2).length() > 0)) {
        localHashMap.put(localObject1[i3], localObject2);
      }
    }
    if (this.handshakeRequired)
    {
      byte[] arrayOfByte = null;
      if (localHashMap.size() > 0)
      {
        localObject2 = new StringBuffer();
        ((StringBuffer)localObject2).append('{');
        Iterator localIterator = localHashMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if (((StringBuffer)localObject2).length() > 1) {
            ((StringBuffer)localObject2).append(";");
          }
          ((StringBuffer)localObject2).append(str + "=" + (String)localHashMap.get(str));
        }
        ((StringBuffer)localObject2).append('}');
        arrayOfByte = ((StringBuffer)localObject2).toString().getBytes();
      }
      localClientSession.ready(arrayOfByte);
    }
    else
    {
      localClientSession.selfReady();
    }
    addSession(paramString, localClientSession);
    synchronized (this.f)
    {
      for (int i4 = 0; i4 < this.f.size(); i4++) {
        ((ConsumerListener)this.f.elementAt(i4)).processConsumerEvent(localClientSession, 0);
      }
    }
    return localClientSession;
  }
  
  @Deprecated
  public ClientSession connect(InetSocketAddress paramInetSocketAddress)
    throws IOException
  {
    return connect(b, paramInetSocketAddress);
  }
  
  @Deprecated
  public ClientSession connect(String paramString, InetSocketAddress paramInetSocketAddress)
    throws IOException
  {
    Properties localProperties = new Properties();
    localProperties.setProperty("host", paramInetSocketAddress.getHostName());
    localProperties.setProperty("port", Integer.toString(paramInetSocketAddress.getPort()));
    return connect(paramString, localProperties);
  }
  
  public void disconnect()
  {
    disconnect(b);
  }
  
  public void disconnect(String paramString)
  {
    JobContainable localJobContainable = (JobContainable)this.U.get(paramString);
    if (localJobContainable == null) {
      return;
    }
    try
    {
      localJobContainable.getChannel().closeChannel();
    }
    catch (Throwable localThrowable1) {}
    if ((localJobContainable instanceof ClientSession))
    {
      ClientSession localClientSession = (ClientSession)localJobContainable;
      if (localClientSession.getReceiver() != null) {
        try
        {
          localClientSession.getReceiver().unbindSession(localClientSession);
        }
        catch (Throwable localThrowable2) {}
      }
      if (localClientSession.getSender() != null) {
        try
        {
          localClientSession.getSender().unbindSession(localClientSession);
        }
        catch (Throwable localThrowable3) {}
      }
    }
    removeSession(paramString);
  }
  
  public void addListener(ConsumerListener paramConsumerListener)
  {
    this.f.add(paramConsumerListener);
  }
  
  public void removeListener(ConsumerListener paramConsumerListener)
  {
    this.f.remove(paramConsumerListener);
  }
  
  public ConsumerListener[] getListeners()
  {
    return (ConsumerListener[])this.f.toArray();
  }
  
  public void addSession(String paramString, ClientSession paramClientSession)
  {
    synchronized (this)
    {
      this.U.put(paramString, paramClientSession);
      this.V.put(paramString, new HashMap());
    }
  }
  
  public void addSession(ClientSession paramClientSession)
  {
    addSession(b, paramClientSession);
  }
  
  public void removeSession(String paramString)
  {
    synchronized (this)
    {
      this.U.remove(paramString);
      this.V.remove(paramString);
    }
  }
  
  public void removeSession()
  {
    removeSession(b);
  }
  
  public ClientSession getSession(String paramString)
  {
    return (ClientSession)this.U.get(paramString);
  }
  
  public ClientSession getSession()
  {
    return getSession(b);
  }
  
  public void registerEnv(String paramString1, String paramString2, Object paramObject)
  {
    ((HashMap)this.V.get(paramString1)).put(paramString2, paramObject);
  }
  
  public void registerEnv(String paramString, Object paramObject)
  {
    ((HashMap)this.V.get(b)).put(paramString, paramObject);
  }
  
  public void deregisterEnv(String paramString1, String paramString2)
  {
    ((HashMap)this.V.get(paramString1)).remove(paramString2);
  }
  
  public void deregisterEnv(String paramString)
  {
    ((HashMap)this.V.get(b)).remove(paramString);
  }
  
  public Object getEnv(String paramString1, String paramString2)
  {
    return ((HashMap)this.V.get(paramString2)).get(paramString2);
  }
  
  public Object getEnv(String paramString)
  {
    HashMap localHashMap = (HashMap)this.V.get(b);
    return localHashMap == null ? null : localHashMap.get(paramString);
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect_mobile.jar
 * Qualified Name:     com.evangelsoft.econnect.client.Consumer
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */