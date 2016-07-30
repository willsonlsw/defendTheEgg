package com.lsw.defendtheeggs;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BgMusicSer extends Service{
	private MediaPlayer mp;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mp=new MediaPlayer();
		mp=MediaPlayer.create(BgMusicSer.this, R.raw.bgmusic);
		mp.setLooping(true);
		try{
			mp.stop();
			mp.prepare();
		}catch(IllegalStateException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mp.stop();
		mp.release();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		mp.start();
		super.onStart(intent, startId);
	}
	
}