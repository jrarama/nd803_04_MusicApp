package com.jprarama.musicapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jprarama.musicapp.R;
import com.jprarama.musicapp.Utility;
import com.jprarama.musicapp.adapter.AudioItemAdapter;
import com.jprarama.musicapp.model.AudioItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AudioItem> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicList = Utility.getAudioFiles(this);
        ListView listView = (ListView) findViewById(R.id.musicList);
        AudioItemAdapter adapter = new AudioItemAdapter(this, android.R.layout.simple_list_item_1, musicList);
        listView.setAdapter(adapter);

        final Activity activity = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(activity, MusicPlayerActivity.class);
                intent.setAction(MusicPlayerActivity.ACTION_PLAY);
                intent.putExtra(MusicPlayerActivity.CURRENT_TRACK_KEY, position);
                intent.putExtra(MusicPlayerActivity.AUDIO_ITEMS_KEY, musicList);

                startActivity(intent);
            }
        });
    }
}