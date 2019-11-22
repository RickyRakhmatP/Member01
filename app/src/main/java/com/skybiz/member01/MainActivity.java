package com.skybiz.member01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.ui_ForgotPassword.ForgotPassword;
import com.skybiz.member01.ui_MemberCode.MemberCode;
import com.skybiz.member01.ui_MyProfile.MyProfile;
import com.skybiz.member01.ui_Point.PointLedger;
import com.skybiz.member01.ui_Product.MenuProduct;
import com.skybiz.member01.ui_RegisterNew.RegisterMember;

public class MainActivity extends AppCompatActivity {


    LinearLayout lnRegister,lnPoint,lnMemberCode,lnLogOut,
            lnMyProfile,lnForgotPassword;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.app_ver);
        lnPoint=(LinearLayout)findViewById(R.id.lnPoint);
        lnRegister=(LinearLayout)findViewById(R.id.lnRegister);
        lnMemberCode=(LinearLayout)findViewById(R.id.lnMemberCode);
        lnForgotPassword=(LinearLayout)findViewById(R.id.lnForgotPassword);
        lnMyProfile=(LinearLayout)findViewById(R.id.lnMyProfile);
        lnLogOut=(LinearLayout)findViewById(R.id.lnLogOut);


        lnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this, RegisterMember.class);
                startActivity(mainIntent);
            }
        });
        lnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin("Point Ledger");
                //Intent mainIntent = new Intent(MainActivity.this, PointLedger.class);
                //startActivity(mainIntent);
            }
        });
        lnMemberCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin("Member Code");
                //Intent mainIntent = new Intent(MainActivity.this, PointLedger.class);
                //startActivity(mainIntent);
            }
        });

        lnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(mainIntent);
            }
        });
        lnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin("My Profile");
                //Intent mainIntent = new Intent(MainActivity.this, MyProfile.class);
                // startActivity(mainIntent);
            }
        });

        lnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showConfirm();
            }
        });
        initData();
        //insertSetting();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            deleteMember();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void showConfirm(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you really want to log out? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                        finish();
                        System.exit(0);
                    }
                });
        alertDialog.show();

    }

    private void initData(){
        initView();
        fncheckmodules();
        checkAlter();
    }
    private void deleteMember(){
        try{
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String qDelete="delete from tb_member";
            db.exeQuery(qDelete);
            String qDeleteCus="delete from customer";
            db.exeQuery(qDeleteCus);
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
    private void  openLogin(String uFrom){
        try {
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String check="select count(*)as numrows from customer";
            Cursor rsCheck=db.getQuery(check);
            int numrows=0;
            while(rsCheck.moveToNext()){
                numrows=rsCheck.getInt(0);
            }
            if(numrows==0) {
                Bundle b = new Bundle();
                b.putString("UFROM_KEY", uFrom);
                DialogLogin dialogLogin = new DialogLogin();
                dialogLogin.setArguments(b);
                dialogLogin.show(getSupportFragmentManager(), "mListItem");
            }else {
                openMenu(uFrom);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void openMenu(String uFrom){
        Intent mainIntent;
        if (uFrom.equals("Point Ledger")){
            mainIntent = new Intent(MainActivity.this, PointLedger.class);
            startActivity(mainIntent);
        }else if(uFrom.equals("Member Code")){
            mainIntent = new Intent(MainActivity.this, MemberCode.class);
            startActivity(mainIntent);
        } else if(uFrom.equals("My Profile")){
            mainIntent = new Intent(MainActivity.this, MyProfile.class);
            startActivity(mainIntent);
        }
    }
    private void insertSetting(){
        try{
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String check="select count(*)as numrows from tb_settingdb";
            Cursor rsCheck=db.getQuery(check);
            int numrows=0;
            while(rsCheck.moveToNext()){
                numrows=rsCheck.getInt(0);
            }
            if(numrows==0){
                String insert="insert into tb_settingdb(ServerName,UserName,Password," +
                        "DBName,Port)values('101.99.66.125', 'vmairgas_celes', 'f1HMaf8=PWp.', " +
                        "'vmairgas_celes', '3306')";
                db.exeQuery(insert);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void fncheckmodules(){
        try{
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String qModule="select Modules from tb_settingdb";
            Cursor rsModule=db.getQuery(qModule);
            String lisModule="";
            while(rsModule.moveToNext()){
                lisModule=rsModule.getString(0);
            }
            String[] modules = lisModule.split(";");
            for (String add : modules) {
                showModule(add);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    private void initView(){
        lnRegister.setVisibility(View.GONE);
        lnPoint.setVisibility(View.GONE);
        lnMemberCode.setVisibility(View.GONE);
        lnMyProfile.setVisibility(View.GONE);
        lnForgotPassword.setVisibility(View.GONE);
        lnLogOut.setVisibility(View.GONE);
    }
    /*
    Point;Member Code;My Profile;Forgot Password;Logout;
     */
    private void showModule(String module){
        switch (module) {
            case "Point":
                lnPoint.setVisibility(View.VISIBLE);
                break;
            case "Member Code":
                lnMemberCode.setVisibility(View.VISIBLE);
                break;
            case "My Profile":
                lnMyProfile.setVisibility(View.VISIBLE);
                break;
            case "Forgot Password":
                lnForgotPassword.setVisibility(View.VISIBLE);
            case "Logout":
                 lnLogOut.setVisibility(View.VISIBLE);
                break;
            case "Register New":
                lnRegister.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void checkAlter() {
        try {
            DBAdapter db = new DBAdapter(this);
            db.openDB();
            if (db.isColumnExists("customer", "P_assword") != true) {
                String AlterTable = "ALTER TABLE customer ADD COLUMN P_assword TEXT default '' ";
                db.exeQuery(AlterTable);
            }
            if (db.isColumnExists("customer", "CategoryCode") != true) {
                String AlterTable = "ALTER TABLE customer ADD COLUMN CategoryCode TEXT default '' ";
                db.exeQuery(AlterTable);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
