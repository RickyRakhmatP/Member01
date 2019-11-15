package com.skybiz.member01.ui_MyProfile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyProfile extends AppCompatActivity {

    TextView txtCusName,txtEmail,txtPassword,
            txtContactTel,txtDOB,txtCusCode,
            txtCategoryCode,txtNRIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setTitle("My Profile");

        txtCusName=(TextView)findViewById(R.id.txtCusName);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtPassword=(TextView)findViewById(R.id.txtPassword);
        txtContactTel=(TextView)findViewById(R.id.txtContactTel);
        txtDOB=(TextView)findViewById(R.id.txtDOB);
        txtCusCode=(TextView)findViewById(R.id.txtCusCode);
        txtCategoryCode=(TextView)findViewById(R.id.txtCategoryCode);
        txtNRIC=(TextView)findViewById(R.id.txtNRIC);

        retData();
    }
    private void retData(){
        try{
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String qCustomer="select CusCode,CusName from tb_member";
            Cursor rsCus=db.getQuery(qCustomer);
            while(rsCus.moveToNext()){
                String CusCode=rsCus.getString(0);
                String CusName=rsCus.getString(1);
                RetCustomer retCustomer=new RetCustomer(this,CusCode);
                retCustomer.execute();
               //retHistory(CusCode,CusName);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public class RetCustomer extends AsyncTask<Void,Void,String>{
        Context c;
        String CusCode;
        String IPAddress,UserName,Password,DBName,Port,URL,z;
        String vCusName,vEmail,vDOB,
                vNRIC,vContactTel,vCategoryCode,
                vPassword;

        public RetCustomer(Context c, String cusCode) {
            this.c = c;
            CusCode = cusCode;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return this.fngetcustomer();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("error")){
                Toast.makeText(c,"Error get data Member", Toast.LENGTH_SHORT).show();
            }else if(s.equals("success")){
                txtCusCode.setText(CusCode);
                txtCusName.setText(vCusName);
                txtEmail.setText(vEmail);
                txtContactTel.setText(vContactTel);
                txtDOB.setText(vDOB);
                txtNRIC.setText(vNRIC);
                txtCategoryCode.setText(vCategoryCode);
                txtPassword.setText(vPassword);
            }
        }
        private String fngetcustomer(){
            try{
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                String querySet="select ServerName,UserName,Password," +
                        "DBName,Port " +
                        "from tb_settingdb";
                Cursor curSet = db.getQuery(querySet);
                while (curSet.moveToNext()) {
                    IPAddress = curSet.getString(0);
                    UserName = curSet.getString(1);
                    Password = curSet.getString(2);
                    DBName = curSet.getString(3);
                    Port = curSet.getString(4);
                }
                Double dTotalPoint=0.00;
                URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                if (conn != null) {
                    String sql ="Select CusName,Email,ContactTel," +
                            " NRICNo, DOB, CategoryCode, P_assword from customer" +
                            " where CusCode='"+CusCode+"'  ";
                    Statement statement = conn.createStatement();
                    statement.execute(sql);
                    ResultSet rsData = statement.getResultSet();
                    int i=0;
                    while (rsData.next()) {
                        vCusName        =rsData.getString(1);
                        vEmail          =rsData.getString(2);
                        vContactTel     =rsData.getString(3);
                        vNRIC           =rsData.getString(4);
                        vDOB            =rsData.getString(5);
                        vCategoryCode   =rsData.getString(6);
                        vPassword       =rsData.getString(7);
                        i++;
                    }
                    if(i>0){
                        z="success";
                    }else{
                        z="error";
                    }
                }else{
                    z="error";
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
}
