package com.niel.code.media;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import com.niel.code.Util.FileControl;

public class RecordVideo {
	private MediaRecorder mRecorder;
	
	private String fileName; 
	
	public RecordVideo() {
		this.mRecorder = new MediaRecorder();
	}
	
	public void setRecordInformation(Camera camera, Surface surface, int width, int height) {
		this.mRecorder.setCamera(camera);
		this.mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		this.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		this.mRecorder.setOutputFile(creatVideoFilePath());
		this.mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		this.mRecorder.setVideoEncodingBitRate(2048000);
		this.mRecorder.setVideoSize(width, height);
		this.mRecorder.setVideoFrameRate(30);
		this.mRecorder.setPreviewDisplay(surface);
		try{
			this.mRecorder.prepare();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startRecord() {
		this.mRecorder.start();
		writeData("Start Time : "+timeStamp2Date(String.valueOf(System.currentTimeMillis())));
	}
	
	public void stopRecorder() {
		if(this.mRecorder != null) {
			this.mRecorder.stop();
			this.mRecorder.reset();
			this.mRecorder.release();
			writeData("End Time : "+timeStamp2Date(String.valueOf(System.currentTimeMillis())));
			writeData("File Name : "+fileName);
		}
	}
	
	public void writeData(String data) {
		File sdcard = Environment.getExternalStorageDirectory();
		File veDir = new File(sdcard, "Video");
		if(!veDir.exists()) {
			veDir.mkdir();
		}
		FileControl.writeFile(veDir, fileName+".xml", data+"\n", true);
	}
	
	public String creatVideoFilePath() {
		File sdcard = Environment.getExternalStorageDirectory();
		File veDir = new File(sdcard, "Video");
		if(!veDir.exists()) {
			veDir.mkdir();
		}
		fileName = timeStamp2Date(String.valueOf(System.currentTimeMillis()));
		File file = new File(veDir, fileName+".mp4");
		String filePath = file.getAbsolutePath();
		Log.d("MediaRecorder", "file path "+filePath);
		return filePath;
	}
	
	public String timeStamp2Date(String timestampstring) {
		Long timestamp = Long.parseLong(timestampstring);
		String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(timestamp));
		return date;
	}
}