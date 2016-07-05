package com.jprarama.musicapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jprarama.musicapp.R;
import com.jprarama.musicapp.model.AudioItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    public static final String TAG = MusicPlayerActivity.class.getName();

    public static final String AUDIO_ITEMS_KEY = "audio_items";
    public static final String CURRENT_TRACK_KEY = "current_track";
    public static final String ACTION_PLAY = "play_audio";
    private static final String UNKNOWN = "unknown";

    private int currentTrack;
    private AudioItem currentAudio;

    private static MediaPlayer mediaPlayer;

    private ArrayList<AudioItem> audioItems;

    private TextView tvArtist;
    private TextView tvTitle;
    private TextView tvTimeMin;
    private TextView tvTimeMax;
    private SeekBar seekBar;

    private ImageButton btnPlay;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);

        initializeMediaPlayer();
        initComponents();
        handleIntent(getIntent());

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaReady();
        }
    }

    private void initComponents() {
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        ImageButton btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        ImageButton btnNext = (ImageButton) findViewById(R.id.btnNext);

        tvTimeMin = (TextView) findViewById(R.id.tvTimeMin);
        tvTimeMax = (TextView) findViewById(R.id.tvTimeMax);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }

                updatePlayButton();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTrack(currentTrack - 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTrack(currentTrack + 1);
            }
        });

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
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    setTrack(currentTrack + 1);
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaReady();
                }
            });
        }
    }

    private void mediaReady() {
        final int duration = mediaPlayer.getDuration();
        tvTimeMax.setText(getMinutes(duration));
        seekBar.setMax(duration);

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        final Activity activity = this;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final TimerTask task = this;
                if (!mediaPlayer.isPlaying()) {
                    boolean cancel = task.cancel();
                    if(!cancel) timer.cancel();
                    return;
                }

                final int curPos = mediaPlayer.getCurrentPosition();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimeMin.setText(getMinutes(curPos));
                        seekBar.setProgress(curPos);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 500);
    }

    private void setTrack(int position) {
        if (audioItems == null || audioItems.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_playlist), Toast.LENGTH_LONG).show();
            return;
        }

        currentTrack = Math.max(0, position % audioItems.size());
        currentAudio = audioItems.get(currentTrack);
        updateDisplay();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentAudio.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_playing), Toast.LENGTH_LONG).show();

            if (currentTrack + 1 < audioItems.size()) {
                setTrack(currentTrack + 1);
            }
            return;
        }
        mediaPlayer.start();
        updatePlayButton();
    }

    private void updatePlayButton() {
        btnPlay.setImageResource(mediaPlayer.isPlaying() ? android.R.drawable.ic_media_pause :
                android.R.drawable.ic_media_play);
    }

    private void updateDisplay() {
        if (currentAudio == null) {
            Log.w(TAG, "Current audio is null");
            return;
        }

        tvTitle.setText(currentAudio.getTitle());

        String artist = currentAudio.getArtist();
        if (artist.contains(UNKNOWN)) {
            artist = getString(R.string.unknown_artist);
        }

        if (currentAudio.getAlbum() != null) {
            artist = artist + ", " + String.format(getString(R.string.album_format), currentAudio.getAlbum());
        }
        tvArtist.setText(artist);
    }

    private void handleIntent(Intent intent) {
        if (ACTION_PLAY.equals(intent.getAction())) {
            currentTrack = intent.getIntExtra(CURRENT_TRACK_KEY, 0);
            audioItems = intent.getParcelableArrayListExtra(AUDIO_ITEMS_KEY);

            Log.w(TAG, "Current Track: " + currentTrack);
            setTrack(currentTrack);
        }
        intent.setAction(null);
        setIntent(null);
    }

    private String getMinutes(int startTime) {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)));
    }

}
