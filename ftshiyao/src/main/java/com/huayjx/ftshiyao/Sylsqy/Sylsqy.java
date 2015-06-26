package com.huayjx.ftshiyao.Sylsqy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huayjx.ftshiyao.BaseActivity;
import com.huayjx.ftshiyao.Cyfwdw.XxlbDatabase;
import com.huayjx.ftshiyao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lhf on 2015/6/8.
 */
public class Sylsqy extends BaseActivity implements OnItemClickListener {

    private XxlbDatabase dbhelper;//数据库类名
    private SQLiteDatabase db;
    private SimpleAdapter adapter;
    private List<HashMap<String, String>> listDatas;

    @InjectView(R.id.yp_btn_cx) Button yp_btn_cx;
    @InjectView(R.id.yp_dwmc) EditText ft_dwmc;
    @InjectView(R.id.yp_xkzh) EditText yp_xkzh;
    @InjectView(R.id.yp_dwdz) EditText yp_dwdz;
    @InjectView(R.id.yp_listview) ListView yp_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sylsqy_layout);

        dbhelper = new XxlbDatabase(this);
        db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        //监听事件
        yp_btn_cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询表中的数据
                Cursor cursor = db.rawQuery("select qymc,zsbh,zcdz,yxqz from Infoyp", null);
                //通过适配器把数据绑定到listview中
                listDatas = new ArrayList<HashMap<String, String>>();
                while (cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("qymc", cursor.getString(cursor.getColumnIndex("qymc")));
                    map.put("zsbh", cursor.getString(cursor.getColumnIndex("zsbh")));
                    map.put("zcdz", cursor.getString(cursor.getColumnIndex("zcdz")));
                    map.put("yxqz", cursor.getString(cursor.getColumnIndex("yxqz")));
                    listDatas.add(map);
                }
                cursor.close();
//                db.close();

                //适配器
                adapter = new SimpleAdapter(Sylsqy.this,listDatas,R.layout.ypitem_layout,new String[]{"qymc","zsbh","zcdz"},new int[]{R.id.qymc,R.id.zsbh,R.id.zcdz});
                yp_listview.setAdapter(adapter);
                yp_listview.setOnItemClickListener(Sylsqy.this);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String,String> map = listDatas.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map",map);

        Intent intent = new Intent(Sylsqy.this,Ypqyxx.class);
        intent.putExtra("bundler",bundle);
        startActivity(intent);
        finish();
    }
}
