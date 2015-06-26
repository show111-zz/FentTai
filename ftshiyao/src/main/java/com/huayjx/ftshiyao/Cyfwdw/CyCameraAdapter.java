package com.huayjx.ftshiyao.Cyfwdw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.huayjx.ftshiyao.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lhf on 2015/6/11.
 */
public class CyCameraAdapter extends ArrayAdapter<Bitmap>{
    private int mResourceId;
    private List<Bitmap> listBitmap;
    private LayoutInflater mLayoutInflater;

    public CyCameraAdapter(Context context, int imgViewResourceId, List<Bitmap> listBitmap) {
        super(context, imgViewResourceId, listBitmap);
        this.mResourceId = imgViewResourceId;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap = getItem(position);
        RelativeLayout userListItem = new RelativeLayout(getContext());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, userListItem,true);
        ImageView image = (ImageView) view.findViewById(R.id.cameraImage);
        image.setImageBitmap(bitmap);
        return view;
        }
    }

