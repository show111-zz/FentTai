package com.huayjx.ftshiyao.DataDownload;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.huayjx.ftshiyao.BaseActivity;
import com.huayjx.ftshiyao.Cyfwdw.XxlbDatabase;
import com.huayjx.ftshiyao.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**下载页
 * Created by lhf on 2015/6/8.
 */
public class Download extends BaseActivity{
    private ProgressDialog pdg; // Dialog
    private XxlbDatabase dbhelper;
    private SQLiteDatabase db;

    @InjectView(R.id.down_cyxz) Button down_cyxz;//餐饮下载
    @InjectView(R.id.down_ypxz) Button down_ypxz;//药品下载
    @InjectView(R.id.down_lxrxz) Button down_lxrxz;//联系人下载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        dbhelper=new XxlbDatabase(Download.this);
        db = dbhelper.getWritableDatabase();

        down_cyxz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlDownLoad = "http://106.120.172.113:8080/ftyy/cyfw/cyfwAction!downdata.dhtml";//餐饮下载
                new DownloadAsyncTask("Infolist").execute(urlDownLoad);//执行异步操作
            }
        });

        down_ypxz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlDownLoad1 = "http://106.120.172.113:8080/ftyy/xxqb/xxqbAction!downdata.dhtml";//药品下载
                new DownloadAsyncTask("Infoyp").execute(urlDownLoad1);//执行异步操作
            }
        });

        down_lxrxz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlDownLoad2 = "http://106.120.172.113:8080/ftyy/checkuser/checkuserAction!downdata.dhtml?depcode=";//检查人下载
                new DownloadAsyncTask("Infolxr").execute(urlDownLoad2);//执行异步操作
            }
        });
    }

    class DownloadAsyncTask extends AsyncTask<String, Void, String> {

         // 表名
        private String tableName;
        public DownloadAsyncTask(String tableName) {
            this.tableName = tableName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdg = ProgressDialog.show(Download.this, null, "正在下载", false, false);
        }

        @Override
        protected String doInBackground(String... params) {
//            Log.e("download url -- ", params[0]);
            return readParse(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pdg.dismiss();

            if(s == null){
                Toast.makeText(Download.this, "下载失败", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(Download.this, "下载成功", Toast.LENGTH_LONG).show();
            List<Map<String, String>> listEnt =
                    JSON.parseObject(s, new TypeReference<List<Map<String, String>>>() {
                    });
            List<ContentValues> listC = tran2ContentValues(listEnt);

            if(listC == null){
                Toast.makeText(Download.this, "没有下载数据", Toast.LENGTH_LONG).show();
                return;
            }

            for(ContentValues cv : listC){
                db.insert(tableName, null, cv);
            }


        }

        /**
         * List<Map> 转换 List<ContentValues>
         * @param list
         * @return
         */
        private List<ContentValues> tran2ContentValues(List<Map<String, String>> list){

            List<ContentValues> lc = null;
            if(list != null && !list.isEmpty()){
                lc = new ArrayList<ContentValues>();
                String[] columNames = getColumNamesByTableName(db,tableName);
                // String[] columNames = getColumNamesByTableName(db, "Infolist");

                if(columNames == null){
                    Log.e("dbTbable", "获取列表失败");
                    return null;
                }
                for(Map<String, String> map : list){
                    ContentValues ct = new ContentValues();
                    for(String columName : columNames){
                        if(columName.equals("_id")){
                            continue;
                        }
                        ct.put(columName, map.get(columName));
                    }
                    lc.add(ct);
                }
            }
            return lc;
        }

        //获取数据库中的表列名
        private String[] getColumNamesByTableName(SQLiteDatabase db, String tableName){
            String sql = "select * from " + tableName + " where 1=1 and _id = '" + 0 + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor != null){
                return cursor.getColumnNames();
            }
            return null;
        }

        //获得json字符串
        public String readParse(String urlDownLoad){
            ByteArrayOutputStream outStream = null;
            InputStream inStream = null;
            try{
                outStream = new ByteArrayOutputStream();

                URL url = new URL(urlDownLoad);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                inStream = conn.getInputStream();

                byte[] data = new byte[1024];
                int len = 0;
                while ((len = inStream.read(data)) != -1) {
                    outStream.write(data, 0, len);
                }
                return new String(outStream.toByteArray());//通过outStream.toByteArray获取到写的数据
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
