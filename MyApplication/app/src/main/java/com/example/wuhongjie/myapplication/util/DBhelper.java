package com.example.wuhongjie.myapplication.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME_STRING = "tgOrderDb";
	private static final int DATABASE_VERSION = 13;

	public DBhelper(Context context) {
		super(context, DATABASE_NAME_STRING, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//客户表
		db.execSQL("CREATE TABLE [EXD_TG_CUST](" +
				"[CUST_CODE] VARCHAR(30) PRIMARY KEY NOT NULL," +
				"[CUST_NAME] VARCHAR(100) NOT NULL," +
				"[AREA_CODE] VARCHAR(30) NULL)");
		// 顾客表
		db.execSQL("CREATE TABLE [EXD_TG_CUSTJOB] (" +
				"[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[CUST_CODE] VARCHAR(30) NOT NULL," +
				"[PCKNO]  INTEGER NULL," +
				"[DEPT]   VARCHAR(20) NULL," +
				"[CNAME]  VARCHAR(30) NULL," +
				"[GENDER] VARCHAR(30) NULL," +
				"[SORT] INTEGER  NULL," +
				"[STATUS] VARCHAR(20) NULL)");

		// 商品资料表
		db.execSQL("CREATE TABLE [EXD_TG_PROD_CLS] ([id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[ORD_NO] VARCHAR(30)  NULL," +
				"[CUST_CODE] VARCHAR(30)  NULL," +
				"[PRD_CODE] VARCHAR(30) NULL," +
				"[PRD_NAME] VARCHAR(80) NULL," +
				"[PRD_MEMO] VARCHAR(200) NULL," +
				"[PROD_CAT_ID] VARCHAR(10) NULL," +
				"[PROD_CAT_NAME] VARCHAR(60) NULL," +
				"[SUITE_GRP_ID] VARCHAR(10) NULL," +
				"[SUITE_GRP_NAME] VARCHAR(30) NULL," +
				"[IMAGE_URL] VARCHAR(50) NULL," +
				"[IMAGE_FILE_NAME] VARCHAR(50) NULL," +
				"[GENDER] VARCHAR(6) NULL," +
				"[UOM] VARCHAR(8) NULL," +
				"[SUITE_CODE] VARCHAR(12) NULL)");
		//套装表
		db.execSQL("CREATE TABLE [EXD_TG_SUITE_GRP] (" +
				"[SUITE_GRP_ID] INTEGER PRIMARY KEY NOT NULL," +
				"[CAT_ID] VARCHAR(6) NULL," +
				"[SUITE_GRP_NAME] VARCHAR(30) NULL," +
				"[SEQ_NUM] VARCHAR(10) NULL)");
		//套装明细表
		db.execSQL("CREATE TABLE [EXD_TG_SUITE_GRP_DTL](" +
				"[DTL_ID] INTEGER PRIMARY KEY NOT NULL," +
				"[SUITE_GRP_ID] INTEGER NOT NULL," +
				"[SUITE_GRP_NAME] VARCHAR(30) NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[SUITE_NAME] VARCHAR(30) NULL," +
				"[SORT] VARCHAR(10) NULL," +
				"[SUITE_SORT] VARCHAR(10) NULL)");
		//规格组，款式甄别
		db.execSQL("CREATE TABLE [EXD_TG_SPEC_GRP](" +
				"[GRP_ID] INTEGER PRIMARY KEY NOT NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[GENDER] VARCHAR(6) NULL," +
				"[GRP_CODE] VARCHAR(10) NULL," +
				"[GRP_NAME] VARCHAR(80) NULL," +
				"[APPROVE] VARCHAR(10) NULL)");
		//规格
		db.execSQL("CREATE TABLE [EXD_TG_SPEC](" +
				"[SPEC_ID] INTEGER PRIMARY KEY NOT NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[GENDER] VARCHAR(6) NULL," +
				"[SPEC_CODE] VARCHAR(20) NULL," +
				"[PATTERN] VARCHAR(30) NULL," +
				"[SPEC] VARCHAR(30) NULL," +
				"[SHAPE] VARCHAR(30) NULL," +
				"[GRP_ID] INTEGER NULL)");
		//术语描述
		db.execSQL("CREATE TABLE [EXD_TG_TERMINOLOGY](" +
				"[TERM_ID] INTEGER PRIMARY KEY NOT NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[TERM_NAME] VARCHAR(80) NULL," +
				"[DEFAULT_VALUE] VARCHAR(10) NULL," +
				"[MAX_VALUE] VARCHAR(10) NULL," +
				"[MIN_VALUE] VARCHAR(10) NULL," +
				"[STEP_VALUE] VARCHAR(10) NULL," +
				"[SORT] VARCHAR(10) NULL," +
				"[CURR_VALUE] VARCHAR(10) NULL," +
				"[GROUP_VALUE] VARCHAR(10) NULL)");
		db.execSQL("CREATE TABLE [EXD_TG_ORDITMJOBPRD](" +
				"[JOB_ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[ORD_NO] VARCHAR(30)  NULL," +
				"[PRD_CODE] VARCHAR(30) NULL," +
				"[SUITE_GRP_ID] VARCHAR(10) NULL," +
				"[PCKNO] INTEGER NULL," +
				"[GRP_ID] INTEGER NULL,"+
				"[SPEC_CODE] VARCHAR(30) NULL," +
				"[TERM_MEMO] VARCHAR(200) NULL," +
				"[QTY] INTEGER NULL," +
				"[DTL_ID] INTEGER NOT NULL," +
				"[UPD_TIME] VARCHAR(30) NULL)"
				);
		db.execSQL("CREATE TABLE [EXD_TG_ORDITMJOBPRD_DTL](" +
				"[ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[JOB_ID] INTEGER NOT NULL," +
				"[TERM_ID] INTEGER NOT NULL," +
				"[CURR_VAL] VARCHAR(20) NULL," +
				"[DESCRIPTION] VARCHAR(50) NULL," +
				"[UPD_TIME] VARCHAR(30) NULL)");
		db.execSQL("CREATE TABLE [EXD_TG_PRD_SPEC_GRP](" +
				"[ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[ORD_NO] VARCHAR(30)  NULL," +
				"[PRD_CODE] VARCHAR(30) NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[GRP_CODE] VARCHAR(10) NULL)");
		db.execSQL("CREATE TABLE [EXD_TG_SYN](" +
				"[ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[UPD_TIME] VARCHAR(30) NULL)");
		db.execSQL("CREATE TABLE [EXD_TG_SPECLINK_DTL](" +
				"[ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[GENDER] VARCHAR(6) NULL," +
				"[SHAPE] VARCHAR(30) NULL," +
				"[DTL_SUITE_CODE] VARCHAR(10) NULL," +
				"[DTL_SHAPE] VARCHAR(30) NULL," +
				"[DTL_STATUS] VARCHAR(30) NULL)");
	}
	public void createTableSpecLink(SQLiteDatabase db){
		db.execSQL("CREATE TABLE [EXD_TG_SPECLINK_DTL](" +
				"[ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"[SUITE_CODE] VARCHAR(10) NULL," +
				"[GENDER] VARCHAR(6) NULL," +
				"[SHAPE] VARCHAR(30) NULL," +
				"[DTL_SUITE_CODE] VARCHAR(10) NULL," +
				"[DTL_SHAPE] VARCHAR(30) NULL," +
				"[DTL_STATUS] VARCHAR(30) NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
			db.execSQL("drop table if exists [EXD_TG_CUST]");
			db.execSQL("drop table if exists [EXD_TG_CUSTJOB]");
			db.execSQL("drop table if exists [EXD_TG_PROD_CLS]");
			db.execSQL("drop table if exists [EXD_TG_SUITE_GRP]");
			db.execSQL("drop table if exists [EXD_TG_SUITE_GRP_DTL]");
			db.execSQL("drop table if exists [EXD_TG_SPEC_GRP]");
			db.execSQL("drop table if exists [EXD_TG_SPEC]");
			db.execSQL("drop table if exists [EXD_TG_TERMINOLOGY]");
			db.execSQL("drop table if exists [EXD_TG_ORDITMJOBPRD]");
			db.execSQL("drop table if exists [EXD_TG_ORDITMJOBPRD_DTL]");
			db.execSQL("drop table if exists [EXD_TG_PRD_SPEC_GRP]");
			db.execSQL("drop table if exists [EXD_TG_SYN]");
			db.execSQL("drop table if exists [EXD_TG_SPECLINK_DTL]");
			onCreate(db);
	}
}
