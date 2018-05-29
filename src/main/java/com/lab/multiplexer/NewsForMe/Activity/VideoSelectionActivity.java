package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lab.multiplexer.NewsForMe.Activity.Adapter.SampleListAdapter;
import com.lab.multiplexer.NewsForMe.Activity.Model.Samples;
import com.lab.multiplexer.NewsForMe.R;


/**
 * A simple activity that allows the user to select a
 * video to play
 */
public class VideoSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_selection_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Live Tv Channels");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ListView exampleList = (ListView) findViewById(R.id.selection_activity_list);
        exampleList.setAdapter(new SampleListAdapter(this, Samples.getVideoSamples()));
        exampleList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startVideoPlayerActivity(position);
    }

    private void startVideoPlayerActivity(int selectedIndex) {
        Intent intent = new Intent(this, FullScreenVideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.EXTRA_INDEX, selectedIndex);
        startActivity(intent);
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