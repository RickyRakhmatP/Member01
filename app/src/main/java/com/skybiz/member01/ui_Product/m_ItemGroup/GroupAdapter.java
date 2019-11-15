package com.skybiz.member01.ui_Product.m_ItemGroup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.m_DataObject.Spacecraft_Group;
import com.skybiz.member01.ui_Product.MenuProduct;

import java.util.ArrayList;

/**
 * Created by 7 on 14/11/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {
    Context c;
    ArrayList<Spacecraft_Group> spacecrafts;
    int row_index;

    RecyclerView rv;
    public GroupAdapter(Context c, ArrayList<Spacecraft_Group> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_itemgroup,parent,false);
        return new GroupHolder(v);

    }

    @Override
    public void onBindViewHolder(GroupHolder holder, final int position) {
        final Spacecraft_Group spacecraft=spacecrafts.get(position);
        holder.vGroup.setText(spacecraft.getDescription());
        //RotateAnimation rotate= (RotateAnimation) AnimationUtils.loadAnimation(c,R.anim.rotateanimation);
       // holder.vGroup.setAnimation(rotate);

        holder.vGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               ((MenuProduct) c).retItem(spacecraft.getItemGroup());
                row_index=position;
                notifyDataSetChanged();
            }
        });
       /* holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick() {
                ((CashReceipt) c).retItem(spacecraft.getItemGroup());
                row_index=position;
                notifyDataSetChanged();
            }
        });*/

        if(row_index==position){
            holder.vGroup.setBackgroundColor(Color.parseColor("#000000"));
            //((MenuProduct) c).retItem(spacecraft.getItemGroup());
        } else{
            holder.vGroup.setBackgroundColor(Color.parseColor("#689f38"));
        }
    }

    @Override
    public int getItemCount()
    {
        return spacecrafts.size();
    }
}
