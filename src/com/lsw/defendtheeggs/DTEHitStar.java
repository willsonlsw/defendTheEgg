package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DTEHitStar{
	private Bitmap bmpHit[];
	private int hitX,hitY;
	private int frameIndex;
	public boolean playEnd;
	public int delay;
	
	public DTEHitStar(Bitmap bmpHit[],int x,int y){
		this.bmpHit=bmpHit;
		hitX=x;
		hitY=y;
		frameIndex=0;
		playEnd=false;
		delay=0;
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpHit[frameIndex], hitX, hitY, paint);
	}
	
	public void logic(){
		delay++;
		if(delay>=6) frameIndex=1;
		if(delay>=12) playEnd=true;		
	}
}