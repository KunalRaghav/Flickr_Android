package com.example.kunalraghav.flickr;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
            Resources resources = getResources();

            ImageView imageView =  findViewById(R.id.PhotoView);
            Glide.with(this)
                    .load(photoData.getBig_img_url())
                    .apply(new RequestOptions()
                            .error(R.drawable.placeholder)
                    )
                    .into(imageView);




            TextView title =findViewById(R.id.Photo_title);
            title.setText(resources.getString(R.string.photo_title_text,photoData.getTitle()));



            TextView tags = findViewById(R.id.PhotoTags);
            tags.setText(resources.getString(R.string.photo_tags_text,photoData.getTags()));





            TextView author = findViewById(R.id.photoAuthor);
            author.setText(resources.getString(R.string.photo_author_text,photoData.getAuthor()));





//            title.setText("Title: "+photoData.getTitle());

        }

    }

}
