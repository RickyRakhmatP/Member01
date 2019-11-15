package com.skybiz.member01.ui_Product.m_Item;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.skybiz.member01.m_Database.m_Local.DBAdapter;
import com.skybiz.member01.m_Database.m_Server.Connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

/**
 * Created by 7 on 14/11/2017.
 */

public class DownloaderItem extends AsyncTask<Void, Void, String> {
    Context c;
    String IPAddress,DBName,UserName,Password,URL,ItemGroup,Port,DBStatus,Printer;
    RecyclerView rv;

    public DownloaderItem(Context c, String ItemGroup, RecyclerView rv) {
        this.c = c;
        this.ItemGroup = ItemGroup;
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
            //parse
            ItemParser p=new ItemParser(c,jsonData,rv);
            p.execute();
        }
    }

    private String downloadData(){
        Connection conn=null;
        Statement stmtG=null;
        Statement stmt=null;
        ResultSet rsGroup=null;
        ResultSet rsItem=null;
        try {
            DBStatus="0";
            Printer="";
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
            conn = Connector.connect(URL, UserName, Password);
            Log.d("URL",URL+UserName+Password);
            if(conn!=null){
                if(ItemGroup.equals("")){
                    String sqlGroup="select ItemGroup from stk_group order by ItemGroup limit 1";
                    stmtG = conn.createStatement();
                    stmtG.execute(sqlGroup);
                    rsGroup=stmtG.getResultSet();
                    while (rsGroup.next()) {
                        ItemGroup=rsGroup.getString("ItemGroup");
                            //Printer=rsGroup.getString("Printer");
                    }
                    stmtG.close();
                }
                String sql="select M.ItemCode,SUBSTRING(M.Description,1,30) as Description, IFNULL(M.UnitPrice,'0')as UnitPrice, " +
                            " M.ItemGroup, M.UOM, M.RetailTaxCode, " +
                            " M.DefaultUOM, M.UOM1, M.UOM2," +
                            " M.UOM3, M.UOM4, M.UOMFactor1," +
                            " M.UOMFactor2, M.UOMFactor3, M.UOMFactor4," +
                            " M.UOMPrice1, M.UOMPrice2, M.UOMPrice3," +
                            " M.UOMPrice4, G.Printer, M.AlternateItem, IFNULL(M.HCDiscount,0)as HCDiscount," +
                            " IFNULL(M.DisRate1,0)as DisRate1, M.BestPriceYN, IFNULL(P.PhotoFile,'')as PhotoFile  " +
                            " from stk_master M left join stk_group G ON M.ItemGroup=G.ItemGroup " +
                            " left join stk_master_photo P ON M.ItemCode=P.ItemCode " +
                            " where M.SuspendedYN='0' and M.ItemGroup='"+ItemGroup+"' Order by M.ItemCode asc ";
                Log.d("QUERY",sql);
                stmt = conn.createStatement();
                stmt.executeQuery("SET NAMES 'LATIN1'");
                stmt.executeQuery("SET CHARACTER SET 'LATIN1'");
                stmt.execute(sql);
                rsItem=stmt.getResultSet();
                JSONArray results = new JSONArray();
                while(rsItem.next()) {
                    JSONObject row = new JSONObject();
                    Double dUnitPrice=rsItem.getDouble("UnitPrice");
                    String UnitPrice= String.format(Locale.US, "%,.2f", dUnitPrice);
                    row.put("ItemCode",rsItem.getString("ItemCode"));
                    row.put("Description", rsItem.getString("Description"));
                    row.put("UnitPrice",UnitPrice) ;
                    row.put("ItemGroup",rsItem.getString("ItemGroup")) ;
                    row.put("UOM", rsItem.getString("UOM")) ;
                    row.put("RetailTaxCode",rsItem.getString("RetailTaxCode")) ;
                    row.put("DefaultUOM", rsItem.getString(7));
                    row.put("UOM1", rsItem.getString(8));
                    row.put("UOM2", rsItem.getString(9));
                    row.put("UOM3",rsItem.getString(10));
                    row.put("UOM4", rsItem.getString(11));
                    row.put("UOMFactor1", rsItem.getString(12));
                    row.put("UOMFactor2", rsItem.getString(13));
                    row.put("UOMFactor3", rsItem.getString(14));
                    row.put("UOMFactor4", rsItem.getString(15));
                    row.put("UOMPrice1", rsItem.getString(16));
                    row.put("UOMPrice2", rsItem.getString(17));
                    row.put("UOMPrice3", rsItem.getString(18));
                    row.put("UOMPrice4", rsItem.getString(19));
                    row.put("Printer", rsItem.getString(20));
                    row.put("AlternateItem", rsItem.getString(21));
                    row.put("HCDiscount", rsItem.getString(22));
                    row.put("DisRate1", rsItem.getString(23));
                    row.put("BestPriceYN", rsItem.getString(24));

                    String PhotoFile=rsItem.getString(25);
                    if(!PhotoFile.isEmpty()) {
                        Blob test = rsItem.getBlob(25);
                        int blobl = (int) test.length();
                        byte[] blobasbyte = test.getBytes(1, blobl);
                        test.free();
                       // Bitmap bmp = BitmapFactory.decodeByteArray(blobasbyte, 0, blobasbyte.length);
                        row.put("PhotoFile", byteToString(blobasbyte));
                    }else{
                        row.put("PhotoFile", PhotoFile);
                    }
                    results.put(row);
                }
                Log.d("RESULT", results.toString());
                return results.toString();
            }
           /* }else {
                if (ItemGroup.equals("")) {
                    String sqlGroup = "select ItemGroup from stk_group order by ItemGroup limit 1";
                    Cursor cGroup = db.getQuery(sqlGroup);
                    while (cGroup.moveToNext()) {
                        ItemGroup = cGroup.getString(0);
                       // Printer = cGroup.getString(1);
                    }
                }
                String sql="select M.ItemCode,SUBSTR(M.Description,1,30) as Description, IFNULL(M.UnitPrice,'0')as UnitPrice, " +
                        " M.ItemGroup, M.UOM, M.RetailTaxCode, " +
                        " M.DefaultUOM, M.UOM1, M.UOM2," +
                        " M.UOM3, M.UOM4, M.UOMFactor1," +
                        " M.UOMFactor2, M.UOMFactor3, M.UOMFactor4," +
                        " M.UOMPrice1, M.UOMPrice2, M.UOMPrice3," +
                        " M.UOMPrice4, G.Printer, M.AlternateItem," +
                        " IFNULL(M.HCDiscount,0)as HCDiscount, IFNULL(M.DisRate1,0)as DisRate1, M.BestPriceYN," +
                        " IFNULL(P.PhotoFile,'')as PhotoFile " +
                        " from stk_master M left join stk_group G ON M.ItemGroup=G.ItemGroup" +
                        " left join stk_master_photo P ON M.ItemCode=P.ItemCode " +
                        " where M.SuspendedYN='0' and M.ItemGroup='"+ItemGroup+"' Order by M.ItemCode asc";
                Log.d("QUERY", sql);
                Cursor cItem = db.getQuery(sql);
                JSONArray results = new JSONArray();
                while (cItem.moveToNext()) {
                    JSONObject row = new JSONObject();
                    Double dUnitPrice = cItem.getDouble(2);
                    String UnitPrice = String.format(Locale.US, "%,.2f", dUnitPrice);
                    row.put("ItemCode", cItem.getString(0));
                    row.put("Description", cItem.getString(1));
                    row.put("UnitPrice", UnitPrice);
                    row.put("ItemGroup", cItem.getString(3));
                    row.put("UOM", cItem.getString(4));
                    row.put("RetailTaxCode", cItem.getString(5));
                    row.put("DefaultUOM", cItem.getString(6));
                    row.put("UOM1", cItem.getString(7));
                    row.put("UOM2", cItem.getString(8));
                    row.put("UOM3", cItem.getString(9));
                    row.put("UOM4", cItem.getString(10));
                    row.put("UOMFactor1", cItem.getString(11));
                    row.put("UOMFactor2", cItem.getString(12));
                    row.put("UOMFactor3", cItem.getString(13));
                    row.put("UOMFactor4", cItem.getString(14));
                    row.put("UOMPrice1", cItem.getString(15));
                    row.put("UOMPrice2", cItem.getString(16));
                    row.put("UOMPrice3", cItem.getString(17));
                    row.put("UOMPrice4", cItem.getString(18));
                    row.put("Printer", cItem.getString(19));
                    row.put("AlternateItem", cItem.getString(20));
                    row.put("HCDiscount", cItem.getString(21));
                    row.put("DisRate1", cItem.getString(22));
                    row.put("BestPriceYN", cItem.getString(23));
                    row.put("PhotoFile", cItem.getString(24));
                    results.put(row);
                }
                Log.d("RESULT", results.toString());
                db.closeDB();
                return results.toString();
            }*/
        }catch (JSONException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(rsItem !=null){
                try{
                    rsItem.close();
                    //rsGroup.close();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                rsItem=null;
                //rsGroup=null;
            }
            if(stmt !=null){
                try{
                    stmt.close();
                    //stmtG.close();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                stmt=null;
               // stmtG=null;
            }
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn=null;
            }

        }
        return null;
    }
    private String encodeChar(String txt){
        String newText="";
        try{
            byte[] b=txt.getBytes("ISO-8859-1");
            newText=new String(b, "UTF-16");
            return newText;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return newText;
    }
    private String decodeChar(String txt){
        String newText="";
        try{
            byte[] b=txt.getBytes("GB2312");
            newText=new String(b,"UTF-8");
            return newText;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return newText;
    }
    private String encodeBmp(Bitmap bmp){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,70,baos);
        byte[] b=baos.toByteArray();
        String base64= Base64.encodeToString(b, Base64.DEFAULT);
        return base64;
    }

    private String byteToString(byte[] bytes){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if(bmp!=null) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] b = baos.toByteArray();
            String base64 = Base64.encodeToString(b, Base64.DEFAULT);
            return base64;
        }
        return "";
    }
}
