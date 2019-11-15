package com.skybiz.member01.ui_Point.m_History;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.skybiz.member01.R;


/**
 * Created by 7 on 27/10/2017.
 */

public class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView vDate, vDescPoint,vPoint,vDocType;
    ItemClickListener itemClickListener;

    public HistoryHolder(View itemView) {
        super(itemView);
        vDate           =(TextView) itemView.findViewById(R.id.vDate);
        vDescPoint      =(TextView) itemView.findViewById(R.id.vDescPoint);
        vPoint          =(TextView) itemView.findViewById(R.id.vPoint);
        vDocType          =(TextView) itemView.findViewById(R.id.vDocType);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v){
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }

}
