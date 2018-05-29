package com.lab.multiplexer.NewsForMe.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.util.TimeFormatUtil;
import com.devbrackets.android.playlistcore.event.MediaProgress;
import com.devbrackets.android.playlistcore.event.PlaylistItemChange;
import com.devbrackets.android.playlistcore.listener.PlaylistListener;
import com.devbrackets.android.playlistcore.listener.ProgressListener;
import com.devbrackets.android.playlistcore.service.PlaylistServiceCore;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Helper.PlaylistManager;
import com.lab.multiplexer.NewsForMe.Activity.Model.MediaItem;
import com.lab.multiplexer.NewsForMe.Activity.Model.Samples;
import com.lab.multiplexer.NewsForMe.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class AudioPlayerActivity extends AppCompatActivity implements PlaylistListener<MediaItem>, ProgressListener {
    public static final String EXTRA_INDEX = "EXTRA_INDEX";
    public static final int PLAYLIST_ID = 4; //Arbitrary, for the example

    private ProgressBar loadingBar;
    private ImageView artworkView;

    private TextView currentPositionView;
    private TextView durationView;

    private SeekBar seekBar;
    private boolean shouldSetDuration;
    private boolean userInteracting;

    private ImageButton previousButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;

    private PlaylistManager playlistManager;
    private int selectedIndex = 0;

    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FM Radio");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        retrieveExtras();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playlistManager.unRegisterPlaylistListener(this);
        playlistManager.unRegisterProgressListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playlistManager = AppController.getPlaylistManager();
        playlistManager.registerPlaylistListener(this);
        playlistManager.registerProgressListener(this);

        //Makes sure to retrieve the current playback information
        updateCurrentPlaybackInformation();
    }

    @Override
    public boolean onPlaylistItemChanged(MediaItem currentItem, boolean hasNext, boolean hasPrevious) {
        shouldSetDuration = true;

        //Updates the button states
        nextButton.setEnabled(hasNext);
        previousButton.setEnabled(hasPrevious);

        //Loads the new image
        picasso.load(R.drawable.radio).into(artworkView);

        return true;
    }

    @Override
    public boolean onPlaybackStateChanged(@NonNull PlaylistServiceCore.PlaybackState playbackState) {
        switch (playbackState) {
            case STOPPED:
                finish();
                break;

            case RETRIEVING:
            case PREPARING:
            case SEEKING:
                restartLoading();
                break;

            case PLAYING:
                doneLoading(true);
                break;

            case PAUSED:
                doneLoading(false);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onProgressUpdated(@NonNull MediaProgress progress) {
        if (shouldSetDuration && progress.getDuration() > 0) {
            shouldSetDuration = false;
            setDuration(progress.getDuration());
        }

        if (!userInteracting) {
            seekBar.setSecondaryProgress((int) (progress.getDuration() * progress.getBufferPercentFloat()));
            seekBar.setProgress((int) progress.getPosition());
            currentPositionView.setText(TimeFormatUtil.formatMs(progress.getPosition()));
        }

        return true;
    }

    /**
     * Makes sure to update the UI to the current playback item.
     */
    private void updateCurrentPlaybackInformation() {
        PlaylistItemChange<MediaItem> itemChangedEvent = playlistManager.getCurrentItemChange();
        if (itemChangedEvent != null) {
            onPlaylistItemChanged(itemChangedEvent.getCurrentItem(), itemChangedEvent.hasNext(), itemChangedEvent.hasPrevious());
        }

        PlaylistServiceCore.PlaybackState currentPlaybackState = playlistManager.getCurrentPlaybackState();
        if (currentPlaybackState != PlaylistServiceCore.PlaybackState.STOPPED) {
            onPlaybackStateChanged(currentPlaybackState);
        }

        MediaProgress progressEvent = playlistManager.getCurrentProgress();
        if (progressEvent != null) {
            onProgressUpdated(progressEvent);
        }
    }

    private void retrieveExtras() {
        Bundle extras = getIntent().getExtras();
        selectedIndex = extras.getInt(EXTRA_INDEX, 0);
    }

    private void init() {
        retrieveViews();
        setupListeners();

        picasso = Picasso.with(getApplicationContext());

        boolean generatedPlaylist = setupPlaylistManager();
        startPlayback(generatedPlaylist);
    }

    private void doneLoading(boolean isPlaying) {
        loadCompleted();
        updatePlayPauseImage(isPlaying);
    }

    private void updatePlayPauseImage(boolean isPlaying) {
        int resId = isPlaying ? R.drawable.playlistcore_ic_pause_black : R.drawable.playlistcore_ic_play_arrow_black;
        playPauseButton.setImageResource(resId);
    }

    public void loadCompleted() {
        playPauseButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void restartLoading() {
        playPauseButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        loadingBar.setVisibility(View.VISIBLE);
    }

    private void setDuration(long duration) {
        seekBar.setMax((int) duration);
        durationView.setText(TimeFormatUtil.formatMs(duration));
    }

    private boolean setupPlaylistManager() {
        playlistManager = AppController.getPlaylistManager();

        if (playlistManager.getId() == PLAYLIST_ID) {
            return false;
        }

        List<MediaItem> mediaItems = new LinkedList<>();
        for (Samples.Sample sample : Samples.getAudioSamples()) {
            MediaItem mediaItem = new MediaItem(sample, true);
            mediaItems.add(mediaItem);
        }

        playlistManager.setParameters(mediaItems, selectedIndex);
        playlistManager.setId(PLAYLIST_ID);

        return true;
    }

    private void retrieveViews() {
        loadingBar = (ProgressBar) findViewById(R.id.audio_player_loading);
        artworkView = (ImageView) findViewById(R.id.audio_player_image);

        currentPositionView = (TextView) findViewById(R.id.audio_player_position);
        durationView = (TextView) findViewById(R.id.audio_player_duration);

        seekBar = (SeekBar) findViewById(R.id.audio_player_seek);

        previousButton = (ImageButton) findViewById(R.id.audio_player_previous);
        playPauseButton = (ImageButton) findViewById(R.id.audio_player_play_pause);
        nextButton = (ImageButton) findViewById(R.id.audio_player_next);
    }

    private void setupListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBarChanged());

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokePrevious();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokePausePlay();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokeNext();
            }
        });
    }

    private void startPlayback(boolean forceStart) {
        //If we are changing audio files, or we haven't played before then start the playback
        if (forceStart || playlistManager.getCurrentPosition() != selectedIndex) {
            playlistManager.setCurrentPosition(selectedIndex);
            playlistManager.play(0, false);
        }
    }

    private class SeekBarChanged implements SeekBar.OnSeekBarChangeListener {
        private int seekPosition = -1;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            seekPosition = progress;
            currentPositionView.setText(TimeFormatUtil.formatMs(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            userInteracting = true;

            seekPosition = seekBar.getProgress();
            playlistManager.invokeSeekStarted();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            userInteracting = false;

            //noinspection Range - seekPosition won't be less than 0
            playlistManager.invokeSeekEnded(seekPosition);
            seekPosition = -1;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
