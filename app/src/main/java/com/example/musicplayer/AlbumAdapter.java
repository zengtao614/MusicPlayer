package com.example.musicplayer;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    private Context mContext;
    private List<Album> mAlbumList;
    private List<BitmapDrawable> mAlbumImageList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView musicAlbum;
        TextView albumName;
        TextView singerName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            musicAlbum = (ImageView)view.findViewById(R.id.music_album);
            albumName = (TextView)view.findViewById(R.id.album_name);
            singerName = (TextView)view.findViewById(R.id.singer_name);
        }
    }

    public AlbumAdapter(List<Album> albumList,List<BitmapDrawable> albumImageList){
        mAlbumList = albumList;
        mAlbumImageList = albumImageList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = mAlbumList.get(position);
        holder.albumName.setText(album.getAlbumName());
        holder.singerName.setText(album.getSingerName());
        //Glide.with(mContext).load(music.getMid()).into(holder.musicAlbum);
        BitmapDrawable bmp = mAlbumImageList.get(position);
        holder.musicAlbum.setImageDrawable(bmp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_item,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Album album = mAlbumList.get(position);
                Intent intent = new Intent(mContext,AlbumInfo.class);
                intent.putExtra(AlbumInfo.ALBUM_NAME,album.getAlbumName());
                intent.putExtra(AlbumInfo.ALBUM_ID,album.getAlbumId());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = mContext.getContentResolver().query(
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