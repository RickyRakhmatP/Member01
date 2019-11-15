package com.skybiz.member01.ui_Point.m_History;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.m_DataObject.Spacecraft_HistoryPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 7 on 27/10/2017.
 */

public class HistoryParser extends AsyncTask<Void, Integer, Integer> {

    Context c;
    String jsonData;
    RecyclerView rv;

    ArrayList<Spacecraft_HistoryPoint> spacecrafts=new ArrayList<>();

    public HistoryParser(Context c, String jsonData, RecyclerView rv) {
        this.c = c;
        this.jsonData = jsonData;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if(result==0){
            Toast.makeText(c,"Unable to parse", Toast.LENGTH_SHORT).show();
        }else{
            HistoryAdapter adapter=new HistoryAdapter(c,spacecrafts);
            rv.setAdapter(adapter);
        }
    }

    private int parseData(){
        try {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;
            spacecrafts.clear();
            Spacecraft_HistoryPoint spacecraft;
            for (int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                spacecraft=new Spacecraft_HistoryPoint();
                spacecraft.setD_ate(jo.getString("D_ate"));
                spacecraft.setPoint(jo.getString("Point"));
                spacecraft.setDocType(jo.getString("DocType"));
                spacecraft.setRemark(jo.getString("Remark"));
                spacecraft.setCusCode(jo.getString("CusCode"));
                spacecraft.setCusName(jo.getString("CusName"));
                spacecraft.setCurCode(jo.getString("CurCode"));
                spacecrafts.add(spacecraft);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
