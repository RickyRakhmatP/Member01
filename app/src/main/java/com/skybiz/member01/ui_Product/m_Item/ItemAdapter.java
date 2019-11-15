package com.skybiz.member01.ui_Product.m_Item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.m_DataObject.Spacecraft_Item;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


//import skybiz.com.cashoff.m_Order.AddItem;

/**
 * Created by 7 on 14/11/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    Context c;
    ArrayList<Spacecraft_Item> spacecrafts;


    RecyclerView rv;
    public ItemAdapter(Context c, ArrayList<Spacecraft_Item> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item,parent,false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final Spacecraft_Item spacecraft=spacecrafts.get(position);
        holder.vItemCode.setText(spacecraft.getItemCode());
        holder.vDescription.setText(spacecraft.getDescription()+"\n"+spacecraft.getAlternateItem());
        holder.vUnitPrice.setText("RM"+spacecraft.getUnitPrice());
        final String PhotoFile=spacecraft.getPhotoFile();
        if(!PhotoFile.isEmpty()){
            holder.vItemCode.setTextColor(Color.parseColor("#000000"));
            holder.vDescription.setTextColor(Color.parseColor("#000000"));
            holder.vUnitPrice.setTextColor(Color.parseColor("#CC0000"));
            holder.vPhotoFile.setVisibility(View.VISIBLE);
            final byte[] imgStr=Base64.decode(spacecraft.getPhotoFile(),Base64.DEFAULT);
            Bitmap bmp=BitmapFactory.decodeByteArray(imgStr,0,imgStr.length);
            Drawable img=new BitmapDrawable(Bitmap.createScaledBitmap(bmp,120,125,true));
            holder.vPhotoFile.setImageDrawable(img);
        }else{
            holder.vPhotoFile.setVisibility(View.GONE);
            holder.vPhotoFile.setImageBitmap(null);
            holder.vPhotoFile.setImageDrawable(null);
            //holder.lnDesc.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.vItemCode.setTextColor(Color.parseColor("#000000"));
            holder.vDescription.setTextColor(Color.parseColor("#000000"));
            holder.vUnitPrice.setTextColor(Color.parseColor("#CC0000"));
        }
    }

    @Override
    public int getItemCount()
    {
        return spacecrafts.size();
    }
    public String convertChar(String txt){
        String newText="";
        try{
            byte[] b=txt.getBytes("ISO-8859-1");
            newText=new String(b);
            return newText;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return newText;
    }
}
