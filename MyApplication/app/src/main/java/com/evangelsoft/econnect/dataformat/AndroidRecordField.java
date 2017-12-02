package com.evangelsoft.econnect.dataformat;

import java.io.UnsupportedEncodingException;

public class AndroidRecordField extends  RecordField{

	public AndroidRecordField(RecordFieldFormat recordFieldFormat) {
		super(recordFieldFormat);
	}
	public String getString(){
		String s="";
		try {
			s = new String(super.getBytes(),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return s;
	  }

}
