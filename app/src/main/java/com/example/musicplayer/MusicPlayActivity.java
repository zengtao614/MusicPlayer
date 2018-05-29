package com.example.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicPlayActivity extends AppCompatActivity {

    final static String MUSIC_NAME = "musicName";
    final static String SINGER_NAME = "singerName";
    final static String ALBUM_ID = "albumId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        ImageView albumView = (ImageView)findViewById(R.id.album_view);
        TextView musicName = (TextView)findViewById(R.id.music_name);
        TextView singerName = (TextView)findViewById(R.id.singer_name);
        albumView.setBackground(getAlbumArt(getIntent().getIntExtra(ALBUM_ID,0)));
        musicName.setText(getIntent().getStringExtra(MUSIC_NAME));
        singerName.setText(getIntent().getStringExtra(SINGER_NAME));

    }

    private BitmapDrawable getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur =this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        Bitmap bm = null;
        BitmapDrawable bmpDraw = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
            bmpDraw = new BitmapDrawable(bm);
        }
        return bmpDraw;
    }
}
