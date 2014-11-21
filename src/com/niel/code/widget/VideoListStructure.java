package com.niel.code.widget;

import android.graphics.Bitmap;

public class VideoListStructure {
	private Bitmap mBitmap;
	private String mVideoName;
	private String mVideoFileName;
	private String mVideoPath;
	private boolean isDelete;
	
	public VideoListStructure() {
		
	}
	
	public void setScreenshot(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}
	
	public Bitmap getScreenshot() {
		return this.mBitmap;
	}
	
	public void setVideoName(String name) {
		this.mVideoName = name;
	}
	
	public String getVideoName() {
		return this.mVideoName;
	}
	
	public void setVideoFileName(String name) {
		this.mVideoFileName = name;
	}
	
	public String getVideoFileName() {
		return this.mVideoFileName;
	}
	
	public void setVideoPath(String path) {
		this.mVideoPath = path;
	}
	
	public String getVideoPath() {
		return this.mVideoPath;
	}
	
	public void deleteFile(boolean delete) {
		isDelete = delete;
	}
	
	public boolean isDetele() {
		return isDelete;
	}
	
	public void releaseMemory() {
		this.mBitmap.recycle();
		this.mBitmap = null;
		this.mVideoName = null;
		this.mVideoFileName = null;
		this.mVideoPath = null;
	}
}