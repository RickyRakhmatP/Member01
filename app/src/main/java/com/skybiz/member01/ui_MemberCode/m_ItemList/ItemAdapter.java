package com.skybiz.member01.ui_MemberCode.m_ItemList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.m_DataObject.SetUOM;
import com.skybiz.member01.m_DataObject.Spacecraft_Item;
import com.skybiz.member01.ui_MemberCode.MemberCode2;
import com.skybiz.member01.m_DataObject.Spacecraft_Item;

import java.util.ArrayList;

/**
 * Created by 7 on 27/10/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    Context c;
    ArrayList<Spacecraft_Item> spacecrafts;
    DialogItem dialogItem;

    public ItemAdapter(Context c, ArrayList<Spacecraft_Item> spacecrafts, DialogItem dialogItem) {
        this.c = c;
        this.spacecrafts = spacecrafts;
        this.dialogItem=dialogItem;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_itemlist,parent,false);
        return new ItemHolder(v);
    }
    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        final Spacecraft_Item spacecraft=spacecrafts.get(position);
        holder.vItemCode.setText(spacecraft.getItemCode());
        holder.vDescription.setText(spacecraft.getDescription());
        holder.vUnitPrice.setText(spacecraft.getUnitPrice() +"\n"+spacecraft.getPoint());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                final String vQty= String.valueOf(spacecraft.getId());
                final String vItemCode=spacecraft.getItemCode();
                final String vDescription=charReplace(spacecraft.getDescription());
                final String vPoint=spacecraft.getPoint();
                SetUOM vData=SetUOM.set(spacecraft.getUnitPrice(),spacecraft.getDefaultUOM(),spacecraft.getUOM(),
                        spacecraft.getUOM1(),spacecraft.getUOM2(),spacecraft.getUOM3(),
                        spacecraft.getUOM4(),spacecraft.getUOMFactor1(),spacecraft.getUOMFactor2(),
                        spacecraft.getUOMFactor3(),spacecraft.getUOMFactor4(),spacecraft.getUOMPrice1(),
                        spacecraft.getUOMPrice2(),spacecraft.getUOMPrice3(),spacecraft.getUOMPrice4());
                final String vUOM=vData.getUOM();
                final String vUnitPrice=vData.getUnitPrice();
                final String vFactorQty=vData.getFactorQty();
                ((MemberCode2)c).setItemRedeem(vItemCode,vDescription,vUnitPrice,vUOM,vFactorQty,vPoint);
                dialogItem.dismiss();
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return spacecrafts.size();
    }

    private String charReplace(String text){
        String newText=text.replaceAll("[\\.$|,|;|']","");
        return newText;
    }
}
