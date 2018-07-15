package com.example.kunalraghav.flickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "MainActivity";
    private FlickrRecycleViewAdapter recycleViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        FloatingActionButton refresh = findViewById(R.id.fab);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: refresh called");
                onResume();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(this,recyclerView,this));
        recycleViewAdapter = new FlickrRecycleViewAdapter(new ArrayList<PhotoData>(),this);
        recyclerView.setAdapter(recycleViewAdapter);
//        String url ="https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
//        RawJsonData getJson = new RawJsonData(this);
//        getJson.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main,menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu created");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }

        if(item.getItemId()==R.id.action_settings){
            Toast.makeText(this,"Settings are WIP",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: start");
        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("en-us",true,this);
//        flickrJsonData.executeOnSameThread("android,oreo");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryTags = sharedPreferences.getString(FLICKR_QUERY,"");
        if(queryTags.length()>0){
            flickrJsonData.execute(queryTags);
        }
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status) {
        if(status==DownloadStatus.OK){
//            Log.d(TAG, "onDataAvailable: data is: "+data);
            recycleViewAdapter.loadNewData(data);
        }else {
            Log.e(TAG, "onDataAvailable: download failed with status: "+status);

        }




    }

    @Override
    public void OnItemClickListener(View view, int position) {
//        Toast.makeText(MainActivity.this,"Normal Tap at "+position+" position",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemLongClickListener(View view, int position) {
//        Toast.makeText(MainActivity.this,"Long Tap "+position+" position",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,recycleViewAdapter.getPhotoData(position));
        startActivity(intent);
    }
}

