package com.skybiz.member01.ui_Product.m_ItemGroup;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.ui_Product.MenuProduct;
import com.skybiz.member01.m_DataObject.Spacecraft_Group;

import java.util.ArrayList;

/**
 * Created by 7 on 14/11/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<GroupHolder> {
    String IPAddress,UserName,Password,DBName;
    Context c;
    ArrayList<Spacecraft_Group> spacecrafts;

    RecyclerView rv;
    public CustomAdapter(Context c, ArrayList<Spacecraft_Group> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_itemgroup,parent,false);
        return new GroupHolder(v);

    }

    @Override
    public void onBindViewHolder(GroupHolder holder, int position) {
        final Spacecraft_Group spacecraft=spacecrafts.get(position);
        holder.vGroup.setText(spacecraft.getItemGroup());
        holder.vGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               ((MenuProduct) c).retItem(spacecraft.getItemGroup());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return spacecrafts.size();
    }
}
