package com.huayjx.ftshiyao.Sylsqy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.huayjx.ftshiyao.BaseActivity;
import com.huayjx.ftshiyao.Cyfwdw.Camera;
import com.huayjx.ftshiyao.Cyfwdw.XxlbDatabase;
import com.huayjx.ftshiyao.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lhf on 2015/6/8.
 */
public class Ypqyxx extends BaseActivity {
    private XxlbDatabase dbhelper;
    private SQLiteDatabase db;
    private Calendar calendar;
    private List<String> listLxr;
    String[] lxrnames ;

    @InjectView(R.id.ypjc_btn) Button ypjc_btn;
    @InjectView(R.id.ypjc_dwmc) EditText ypjc_dwmc;
    @InjectView(R.id.ypjc_dwdz) EditText ypjc_dwdz;
    @InjectView(R.id.ypjc_xkzh) EditText ypjc_xkzh;
    @InjectView(R.id.ypjc_yxqx) EditText ypjc_yxqx;

    @InjectView(R.id.ypjc_jcr) TextView ypjc_jcr;
    @InjectView(R.id.ypjc_jcsj) TextView ypjc_jcsj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ypqyxx_layout);

        //打开数据库
        dbhelper = new XxlbDatabase(this);
        db = dbhelper.getWritableDatabase();

        Bundle bundle =getIntent().getExtras().getBundle("bundler");
        HashMap<String,String> map = (HashMap<String,String>) bundle.getSerializable("map");
        ypjc_dwmc.setText(String.valueOf(map.get("qymc")));
        ypjc_dwdz.setText(String.valueOf(map.get("zcdz")));
        ypjc_xkzh.setText(String.valueOf(map.get("zsbh")));
        ypjc_yxqx.setText(String.valueOf(map.get("yxqz")));

        calendar = Calendar.getInstance();

        //点击检查人
        ypjc_jcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogmeg();
            }
        });

        ypjc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ypqyxx.this, YpCamera.class);
                startActivity(intent);
            }
        });

        ypjc_jcsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(Ypqyxx.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ypjc_jcsj.setText(String.valueOf(year + "-" + (monthOfYear+1) + "-" + dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });


    }
    public void showDialogmeg(){
        lxrnames = getLxr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择联系人");

        final boolean[] checkedItems = new boolean[lxrnames.length];
        for (int i = 0; i < lxrnames.length; i++) {
            checkedItems[i] = false;
        }
        builder.setMultiChoiceItems(lxrnames, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    checkedItems[which] = isChecked;
                }
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < lxrnames.length; i++) {
                    if (checkedItems[i]) {
                        sb.append(lxrnames[i] + " ");
                    }
                }
                ypjc_jcr.setText(sb.toString());
            }
        });
        builder.show();
    }

    public String[] getLxr(){
        //查询表中的数据
        Cursor cursor = db.rawQuery("select user_name from Infolxr where depart_name='丰台区丰台街道食药所'", null);
        listLxr = new ArrayList<String>();
        while (cursor.moveToNext()) {
            listLxr.add(cursor.getString(cursor.getColumnIndex("user_name")));
        }
        String[] lxrs = listLxr.toArray(new String[listLxr.size()]);
        cursor.close();
        return lxrs;
    }
}
