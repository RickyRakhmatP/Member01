package com.skybiz.member01.m_Database.m_Local;

/**
 * Created by 7 on 30/10/2017.
 */

public class Constants {
    //COLUMNS
    static final String DB_NAME        ="db_celesmember";
    static final int DB_VERSION        ='1';
    static final String CREATE_TB      ="CREATE TABLE tb_settingdb(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "ServerName TEXT NOT NULL, UserName TEXT NOT NULL, Password TEXT, " +
            " DBName TEXT, Port TEXT, Remark TEXT, " +
            " ConnYN TEXT, DBStatus TEXT, ItemConn TEXT, PostAs TEXT, " +
            " EncodeType TEXT, ReceiptType TEXT, Mgt01YN TEXT, " +
            " BranchCode TEXT, LocationCode TEXT, Modules TEXT, " +
            " DepartmentCode TEXT, CompCustomerYN TEXT, AdminPassword TEXT, " +
            " CategoryCode TEXT, SalesPersonCode TEXT, DirectPrintYN TEXT, " +
            " UserCode TEXT, CounterCode TEXT, FastKeypadYN TEXT);";

    static final String CREATE_STK_MASTER="CREATE TABLE stk_master(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "ItemCode TEXT NOT NULL, Description TEXT, ItemGroup TEXT, UnitPrice REAL, UnitCost TEXT,UOM TEXT, DefaultUOM TEXT," +
            "  UOM1 TEXT, UOM2 TEXT, UOM3 TEXT, UOM4 TEXT, UOMFactor1 TEXT, UOMFactor2 TEXT, UOMFactor3 TEXT, UOMFactor4 TEXT," +
            "  UOMPrice1 REAL, UOMPrice2 REAL, UOMPrice3 REAL, UOMPrice4 REAL, MSP REAL, MSP1 REAL, MSP2 REAL, MSP3 REAL, MSP4 REAL," +
            "  AnalysisCode1 TEXT, AnalysisCode2 TEXT, AnalysisCode3 TEXT, AnalysisCode4 TEXT, AnalysisCode5 TEXT, MAXSP REAL, MAXSP1 REAL, MAXSP2 REAL, MAXSP3 REAL, MAXSP4 REAL," +
            "  SalesTaxCode TEXT, RetailTaxCode TEXT, PurchaseTaxCode TEXT, FixedPriceYN TEXT, SuspendedYN TEXT, AlternateItem TEXT);";

    static final String CREATE_STK_ITEMLOCATION="CREATE TABLE stk_itemlocation(RunNo INTEGER PRIMARY KEY AUTOINCREMENT, LocationCode TEXT, LocationName TEXT, "+
            " BranchCode TEXT, DepartmentCode TEXT, ProjectCode TEXT, " +
            " S_ection TEXT, C_olumn TEXT, R_ow TEXT, " +
            " DetailYN TEXT, Link TEXT);";

    static final String CREATE_OSTK_STKCHECK_DT="CREATE TABLE ostk_stkcheck_dt(RunNo INTEGER PRIMARY KEY AUTOINCREMENT, Doc1No TEXT, RunNoHd TEXT, "+
            " ItemCode TEXT, ItemDesc TEXT, OnHandQty REAL, " +
            " CheckQty REAL, UnitCost REAL, LineAmt REAL, " +
            " PostedYN TEXT, ProcessYN TEXT, D_ate TEXT," +
            " UOM TEXT,FactorQty REAL, LocationCode TEXT," +
            " UserEmail TEXT, SyncYN TEXT);";

    static final String CREATE_DUMLIST="CREATE TABLE dumlist(RunNo INTEGER PRIMARY KEY AUTOINCREMENT, Doc1No TEXT, RunNoHd TEXT, "+
            " ItemCode TEXT, ItemDesc TEXT, OnHandQty REAL, " +
            " CheckQty REAL, UnitCost REAL, LineAmt REAL, " +
            " PostedYN TEXT, ProcessYN TEXT, D_ate TEXT," +
            " UOM TEXT,FactorQty REAL, LocationCode TEXT," +
            " UserEmail TEXT, AlternateItem TEXT);";

    static final String CREATE_STK_GROUP="CREATE TABLE stk_group(RunNo INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "ItemGroup TEXT NOT NULL, Description TEXT, Description2 TEXT, " +
            " L_ink TEXT, Modifier1 TEXT, Modifier2 TEXT, " +
            " DateTimeModified TEXT, Printer TEXT);";

    static final String CREATE_COMPANYSETUP="CREATE TABLE companysetup(RunNo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " CompanyCode TEXT, CompanyName TEXT, Address TEXT, ComTown TEXT, ComState TEXT," +
            " ComCountry TEXT, Tel1 TEXT, Fax1 TEXT,CompanyEmail TEXT, GSTNo TEXT, CurCode TEXT);" ;

    static final String CREATE_SYS_RUNNO_DT="CREATE TABLE sys_runno_dt(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "RunNoCode TEXT NOT NULL, Prefix TEXT, LastNo TEXT);";

    static final String CREATE_STK_TAX="CREATE TABLE stk_tax(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "TaxCode TEXT NOT NULL, R_ate TEXT, TaxType TEXT, GSTTaxType TEXT, GSTTaxCode TEXT, Description TEXT);";

    static final String CREATE_SYS_GENERAL_SETUP2="CREATE TABLE sys_general_setup2(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "PostGlobalTaxYN TEXT, Remark TEXT);";

    static final String CREATE_CLOUD="CREATE TABLE cloud_inventory_dt(RunNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " Doc1No TEXT, N_o TEXT, ItemCode TEXT, Description TEXT, " +
            " QtyIN REAL, QtyOUT REAL, FactorQty REAL, UOM TEXT, " +
            " UOMSingular TEXT, HCUnitCost REAL, DisRate1 REAL, HCDiscount REAL, " +
            " TaxRate1 REAL, HCTax REAL, DetailTaxCode TEXT, HCLineAmt REAL," +
            " BranchCode TEXT, DepartmentCode TEXT, ProjectCode TEXT, SalesPersonCode TEXT, " +
            " LocationCode TEXT,WarrantyDate TEXT, BlankLine TEXT, DocType TEXT," +
            " AnalysisCode1 TEXT, AnalysisCode2 TEXT, SerialNoRunNo TEXT, LineNo TEXT, " +
            " Inv2No TEXT, Inv2RunNo TEXT, PurReq_INRunNo TEXT, SORunNo TEXT," +
            " SerialNumber TEXT,  ServiceRunNo TEXT, LineType TEXT, ItemBatch TEXT," +
            " GradeDescription TEXT, ComputerName TEXT, LocationCodeTo TEXT);";

    static final String CREATE_STK_INVENTORY_DT="CREATE TABLE stk_inventory_dt(RunNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " Doc1No TEXT, N_o TEXT, ItemCode TEXT, Description TEXT, " +
            " QtyIN REAL, QtyOUT REAL, FactorQty REAL, UOM TEXT, " +
            " UOMSingular TEXT, HCUnitCost REAL, DisRate1 REAL, HCDiscount REAL, " +
            " TaxRate1 REAL, HCTax REAL, DetailTaxCode TEXT, HCLineAmt REAL," +
            " BranchCode TEXT, DepartmentCode TEXT, ProjectCode TEXT, SalesPersonCode TEXT, " +
            " LocationCode TEXT, WarrantyDate TEXT, BlankLine TEXT, DocType TEXT," +
            " AnalysisCode1 TEXT, AnalysisCode2 TEXT, SerialNoRunNo TEXT, LineNo TEXT, " +
            " Inv2No TEXT, Inv2RunNo TEXT, PurReq_INRunNo TEXT, SORunNo TEXT," +
            " SerialNumber TEXT,  ServiceRunNo TEXT, LineType TEXT, ItemBatch TEXT," +
            " GradeDescription TEXT, SyncYN TEXT, LocationCodeTo TEXT);";

    static final String CREATE_STK_INVENTORY_HD="CREATE TABLE stk_inventory_hd(RunNo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " Doc1No TEXT, Doc2No TEXT, Doc3No TEXT," +
            " D_ate TEXT,D_ateTime TEXT, CusCode TEXT," +
            " CurCode TEXT, CurRate1 TEXT, CurRate2 TEXT, " +
            " CurRate3 TEXT, Attention TEXT, AddCode," +
            " GbDisRate1 REAL, GbDisRate2 REAL, GbDisRate3 REAL, " +
            " HCGbDiscount REAL, GbTaxRate1 REAL, GbTaxRate2 REAL, " +
            " GbTaxRate3 REAL, HCGbTax REAL,HCNetAmt REAL, "+
            " DocType TEXT, ApprovedYN TEXT, PostedYN TEXT, " +
            " UDRunNo TEXT, L_ink TEXT, PendingReceiptYN TEXT, " +
            " Reason TEXT, SyncYN TEXT);";

    static final String CREATE_CUSTOMER="CREATE TABLE customer(RunNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " FinCatCode TEXT ,AccountCode TEXT, CusCode TEXT, CusName TEXT, Address TEXT," +
            " CurCode TEXT, TermCode TEXT, D_ay TEXT, SalesPersonCode TEXT, Tel TEXT, Tel2 TEXT," +
            " Fax TEXT, Fax2 TEXT, Contact TEXT, ContactTel TEXT, Email TEXT, StatusBadYN TEXT," +
            " Town TEXT, State TEXT, Country TEXT, PostCode TEXT, L_ink TEXT, NRICNo TEXT, DOB TEXT, Sex TEXT," +
            " MemberType TEXT, CardNo TEXT, PaymentCode TEXT, DateTimeModified TEXT);";

    static final String CREATE_TBMEMBER="CREATE TABLE tb_member(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "CusCode TEXT NOT NULL, CusName TEXT, TermCode TEXT," +
            " D_ay TEXT, SalesPersonCode TEXT, RunNoCus TEXT);";

    static final String CREATE_TBLOC="CREATE TABLE tb_location(RunNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "LocationCode TEXT, LocationCodeTo TEXT, Remark TEXT);";

    static final String CREATE_STK_DETAIL_TRN_OUT="CREATE TABLE stk_detail_trn_out(RunNo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " ItemCode TEXT, Doc3No TEXT, D_ate TEXT, QtyOUT REAL, " +
            " QtyIN REAL, FactorQty REAL, UOM TEXT, UnitPrice REAL, " +
            " CusCode TEXT, DocType3 TEXT, Doc3NoRunNo TEXT, LocationCode TEXT, " +
            " L_ink TEXT, HCTax REAL, BookDate TEXT, SyncYN TEXT," +
            " Doc1NoRunNo TEXT, Doc2NoRunNo TEXT, DocType1 TEXT, DocType2 TEXT," +
            " Doc1No TEXT, Doc2No TEXT, ItemBatch TEXT, " +
            " VoidYN TEXT, GlobalTaxCode TEXT, DetailTaxCode TEXT);" ;

    static final String CREATE_STK_DETAIL_TRN_IN="CREATE TABLE stk_detail_trn_in(RunNo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " ItemCode TEXT, Doc3No TEXT, D_ate TEXT, " +
            " QtyIN REAL, FactorQty REAL, UOM TEXT, UnitCost REAL, " +
            " CusCode TEXT, DocType3 TEXT, Doc3NoRunNo TEXT, LocationCode TEXT, " +
            " L_ink TEXT, HCTax REAL, BookDate TEXT, SyncYN TEXT," +
            " Doc1NoRunNo TEXT, Doc2NoRunNo TEXT, DocType1 TEXT, DocType2 TEXT," +
            " Doc1No TEXT, Doc2No TEXT, ItemBatch TEXT, GlobalTaxCode TEXT, " +
            " DetailTaxCode TEXT, QtyBalance REAL, MovingAveCost REAL, LandingCost REAL," +
            " OCCost REAL, DocType4 TEXT, ExpireDate TEXT);" ;

    static final String CREATE_STK_GRNDO_DT="CREATE TABLE stk_grndo_dt(RunNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " Doc1No TEXT, N_o TEXT, ItemCode TEXT, Description TEXT, " +
            " Qty REAL, FactorQty REAL, UOM TEXT,  " +
            " UOMSingular TEXT, HCUnitCost REAL, DisRate1 REAL, HCDiscount REAL, " +
            " TaxRate1 REAL, HCTax REAL, DetailTaxCode TEXT, HCLineAmt REAL," +
            " BranchCode TEXT, DepartmentCode TEXT, ProjectCode TEXT, SalesPersonCode TEXT, " +
            " LocationCode TEXT, WarrantyDate TEXT, BlankLine TEXT, DocType TEXT," +
            " AnalysisCode1 TEXT, AnalysisCode2 TEXT, SerialNoRunNo TEXT, LineNo TEXT, " +
            " PORunNo TEXT, SerialNumber TEXT,  ServiceRunNo TEXT, LineType TEXT, " +
            " ItemBatch TEXT, GradeDescription TEXT, SyncYN TEXT);";

    static final String CREATE_STK_GRNDO_HD="CREATE TABLE stk_grndo_hd(RunNo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " Doc1No TEXT, Doc2No TEXT, Doc3No TEXT," +
            " D_ate TEXT, D_ateTime TEXT, CusCode TEXT," +
            " CurCode TEXT, CurRate1 TEXT, CurRate2 TEXT, " +
            " CurRate3 TEXT, Attention TEXT, GlobalTaxCode TEXT, " +
            " GbDisRate1 REAL, GbDisRate2 REAL, GbDisRate3 REAL, " +
            " HCGbDiscount REAL, GbTaxRate1 REAL, GbTaxRate2 REAL, " +
            " GbTaxRate3 REAL, HCGbTax REAL, HCNetAmt REAL, "+
            " DocType TEXT, UDRunNo TEXT, L_ink TEXT, Status TEXT, " +
            " HCDtTax REAL, SyncYN TEXT);";

    static final String INSERT_PREFIX1="insert into sys_runno_dt(Prefix,LastNo,RunNoCode)values('GRNDO','000001','GRNDO');";
    static final String INSERT_PREFIX2="insert into sys_runno_dt(Prefix,LastNo,RunNoCode)values('IS','000001','IS');";
    static final String INSERT_PREFIX3="insert into sys_runno_dt(Prefix,LastNo,RunNoCode)values('RS','000001','RS');";
    static final String INSERT_PREFIX4="insert into sys_runno_dt(Prefix,LastNo,RunNoCode)values('TS','000001','TS');";


    static final String INSERT_SETTINGDB="insert into tb_settingdb(ServerName,UserName,Password," +
                                        " DBName,Port)values('101.99.66.125', 'vmairgas_celes', 'f1HMaf8=PWp.', " +
                                        " 'vmairgas_celes', '3306');";

}
