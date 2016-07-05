package com.jprarama.musicapp.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jprarama.musicapp.R;
import com.jprarama.musicapp.Utility;
import com.jprarama.musicapp.adapter.AudioItemAdapter;
import com.jprarama.musicapp.model.AudioItem;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = SearchResultActivity.class.getName();

    private ArrayList<AudioItem> musicList;
    private AudioItemAdapter adapter;
    private ListView listView;
    private TextView tvQuery;
    private TextView tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        tvQuery = (TextView) findViewById(R.id.tvQuery);
        tvNoResults = (TextView) findViewById(R.id.tvNoResults);

        adapter = new AudioItemAdapter(this, android.R.layout.simple_list_item_1);
        adapter.setNotifyOnChange(false);
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

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            tvQuery.setText(query);

            musicList = Utility.getAudioFiles(this, query);
            listItems();
        }

        intent.setAction(null);
        setIntent(null);
    }

    private void listItems() {
        if (musicList == null) {
            Log.w(TAG, "Music List is null");
            return;
        }

        if (musicList.isEmpty()) {
            tvNoResults.setText(getString(R.string.no_results));
            tvNoResults.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return;
        }
        tvNoResults.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(musicList);
        adapter.notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);
    }
}
