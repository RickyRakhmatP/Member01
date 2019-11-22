package com.skybiz.member01.ui_RegisterNew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterMember extends AppCompatActivity {

    TextInputEditText txtCusName,txtEmail,txtContactTel,txtDOB,txtCardNo,txtNRICNo,txtPassword,txtConfirmPassword;
    Button btnSave,btnCancel;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);
        getSupportActionBar().setTitle("Register New Member");
        txtCusName=(TextInputEditText)findViewById(R.id.txtCusName);
        txtEmail=(TextInputEditText)findViewById(R.id.txtEmail);
        txtContactTel=(TextInputEditText)findViewById(R.id.txtContactTel);
        txtDOB=(TextInputEditText)findViewById(R.id.txtDOB);
        txtCardNo=(TextInputEditText)findViewById(R.id.txtCardNo);
        txtNRICNo=(TextInputEditText)findViewById(R.id.txtNRICNo);
        txtPassword=(TextInputEditText)findViewById(R.id.txtPassword);
        txtConfirmPassword=(TextInputEditText)findViewById(R.id.txtConfirmPassword);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnreset();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnregister();
            }
        });
        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnshowcalendar();
            }
        });

        initData();
    }
    private void initData(){
        txtDOB.setText(datedShort());
    }
    private void fnshowcalendar(){
        final Calendar c=Calendar.getInstance();
        int mYear=c.get(Calendar.YEAR);
        int mMonth=c.get(Calendar.MONTH);
        int mDay=c.get(Calendar.DAY_OF_MONTH);
        final DecimalFormat mFormat= new DecimalFormat("00");
        datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                final Double dMonth=(monthOfYear+1)*1.00;
                final Double dDay=dayOfMonth*1.00;
                txtDOB.setText(year+"-"+mFormat.format(dMonth)+"-"+mFormat.format(dDay));
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    private void fnreset(){
        txtCusName.getText().clear();
        txtCardNo.getText().clear();
        txtContactTel.getText().clear();
        txtConfirmPassword.getText().clear();
        txtEmail.getText().clear();
        txtPassword.getText().clear();
        txtDOB.getText().clear();
        txtNRICNo.getText().clear();
    }
    private void fnregister(){
        btnSave.setEnabled(false);
        String CusName=txtCusName.getText().toString();
        String Email=txtEmail.getText().toString();
        String ContactTel=txtContactTel.getText().toString();
        String DOB=txtDOB.getText().toString();
        String Password=txtPassword.getText().toString();
        String ConfirmPassword=txtConfirmPassword.getText().toString();
        String NRICNo=txtNRICNo.getText().toString();
        String CardNo=txtCardNo.getText().toString();
        if(CusName.isEmpty()){
            Toast.makeText(this,"Name Cannot Empty", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }else if(Email.isEmpty()){
            Toast.makeText(this,"Email Cannot Empty", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }else if(Password.isEmpty()){
            Toast.makeText(this,"Password Cannot Empty", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }else if(ConfirmPassword.isEmpty()){
            Toast.makeText(this,"Confirm Password Cannot Empty", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }else{
            if(Password.equals(ConfirmPassword)){
                 RegisterNew registerNew=new RegisterNew(this,CusName,Email,ContactTel,DOB,Password,CardNo,NRICNo) ;
                 registerNew.execute();
            }else{
                Toast.makeText(this,"Password Not Match", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            }
        }
    }

    private class RegisterNew extends AsyncTask<Void,Void,String>{
        Context c;
        String CusName,Email,ContactTel,
                DOB,P_assword,CardNo,
                NRICNo,CusCode="";

        String IPAddress,UserName,Password,
                DBName,Port,URL,
                z="error",UserCode,ItemConn,
                EncodeType,CurCode,CounterCode;

        public RegisterNew(Context c, String cusName, String email, String contactTel, String DOB, String P_assword, String cardNo, String NRICNo) {
            this.c = c;
            CusName = cusName;
            Email = email;
            ContactTel = contactTel;
            this.DOB = DOB;
            this.P_assword = P_assword;
            CardNo = cardNo;
            this.NRICNo = NRICNo;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return this.fnsave();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("error")) {
                btnSave.setEnabled(true);
                Toast.makeText(c, "Connection Error", Toast.LENGTH_SHORT).show();
            }else if(s.equals("duplicate")){
                btnSave.setEnabled(true);
                Toast.makeText(c,"Email Already Exist !", Toast.LENGTH_SHORT).show();
            }else if(s.equals("success")){
                Toast.makeText(c,"Register New Member sucessful", Toast.LENGTH_SHORT).show();
                fnreset();
                btnSave.setEnabled(true);
            }
            btnSave.setEnabled(true);
        }
        private String fnsave(){
            try {
                String D_ateTime        =datedNow();
                String RegistrationDate =datedShort();
                String D_ate            =datedShort();
                String ExpirationDate   =fnaddmonth(12);
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                CurCode = "RM";
                String querySet = "select ServerName, UserName, Password," +
                        " DBName, Port, ItemConn, " +
                        " EncodeType, UserCode, CounterCode " +
                        " from tb_settingdb";
                Cursor curSet = db.getQuery(querySet);
                while (curSet.moveToNext()) {
                    IPAddress = curSet.getString(0);
                    UserName = curSet.getString(1);
                    Password = curSet.getString(2);
                    DBName = curSet.getString(3);
                    Port = curSet.getString(4);
                    ItemConn = curSet.getString(5);
                    EncodeType = curSet.getString(6);
                    UserCode = curSet.getString(7);
                }
                curSet.close();
                URL     = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                if (conn != null) {

                    String qDup="select count(*)as numrows from customer where Email='"+Email+"'  ";
                    Statement stmtDup=conn.createStatement();
                    stmtDup.execute(qDup);
                    ResultSet rsDup=stmtDup.getResultSet();
                    int numrows=0;
                    while (rsDup.next()) {
                        numrows=rsDup.getInt(1);

                    }
                    if(numrows==0) {
                        String qCheck = "select CusCode from customer where CusCode like 'EM%' order by CusCode desc limit 1 ";
                        Statement stmtCheck = conn.createStatement();
                        stmtCheck.execute(qCheck);
                        ResultSet rsCheck = stmtCheck.getResultSet();
                        int i = 0;
                        String NewCusCode = "EM0000001";
                        String OldCusCode = "";
                        while (rsCheck.next()) {
                            OldCusCode = rsCheck.getString(1);
                            i++;
                        }
                        if (i > 0) {
                            String xCusCode = "1" + OldCusCode.replaceAll("EM", "");
                            int iLastNo = Integer.parseInt(xCusCode);
                            int iNewLastNo = iLastNo + 1;
                            String NewLastNo = String.valueOf(iNewLastNo);
                            String vCusCode = NewLastNo.substring(1, NewLastNo.length());
                            CusCode = "EM"+vCusCode;
                        } else {
                            CusCode = NewCusCode;
                        }

                        String insert = "insert into customer(CusCode,CusName,FinCatCode," +
                                " AccountCode, Address, CurCode," +
                                " TermCode, D_ay, SalesPersonCode," +
                                " Tel, Tel2, Fax," +
                                " Fax2, Contact, ContactTel," +
                                " Email, StatusBadYN, Town," +
                                " State, Country, PostCode," +
                                " L_ink, NRICNo, DOB," +
                                " Sex, MemberType, CardNo," +
                                " PaymentCode, DateTimeModified, CategoryCode," +
                                " RegistrationDate, ExpirationDate, MaritialStatus," +
                                " Race, DateStart, P_assword," +
                                " CusCode2)values('" + CusCode + "', '" + CusName + "', 'B55'," +
                                " 'B55-0000', '', 'RM'," +
                                " '1', '0', ''," +
                                " '', '', ''," +
                                " '', '', '" + ContactTel + "'," +
                                " '" + Email + "', '0', ''," +
                                " '', '', ''," +
                                " '1', '" + NRICNo + "', '" + DOB + "'," +
                                " '', '', '" + CardNo + "'," +
                                " '', '" + D_ateTime + "', ''," +
                                " '" + RegistrationDate + "', '" + ExpirationDate + "', ''," +
                                " '', '" + D_ate + "', '" + P_assword + "', " +
                                " '"+CusCode+"')";
                        Log.d("QUERY", insert);
                        Statement stmtInsert = conn.createStatement();
                        stmtInsert.execute(insert);
                        z = "success";
                    }else{
                        z="duplicate";
                    }
                }else{
                    z="error";
                }
                return z;
            }catch (SQLiteException e){
                e.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }
            return z;
        }
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
}

