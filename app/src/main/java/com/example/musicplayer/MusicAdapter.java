package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;


import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
    private Context mContext;
    private List<Music> mMusicList;
    private List<BitmapDrawable> mAlbumImageList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicView;
        ImageView musicAlbum;
        TextView musicName;
        TextView singerName;

        public ViewHolder(View view){
            super(view);
            musicView = view;
            musicAlbum = (ImageView)view.findViewById(R.id.iv_album);
            musicName = (TextView)view.findViewById(R.id.tv_musicname);
            singerName = (TextView)view.findViewById(R.id.tv_singer);
        }
    }

    public MusicAdapter(List<Music> musicList,List<BitmapDrawable> albumImageList){
        mMusicList = musicList;
        mAlbumImageList = albumImageList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mMusicList.get(position);
        holder.musicName.setText(music.getMname());
        holder.singerName.setText(music.getSname());
        //Glide.with(mContext).load(music.getMid()).into(holder.musicAlbum);
        BitmapDrawable bmp = mAlbumImageList.get(position);
        holder.musicAlbum.setImageDrawable(bmp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.musicView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Music music = mMusicList.get(position);
                Intent intent = new Intent(mContext,MusicPlayActivity.class);
                intent.putExtra(MusicPlayActivity.MUSIC_NAME,music.getMname());
                intent.putExtra(MusicPlayActivity.SINGER_NAME,music.getSname());
                intent.putExtra(MusicPlayActivity.ALBUM_ID,music.getalbumId());
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}