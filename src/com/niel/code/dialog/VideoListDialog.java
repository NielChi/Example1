package com.niel.code.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.niel.code.Tool.FileManager;
import com.niel.code.example.R;
import com.niel.code.widget.VideoListAdapter;
import com.niel.code.widget.VideoListStructure;

public class VideoListDialog extends Activity {
	private final String TAG = "Video"; 
	private Context mContext;
	
	private ArrayList<HashMap<String, String>> mVideoList;
	private VideoView mVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = getApplicationContext();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.video_list_dialog);
		((Button) findViewById(R.id.closeButton)).setOnClickListener(onClick);
		mVideoList = FileManager.getMP4List(Environment.getExternalStorageDirectory()+"/Video");
		for(HashMap<String, String> map : mVideoList) {
			Log.d(TAG,"name "+map.get("name")+" path "+map.get("path"));
		}
		
		ArrayList<VideoListStructure> list = FileManager.getList(Environment.getExternalStorageDirectory()+"/Video");
		Log.d(TAG,"list size "+list.size());
		((ListView) findViewById(R.id.listView)).setAdapter(new VideoListAdapter(mContext, list));
		((ListView) findViewById(R.id.listView)).setOnItemClickListener(onItemClick);
		((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).setAdapterItem(list);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		for(int i = 0; i < ((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getCount(); i++) {
			if(((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).isDetele()) {
				Log.d(TAG,"remove file "+((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoFileName()
						+" "+((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoName()
						+" "+((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoPath());
				File file = new File((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoPath().split("\\.")[0])+".mp4");
				if(file.exists()) {
					file.delete();
				}
				file = new File((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(i)).getVideoPath().split("\\.")[0])+".xml");
				if(file.exists()) {
					file.delete();
				}
               
			}
		}
		super.onBackPressed();
	}
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.closeButton:
				onBackPressed();
				break;
			}
		}
	};
	
	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			// play media
			Log.d(TAG,"HIHIHIHIHIHIHIHIHIHI "+(((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(position)).getVideoPath().split("\\.")[0])+".mp4");
			((VideoView) findViewById(R.id.videoView)).setVideoPath((((VideoListStructure)((VideoListAdapter)(((ListView) findViewById(R.id.listView)).getAdapter())).getItem(position)).getVideoPath().split("\\.")[0])+".mp4");
			((VideoView) findViewById(R.id.videoView)).requestFocus();
			((VideoView) findViewById(R.id.videoView)).setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
				}
			});
			((VideoView) findViewById(R.id.videoView)).setVisibility(View.VISIBLE);
			((VideoView) findViewById(R.id.videoView)).start();
			((ListView) findViewById(R.id.listView)).setVisibility(View.GONE);
			((Button) findViewById(R.id.closeButton)).setVisibility(View.GONE);
			((VideoView) findViewById(R.id.videoView)).setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					((VideoView) findViewById(R.id.videoView)).setVisibility(View.GONE);
					((VideoView) findViewById(R.id.videoView)).stopPlayback();
					((ListView) findViewById(R.id.listView)).setVisibility(View.VISIBLE);
					((Button) findViewById(R.id.closeButton)).setVisibility(View.VISIBLE);
				}
			});
		}
	};
	
}