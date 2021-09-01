package com.sekhgmainuddin.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateseek.interrupt();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    ImageView pause,previous,next;
    TextView textView;
    Thread updateseek;
    int position;
    String currentSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        seekBar=findViewById(R.id.seekBar);
        pause=findViewById(R.id.pause);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        textView=findViewById(R.id.textView);
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle=intent.getExtras();
        ArrayList<File> Songlist=(ArrayList)bundle.getParcelableArrayList("Songlist");
        position=intent.getIntExtra("position",0);
        currentSong=intent.getStringExtra("CurrentSong");
        textView.setText(currentSong);
        Uri uri= Uri.parse(Songlist.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek = new Thread(){
            @Override
            public void run() {
                super.run();
                int currentPosition=0;
                try {
                    while(currentPosition<mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(600);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.play);
                }
                else{
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(position!=0){
                        position=position-1;
                    }
                    else{
                        position=Songlist.size()-1;
                    }
                    Uri uri=Uri.parse(Songlist.get(position).toString());
                    mediaPlayer=MediaPlayer.create(MainActivity2.this,uri);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    currentSong=Songlist.get(position).getName();
                    textView.setText(currentSong);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(position==(Songlist.size()-1)){
                        position=0;
                    }
                    else{
                        position=position+1;
                    }
                    Uri uri=Uri.parse(Songlist.get(position).toString());
                    mediaPlayer=MediaPlayer.create(MainActivity2.this,uri);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    currentSong=Songlist.get(position).getName();
                    textView.setText(currentSong);
            }
        });
    }
}