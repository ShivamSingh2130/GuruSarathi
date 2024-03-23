package com.example.gurusarthi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    ImageView fullimg,cross;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        fullimg=findViewById(R.id.fullimage);
        cross=findViewById(R.id.cross);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.get().load(getIntent().getStringExtra("image")) .placeholder(R.drawable.photocamera) // Placeholder image resource
                .error(R.drawable.photocamera) // Error image resource
                .into(fullimg);

    }
}