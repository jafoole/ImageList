package com.oliverbud.android.imagelist;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.GenericEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by oliverbud on 5/26/15.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>{

    private ArrayList<ImageDataItem> dataItems;


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public FrameLayout layout;

        public ViewHolder(View view){
            super(view);
            image = (ImageView)view.findViewById(R.id.imageView);
            name = (TextView)view.findViewById(R.id.textView);
            layout = (FrameLayout)view.findViewById(R.id.layout);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new GenericEvent("Hello everyone!"));
                }
            });

        }
    }

    public ImageListAdapter(ArrayList<ImageDataItem> dataItems){
        this.dataItems = dataItems;

    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String url = dataItems.get(i).url;

        viewHolder.name.setText(dataItems.get(i).name);

        if (url != null) {
            viewHolder.image.setLayoutParams(new LinearLayout.LayoutParams(dataItems.get(i).width, dataItems.get(i).height));

            Picasso.with(App.getAppContext()).load(url).placeholder(new ColorDrawable(dataItems.get(i).color)).into(viewHolder.image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.image_row_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

}
