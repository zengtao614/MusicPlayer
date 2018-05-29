package com.example.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Music> musicList = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private List<Album> albumList = new ArrayList<>();
    private AlbumAdapter albumAdapter;

    private List<BitmapDrawable> albumImageList = new ArrayList<>();


    private DrawerLayout mDrawerLayout;
    private ImageView getalbum;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getalbum = (ImageView)findViewById(R.id.iv_album);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_dehaze_white_36dp);
        }
        navigationView.setCheckedItem(R.id.nav_music);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_album:
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        albumAdapter = new AlbumAdapter(albumList,albumImageList);
                        recyclerView.setAdapter(albumAdapter);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_music:
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        musicAdapter = new MusicAdapter(musicList,albumImageList);
                        recyclerView.setAdapter(musicAdapter);
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;

            }
        });

        Listinit();
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(musicList,albumImageList);
        recyclerView.setAdapter(musicAdapter);
    }



    private void Listinit(){
        musicList.clear();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=MainActivity.this.getContentResolver().query(uri,
                new String[]{
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID},null,null,null);
        while (cursor.moveToNext()) {
            musicList.add(new Music(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

            ));
        }

        albumList.clear();
        cursor = MainActivity.this.getContentResolver().query(uri,
                new String[]{
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM_ID},null,null,null);
        while (cursor.moveToNext()) {
            albumList.add(new Album(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
            ));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                albumImageList.clear();
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor=MainActivity.this.getContentResolver().query(uri,
                        new String[]{MediaStore.Audio.Media.ALBUM_ID},null,null,null);
                while (cursor.moveToNext()) {
                albumImageList.add(getAlbumArt(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
            }}
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"设置",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    public void playView(View view){
        Intent intent = new Intent(this,MusicPlayActivity.class);
        //intent.putExtra(MusicPlayActivity.MUSIC_NAME,)
        startActivity(intent);
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
