package com.niel.code.widget;

import java.util.ArrayList;

import com.niel.code.example.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {
	
	private final String TAG = "VIDEOADAPTER";
	private final int MAXSIZE = 100;
	private Context mContext;
	
	private ArrayList<VideoListStructure> item;
	
	private VideoListHolder mHolder;
	
	public VideoListAdapter(Context context, ArrayList<VideoListStructure> sturcture) {
		mContext = context;
		item = (ArrayList<VideoListStructure>) sturcture.clone();
	}
	
	public void setAdapterItem(ArrayList<VideoListStructure> sturcture) {
		if(item != null) {
			if(item.size() != 0) {
				item.clear();
			}
			item = (ArrayList<VideoListStructure>) sturcture.clone();
			Log.d(TAG,"item size "+item.size());
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if(item == null) return 0;
		return item.size() >= MAXSIZE ? MAXSIZE : item.size();
	}

	@Override
	public Object getItem(int position) {
		if(item == null) return 0;
		return position >= MAXSIZE ? null : item.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if(convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.video_list_adapter, null);
			mHolder = new VideoListHolder();
			mHolder.mScreenshot = (ImageView) view.findViewById(R.id.videoScreenshot);
			mHolder.mVideoName = (TextView) view.findViewById(R.id.videoName);
			mHolder.mVideoFileName = (TextView) view.findViewById(R.id.fileName);
			mHolder.isDeleteCheck = (CheckBox) view.findViewById(R.id.checkBox);
			view.setTag(view.getId(), mHolder);
		} else {
			view = convertView;
			mHolder = (VideoListHolder) view.getTag(view.getId());
		}
		
		if( (position % 2) == 0) {
			view.setBackgroundColor(Color.parseColor("#CCCCCC"));
		} else {
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		if(item.size() != 0) {
			mHolder.mScreenshot.setImageBitmap(item.get(position).getScreenshot());
			mHolder.mVideoName.setText(item.get(position).getVideoName());
			mHolder.mVideoFileName.setText(item.get(position).getVideoFileName());
			mHolder.isDeleteCheck.setFocusable(false);
			mHolder.isDeleteCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					item.get(position).deleteFile(isChecked);
				}
			});
		}
		return view;
	}
	
}