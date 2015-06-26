package com.huayjx.ftshiyao.Cyfwdw;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huayjx.ftshiyao.BaseActivity;
import com.huayjx.ftshiyao.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/** 餐饮企业信息
 * Created by lhf on 2015/6/6.
 */
public class Cyqyxx extends BaseActivity {
    private XxlbDatabase dbhelper;
    private SQLiteDatabase db;
    private Calendar calendar;
    private List<String> listLxr;
    String[] lxrnames ;

    @InjectView(R.id.jc_btn) Button jc_btn;
    @InjectView(R.id.jc_dwmc) EditText jc_dwmc;
    @InjectView(R.id.jc_dwdz) EditText jc_dwdz;
    @InjectView(R.id.jc_xkzh) EditText jc_xkzh;
    @InjectView(R.id.jc_yxqx) EditText jc_yxqx;

    @InjectView(R.id.jc_jcr) TextView jc_jcr;
    @InjectView(R.id.jc_jcsj) TextView jc_jcsj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyqyxx_layout);

        //打开数据库
        dbhelper = new XxlbDatabase(this);
        db = dbhelper.getWritableDatabase();

        Bundle bundle =getIntent().getExtras().getBundle("bundle");
        HashMap<String,String> map = (HashMap<String,String>) bundle.getSerializable("map");
        jc_dwmc.setText(String.valueOf(map.get("ent_name")));
        jc_dwdz.setText(String.valueOf(map.get("lic_no")));
        jc_xkzh.setText(String.valueOf(map.get("reg_address")));
        jc_yxqx.setText(String.valueOf(map.get("lastdate")));

        calendar = Calendar.getInstance();

        //点击检查人
        jc_jcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogmeg();
            }
        });

        //时间对话框
        jc_jcsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(Cyqyxx.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        jc_jcsj.setText(String.valueOf(year+"-"+(monthOfYear+1)+"-"+dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });

        //拍照点击事件
        jc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cyqyxx.this, Camera.class);
                startActivity(intent);
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
                jc_jcr.setText(sb.toString());
            }
        });
        builder.show();
    }

    public String[] getLxr(){
        //查询表中的数据
        Cursor cursor = db.rawQuery("select user_name from Infolxr where depart_name='丰台区东铁营街道食药所'", null);
        listLxr = new ArrayList<String>();
        while (cursor.moveToNext()) {
            listLxr.add(cursor.getString(cursor.getColumnIndex("user_name")));
        }
        String[] lxrs = listLxr.toArray(new String[listLxr.size()]);
        cursor.close();
        return lxrs;
    }

}