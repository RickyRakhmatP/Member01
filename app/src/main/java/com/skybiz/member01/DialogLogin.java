package com.skybiz.member01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;


import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 7 on 23/02/2018.
 */

public class DialogLogin extends AppCompatDialogFragment {
    View view;
    Button btnOK;
    EditText txtPassword,txtEmail;
    String ItemCode,T_ype,UFrom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_login, container, false);
        btnOK=(Button)view.findViewById(R.id.btnOK);
        txtPassword=(EditText)view.findViewById(R.id.txtPassword);
        txtEmail=(EditText)view.findViewById(R.id.txtEmail);
        UFrom=this.getArguments().getString("UFROM_KEY");
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOK();
            }
        });
        return view;
    }

    private void fnOK() {
        String Email = txtEmail.getText().toString();
        String Password = txtPassword.getText().toString();
        if (Email.isEmpty()){
            Toast.makeText(getActivity(),"Email Cannot Empty", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
        }else if(Password.isEmpty()){
            Toast.makeText(getActivity(),"Invalid, Email or Password", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
        }else {
            CheckLogin checkLogin = new CheckLogin(getActivity(), Email, Password);
            checkLogin.execute();
        }

    }
    private void openMenu(){
        ((MainActivity)getActivity()).openMenu(UFrom);
        dismiss();
    }

    public class CheckLogin extends AsyncTask<Void,Void,String>{
        Context c;
        String vEmail,vPassword,vCusCode,vCusName;
        String IPAddress,UserName,Password,DBName,Port,URL,z="error";

        public CheckLogin(Context c, String email, String password) {
            this.c = c;
            vEmail = email;
            vPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.checklogin();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("empty")) {
                Toast.makeText(c, "Invalid, Email or Password", Toast.LENGTH_SHORT).show();
            }else if(result.equals("error")){
                    Toast.makeText(c,"Error Connection", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                openMenu();
            }
        }
        private String checklogin(){
            try{
                DBAdapter db = new DBAdapter(c);
                db.openDB();
                String querySet="select ServerName,UserName,Password," +
                        "DBName,Port "+
                        " from tb_settingdb";
                Cursor cur1=db.getQuery(querySet);
                while (cur1.moveToNext()) {
                    IPAddress   = cur1.getString(0);
                    UserName    = cur1.getString(1);
                    Password    = cur1.getString(2);
                    DBName      = cur1.getString(3);
                    Port        = cur1.getString(4);
                }
                cur1.close();
                URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
                Connection conn = Connector.connect(URL, UserName, Password);
                Log.d("URL",URL);
                if (conn != null) {
                    String qCheck = "select count(*)as numrows from customer where Email='" + vEmail + "' and P_assword='" + vPassword + "' ";
                    Statement statement = conn.createStatement();
                    statement.execute(qCheck);
                    ResultSet rsCheck = statement.getResultSet();
                    int numrows = 0;
                    while (rsCheck.next()) {
                        numrows = rsCheck.getInt(1);
                    }
                    if (numrows>0){
                        String qCustomer = "select CusCode,CusName,TermCode," +
                                "SalesPersonCode, '0' from  customer where Email='" + vEmail + "'" +
                                " and P_assword='" + vPassword + "' ";
                        Statement stmtCus = conn.createStatement();
                        stmtCus.execute(qCustomer);
                        ResultSet rsCus = stmtCus.getResultSet();
                        while (rsCus.next()) {
                            vCusCode = rsCus.getString(1);
                            vCusName = rsCus.getString(2);
                            String TermCode = rsCus.getString(3);
                            String SalesPersonCode = rsCus.getString(4);
                            String RunNo = rsCus.getString(5);
                            String insertMember="insert into tb_member(CusCode, CusName, TermCode, " +
                                    "D_ay, SalesPersonCode, RunNoCus)values('"+vCusCode+"', '"+vCusName+"', '"+TermCode+"', " +
                                    "'', '"+SalesPersonCode+"', '"+RunNo+"')";
                            db.exeQuery(insertMember);
                        }
                        z="success";
                    }else{
                        z="empty";
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
