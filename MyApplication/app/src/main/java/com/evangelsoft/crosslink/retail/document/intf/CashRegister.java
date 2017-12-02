package com.evangelsoft.crosslink.retail.document.intf;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.plant.EntityFlushable;
import com.evangelsoft.econnect.plant.EntityReadable;
import com.evangelsoft.econnect.plant.TxMode;
import com.evangelsoft.econnect.session.RemoteException;

public abstract interface CashRegister
  extends EntityReadable, EntityFlushable
{
  @TxMode(0)
  public abstract boolean listAll(Object paramObject, VariantHolder<Object> paramVariantHolder, VariantHolder<String> paramVariantHolder1)
    throws RemoteException;
  
  @TxMode(1)
  public abstract boolean flushAll(Object paramObject, VariantHolder<Object> paramVariantHolder, VariantHolder<String> paramVariantHolder1)
    throws RemoteException;
}

/* Location:           D:\PosRedev\Deploy\PosBetaClient\retail.jar
 * Qualified Name:     com.evangelsoft.crosslink.retail.document.intf.CashRegister
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.0.1
 */