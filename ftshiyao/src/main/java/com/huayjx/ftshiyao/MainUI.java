package com.huayjx.ftshiyao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.huayjx.ftshiyao.Cyfwdw.Cyfwdw;
import com.huayjx.ftshiyao.DataDownload.Download;
import com.huayjx.ftshiyao.Sylsqy.Sylsqy;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.InjectView;

/**
 * Created by lhf on 2015/6/5.
 */
public class MainUI extends BaseActivity {
    int[] images = {R.mipmap.ic_cyfw,R.mipmap.ic_ypls,R.mipmap.ic_sjxz,R.mipmap.ic_exit};
    String[] names = {"餐饮服务单位","食药零售企业","数据下载","退出"};

    @InjectView(R.id.gridView) GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //生成动态数组，并传入数据
        ArrayList<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<images.length;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("itemImage",images[i]);
            map.put("itemName",names[i]);
            items.add(map);
        }

        //适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,items,
                R.layout.gridview_item,new String[]{"itemImage","itemName",},
                new int[]{R.id.itemImage,R.id.itemName});
        gridView.setAdapter(simpleAdapter);


        //点击事件
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("餐饮服务单位".equals(names[position].toString())){
                    Intent intent1 = new Intent(MainUI.this,Cyfwdw.class);
                    startActivity(intent1);
                }else if("食药零售企业".equals(names[position].toString())){
                    Intent intent2 = new Intent(MainUI.this,Sylsqy.class);
                    startActivity(intent2);
                }else if("数据下载".equals(names[position].toString())){
                    Intent intent3 = new Intent(MainUI.this,Download.class);
                    startActivity(intent3);
                }else if("退出".equals(names[position].toString())){
                    finish();
                    System.exit(0);
                }

            }
        });

    }
}