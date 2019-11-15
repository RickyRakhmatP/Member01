package com.skybiz.member01.ui_ForgotPassword;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skybiz.member01.MainActivity;
import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;
import com.skybiz.member01.m_Email.GmailSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForgotPassword extends AppCompatActivity {


    EditText txtEmail;
    Button btnBack,btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot Password");
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        btnBack=(Button)findViewById(R.id.btnBack);
        btnSave=(Button)findViewById(R.id.btnSave);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendPassword();
            }
        });
    }

    private void disabledSave(){
        btnSave.setEnabled(false);
        btnSave.setBackgroundColor(getResources().getColor(R.color.colorBlack));
    }
    private void enabledSave(){
        btnSave.setEnabled(true);
        btnSave.setBackgroundColor(getResources().getColor(R.color.colorOrange));
        txtEmail.getText().clear();
    }
    private void sendPassword(){
        String Email=txtEmail.getText().toString();
        if(Email.isEmpty()){
            Toast.makeText(this, "Email Cannot Empty", Toast.LENGTH_SHORT).show();
        }else{
            SendPassword sendPassword=new SendPassword(this,Email);
            sendPassword.execute();
            disabledSave();
        }
    }

    public class SendPassword extends AsyncTask<Void,Void,String>{
        Context c;
        String Email,vPassword;
        String z;
        String IPAddress,DBName,UserName,Password,URL,Port;

        public SendPassword(Context c, String email) {
            this.c = c;
            Email = email;
        }


        @Override
        protected String doInBackground(Void... voids) {
            return this.fnsendemail();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("empty")){
                Toast.makeText(c, "Email cannot found", Toast.LENGTH_SHORT).show();
            }else if(s.equals("success")){
                SenderEmail(Email,vPassword);
                enabledSave();
                Toast.makeText(c, "Please check your Email later", Toast.LENGTH_SHORT).show();

            }
        }
        private String fnsendemail(){
            try{

                DBAdapter db=new DBAdapter(c);
                db.openDB();
                String querySet="select ServerName,UserName,Password," +
                        "DBName,Port "+
                        " from tb_settingdb";
                Cursor cur1=db.getQuery(querySet);
                while (cur1.moveToNext()) {
                    IPAddress = cur1.getString(0);
                    UserName = cur1.getString(1);
                    Password = cur1.getString(2);
                    DBName = cur1.getString(3);
                    Port= cur1.getString(4);
                }

                URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1&rewriteBatchedStatements=true";
                Connection conn = Connector.connect(URL, UserName, Password);
                Log.d("URL",URL+UserName+Password);
                if(conn!=null) {
                    String sqlCheck="select P_assword from customer where Email='"+Email+"' ";
                    Statement stmt = conn.createStatement();
                    stmt.execute(sqlCheck);
                    ResultSet rsCheck =stmt.getResultSet();
                    int numrows=0;

                    while (rsCheck.next()) {
                        vPassword=rsCheck.getString(1);
                        numrows++;
                    }
                    if(numrows>0) {
                        z="success";
                    }else{
                        z="empty";
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

    private void SenderEmail(final String Email, final String vPassword){
        /*MailService mailer = new MailService("web1rickyrakhmat@gmail.com","web1@skybiz.com.my","Subject","TextBody", "<b>HtmlBody</b>");
        try {
            mailer.sendAuthenticated();
        } catch (Exception e) {
            Log.d("Error", "Failed sending email.", e);
        }*/
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender("designersatu@gmail.com",
                            "permatabuana");
                    sender.sendMail("Member Forgot Password", "Your Email : "+Email+" \r\n Your Password : "+vPassword,
                            "designersatu@gmail.com", "web1@skybiz.com.my,"+Email);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }

}
