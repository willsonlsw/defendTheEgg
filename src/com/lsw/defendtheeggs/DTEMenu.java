package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class DTEMenu{
	
	public static final int START_GAME_BTN=1;
	public static final int OPTIONS_BTN=2;
	public static final int RANKINGS_BTN=3;
	public static final int ABOUT_BTN=4;
	public static final int EXIT_BTN=5;
	
	private Bitmap bmpMenu;
	private Bitmap bmpMenuPress;
	private int pressKey;
	private int menuH,menuW,menuX,menuY;
	
	public DTEMenu(Bitmap bmpM,Bitmap bmpMP){
		this.bmpMenu=bmpM;
		this.bmpMenuPress=bmpMP;
		menuH=bmpMenu.getHeight()/5;
		menuW=bmpMenu.getWidth();
		menuX=DTEView.screenW-menuW;
		menuY=(DTEView.screenH-menuH*5)/2;
		pressKey=0;
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpMenu,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
		if(pressKey>0){
			switch(pressKey){
			case START_GAME_BTN:
				canvas.save();
				canvas.clipRect(DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, DTEView.screenW, (DTEView.screenH-menuH*5)/2+menuH);
				canvas.drawBitmap(bmpMenuPress,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
				canvas.restore();
				break;
			case OPTIONS_BTN:
				canvas.save();
				canvas.clipRect(DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2+menuH, DTEView.screenW, (DTEView.screenH-menuH*5)/2+menuH*2);
				canvas.drawBitmap(bmpMenuPress,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
				canvas.restore();
				break;
			case RANKINGS_BTN:
				canvas.save();
				canvas.clipRect(DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2+menuH*2, DTEView.screenW, (DTEView.screenH-menuH*5)/2+menuH*3);
				canvas.drawBitmap(bmpMenuPress,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
				canvas.restore();
				break;
			case ABOUT_BTN:
				canvas.save();
				canvas.clipRect(DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2+menuH*3, DTEView.screenW, (DTEView.screenH-menuH*5)/2+menuH*4);
				canvas.drawBitmap(bmpMenuPress,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
				canvas.restore();
				break;
			case EXIT_BTN:
				canvas.save();
				canvas.clipRect(DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2+menuH*4, DTEView.screenW, (DTEView.screenH-menuH*5)/2+menuH*5);
				canvas.drawBitmap(bmpMenuPress,DTEView.screenW-menuW,(DTEView.screenH-menuH*5)/2, paint);
				canvas.restore();
				break;
			}
		}
	}
	
	public void onTouchEvent(MotionEvent event){
		int pointX=(int)event.getX();
		int pointY=(int)event.getY();
		if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){
			if(pointX>menuX && pointY>menuY && pointY<menuY+menuH*5){
				if(pointY<menuY+menuH) pressKey=START_GAME_BTN;
				else if(pointY>menuY+menuH && pointY<menuY+menuH*2) pressKey=OPTIONS_BTN;
				else if(pointY>menuY+menuH*2 && pointY<menuY+menuH*3) pressKey=RANKINGS_BTN;
				else if(pointY>menuY+menuH*3 && pointY<menuY+menuH*4) pressKey=ABOUT_BTN;
				else if(pointY>menuY+menuH*4 && pointY<menuY+menuH*5) pressKey=EXIT_BTN;
			}else pressKey=0;
		}else if(event.getAction()==MotionEvent.ACTION_UP){
				pressKey=0;
				if(pointY>menuY && pointY<menuY+menuH){
					DTEView.gameState=DTEView.GAMEING;
					DTEView.ins.dteOptions.setGameDif();
				}
				else if(pointY>menuY+menuH && pointY<menuY+menuH*2){
					DTEView.gameState=DTEView.GAME_OPTIONS;
					DTEView.ins.dteOptions.enterOption();
				}
				else if(pointY>menuY+menuH*2 && pointY<menuY+menuH*3){
					DTEView.gameState=DTEView.GAME_RANKINGS;
					DTEView.ins.dteRankings.enterRankings();
				}
				else if(pointY>menuY+menuH*3 && pointY<menuY+menuH*4){
					DTEView.gameState=DTEView.GAME_ABOUT;
					DTEView.ins.dteAbout.enterAbout();
				}
				else if(pointY>menuY+menuH*4 && pointY<menuY+menuH*5) DTEView.gameState=DTEView.GAME_EXIT;
		}
	}
	
}