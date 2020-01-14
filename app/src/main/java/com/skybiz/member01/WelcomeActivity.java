package com.skybiz.member01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private Session session;
    EditText txtStoreCode;

    String activeName;
    List<String> disableNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        // Checking for first time launch - before calling setContentView()
        session = new Session(this);
        if (!session.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
       // btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        txtStoreCode=(EditText)findViewById(R.id.txtStoreCode) ;


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        /*btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);

                if (current < layouts.length) {
                    // move to next screen
                   /* if(current==1) {
                        //viewPager.setCurrentItem(current);
                    }else{
                        viewPager.setCurrentItem(current);
                    }*/
                    viewPager.setCurrentItem(current);
                } else {
                    fngetlicense();
                    //launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        session.setFirstTimeLaunch(false);
        setAppIcon("com.skybiz.member01.Jati");
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                txtStoreCode.setVisibility(View.VISIBLE);
               // btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                txtStoreCode.setVisibility(View.GONE);
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void setAppIcon(String activeName) {

        getPackageManager().setComponentEnabledSetting(
                    new ComponentName("com.skybiz.member01", activeName),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        getPackageManager().setComponentEnabledSetting(
                new ComponentName("com.skybiz.member01", "com.skybiz.member01.FirstOpen"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }


    private void fngetlicense(){
        String StoreCode=txtStoreCode.getText().toString();
        if(StoreCode.isEmpty()) {
            Toast.makeText(this,"Store Code Cannot Empty", Toast.LENGTH_SHORT).show();
        }else{
            ConnLicense connLicense = new ConnLicense(this, StoreCode);
            connLicense.execute();
        }
    }
    private class ConnLicense extends AsyncTask<Void,Void,String> {
        Context c;
        String z,IPAddress,UserName,Password,DBName,Port;
        String StoreCode="";
        // TelephonyManager telephonyManager;
        private ConnLicense(Context c, String StoreCode) {
            this.c = c;
            this.StoreCode=StoreCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                z="success";
                URL url = new URL("http://skybiz.com.my/userlicensesetting/androidlicense.txt");
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                z="";
                int i=0;
                while ((str = in.readLine()) != null) {
                    Log.d("STRING",str);
                    fngetStr(i,str);
                    z +=str;
                    i++;
                    // str is one line of text; readLine() strips the newline character(s)
                }
                in.close();
                return z;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return z;
        }

        @Override
        protected void onPostExecute(String isConnect) {
            super.onPostExecute(isConnect);
            if(isConnect.equals("error")){
                Toast.makeText(c,"Get Conn License Failure", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(c,"Success Conn License", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CheckLicenese checkLicenese=new CheckLicenese(c,IPAddress,UserName,
                                Password,DBName,Port,StoreCode);
                        checkLicenese.execute();
                    }
                }, 1700);
            }
        }
        private void fngetStr(int no,String str){
            int position = str.indexOf("=")+1;
            switch (no) {
                case 0:
                    int len0=str.length();
                    IPAddress=str.substring(position,len0);
                    break;
                case 1:
                    //Log.d("STR",str+String.valueOf(position)+String.valueOf(str.length()-position));
                    int len1=str.length();
                    Port=str.substring(position,len1);
                    break;
                case 2:
                    int len2=str.length();
                    UserName=str.substring(position,len2);
                    break;
                case 3:
                    int len3=str.length();
                    Password=str.substring(position,len3);
                    break;
                case 4:
                    int len4=str.length();
                    DBName=str.substring(position,len4);
                    break;
            }
        }
    }

    public class CheckLicenese extends AsyncTask<Void,Void,String>{
        Context c;
        String z="error",IPAddress,UserName,Password,DBName,Port,StoreCode;
        String vIPAddress,vUserName,vPassword,
                vDBName,vPort,vCompanyName,
                vOnlineYN,vMgt01YN,vBranchCode,
                vLocationCode,vModules,vDepartmentCode,
                vCompCustomerYN,vAdminPassword,vCategoryCode,
                vDirectPrintYN,vSalesPersonCode,vUserCode,vCounterCode,
                vCompanyAddress,vCloudSettingYN,vFastKeypadYN,msgError="";
        DBAdapter db=null;
        String vURL="";
        // TelephonyManager telephonyManager;

        public CheckLicenese(Context c, String IPAddress, String userName,
                             String password, String DBName, String port,
                             String StoreCode) {
            this.c = c;
            this.IPAddress = IPAddress;
            UserName = userName;
            Password = password;
            this.DBName = DBName;
            Port = port;
            this.StoreCode = StoreCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.checkLicense();
        }

        @Override
        protected void onPostExecute(String isConnect) {
            super.onPostExecute(isConnect);
            if(isConnect.equals("error")){
                Toast.makeText(c,"Error Connection - "+vURL+" :"+msgError, Toast.LENGTH_SHORT).show();
            }else if(isConnect.equals("ada")){
                Toast.makeText(c,"Success get setting server", Toast.LENGTH_SHORT).show();
                launchHomeScreen();
            }else if (isConnect.equals("kosong")){
                Toast.makeText(c,"Information not available, please request vendor to register the device!", Toast.LENGTH_SHORT).show();
            }else if(isConnect.equals("expired")){
                Toast.makeText(c,"Expiry Date, please request vendor to update renewal expiry date !", Toast.LENGTH_SHORT).show();
            }
        }
        private String  checkLicense(){
            try{
                db=new DBAdapter(c);
                db.openDB();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String datedNow = sdf.format(date);
                String URLc="jdbc:mysql://"+IPAddress+":"+Port+"/"+DBName;
                vURL="jdbc:mysql://"+IPAddress+":"+Port+"/"+DBName;
                Connection conn= Connector.connect(URLc, UserName, Password);
                if (conn == null) {
                    Log.d("ERROR",URLc+UserName+Password);
                    z = "error";
                }else{
                    String qExp="select count(*)as expyn from androidlicense where MACAddress='member01' and StoreCode='"+StoreCode+"' and NextRenewalDate>='"+datedNow+"' ";
                    Statement stmtExp     = conn.createStatement();
                    stmtExp.execute(qExp);
                    ResultSet rsExp      = stmtExp.getResultSet();
                    int expyn=0;
                    while(rsExp.next()){
                        expyn=rsExp.getInt(1);
                    }
                    if(expyn>0) {
                        String numrow="0";
                        String vCheck = "SELECT COUNT(MACAddress)as NumRows, DB_IP, DB_ID, " +
                                "DB_Password, DB_Port, DatabaseName," +
                                "OnlineYN, CompanyName, Mgt01YN," +
                                "IFNULL(BranchCode,'')as BranchCode, IFNULL(LocationCode,'') as LocationCode, " +
                                "IFNULL(CounterCode,'') as CounterCode, IFNULL(Modules,'') as Modules, " +
                                "IFNULL(DepartmentCode, '')as DepartmentCode," +
                                "CompCustomerYN, AdminPassword, CategoryCode, " +
                                "SalesPersonCode, DirectPrintYN, IFNULL(UserCode,'')as UserCode," +
                                "IFNULL(CompanyAddress,'')as CompanyAddress, CloudSettingYN, " +
                                "FastKeypadYN " +
                                "from androidlicense where  MACAddress='member01' and StoreCode='"+StoreCode+"'  ";
                        Log.d("QUERY",vCheck);
                        Statement statement = conn.createStatement();
                        statement.execute(vCheck);
                        ResultSet rsDB = statement.getResultSet();
                        while (rsDB.next()) {
                            numrow = rsDB.getString("NumRows");
                            if (!numrow.equals("0")) {
                                z = "ada";
                                vIPAddress  = rsDB.getString("DB_IP");
                                vUserName   = rsDB.getString("DB_ID");
                                vPort       = rsDB.getString("DB_Port");
                                vPassword   = rsDB.getString("DB_Password");
                                vDBName     = rsDB.getString("DatabaseName");
                                vOnlineYN   = rsDB.getString("OnlineYN");
                                vCompanyName = rsDB.getString("CompanyName");
                                vMgt01YN    = rsDB.getString("Mgt01YN");
                                vBranchCode = rsDB.getString("BranchCode");
                                vLocationCode = rsDB.getString("LocationCode");
                                vModules = rsDB.getString("Modules");
                                vDepartmentCode = rsDB.getString("DepartmentCode");
                                vCompCustomerYN = rsDB.getString("CompCustomerYN");
                                vAdminPassword = rsDB.getString("AdminPassword");
                                vCategoryCode = rsDB.getString("CategoryCode");
                                vSalesPersonCode = rsDB.getString("SalesPersonCode");
                                vDirectPrintYN = rsDB.getString("DirectPrintYN");
                                vUserCode = rsDB.getString("UserCode");
                                vCounterCode = rsDB.getString("CounterCode");
                                vCloudSettingYN = rsDB.getString("CloudSettingYN");
                                vCompanyAddress = rsDB.getString("CompanyAddress");
                                vFastKeypadYN = rsDB.getString("FastKeypadYN");

                                String vDelete = "delete from tb_settingdb";
                                db.exeQuery(vDelete);
                                String query = "insert into tb_settingdb(ServerName,UserName,Password," +
                                        "DBName,Port, ConnYN," +
                                        "DBStatus, ItemConn, PostAs," +
                                        "EncodeType,ReceiptType,Mgt01YN," +
                                        "BranchCode, LocationCode, Modules," +
                                        "DepartmentCode,CompCustomerYN,AdminPassword," +
                                        "CategoryCode,SalesPersonCode,DirectPrintYN," +
                                        "UserCode,CounterCode,FastKeypadYN)" +
                                        "values('" + vIPAddress + "', '" + vUserName + "', '" + vPassword + "'," +
                                        " '" + vDBName + "', '" + vPort + "', '1'," +
                                        " '" + vOnlineYN + "', '" + vOnlineYN + "','0'," +
                                        " 'UTF-8', 'Normal','" + vMgt01YN + "'," +
                                        " '" + vBranchCode + "', '" + vLocationCode + "', '" + vModules + "'," +
                                        " '" + vDepartmentCode + "','" + vCompCustomerYN + "', '" + vAdminPassword + "'," +
                                        " '" + vCategoryCode + "','" + vSalesPersonCode + "', '" + vDirectPrintYN + "'," +
                                        " '" + vUserCode + "', '" + vCounterCode + "', '"+vFastKeypadYN+"')";
                                Log.d("INSERT SETTING", query);
                                db.exeQuery(query);
                            } else {
                                z = "kosong";
                            }
                        }
                    }else{
                        z="expired";
                    }
                }
                db.closeDB();
            }catch (SQLException e){
                e.printStackTrace();
                msgError=e.getMessage();
            }catch (SQLiteException e){
                e.printStackTrace();
            }
            return z;
        }
    }




}
