package com.kentkolej.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FotoZoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_zoom);

        Intent zoom = getIntent();
        String url = zoom.getStringExtra("foto_url");

        ImageView img = findViewById(R.id.img_zoom);
        ImageButton geri = findViewById(R.id.btn_zoom_geri);

        Picasso.get()
                .load("http://www.kentkoleji.com.tr/v2/json/chat/upload/"+url+".jpg")
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.mipmap.ic_launcher_round)
                .into(img);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
