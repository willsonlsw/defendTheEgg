package com.lsw.defendtheeggs;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class DTEEgg{
	public static final int IN_NEST=1;
	public static final int ON_GROUND=2;
	public static final int STOLEN=3; 
	
	public int eggState=IN_NEST;
	public boolean isDead;
	public int eggIndex;
	
	private Bitmap bmpEgg[];
	private int frameIndex;
	private int delay=0;
	
	public int eggX,eggY;
	public int frameH,frameW;
	public DTEMouse dteMouse;
	
	public DTEEgg(Bitmap bmpE[],int X,int Y,int ei){
		eggX=X;eggY=Y;
		bmpEgg=bmpE;
		eggIndex=ei;
		frameH=bmpEgg[0].getHeight();
		frameW=bmpEgg[0].getWidth();
		isDead=false;
		dteMouse=null;
	}
	
	public void setEggXY(int X,int Y){
		eggX=X;eggY=Y;
	}
	
	public void draw(Canvas canvas,Paint paint){
		switch(eggState){
		case IN_NEST:
			break;
		case ON_GROUND:
			canvas.drawBitmap(bmpEgg[frameIndex], eggX, eggY, paint);
			break;
		case STOLEN:
			break;
		}
	}
	
	public void logic(){
		switch(eggState){
		case IN_NEST:
			break;
		case ON_GROUND:
			if(delay%10==0){
				frameIndex++;
				if(frameIndex>=2) frameIndex=0;
			}
			delay++;
			break;
		case STOLEN:
			delay=0;
			break;
		}
		if(dteMouse!=null && dteMouse.isDead) dteMouse=null;
		if(dteMouse==null){
			if(eggX+bmpEgg[0].getWidth()<-10 || eggX>DTEView.screenW+10 || eggY>DTEView.screenH+10) isDead=true;
		}		
	}
}