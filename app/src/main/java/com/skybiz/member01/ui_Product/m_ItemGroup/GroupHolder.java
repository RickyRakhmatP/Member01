package com.skybiz.member01.ui_Product.m_ItemGroup;


import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;

/**
 * Created by 7 on 14/11/2017.
 */

public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //VerticalTextView vGroup ;
    Button vGroup;
    ItemClickListener itemClickListener;

    public GroupHolder(View itemView) {
        super(itemView);
        vGroup=(Button) itemView.findViewById(R.id.btnGroup);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v){
        this.itemClickListener.onItemClick();
    }
}
