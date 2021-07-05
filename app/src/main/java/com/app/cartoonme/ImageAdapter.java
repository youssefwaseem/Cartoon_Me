package com.app.cartoonme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

/*public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>
{
    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context context,List<Upload> uploads)
    {
       mContext=context;
       mUploads=uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
       return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
     Upload uploadCurrent=mUploads.get(position);
     holder.textViewName.setText(uploadCurrent.getmName());
      /* Picasso.with(mContext)
                .load(uploadCurrent.getmImageurl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);*/
    /*   Glide.with(mContext)
                .asBitmap()
                .load(uploadCurrent.getmImageurl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.text_view_name);
            imageView=itemView.findViewById(R.id.image_view_upload);
        }
    }
}*/
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    private static final String Tag="RecyclerView";

    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context mContext, List<Upload> mUploads)
    {
    this.mContext=mContext;
    this.mUploads=mUploads;

    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
      // holder.textview.setText(mUploads.get(position).getmName());

       Glide.with(mContext)
               .load(mUploads.get(position).getmImageurl())
               .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       ImageView imageview;
       TextView textview;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageview=itemView.findViewById(R.id.image_view_upload);
            textview=itemView.findViewById(R.id.text_view_name);
        }
    }

}
