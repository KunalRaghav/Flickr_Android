package com.example.kunalraghav.flickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);            android:parentActivityName=".MainActivity"

//        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        PhotoData photoData =(PhotoData) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photoData != null){
            ImageView imageView =  findViewById(R.id.PhotoView);
            TextView title =findViewById(R.id.Photo_title);
            TextView tags = findViewById(R.id.PhotoTags);
            TextView author = findViewById(R.id.photoAuthor);
            title.setText("Title: "+photoData.getTitle());
            Glide.with(this)
                    .load(photoData.getBig_img_url())
                    .apply(new RequestOptions()
                            .error(R.drawable.placeholder)
                    )
                    .into(imageView);
            author.setText("Author: "+photoData.getAuthor());
            tags.setText("Tags: "+photoData.getTags());
        }

    }

}
