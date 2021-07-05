package com.app.cartoonme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

public class myStories extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories);
        ListView myListStory=findViewById(R.id.List_view_pdf);
//        recyclerView=findViewById(R.id.storiesrecyc);
        /*recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        //adapter=new rvAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
*/


    }
}