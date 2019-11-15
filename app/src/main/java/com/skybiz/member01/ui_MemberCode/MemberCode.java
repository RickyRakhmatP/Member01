package com.skybiz.member01.ui_MemberCode;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skybiz.member01.R;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemberCode extends AppCompatActivity {

    TextView txtMemberCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_code);
        getSupportActionBar().setTitle("Member Code");
        txtMemberCode=(TextView) findViewById(R.id.txtMemberCode);
        txtMemberCode.setEnabled(false);
        initData();
    }
    private void initData(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                genCode();
            }
        }, 500);

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

}
