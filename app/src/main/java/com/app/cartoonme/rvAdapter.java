package com.app.cartoonme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.viewholder> {
    private ArrayList<story> storylist;
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.story,parent,false);
        viewholder vh=new viewholder(v);
        return vh;
    }

    public rvAdapter(ArrayList<story> storyArrayList)
    {
        storylist=storyArrayList;
    }
    //position: position of items in the list
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
          story currentstory=storylist.get(position);
          holder.storyimage.setImageResource(currentstory.getStoryImageResource());
          holder.storyname.setText(currentstory.getStoryname());
          holder.numofpages.setText(currentstory.getNumofpages());
    }

    @Override
    public int getItemCount() {
        return storylist.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder
    {
        public ImageView storyimage;
        public TextView storyname;
        public TextView numofpages;
        public Button sharebtn;
        public Button save_as_pdfbtn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            storyimage=itemView.findViewById(R.id.storyimage);
            storyname=itemView.findViewById(R.id.storyname);
           // numofpages=itemView.findViewById(R.id.numofpages);
            //sharebtn=itemView.findViewById(R.id.shareStory);
            //save_as_pdfbtn=itemView.findViewById(R.id.Deletepdf_btn);
        }
    }

}
