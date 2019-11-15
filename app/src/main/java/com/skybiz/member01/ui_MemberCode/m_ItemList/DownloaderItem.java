package com.skybiz.member01.ui_MemberCode.m_ItemList;

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
import java.util.Locale;


/**
 * Created by 7 on 16/04/2018.
 */

public class DownloaderItem extends AsyncTask<Void,Void,String> {
    Context c;
    String Keyword,DocType,SearchBy;
    RecyclerView rv;
    String IPAddress,UserName,Password,DBName,Port,URL,z,DBStatus,ItemConn,EncodeType;
    String CurCode;
    DialogItem dialogItem;
    JSONObject jsonReq,jsonRes;

    public DownloaderItem(Context c, String SearchBy, String Keyword, RecyclerView rv, DialogItem dialogItem) {
        this.c = c;
        this.DocType = DocType;
        this.SearchBy = SearchBy;
        this.Keyword = Keyword;
        this.rv = rv;
        this.dialogItem=dialogItem;
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
            ItemParser p=new ItemParser(c,result,rv,dialogItem);
            p.execute();
        }
    }

    private String downloadData(){
        try {
            DBAdapter db = new DBAdapter(c);
            db.openDB();
           /* Cursor cur = db.getGeneralSetup();
            while (cur.moveToNext()) {
                CurCode = cur.getString(1);
            }*/
            CurCode = "RM";
            String querySet="select ServerName,UserName,Password," +
                    "DBName,Port " +
                    " from tb_settingdb";
            Cursor curSet = db.getQuery(querySet);
            while (curSet.moveToNext()) {
                IPAddress = curSet.getString(0);
                UserName = curSet.getString(1);
                Password = curSet.getString(2);
                DBName = curSet.getString(3);
                Port = curSet.getString(4);
            }
            String vClause="";
            if(SearchBy.equals("By Code")){
                vClause="and M.ItemCode like '%"+Keyword+"%'";
            }else if(SearchBy.equals("By Desc")){
                vClause="and M.Description like '%"+Keyword+"%'";
            }
            URL = "jdbc:mysql://" + IPAddress +":"+Port+ "/" + DBName+"?useUnicode=yes&characterEncoding=ISO-8859-1";
            Connection conn= Connector.connect(URL, UserName, Password);
            if (conn != null) {
                String sql = "select '" + CurCode + "' as CurCode, '0' as Qty, IFNULL(M.UnitPrice,0) as UnitPrice," +
                            " M.ItemCode, M.Description, M.ItemGroup, " +
                            " IFNULL(G.Printer,'')as Printer, IFNULL(G.Modifier1,'')as Modifier, M.DefaultUOM, " +
                            " M.UOM, M.UOM1, M.UOM2, " +
                            " M.UOM3, M.UOM4, M.UOMFactor1," +
                            " M.UOMFactor2, M.UOMFactor3, M.UOMFactor4," +
                            " M.UOMPrice1, M.UOMPrice2, M.UOMPrice3, " +
                            " M.UOMPrice4, M.RetailTaxCode, M.AlternateItem, " +
                            " IFNULL(M.HCDiscount,0) as HCDiscount, IFNULL(M.DisRate1,'')as DisRate1," +
                            " IFNULL(P.Point,0) AS POINT " +
                            " from stk_master M inner join ret_point P on M.ItemCode=P.Item " +
                            " left join stk_group G ON M.ItemGroup=G.ItemGroup " +
                            " where M.SuspendedYN='0' "+vClause+" and P.DocType='Redeem'  " +
                            " Order By M.ItemCode ";
                Log.d("QUERY",sql);
                JSONArray results = new JSONArray();
                Statement statement = conn.createStatement();
                statement.executeQuery("SET NAMES 'LATIN1'");
                statement.executeQuery("SET CHARACTER SET 'LATIN1'");
                if (statement.execute(sql)) {
                    ResultSet resultSet = statement.getResultSet();
                    ResultSetMetaData columns = resultSet.getMetaData();
                    while (resultSet.next()) {
                            Double dUnitPrice=resultSet.getDouble(3);
                            String UnitPrice     =  String.format(Locale.US, "%,.2f", dUnitPrice);
                            JSONObject row = new JSONObject();
                            row.put("CurCode",resultSet.getString(1));
                            row.put("Qty",resultSet.getString(2));
                            row.put("UnitPrice",UnitPrice);
                            row.put("ItemCode",resultSet.getString(4));
                            row.put("Description", resultSet.getString(5));
                            row.put("ItemGroup",resultSet.getString(6));
                            row.put("Printer",resultSet.getString(7));
                            row.put("Modifier1",resultSet.getString(8));
                            row.put("DefaultUOM",resultSet.getString(9));
                            row.put("UOM",resultSet.getString(10));
                            row.put("UOM1",resultSet.getString(11));
                            row.put("UOM2",resultSet.getString(12));
                            row.put("UOM3",resultSet.getString(13));
                            row.put("UOM4",resultSet.getString(14));
                            row.put("UOMFactor1",resultSet.getString(15));
                            row.put("UOMFactor2",resultSet.getString(16));
                            row.put("UOMFactor3",resultSet.getString(17));
                            row.put("UOMFactor4",resultSet.getString(18));
                            row.put("UOMPrice1",resultSet.getString(19));
                            row.put("UOMPrice2",resultSet.getString(20));
                            row.put("UOMPrice3",resultSet.getString(21));
                            row.put("UOMPrice4",resultSet.getString(22));
                            row.put("RetailTaxCode",resultSet.getString(23));
                            row.put("AlternateItem",resultSet.getString(24));
                            row.put("HCDiscount",resultSet.getString(25));
                            row.put("DisRate1",resultSet.getString(26));
                            row.put("Point",zeroDecimal(resultSet.getDouble(27)));
                            results.put(row);
                        }
                        resultSet.close();
                }
                statement.close();
                Log.d("JSON",results.toString());
                return results.toString();
            }
        }catch (SQLException e ){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String charReplace(String text){
        String newText=text.replaceAll("[\\.$|,|;|']","");
        return newText;
    }
    private String zeroDecimal(Double values){
        String textDecimal="";
        textDecimal= String.format(Locale.US, "%,.0f", values);
        return textDecimal;
    }
}
