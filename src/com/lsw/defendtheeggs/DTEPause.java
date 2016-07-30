package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class DTEPause{
	private static final int PAUSE_CONTINUE=1;
	private static final int PAUSE_BACKTOMENU=2;
	private int pressKey=0;
	
	private Bitmap bmpContinue[];
	private Bitmap bmpBackToMenu[];
	private int frameH,frameW;
	private int btnCX,btnCY,btnBX,btnBY;
	
	Paint paint;
	
	public DTEPause(Bitmap bmpC[],Bitmap bmpB[]){
		bmpContinue=bmpC;
		bmpBackToMenu=bmpB;		
		frameH=bmpContinue[0].getHeight();
		frameW=bmpContinue[0].getWidth();
		paint=new Paint();
		paint.setARGB(0xa0, 0xff, 0x00, 0x00);
		btnCX=DTEView.screenW/2-frameW-10;
		btnCY=(DTEView.screenH-frameH)/2;
		btnBX=DTEView.screenW/2+10;
		btnBY=(DTEView.screenH-frameH)/2;
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmpContinue[0],btnCX,btnCY,paint);
		canvas.drawBitmap(bmpBackToMenu[0],btnBX,btnBY,paint);
		if(pressKey==PAUSE_CONTINUE){
			canvas.drawBitmap(bmpContinue[1],btnCX,btnCY,paint);
		}else if(pressKey==PAUSE_BACKTOMENU){
			canvas.drawBitmap(bmpBackToMenu[1],btnBX,btnBY,paint);
		}
	}
	
	public void onTouchEvent(MotionEvent event){
		int pointX=(int)event.getX();
		int pointY=(int)event.getY();
		if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE){
			if(btnCX<pointX && pointX<btnCX+frameW && btnCY<pointY && pointY<btnCY+frameH){
				pressKey=1;
			}else if(btnBX<pointX && pointX<btnBX+frameW && btnBY<pointY && pointY<btnBY+frameH){
				pressKey=2;
			}else pressKey=0;
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			if(btnCX<pointX && pointX<btnCX+frameW && btnCY<pointY && pointY<btnCY+frameH){
				DTEView.gameState=DTEView.GAMEING;
			}else if(btnBX<pointX && pointX<btnBX+frameW && btnBY<pointY && pointY<btnBY+frameH){
				DTEView.ins.initGame();
				DTEView.gameState=DTEView.GAME_MENU;
			}
			pressKey=0;
		}
	}
}