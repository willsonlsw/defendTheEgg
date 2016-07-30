package com.lsw.defendtheeggs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class DefendTheEggs extends Activity {
	public static DefendTheEggs instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new DTEView(this));
    }

	public void startBgMusicSer(){
		Intent intent=new Intent(DefendTheEggs.this,BgMusicSer.class);
		startService(intent);
    }
    
    public void stopBgMusicSer(){
		Intent intent=new Intent(DefendTheEggs.this,BgMusicSer.class);
		stopService(intent);
    }
}
