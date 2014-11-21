package com.niel.code.example;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.niel.code.dialog.VideoListDialog;
import com.niel.code.media.RecordVideo;
import com.niel.code.widget.CameraPreview;

public class Example extends Activity implements SurfaceHolder.Callback {
	
	private RecordVideo mRecord = null;
	private Camera mCamera;
	private Callback mCallback;
	private long foolproofTime = 0L;
	
	private RecordStatus mStatus;
	enum RecordStatus {
		RECORD_START, RECORD_STOP
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera_main);
		mStatus = RecordStatus.RECORD_STOP;
		mCallback = this;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				((CameraPreview)findViewById(R.id.cameraPreview)).setCallback(mCallback);
				((CameraPreview)findViewById(R.id.cameraPreview)).startPreview();
				mCamera = ((CameraPreview)findViewById(R.id.cameraPreview)).getCamera();
				((LinearLayout) findViewById(R.id.loadingProgress)).setVisibility(View.GONE);
			}
		}, 1000);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initLayout();
	}
	
	private void initLayout() {
		((ImageButton) findViewById(R.id.recordButton)).setOnClickListener(onClick);
		((ImageButton) findViewById(R.id.videoListButton)).setOnClickListener(onClick);
		if(mStatus == RecordStatus.RECORD_STOP) {
			((ImageView) findViewById(R.id.recordPrompt)).setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onPause() {
		if(mCamera != null) {
			mCamera.stopPreview();
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if(mCamera != null) {
			mCamera.unlock();
			mCamera.release();
			mCamera = null;
		}
		super.onDestroy();
	}
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.recordButton:
				if(mStatus == RecordStatus.RECORD_STOP) {
					foolproofTime = System.currentTimeMillis();
					((ImageButton) findViewById(R.id.recordButton)).setImageResource(R.drawable.gtk_media_stop);
					((ImageView) findViewById(R.id.recordPrompt)).setVisibility(View.VISIBLE);
					mStatus = RecordStatus.RECORD_START;
					mCamera.unlock();
					mRecord = new RecordVideo();
					mRecord.setRecordInformation(mCamera, ((CameraPreview)findViewById(R.id.cameraPreview)).getSurfaceHolder().getSurface(),
							1280, 
							720);
					mRecord.startRecord();
				} else if(mStatus == RecordStatus.RECORD_START) {
					if((System.currentTimeMillis() - foolproofTime) > 5000) {
						((ImageButton) findViewById(R.id.recordButton)).setImageResource(R.drawable.gtk_media_record);
						((ImageView) findViewById(R.id.recordPrompt)).setVisibility(View.GONE);
						mStatus = RecordStatus.RECORD_STOP;
						mRecord.stopRecorder();
					}
				}
				break;
			case R.id.videoListButton:
				Intent intent = new Intent(getApplicationContext(), VideoListDialog.class);
				startActivity(intent);
				break;
			}
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
}