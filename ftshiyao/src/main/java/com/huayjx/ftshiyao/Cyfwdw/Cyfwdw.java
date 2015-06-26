package com.huayjx.ftshiyao.Cyfwdw;

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

import com.huayjx.ftshiyao.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**餐饮服务单位
 * Created by lhf on 2015/6/5.
 */
public class Cyfwdw extends BaseActivity implements OnItemClickListener {
    private XxlbDatabase dbhelper;//数据库类名
    private SQLiteDatabase db;
    private SimpleAdapter adapter;
    private ArrayList<Map<String, String>> listData;

    @InjectView(R.id.ft_btn_cx) Button ft_btn_cx;
    @InjectView(R.id.ent_name) EditText ent_name;
    @InjectView(R.id.lic_no) EditText lic_no;
    @InjectView(R.id.reg_address) EditText reg_address;
    @InjectView(R.id.ft_listview) ListView ft_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyfwdw_layout);

        //打开数据库
        dbhelper = new XxlbDatabase(this);
        db = dbhelper.getWritableDatabase();

        //监听事件
        ft_btn_cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询表中的数据
                Cursor cursor = db.rawQuery("select ent_name,lic_no,reg_address,lastdate from Infolist", null);
                //通过适配器把数据绑定到listview中
                listData = new ArrayList<Map<String, String>>();
                  while (cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ent_name", cursor.getString(cursor.getColumnIndex("ent_name")));
                        map.put("lic_no", cursor.getString(cursor.getColumnIndex("lic_no")));
                        map.put("reg_address", cursor.getString(cursor.getColumnIndex("reg_address")));
                        map.put("lastdate", cursor.getString(cursor.getColumnIndex("lastdate")));
                    listData.add(map);
                }
                cursor.close();
//                db.close();

                //适配器
                adapter = new SimpleAdapter(Cyfwdw.this, listData, R.layout.item_layout, new String[]{"ent_name", "lic_no", "reg_address"}, new int[]{R.id.ent_name, R.id.lic_no, R.id.reg_address});
                ft_listview.setAdapter(adapter);
                ft_listview.setOnItemClickListener(Cyfwdw.this);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> map = listData.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", (Serializable)map);
        Intent intent = new Intent(Cyfwdw.this, Cyqyxx.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        finish();
    }
}