package com.niel.code.widget;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView {
	private SurfaceHolder mHolder = null;
	private Camera mCamera = null;

	public CameraPreview(Context context) {
		super(context);
	}
	
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public SurfaceHolder getSurfaceHolder() {
		return mHolder;
	}
	
	public Camera getCamera() {
		return mCamera;
	}
	
	public void startPreview() {
		if(mCamera == null) {
			mCamera = Camera.open();
		}
        
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewFrameRate(30);
        parameters.setPreviewSize(1280, 720);
        mCamera.setParameters(parameters);
        try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopPreview() {
		if(mCamera != null)
			mCamera.stopPreview();
	}
	
	public void setCallback(Callback callback) {
		mHolder = getHolder();
		mHolder.addCallback(callback);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	private void releaseCamera() {
		if (mCamera != null) {
	        mCamera.release();
	        mCamera = null;
	    }
	}
	
	public void stopPreviewAndFreeCamera() {
	    if (mCamera != null) {
	        mCamera.stopPreview();
	        mCamera.release();
	        mCamera = null;
	    }
	}
}