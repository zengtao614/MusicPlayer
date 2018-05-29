package com.example.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

public class AlbumInfo extends AppCompatActivity {

    final static String ALBUM_ID = "albumId";
    final static String ALBUM_NAME = "albumName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        Intent intent = getIntent();
        String albumName = intent.getStringExtra(ALBUM_NAME);
        int albumId = intent.getIntExtra(ALBUM_ID,0);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.colapsing_layout);
        ImageView albumView = (ImageView)findViewById(R.id.album_image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(albumName);
        Bitmap bm = null;
        String albumArt = getAlbumArt(albumId);
        if (albumArt != null) {
            bm = BitmapFactory.decodeFile(albumArt);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            albumView.setImageDrawable(bmpDraw);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

}
