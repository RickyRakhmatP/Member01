package com.skybiz.member01.ui_Point.m_History;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;
import com.skybiz.member01.ui_Point.PointLedger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


/**
 * Created by 7 on 16/04/2018.
 */

public class DownloadHistory extends AsyncTask<Void,Void,String> {
    Context c;
    String CusCode;
    RecyclerView rv;
    String IPAddress,UserName,Password,DBName,Port,URL,z,DBStatus,ItemConn;
    String CurCode;
    String TotalPoint;
    public DownloadHistory(Context c, String CusCode, RecyclerView rv) {
        this.c = c;
        this.CusCode = CusCode;
        this.rv = rv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result==null){
            Toast.makeText(c,"Unsuccessfull, No data retrieve", Toast.LENGTH_SHORT).show();
        }else{
            ((PointLedger)c).setHeader(CusCode,CurCode,TotalPoint);
            HistoryParser p=new HistoryParser(c,result,rv);
            p.execute();
        }
    }

    private String downloadData(){
        try {
            CurCode="RM";
            DBAdapter db = new DBAdapter(c);
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
            Double dTotalPoint=0.00;
            URL = "jdbc:mysql://" + IPAddress +":"+Port+ "/" + DBName+"?useUnicode=yes&characterEncoding=ISO-8859-1";
            Connection conn= Connector.connect(URL, UserName, Password);
            if (conn != null) {
                String sql ="Select DATE_FORMAT(P.D_ateTime,'%Y-%m-%d %H:%i:%s') as D_ateTime,IFNULL(P.Point,0) as Point,P.DocType," +
                            " P.Remark,C.CusCode,C.CusName, '"+CurCode+"' as CurCode "+
                            " from ret_pointadjustment P inner join customer C " +
                            " on P.cuscode=C.CusCode where C.CusCode='"+CusCode+"' order by P.RunNo desc ";
                Log.d("QUERY",sql);
                JSONArray results = new JSONArray();
                Statement statement = conn.createStatement();
                if (statement.execute(sql)) {
                    ResultSet rsData = statement.getResultSet();
                    while (rsData.next()) {
                        JSONObject row = new JSONObject();
                        row.put("D_ate",rsData.getString(1));
                        row.put("Point",rsData.getString(2));
                        row.put("DocType",rsData.getString(3));
                        row.put("Remark",rsData.getString(4));
                        row.put("CusCode",rsData.getString(5));
                        row.put("CusName",rsData.getString(6));
                        row.put("CurCode",rsData.getString(7));
                        String DocType=rsData.getString(3);
                        if(DocType.equals("Increase")) {
                            dTotalPoint += rsData.getDouble(2);
                        }else{
                            dTotalPoint -= rsData.getDouble(2);
                        }
                        results.put(row);
                    }
                    TotalPoint=zeroDecimal(dTotalPoint);
                    rsData.close();
                }
                statement.close();
                return results.toString();
            }
        }catch (SQLException e ){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String twoDecimal(Double values){
        String textDecimal="";
        textDecimal= String.format(Locale.US, "%,.2f", values);
        return textDecimal;
    }
    private String zeroDecimal(Double values){
        String textDecimal="";
        textDecimal= String.format(Locale.US, "%,.0f", values);
        return textDecimal;
    }
}
