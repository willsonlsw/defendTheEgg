package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class DTEButton{
	public static final int MENU=0;
	public static final int GAMEING=1;
	public static final int OPTIONS=2;
	public static final int RANKINGS=3;
	public static final int PAUSE=4;
	public static final int ABOUT=5;
	public static final int END=6;
	
	private int gameState;
	private boolean btnPress=false; 
	private boolean btnAction=false;
	
	private Bitmap bmpButton[];
	private Paint paint;
	private int buttonH,buttonW;
	private int btnY,btnX;
	
	public DTEButton(Bitmap bmpB[]){
		bmpButton=bmpB;
		buttonH=bmpButton[0].getHeight();
		buttonW=bmpButton[0].getWidth();
		paint=new Paint();
		paint.setARGB(0xa0, 0xff, 0x00, 0x00);
		btnY=DTEView.screenH;
		btnX=0;
	}
	
	public void btnState(int gS){
		gameState=gS;
	}
	
	public void draw(Canvas canvas,Paint paint){
		switch(gameState){
		case GAMEING:
			if(!btnPress){
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[0], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}else{
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[1], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}
			break;
		case PAUSE:
			if(!btnPress){
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[2], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}else{
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[3], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}
			break;
		case ABOUT:
		case END:
		case OPTIONS:
		case RANKINGS:
			if(!btnPress){
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[4], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}else{
				canvas.save();
				canvas.scale((float)1.0/4,(float)1.0/4,(float)buttonW/4/2,(float)DTEView.screenH-buttonH/4/2);
				canvas.drawBitmap(bmpButton[5], 0, DTEView.screenH-buttonH,this.paint);
				canvas.restore();	
			}
			break;
		}
	}
	
	public void logic(){
		if(btnAction){
			switch(gameState){
			case GAMEING:
				DTEView.gameState=DTEView.GAME_PAUSE;
				break;
			case PAUSE:
				DTEView.gameState=DTEView.GAMEING;
				break;
			case ABOUT:
			case OPTIONS:
			case RANKINGS:
				DTEView.gameState=DTEView.GAME_MENU;
				break;
			case END:
				DTEView.ins.initGame();
				DTEView.gameState=DTEView.GAME_MENU;
				break;
			}
			btnAction=false;
		}
	}
	
	public void onTouchEvent(MotionEvent event){
		int pointX=(int)event.getX();
		int pointY=(int)event.getY();
		if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE){
			if(btnX<pointX && pointX<btnX+buttonW/4+10 && btnY-buttonH/4<pointY && pointY<btnY){
				btnPress=true;
			}else btnPress=false;
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			if(btnX<pointX && pointX<btnX+buttonW/4+10 && btnY-buttonH/4<pointY && pointY<btnY){
				btnPress=false;
				btnAction=true;
			}			
		}
	}
}