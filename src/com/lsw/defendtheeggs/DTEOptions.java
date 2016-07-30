package com.lsw.defendtheeggs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.view.MotionEvent;

public class DTEOptions{
	public static final int GAME_EASY=1;
	public static final int GAME_MIDIUM=2;
	public static final int GAME_HARD=3;
	
	public int gameDif=GAME_EASY;
	public static boolean gameSounds=false;
	public static boolean gameMusic=false;
	
	private Bitmap bmpBg;
	private Bitmap bmpSound;
	private Bitmap bmpDif[]; 
	
	private boolean entOption;
	private Paint paintBg;
	
	private int dis;
	private int toppx;
	private int frameW;
	private int x;
	private int easyY,midiumY,hardY,soundY,difH,soundH,musicY;
	
	
	public DTEOptions(Bitmap bmpB,Bitmap bmpS,Bitmap bmpD[]){
		bmpBg=bmpB;
		bmpSound=bmpS;
		bmpDif=bmpD;
		frameW=bmpSound.getWidth();
		paintBg=new Paint();
		paintBg.setARGB(0xb0, 0xff, 0x00, 0x00);
		toppx=10;
		difH=bmpDif[0].getHeight()/3;
		soundH=bmpSound.getHeight()/5;
		x=(DTEView.screenW-frameW)/2;
		easyY=toppx+soundH;;
		midiumY=easyY+difH;
		hardY=midiumY+difH;
		soundY=hardY+difH;
		musicY=soundY+difH;
	}
	
	public void enterOption(){
		entOption=true;
		dis=-bmpBg.getHeight();
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpBg,(DTEView.screenW-bmpBg.getWidth())/2,dis, paintBg);
		if(!entOption){
			canvas.save();
			canvas.clipRect(x,toppx,x+frameW,toppx+soundH);
			canvas.drawBitmap(bmpSound,x,toppx,paint);
			canvas.restore();
			//toppx+=bmpSound.getHeight()/3;
			canvas.drawBitmap(bmpDif[0],x,easyY,paint);
			
			switch(gameDif){
			case GAME_EASY:
				//easyY=toppx;
				canvas.save();
				canvas.clipRect(x,easyY,x+frameW,midiumY);
				canvas.drawBitmap(bmpDif[1],x,easyY,paint);
				canvas.restore();
				break;
			case GAME_MIDIUM:
				//midiumY=toppx+bmpDif[1].getHeight()/3;
				canvas.save();
				canvas.clipRect(x,midiumY,x+frameW,hardY);
				canvas.drawBitmap(bmpDif[1],x,easyY,paint);
				canvas.restore();
				break;
			case GAME_HARD:
				//hardY=toppx+bmpDif[1].getHeight()*2/3;
				canvas.save();
				canvas.clipRect(x,hardY,x+frameW,hardY+difH);
				canvas.drawBitmap(bmpDif[1],x,easyY,paint);
				canvas.restore();
				break;
			}
			
			//toppx+=bmpDif[0].getHeight();
			//soundY=toppx;
			if(!gameSounds){
				canvas.save();
				canvas.clipRect(x,soundY,x+frameW,soundY+bmpSound.getHeight()/5);
				canvas.drawBitmap(bmpSound, x, soundY-soundH, paint);
				canvas.restore();
			}else{
				canvas.save();
				canvas.clipRect(x,soundY,x+frameW,soundY+bmpSound.getHeight()/5);
				canvas.drawBitmap(bmpSound, x, soundY-soundH*2, paint);
				canvas.restore();
			}
			
			if(!gameMusic){
				canvas.save();
				canvas.clipRect(x,musicY,x+frameW,musicY+bmpSound.getHeight()/5);
				canvas.drawBitmap(bmpSound, x, musicY-soundH*3, paint);
				canvas.restore();
			}else{
				canvas.save();
				canvas.clipRect(x,musicY,x+frameW,musicY+bmpSound.getHeight()/5);
				canvas.drawBitmap(bmpSound, x, musicY-soundH*4, paint);
				canvas.restore();
			}
		}		
	}
	
	public void logic(){
		if(entOption){
			dis+=25;
		}
		if(dis+bmpBg.getHeight()>DTEView.screenH*9/10) entOption=false;
		setGameDif();
		if(gameMusic){
			DefendTheEggs.instance.startBgMusicSer();
		}else{
			DefendTheEggs.instance.stopBgMusicSer();
		}
	}
	
	public void setGameDif(){
		DTEView.ins.dtePlayer.gameDif=gameDif;
	}
	
	public void onTouchEvent(MotionEvent event){
		int pointX=(int)event.getX();
		int pointY=(int)event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP){
			if(x<pointX && pointX<x+frameW){
				if(easyY<pointY && pointY<easyY+difH) gameDif=GAME_EASY;
				else if(midiumY<pointY && pointY<midiumY+difH) gameDif=GAME_MIDIUM;
				else if(hardY<pointY && pointY<hardY+difH) gameDif=GAME_HARD;
				if(soundY<pointY && pointY<soundY+soundH){
					if(gameSounds) gameSounds=false;
					else gameSounds=true;
				}
				if(musicY<pointY && pointY<musicY+soundH){
					if(gameMusic) gameMusic=false;
					else gameMusic=true;
				}
			}
		}
	}
	
}