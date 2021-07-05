package com.app.cartoonme;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private Context mcontext;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

           ClearALL();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
         //getDataFromFireBase();
        ClearALL();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             for(DataSnapshot postSnapshot:snapshot.getChildren())
             {
                Upload upload=postSnapshot.getValue(Upload.class);
                mUploads.add(upload);
             }
             mAdapter=new ImageAdapter(ImagesActivity.this,mUploads);

             mRecyclerView.setAdapter(mAdapter);
             mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

    }

   /* private void getDataFromFireBase()
    {
        Query query=mDatabaseRef.child("uploads");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ClearALL();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Upload upload=new Upload();
                    upload.setmImageurl(snapshot1.child("mImageurl").getValue().toString());
                    upload.setmName(snapshot1.child("mName").getValue().toString());
                    mUploads.add(upload);
                }
                mAdapter =new ImageAdapter(ImagesActivity.this,mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
*/
   private void ClearALL()
    {
        if(mUploads!=null)
        {
            mUploads.clear();
            if(mAdapter!=null)
            {
                mAdapter.notifyDataSetChanged();
            }
        }
        mUploads=new ArrayList<>();
    }

}