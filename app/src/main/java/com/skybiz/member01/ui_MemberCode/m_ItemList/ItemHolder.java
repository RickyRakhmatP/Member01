package com.skybiz.member01.ui_MemberCode.m_ItemList;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;


/**
 * Created by 7 on 27/10/2017.
 */

public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView vItemCode, vDescription,vUnitPrice;
    ItemClickListener itemClickListener;

    public ItemHolder(View itemView) {
        super(itemView);
        vItemCode     =(TextView) itemView.findViewById(R.id.vItemCode);
        vDescription  =(TextView) itemView.findViewById(R.id.vDescription);
        vUnitPrice    =(TextView) itemView.findViewById(R.id.vUnitPrice);
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
