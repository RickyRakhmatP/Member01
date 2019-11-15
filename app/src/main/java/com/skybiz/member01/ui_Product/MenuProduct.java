package com.skybiz.member01.ui_Product;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skybiz.member01.R;
import com.skybiz.member01.ui_Product.m_Item.DownloaderItem;
import com.skybiz.member01.ui_Product.m_ItemGroup.DownloaderGroup;

public class MenuProduct extends AppCompatActivity {

    RecyclerView rvProduct, rvGroup;
    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setTitle("Products");
        rvGroup = (RecyclerView) findViewById(R.id.rvGroup);
        rvProduct = (RecyclerView) findViewById(R.id.rvProduct);

        initData();
    }

    private void initData(){
        retItem("");
        retGroup();
    }
    public void retItem(String ItemGroup){
        rvProduct.setHasFixedSize(true);
        lLayout = new GridLayoutManager(this , 2);
        rvProduct.setLayoutManager(lLayout);
        rvProduct.setItemAnimator(new DefaultItemAnimator());
        DownloaderItem dItem=new DownloaderItem(this,ItemGroup,rvProduct);
        dItem.execute();
    }
    public void retGroup(){
        rvGroup.setHasFixedSize(true);
        rvGroup.setLayoutManager(new LinearLayoutManager(this));
        rvGroup.setItemAnimator(new DefaultItemAnimator());
        DownloaderGroup dGroup=new DownloaderGroup(this,rvGroup);
        dGroup.execute();
    }
}
