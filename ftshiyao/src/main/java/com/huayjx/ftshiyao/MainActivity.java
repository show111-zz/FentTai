package com.huayjx.ftshiyao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {
    private ProgressDialog pdg; // Dialog
    private String path;
    String userText;
    String passText;

    @InjectView(R.id.login_btn) Button login_btn;
    @InjectView(R.id.ft_username) EditText ft_username;
    @InjectView(R.id.ft_password) EditText ft_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         登陆
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userText = ft_username.getText().toString().trim();
                passText = ft_password.getText().toString().trim();
                new LoginTask(MainActivity.this, userText, passText).execute();
            }
        });
    }

    class LoginTask extends AsyncTask<Void, String, Boolean> {
        private Context context;
        private String userText;
        private String passText;

        public LoginTask(Context context, String userText, String passText) {
            this.context = context;
            this.userText = userText;
            this.passText = passText;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            pdg = ProgressDialog.show(MainActivity.this, null, "正在登陆", false, false);
            path = "http://106.120.172.113:8080/ftyy/login/loginAction!applogin.dhtml?userName=" + userText + "&password=" + passText;
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> map = new HashMap<String, String>();
            // 通过HttpUrlConnection发送GET请求
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();

                if (code == 200) {
                    InputStream is = conn.getInputStream();
                    String msg = streamToString(is);
                    map = parseJsonStr(msg);
                    Boolean status = map.get("msg").equals("登录成功");
                    return status;
                }else{
                    Toast.makeText(MainActivity.this,"账号密码错误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Boolean flag) {
            if (flag) {
                pdg.dismiss();
                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                //跳转到主界面
                Intent intent = new Intent(MainActivity.this, MainUI.class);
                context.startActivity(intent);
            } else {
                pdg.dismiss();
                Toast.makeText(MainActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
            }
        }

        public String streamToString(InputStream is) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();//此类实现了一个输出流，其中的数据被写入一个 byte 数组。
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                byte[] byteArray = baos.toByteArray();//创建一个新分配的byte数组
                return new String(byteArray);
            } catch (Exception e) {
                return null;
            }finally {
                if(baos==null){
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(is==null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Map<String, String> parseJsonStr(String jsonStr) {
            return JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
                    });
        }

    }

}





