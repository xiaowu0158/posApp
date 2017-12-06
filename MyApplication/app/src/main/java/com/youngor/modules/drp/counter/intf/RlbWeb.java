package com.youngor.modules.drp.counter.intf;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.plant.TxMode;
import com.evangelsoft.econnect.plant.TxUnit;
import com.evangelsoft.econnect.session.RemoteException;

public interface RlbWeb {
	@TxMode(TxUnit.TX_NONE)
	boolean getProduct(Object key, VariantHolder<Object> data,
					   VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean getVipCust(Object key, VariantHolder<Object> data,
					   VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean getWarehStk(Object key, VariantHolder<Object> data,
						VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean getPolicy(Object key, VariantHolder<Object> data,
					  VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean getPolicyStructure(VariantHolder<Object> data,
							   VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean executePolicy(Object key, VariantHolder<Object> data,
						  VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean executePolicyForCust(Object key, VariantHolder<Object> data,
								 VariantHolder<String> errMsg) throws RemoteException;
	@TxMode(TxUnit.TX_NONE)
	boolean getRlbStructure(VariantHolder<Object> data,
							VariantHolder<String> errMsg)throws RemoteException;

	@TxMode(TxUnit.TX_NONE)
	boolean getShopSellerWarehLoc(Object key, VariantHolder<Object> data,
								  VariantHolder<String> errMsg)throws RemoteException;
}
