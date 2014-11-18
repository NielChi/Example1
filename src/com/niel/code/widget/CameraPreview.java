package com.niel.code.widget;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder = null;
	private Camera mCamera = null;

	public CameraPreview(Context context) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(width, height);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		releaseCamera();
		if(mCamera == null) {
			mCamera = Camera.open();
		}
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mCamera != null) {
			stopPreviewAndFreeCamera();
		}
	}
	
	private void releaseCamera() {
		if (mCamera != null) {
	        mCamera.release();
	        mCamera = null;
	    }
	}
	
	private void stopPreviewAndFreeCamera() {

	    if (mCamera != null) {
	        mCamera.stopPreview();
	        mCamera.release();
	        mCamera = null;
	    }
	}
	
}