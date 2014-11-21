package com.niel.code.dialog;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.niel.code.Tool.FileManager;
import com.niel.code.example.R;
import com.niel.code.widget.VideoListAdapter;
import com.niel.code.widget.VideoListStructure;

public class VideoListDialog extends Activity {
	private final String TAG = "Video"; 
	private final int DELAYTIME = 1000;
	private Context mContext;
	
	private PlayStatus mStatus;
	enum PlayStatus {
		STATUS_IDEL, STATUS_PREPARED, STATUS_PLAY, STATUS_PAUSE, STATUS_STOP
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = getApplicationContext();
		mStatus = PlayStatus.STATUS_IDEL;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.video_list_dialog);
		((Button) findViewById(R.id.closeButton)).setOnClickListener(onClick);
		((ImageButton) findViewById(R.id.videoPlayPause)).setOnClickListener(onClick);
		ArrayList<VideoListStructure> list = FileManager.getList(Environment.getExternalStorageDirectory()+"/Video");
		((ListView) findViewById(R.id.listView)).setAdapter(new VideoListAdapter(mContext, list));
		((ListView) findViewById(R.id.listView)).setOnItemClickListener(onItemClick);
		((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).setAdapterItem(list);
		((LinearLayout) findViewById(R.id.mediaControlView)).setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mStatus != PlayStatus.STATUS_STOP) {
			((VideoView) findViewById(R.id.videoView)).suspend();
			((VideoView) findViewById(R.id.videoView)).stopPlayback();
			mStatus = PlayStatus.STATUS_STOP;
			((LinearLayout) findViewById(R.id.mediaControlView)).setVisibility(View.GONE);
		}
		((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).releaseMemory();
	}

	@Override
	public void onBackPressed() {
		for(int i = 0; i < ((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getCount(); i++) {
			if(((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).isDetele()) {
				File file = new File((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoPath().split("\\.")[0])+".mp4");
				if(file.exists()) {
					file.delete();
				}
				file = new File((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoPath().split("\\.")[0])+".xml");
				if(file.exists()) {
					file.delete();
				}
				((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).releaseMemory();
			}
		}
		super.onBackPressed();
	}
	
	public Runnable onProgress = new Runnable() {
		
		@Override
		public void run() {
			if( (mStatus != PlayStatus.STATUS_IDEL) && (mStatus != PlayStatus.STATUS_STOP) ){
				setCurrentPosition();
			}
		}
	}; 

	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.closeButton:
				onBackPressed();
				break;
			case R.id.videoPlayPause:
				if( (mStatus != PlayStatus.STATUS_IDEL) && (mStatus != PlayStatus.STATUS_STOP) ){
					if(mStatus == PlayStatus.STATUS_PLAY) {
						mStatus = PlayStatus.STATUS_PAUSE;
						((VideoView) findViewById(R.id.videoView)).pause();
						((ImageButton) findViewById(R.id.videoPlayPause)).setImageResource(R.drawable.gtk_media_play_ltr);
					} else if(mStatus == PlayStatus.STATUS_PAUSE) {
						mStatus = PlayStatus.STATUS_PLAY;
						((VideoView) findViewById(R.id.videoView)).start();
						((ImageButton) findViewById(R.id.videoPlayPause)).setImageResource(R.drawable.gtk_media_pause);
					}
				}
				break;
			}
		}
	};
	
	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			((VideoView) findViewById(R.id.videoView)).setVideoPath((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(position)).getVideoPath().split("\\.")[0])+".mp4");
			((VideoView) findViewById(R.id.videoView)).setVisibility(View.VISIBLE);
			((VideoView) findViewById(R.id.videoView)).setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					setVideoDuration(mp.getDuration());
				}
			});
			((VideoView) findViewById(R.id.videoView)).start();
			mStatus = PlayStatus.STATUS_PLAY;
			((ListView) findViewById(R.id.listView)).setVisibility(View.GONE);
			((Button) findViewById(R.id.closeButton)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.mediaControlView)).setVisibility(View.VISIBLE);
			((VideoView) findViewById(R.id.videoView)).setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					((VideoView) findViewById(R.id.videoView)).setVisibility(View.GONE);
					((VideoView) findViewById(R.id.videoView)).stopPlayback();
					((ListView) findViewById(R.id.listView)).setVisibility(View.VISIBLE);
					((Button) findViewById(R.id.closeButton)).setVisibility(View.VISIBLE);
					mStatus = PlayStatus.STATUS_STOP;
					((LinearLayout) findViewById(R.id.mediaControlView)).setVisibility(View.GONE);
					((ProgressBar) findViewById(R.id.videoProgress)).removeCallbacks(onProgress);
				}
			});
		}
	};
	
	private void setVideoDuration(int duration) {
		int durationTime = duration / 1000;
		int mm = durationTime / 60;
		int hh = mm / 60;
		int ss = durationTime % 60;
		mm %= 60;
		((TextView) findViewById(R.id.videoTotalTime)).setText(String.format("%02d:%02d:%02d", hh,mm,ss));
		((ProgressBar) findViewById(R.id.videoProgress)).setMax(durationTime);
		((ProgressBar) findViewById(R.id.videoProgress)).setProgress(0);
		((ProgressBar) findViewById(R.id.videoProgress)).postDelayed(onProgress, DELAYTIME);
	}
	
	private void setCurrentPosition() {
		int currentTime = ((VideoView) findViewById(R.id.videoView)).getCurrentPosition() / 1000;
		int mm = currentTime / 60;
		int hh = mm / 60;
		int ss = currentTime % 60;
		mm %= 60;
		((TextView) findViewById(R.id.videoTime)).setText(String.format("%02d:%02d:%02d", hh,mm,ss));
		((ProgressBar) findViewById(R.id.videoProgress)).setProgress(currentTime);
		((ProgressBar) findViewById(R.id.videoProgress)).postDelayed(onProgress, DELAYTIME);
	}
	
}