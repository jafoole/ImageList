package com.oliverbud.android.imagelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by oliverbud on 5/26/15.
 */
public class ImageListAdapter extends ArrayAdapter<ImageDataItem>{

    private Context context;
    private ArrayList<ImageDataItem> dataItems;



    static class ViewHolder {
        public ImageView image;
        public TextView name;
        public FrameLayout layout;
    }

    public ArrayList<ImageDataItem> getData(){
        return  this.dataItems;
    }

    public ImageListAdapter(Context context, ArrayList<ImageDataItem> dataItems){
        super(context, 0, dataItems);
        this.context = context;
        this.dataItems = dataItems;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.image_row_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.imageView);
            viewHolder.name = (TextView) rowView
                    .findViewById(R.id.textView);
            viewHolder.layout = (FrameLayout) rowView
                    .findViewById(R.id.layout);
            rowView.setTag(viewHolder);
        }


        ViewHolder holder = (ViewHolder) rowView.getTag();
        String url = getItem(position).url;

        holder.name.setText(getItem(position).name);

        Log.d("itemListApp", "holder getview");

        holder.image.setLayoutParams(new LinearLayout.LayoutParams(getItem(position).width, getItem(position).height));
        Picasso.with(context).load(url).placeholder(new ColorDrawable(Color.TRANSPARENT)).into(holder.image);

        return rowView;
    }
}
