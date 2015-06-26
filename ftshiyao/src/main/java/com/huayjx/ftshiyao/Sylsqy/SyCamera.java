package com.huayjx.ftshiyao.Sylsqy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.huayjx.ftshiyao.BaseActivity;
import com.huayjx.ftshiyao.Cyfwdw.CyCameraAdapter;
import com.huayjx.ftshiyao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lhf on 2015/6/12.
 */
public class SyCamera  extends BaseActivity {
    String [] tips = {"外观环境","就餐场所","库房","粗加工间","烹饪间","餐用具清洗消毒间","外景"};
    private ArrayList<HashMap<String, Object>> listPicName = null;
    private List<Bitmap> listBitmap = new ArrayList<Bitmap>();
    private SyCameraAdapter adapter;

    @InjectView(R.id.cameraView) GridView cameraView;//照片框
    @InjectView(R.id.btnCamera) Button btnCamera;//开始拍照按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        //拍照
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogmeg();
            }
        });
    }

    public void showDialogmeg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择拍照环境");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //调用系统相机
                Intent camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent, 1);
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //完成照相后回调用此方法
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //照相完成 点击确定
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Toast.makeText(SyCamera.this, "请安装SD卡", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = data.getExtras(); //获取intent里面的bundle对象
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            listBitmap.add(bitmap);
            adapter = new SyCameraAdapter(SyCamera.this,R.layout.camer_grid_layout,listBitmap);
            cameraView.setAdapter(adapter);
        }
    }

}
