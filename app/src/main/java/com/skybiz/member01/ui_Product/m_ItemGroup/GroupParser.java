package com.skybiz.member01.ui_Product.m_ItemGroup;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.skybiz.member01.m_DataObject.Spacecraft_Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by 7 on 14/11/2017.
 */

public class GroupParser extends AsyncTask<Void, Integer, Integer> {
    Context c;
    String jsonData;
    RecyclerView rv;
    ArrayList<Spacecraft_Group> spacecrafts=new ArrayList<>();
    CustomAdapter adapter;

    public GroupParser(Context c, String jsonData, RecyclerView rv) {
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
            GroupAdapter adapter=new GroupAdapter(c,spacecrafts);
            rv.setAdapter(adapter);
        }
    }

    private int parseData(){
        try {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;
            spacecrafts.clear();
            Spacecraft_Group spacecraft;
            for (int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                String ItemGroup=jo.getString("ItemGroup");
                String Description=jo.getString("Description");
                spacecraft=new Spacecraft_Group();
                spacecraft.setItemGroup(ItemGroup);
                spacecraft.setDescription(Description);
                spacecrafts.add(spacecraft);
            }
            /*spacecraft=new Spacecraft_Group();
            spacecraft.setItemGroup("Miscellaneous");
            spacecraft.setDescription("Miscellaneous");
            spacecrafts.add(spacecraft);*/
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

