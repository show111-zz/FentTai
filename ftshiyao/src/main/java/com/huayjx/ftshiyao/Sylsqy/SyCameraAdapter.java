package com.huayjx.ftshiyao.Sylsqy;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huayjx.ftshiyao.R;

import java.util.List;

/**
 * Created by lhf on 2015/6/12.
 */
public class SyCameraAdapter extends ArrayAdapter<Bitmap> {
    private int mResourceId;
    private List<Bitmap> listBitmap;
    private LayoutInflater mLayoutInflater;

    public SyCameraAdapter(Context context, int imgViewResourceId, List<Bitmap> listBitmap) {
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
