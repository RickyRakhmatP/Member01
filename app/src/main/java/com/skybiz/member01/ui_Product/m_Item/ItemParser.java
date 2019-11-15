package com.skybiz.member01.ui_Product.m_Item;

import android.content.Context;
import android.os.AsyncTask;

import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.m_DataObject.Spacecraft_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 7 on 14/11/2017.
 */

public class ItemParser extends AsyncTask<Void, Integer, Integer> {
    Context c;
    String jsonData;
    RecyclerView rv;

    ArrayList<Spacecraft_Item> spacecrafts=new ArrayList<>();
    ItemAdapter adapter;

    public ItemParser(Context c, String jsonData, RecyclerView rv) {
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
            //bind data to recycleview
            ItemAdapter adapter=new ItemAdapter(c,spacecrafts);
            rv.setAdapter(adapter);
        }
    }

    private int parseData(){
        try {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;

            spacecrafts.clear();
            Spacecraft_Item spacecraft;
            for (int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                String ItemCode=jo.getString("ItemCode");
                String Description=jo.getString("Description");
                String UnitPrice=jo.getString("UnitPrice");
                String ItemGroup=jo.getString("ItemGroup");
                String UOM=jo.getString("UOM");
                String RetailTaxCode=jo.getString("RetailTaxCode");
                spacecraft=new Spacecraft_Item();
                spacecraft.setItemCode(ItemCode);
                spacecraft.setDescription(Description);
                spacecraft.setUnitPrice(UnitPrice);
                spacecraft.setItemGroup(ItemGroup);
                spacecraft.setUOM(UOM);
                spacecraft.setRetailTaxCode(RetailTaxCode);
                spacecraft.setDefaultUOM(jo.getString("DefaultUOM"));
                spacecraft.setUOM1(jo.getString("UOM1"));
                spacecraft.setUOM2(jo.getString("UOM2"));
                spacecraft.setUOM3(jo.getString("UOM3"));
                spacecraft.setUOM4(jo.getString("UOM4"));
                spacecraft.setUOMFactor1(jo.getString("UOMFactor1"));
                spacecraft.setUOMFactor2(jo.getString("UOMFactor2"));
                spacecraft.setUOMFactor3(jo.getString("UOMFactor3"));
                spacecraft.setUOMFactor4(jo.getString("UOMFactor4"));
                spacecraft.setUOMPrice1(jo.getString("UOMPrice1"));
                spacecraft.setUOMPrice2(jo.getString("UOMPrice2"));
                spacecraft.setUOMPrice3(jo.getString("UOMPrice3"));
                spacecraft.setUOMPrice4(jo.getString("UOMPrice4"));
                spacecraft.setPrinter(jo.getString("Printer"));
                spacecraft.setAlternateItem(jo.getString("AlternateItem"));
                spacecraft.setHCDiscount(jo.getString("HCDiscount"));
                spacecraft.setDisRate1(jo.getString("DisRate1"));
                spacecraft.setBestPriceYN(jo.getString("BestPriceYN"));
                spacecraft.setPhotoFile(jo.getString("PhotoFile"));
                spacecrafts.add(spacecraft);
            }
            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

