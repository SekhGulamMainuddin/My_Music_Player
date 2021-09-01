package com.sekhgmainuddin.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    int flag=0;
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (flag == 0) {
                            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                            flag++;
                        }
                        ArrayList<File> Mysongs = Collector(Environment.getExternalStorageDirectory());
                        String songname[]=new String[Mysongs.size()];
                        for(int i=0;i<Mysongs.size();i++){
                            songname[i]=Mysongs.get(i).getName();
                        }
                        ArrayAdapter ad = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,songname);
                        listView.setAdapter(ad);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String currentSong=listView.getItemAtPosition(i).toString();
                                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                                intent.putExtra("Songlist",Mysongs);
                                intent.putExtra("CurrentSong",currentSong);
                                intent.putExtra("Position",i);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }
    protected ArrayList Collector(File name){
        File[] songs=name.listFiles();
        ArrayList song=new ArrayList();
        if(songs!=null){
            for(File myfile:songs){
                if(myfile.isDirectory() && !myfile.isHidden()){
                    song.addAll(Collector(myfile));
                }
                else{
                    if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")){
                        song.add(myfile);
                    }
                }
            }

        }
        return song;
    }
}