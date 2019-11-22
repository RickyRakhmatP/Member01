package com.skybiz.member01.ui_MemberCode;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skybiz.member01.MyBounceInterpolator;
import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;
import com.skybiz.member01.m_Util.Barcode.BitmapUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemberCode extends AppCompatActivity {

    TextView txtMemberCode;
    TextView txtDateBF,txtPointBF,txtDateCF,txtPointCF,txtDateRedeem;
    EditText txtRedeem;
    Double dPointBF=0.00;
    Button btnGenerate;
    ImageView imgBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membercode);
        getSupportActionBar().setTitle("Member Code");
        txtDateBF=(TextView)findViewById(R.id.txtDateBF);
        txtDateRedeem=(TextView)findViewById(R.id.txtDateRedeem);
        txtPointBF=(TextView)findViewById(R.id.txtPointBF);
        txtRedeem=(EditText) findViewById(R.id.txtRedeem);
        btnGenerate=(Button)findViewById(R.id.btnGenerate);
        imgBarcode=(ImageView) findViewById(R.id.imgBarcode);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fngenerate();
            }
        });
        //txtMemberCode=(TextView) findViewById(R.id.txtMemberCode);
        //txtMemberCode.setEnabled(false);
        initData();
    }
    private void initData(){
       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                genCode();
            }
        }, 500);*/
        RetBF retBF=new RetBF(this);
        retBF.execute();
    }

    private void fngenerate(){
        btnGenerate.setEnabled(false);
        didTapButton(btnGenerate);
        fnsavepoint();
    }
    private void fnsavepoint(){
        String PointRedeem=txtRedeem.getText().toString();
        if(!PointRedeem.isEmpty()) {
            Double dPointRedeem = Double.parseDouble(PointRedeem);
            if (dPointRedeem > dPointBF ) {
                btnGenerate.setEnabled(true);
                Toast.makeText(this,"Point Redeem Cannot More Than Balance", Toast.LENGTH_SHORT).show();
            } else {
                SaveRedeem saveRedeem = new SaveRedeem(this, PointRedeem);
                saveRedeem.execute();
            }
        }else{
            btnGenerate.setEnabled(true);
            Toast.makeText(this,"Point Redeem Cannot Empty", Toast.LENGTH_SHORT).show();
        }
    }
    private void fnsavevoucher(String Point){
        SaveVoucher saveVoucher=new SaveVoucher(this,Point);
        saveVoucher.execute();
    }
    private void setActionBar(String CusName){
        getSupportActionBar().setTitle(CusName);
    }

    private class RetBF extends AsyncTask<Void,Void,String> {
        Context c;
        String CusCode="",CusName="";
        String IPAddress,UserName,Password,DBName,Port,URL,z,DBStatus,ItemConn,EncodeType;
        String PointBF;

        private RetBF(Context c) {
            this.c = c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.calcBF();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result==null){
                Toast.makeText(c,"Failure, calculate Point B/F", Toast.LENGTH_SHORT).show();
            }else{
                txtDateBF.setText(datedNow());
                txtDateRedeem.setText(datedNow());
                txtPointBF.setText(PointBF+"P");
                dPointBF=Double.parseDouble(PointBF.replace(",",""));
                setActionBar(CusName);
            }
        }
        private String calcBF(){
            try{
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                String querySet="select ServerName, UserName, Password," +
                        " DBName, Port, ItemConn, " +
                        " EncodeType " +
                        " from tb_settingdb";
                Cursor curSet = db.getQuery(querySet);
                while (curSet.moveToNext()) {
                    IPAddress   = curSet.getString(0);
                    UserName    = curSet.getString(1);
                    Password    = curSet.getString(2);
                    DBName      = curSet.getString(3);
                    Port        = curSet.getString(4);
                    ItemConn    = curSet.getString(5);
                    EncodeType  = curSet.getString(6);
                }
                curSet.close();
                String queryCus="select CusCode,CusName from customer";
                Cursor rsCus=db.getQuery(queryCus);
                while(rsCus.moveToNext()){
                    CusCode=rsCus.getString(0);
                    CusName=rsCus.getString(1);
                }
                rsCus.close();

                Double dTotalPoint=0.00;
                URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                if (conn != null) {
                    String sql ="Select DATE_FORMAT(P.D_ateTime,'%Y-%m-%d %H:%i:%s') as D_ateTime,IFNULL(SUM(P.Point),0) as Point,P.DocType," +
                                " P.Remark,C.CusCode,C.CusName "+
                                " from ret_pointadjustment P inner join customer C " +
                                " on P.cuscode=C.CusCode where C.CusCode='"+CusCode+"' GROUP BY P.cuscode ";
                    Statement statement = conn.createStatement();
                    if (statement.execute(sql)) {
                        ResultSet rsData = statement.getResultSet();
                        while (rsData.next()) {
                            String DocType   = rsData.getString(3);
                            dTotalPoint      = rsData.getDouble(2);
                           /* if(DocType.equals("Increase")) {
                                dTotalPoint += rsData.getDouble(2);
                            }else{
                                dTotalPoint += rsData.getDouble(2);
                            }*/
                        }
                        PointBF=zeroDecimal(dTotalPoint);
                        z="success";
                    }
                }
                db.closeDB();
                return z;
            }catch (SQLiteException e){
                e.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }
            return z;
        }
    }

    private class SaveRedeem extends AsyncTask<Void,Void,String>{
        Context c;
        String CusCode,ItemCode,Description,UnitPrice,UOM,FactorQty,Point;
        String IPAddress,UserName,Password,
                DBName,Port,URL,
                z="error",DBStatus,ItemConn,
                EncodeType,CurCode;

        private SaveRedeem(Context c, String point) {
            this.c = c;
            Point = point;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.calcBF();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("error")){
                Toast.makeText(c,"Failure, Save Redeem", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(c,"Successful, Save Redeem", Toast.LENGTH_SHORT).show();
                fnsavevoucher(Point);
                fngenbarcode(ItemCode);
            }
        }
        private String calcBF() {
            try {
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                CurCode = "RM";
                String querySet="select ServerName, UserName, Password," +
                        " DBName, Port, ItemConn, " +
                        " EncodeType " +
                        " from tb_settingdb";
                Cursor curSet = db.getQuery(querySet);
                while (curSet.moveToNext()) {
                    IPAddress   = curSet.getString(0);
                    UserName    = curSet.getString(1);
                    Password    = curSet.getString(2);
                    DBName      = curSet.getString(3);
                    Port        = curSet.getString(4);
                    ItemConn    = curSet.getString(5);
                    EncodeType  = curSet.getString(6);
                }
                String D_ateTime=datedNow();
                String D_ate=datedShort();
                String Doc1No=datedTime();
                String queryCus="select CusCode,CusName from customer";
                Cursor rsCus=db.getQuery(queryCus);
                while(rsCus.moveToNext()){
                    CusCode=rsCus.getString(0);

                }
                Description ="Point Redeem";
                UOM         ="Point";
                ItemCode    =CusCode+"%F"+Point;
                UnitPrice   =Point;
                URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                if (conn != null) {
                        /*String insertHd="Insert into ret_pointredeem_hd(Doc1No,D_ate," +
                                "Remark,ClientCode,L_ink)values('"+Doc1No+"', '"+D_ateTime+"'," +
                                "'', '"+CusCode+"', '1')";
                        Statement statement = conn.createStatement();
                        statement.execute(insertHd);

                        String insertDt="Insert into ret_pointredeem_dt(Doc1No,ItemCode,Point," +
                                "UnitCost,Qty,FactorQty," +
                                "UOM,Description)values('"+Doc1No+"', '"+ItemCode+"', '"+Point+"'," +
                                "'"+UnitPrice+"', '1', '1'," +
                                "'"+UOM+"', '"+Description+"')";
                        Statement stmtDt = conn.createStatement();
                        stmtDt.execute(insertDt);*/

                        String vRemark=ItemCode;
                        String insertAdj="Insert into ret_pointadjustment(cuscode, D_ate, Point," +
                                " DocType, Remark, D_ateTime, " +
                                " Screen)values('"+CusCode+"', '"+D_ate+"', '-"+Point+"'," +
                                " 'Decrease', '"+vRemark+"', '"+D_ateTime+"'," +
                                " 'eMember01 - Generate Code' )";
                        Statement stmtAdj = conn.createStatement();
                        stmtAdj.execute(insertAdj);
                        z="success";
                }else{
                    z="error";
                }
                return z;
            } catch (SQLiteException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return z;
        }
    }

    private class SaveVoucher extends AsyncTask<Void,Void,String>{
        Context c;
        String CusCode,VoucherCode,
                DateFrom,UnitPrice,UOM,DateTo,Point;
        String IPAddress,UserName,Password,
                DBName,Port,URL,
                z="error",UserCode,ItemConn,
                EncodeType,CurCode,CounterCode;

        private SaveVoucher(Context c, String point) {
            this.c = c;
            Point = point;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.calcBF();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("error")){
                Toast.makeText(c,"Failure, Save Voucher", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(c,"Successful, Save Voucher", Toast.LENGTH_SHORT).show();

            }
        }
        private String calcBF() {
            try {
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                CurCode = "RM";
                String querySet="select ServerName, UserName, Password," +
                        " DBName, Port, ItemConn, " +
                        " EncodeType, UserCode, CounterCode " +
                        " from tb_settingdb";
                Cursor curSet = db.getQuery(querySet);
                while (curSet.moveToNext()) {
                    IPAddress   = curSet.getString(0);
                    UserName    = curSet.getString(1);
                    Password    = curSet.getString(2);
                    DBName      = curSet.getString(3);
                    Port        = curSet.getString(4);
                    ItemConn    = curSet.getString(5);
                    EncodeType  = curSet.getString(6);
                    UserCode    = curSet.getString(7);
                }
                String D_ateTime=datedNow();
                String D_ate=datedShort();
                String Doc1No=datedTime();
                String queryCus="select CusCode,CusName from customer";
                Cursor rsCus=db.getQuery(queryCus);
                while(rsCus.moveToNext()){
                    CusCode=rsCus.getString(0);

                }

                DateFrom=datedShort();
                DateTo  =fnaddmonth(1);
                URL     = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                if (conn != null) {
                    String qCheckLast="select RunNo from stk_voucher order by RunNo Desc limit 1";
                    Statement stmtLast=conn.createStatement();
                    stmtLast.execute(qCheckLast);
                    ResultSet rsLast=stmtLast.getResultSet();
                    int RunNo=0;
                    while(rsLast.next()){
                        RunNo=rsLast.getInt(1);
                    }
                    int LastNo      =RunNo+1;
                    VoucherCode     =Doc1No+LastNo;
                    String strSQL = "INSERT INTO stk_voucher (VoucherCode, VoucherCode2, Value, " +
                            "DateFrom, DateTo, T_ype, " +
                            "PaymentCode, CardNo, UserCode, " +
                            "CounterCode)values('"+VoucherCode+"', '', '"+Point+"', " +
                            "'"+DateFrom+"', '"+DateTo+"', 'mnuVoucher', " +
                            "'', '', '"+UserCode+"', " +
                            "'"+CounterCode+"') ";
                    Statement stmtVoucher = conn.createStatement();
                    stmtVoucher.execute(strSQL);
                    z="success";
                }else{
                    z="error";
                }
                return z;
            } catch (SQLiteException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return z;
        }


    }



    private void genCode(){
        try {
            DBAdapter db = new DBAdapter(this);
            db.openDB();
            String qCus = "select RunNoCus,CusCode,CusName from tb_member";
            Cursor rsCus = db.getQuery(qCus);
            String CusCode = "";
            String CusName = "";
            int RunNoCus = 0;
            while (rsCus.moveToNext()) {
                RunNoCus = rsCus.getInt(0);
                CusCode = rsCus.getString(1);
                CusName = rsCus.getString(2);
            }
            int d = Integer.parseInt(d_ay());
            int m = Integer.parseInt(m_onth());
            int y = Integer.parseInt(y_ear());
            long SecretCode = RunNoCus * d * m * y;
            txtMemberCode.setText(String.valueOf(SecretCode));
        }catch (SQLiteException e){
            e.printStackTrace();
        }

    }

    private String d_ay(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        String D_ay = sdf.format(date);
        return D_ay;
    }
    private String m_onth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = new Date();
        String M_onth = sdf.format(date);
        return M_onth;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private String y_ear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        Date date = new Date();
        String Y_ear = sdf.format(date);
        return Y_ear;
    }
    private String datedNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String D_ateTime = sdf.format(date);
        return D_ateTime;
    }
    private String datedShort(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String D_ateTime = sdf.format(date);
        return D_ateTime;
    }
    private String datedTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String D_ateTime = sdf.format(date);
        return D_ateTime;
    }
    private String zeroDecimal(Double values){
        String textDecimal="";
        textDecimal=String.format(Locale.US, "%,.0f", values);
        return textDecimal;
    }

    private String fnaddmonth(int calMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String curentDate = sdf.format(date);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(curentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, calMonth);
        Date resultdate = new Date(c.getTimeInMillis());
        String NewDate = sdf.format(resultdate);
        return NewDate;
    }


    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
    }

    private void fngenbarcode(String C_ode){
        Bitmap bmpBarcode   = BitmapUtil.generateBitmap(C_ode,4,375,100);
        //Drawable image      = new BitmapDrawable(Bitmap.createScaledBitmap(bmpBarcode, 270, 145, true));
        imgBarcode.setImageBitmap(bmpBarcode);
    }
}
