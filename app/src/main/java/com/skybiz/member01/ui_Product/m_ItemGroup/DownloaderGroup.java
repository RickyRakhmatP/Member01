package com.skybiz.member01.ui_Product.m_ItemGroup;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by 7 on 14/11/2017.
 */

public class DownloaderGroup extends AsyncTask<Void, Void, String> {
    Context c;
    RecyclerView rv;
    String IPAddress,UserName,Password,DBName,Port,URL,z,DBStatus,ItemConn;
    JSONObject jsonReq,jsonRes;
    public DownloaderGroup(Context c, RecyclerView rv) {
        this.c = c;
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
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        if(jsonData==null){
            Toast.makeText(c,"Unsuccessfull, No data retrieve", Toast.LENGTH_SHORT).show();
        }else{
            GroupParser p=new GroupParser(c,jsonData,rv);
            p.execute();
        }
    }

    private String downloadData(){
        try {
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

            URL = "jdbc:mysql://" + IPAddress + ":" + Port + "/" + DBName + "?useUnicode=yes&characterEncoding=ISO-8859-1";
            Connection conn = Connector.connect(URL, UserName, Password);
            if (conn != null) {
                String sql = "select ItemGroup,Description from stk_group Order By Description";
                JSONArray results = new JSONArray();
                Statement statement = conn.createStatement();
                statement.executeQuery("SET NAMES 'LATIN1'");
                statement.executeQuery("SET CHARACTER SET 'LATIN1'");
                if (statement.execute(sql)) {
                    ResultSet resultSet = statement.getResultSet();
                    ResultSetMetaData columns = resultSet.getMetaData();
                    while (resultSet.next()) {
                        JSONObject row = new JSONObject();
                        row.put("ItemGroup",resultSet.getString(1));
                        row.put("Description", resultSet.getString(2));
                        results.put(row);
                    }
                    resultSet.close();
                }
                Log.d("JSON",results.toString());
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

    /*private String encodeChar(String txt){
        String newText="";
        try{
            byte[] b=txt.getBytes("ISO-8859-1");
            newText=new String(b,"utf-8");
            return newText;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return newText;
    }*/
}
