package com.youngor.modules.drp.counter.intf;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.plant.TxMode;
import com.evangelsoft.econnect.plant.TxUnit;
import com.evangelsoft.econnect.session.RemoteException;

public interface RlbWeb {
	@TxMode(TxUnit.TX_NONE)
	public boolean getProduct(Object key, VariantHolder<Object> data,
							  VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean getVipCust(Object key, VariantHolder<Object> data,
							  VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean getWarehStk(Object key, VariantHolder<Object> data,
							   VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean getPolicy(Object key, VariantHolder<Object> data,
							 VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean getPolicyStructure(VariantHolder<Object> data,
									  VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean executePolicy(Object key,VariantHolder<Object> data,
								 VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean executePolicyForCust(Object key,VariantHolder<Object> data,
										VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	public boolean getRlbStructure(VariantHolder<Object> data,
								   VariantHolder<String> errMsg)throws RemoteException;
}
