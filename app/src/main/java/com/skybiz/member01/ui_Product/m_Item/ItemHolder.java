package com.skybiz.member01.ui_Product.m_Item;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;

/**
 * Created by 7 on 14/11/2017.
 */

public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView vItemCode, vDescription,vUnitPrice;
    ImageView vPhotoFile;
    Button btnAdd,btnMinus;
    LinearLayout lnDesc;
    ItemClickListener itemClickListener;

    public ItemHolder(View itemView) {
        super(itemView);

        vItemCode       =(TextView) itemView.findViewById(R.id.vItemCode);
        vDescription    =(TextView) itemView.findViewById(R.id.vDescription);
        vUnitPrice      =(TextView) itemView.findViewById(R.id.vUnitPrice);
        vPhotoFile      =(ImageView)itemView.findViewById(R.id.vPhotoFile);
        lnDesc          =(LinearLayout)itemView.findViewById(R.id.lnDesc);

        // itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v){
        this.itemClickListener.onItemClick();
    }
}
