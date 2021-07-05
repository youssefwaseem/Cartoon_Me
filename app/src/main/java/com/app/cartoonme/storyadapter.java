package com.app.cartoonme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class storyadapter extends ArrayAdapter<String> {
    private Context contextt;
    int res;
    public storyadapter(Context context,int resource,ArrayList<String> uploadName){
        super(context,resource,uploadName);
        contextt=context;
        res=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       String storyname= getItem(position).toString();

        LayoutInflater inflater=LayoutInflater.from(contextt);
        convertView=inflater.inflate(res,parent,false);

        TextView txt = convertView.findViewById(R.id.storyname);
        txt.setText(storyname);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pdf
            }
        });




        Button button = convertView.findViewById(R.id.sharebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share pdf

                Toast.makeText(getContext(),position+" this", Toast.LENGTH_SHORT).show();
               // Upload_File upload_file = mUploads.get(position);
               // url[0] = upload_file.getmImageurl();
                Intent ShareIntent=new Intent(Intent.ACTION_SEND);
                ShareIntent.setType("application/pdf");
               // ShareIntent.putExtra(Intent.EXTRA_TEXT,url[0]);
                ShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
              //  startActivity(Intent.createChooser(ShareIntent,"Share Story Via..."));
            }
        });



        Button button2 = convertView.findViewById(R.id.deletebtn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete story
            }
        });
        return convertView;
    }
}
