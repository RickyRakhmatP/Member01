package com.skybiz.member01.ui_Point;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.ui_Point.m_History.DownloadHistory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PointLedger extends AppCompatActivity {


    private GridLayoutManager lLayout;
    RecyclerView rv;
    TextView txtDate,txtDescPoint,txtPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_ledger);
        getSupportActionBar().setTitle("Point Ledger");
        rv=(RecyclerView)findViewById(R.id.rvHistoryPoint);
        txtDate=(TextView) findViewById(R.id.txtDate);
        txtDescPoint=(TextView) findViewById(R.id.txtDescPoint);
        txtPoint=(TextView) findViewById(R.id.txtPoint);
        initData();
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

    private void initData(){
        try{
            DBAdapter db=new DBAdapter(this);
            db.openDB();
            String qCustomer="select CusCode,CusName from tb_member";
            Cursor rsCus=db.getQuery(qCustomer);
            while(rsCus.moveToNext()){
                String CusCode=rsCus.getString(0);
                String CusName=rsCus.getString(1);
                retHistory(CusCode,CusName);
            }
            db.closeDB();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
    public void retHistory(String CusCode, String CusName){
        getSupportActionBar().setTitle(CusName);
        rv.setHasFixedSize(true);
        lLayout = new GridLayoutManager(this, 1);
        rv.setLayoutManager(lLayout);
        rv.setItemAnimator(new DefaultItemAnimator());
        DownloadHistory downloadHistory=new DownloadHistory(this,CusCode,rv);
        downloadHistory.execute();
    }
    public void setHeader(String CusCode,String CurCode, String TotalPoint){
        txtPoint.setText(TotalPoint+"P");
        txtDescPoint.setText("Total Points Available :");
        txtDate.setText(datedNow());
    }


    private String datedNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String D_ateTime = sdf.format(date);
        return D_ateTime;
    }


}
