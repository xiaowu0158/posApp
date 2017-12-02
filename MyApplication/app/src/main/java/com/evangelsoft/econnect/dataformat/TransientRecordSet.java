package com.evangelsoft.econnect.dataformat;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TransientRecordSet
  extends RecordSet
  implements Externalizable
{
  public TransientRecordSet() {}
  
  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    unmarshal(paramObjectInput, false);
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    try
    {
      marshal(paramObjectOutput, false);
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new IOException(localInterruptedException.getMessage());
    }
  }
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\econnect.jar
 * Qualified Name:     com.evangelsoft.econnect.dataformat.TransientRecordSet
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */