package com.skybiz.member01.ui_Point.m_History;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.m_DataObject.Spacecraft_HistoryPoint;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by 7 on 27/10/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
    Context c;
    ArrayList<Spacecraft_HistoryPoint> spacecrafts;


    public HistoryAdapter(Context c, ArrayList<Spacecraft_HistoryPoint> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_historypoint,parent,false);
        return new HistoryHolder(v);
    }
    @Override
    public void onBindViewHolder(final HistoryHolder holder, int position) {
        final Spacecraft_HistoryPoint spacecraft=spacecrafts.get(position);
        holder.vDate.setText(spacecraft.getD_ate());
        final Double dPoint= Double.parseDouble(spacecraft.getPoint());
        final String DocType=spacecraft.getDocType();
        if(DocType.equals("Increase")) {
            //drawble
            int i=c.getResources().getColor(R.color.color_primary2);
            holder.vPoint.setTextColor(i);
            holder.vPoint.setText(zeroDecimal(dPoint) + "P");
            holder.vDescPoint.setText("Bill # " + spacecraft.getRemark());
            holder.vDocType.setText("COLLECTED");
            holder.vDocType.setBackgroundColor(Color.parseColor("#ffffff"));
            Drawable img = c.getResources().getDrawable( R.drawable.point_collect);
            holder.vIcon.setImageDrawable(img);
        }else if(DocType.equals("Decrease")){
            int i=c.getResources().getColor(R.color.color_danger);
            holder.vPoint.setTextColor(i);
            holder.vPoint.setText(zeroDecimal(dPoint) + "P");
            holder.vDescPoint.setText(spacecraft.getRemark());
            holder.vDocType.setText("REDEEMED");
            holder.vDocType.setBackgroundColor(Color.parseColor("#ffffff"));
            Drawable img = c.getResources().getDrawable( R.drawable.point_redeem);
            holder.vIcon.setImageDrawable(img);

        }
    }
    @Override
    public int getItemCount()
    {
        return spacecrafts.size();
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
