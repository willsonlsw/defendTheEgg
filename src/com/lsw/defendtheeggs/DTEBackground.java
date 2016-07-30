package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class DTEBackground{
	
	private Bitmap bmpBackGround;
	private Bitmap bmpNestEgg[][];
	private int frameIndex=0;
	private int nestH,nestW;
	private int egg=3;
	private int countBackground;

	public DTEBackground(Bitmap bmpBg,Bitmap bmpNE[][]){
		this.bmpBackGround=Bitmap.createScaledBitmap(bmpBg,DTEView.screenW,DTEView.screenH,true);
		this.bmpNestEgg=bmpNE;	
		nestH=bmpNestEgg[0][0].getHeight();
		nestW=bmpNestEgg[0][0].getWidth();
		countBackground=0;
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpBackGround,0,0,paint);
		canvas.drawBitmap(bmpNestEgg[egg][frameIndex],(DTEView.screenW-nestW)/2,(DTEView.screenH-nestH)/2,paint);
	}
	
	public void setEgg(int n){
		this.egg=n;
	}
	
	public void eggStolenOne(){
		this.egg--;
		if(this.egg<0) this.egg=0;
	}
	
	public void logic(){
		countBackground++;
		if(countBackground%20==0){
			frameIndex++;
			if(frameIndex>=2) frameIndex=0;
		}
		
	}
}