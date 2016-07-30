package com.lsw.defendtheeggs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;

public class DTEHedgehog{
	public static final int HED_RUN=1;
	public static final int HED_ANGER=2;
	public int hedState=HED_RUN;
	private int lastState=HED_RUN;
	
	private Bitmap bmpHed[];
	private Bitmap bmpAngerHed[];
	private Bitmap bmpNumber[];
	private int frameIndex;
	
	public int frameH,frameW;
	public int hedX,hedY;
	public int speed;
	public boolean isDead;
	public boolean toRight;
	
	private int angerDelay=3,angerCount=0,angerTime=20;
	
	public DTEHedgehog(Bitmap bmpHed[],Bitmap bmpAngerHed[],Bitmap bmpN[],int x,int y){
		this.bmpHed=bmpHed;
		this.bmpAngerHed=bmpAngerHed;
		this.bmpNumber=bmpN;
		hedX=x;
		hedY=y;
		frameIndex=0;
		isDead=false;
		if(hedX<0){
			toRight=true;
			speed=6;
		}else{
			toRight=false;
			speed=-6;
		}
		frameH=this.bmpHed[0].getHeight();
		frameW=this.bmpHed[0].getWidth();
	}
	
	public void draw(Canvas canvas,Paint paint){
		switch(hedState){
		case HED_RUN:
			if(toRight){
				canvas.save();
				canvas.scale(-1,1,hedX+frameW/2,hedY+frameH/2);
				canvas.drawBitmap(bmpHed[frameIndex], hedX, hedY, paint);
				canvas.restore();
			}else canvas.drawBitmap(bmpHed[frameIndex],hedX,hedY,paint);
			break;
		case HED_ANGER:
			if(toRight){
				canvas.save();
				canvas.scale(-1,1,hedX+frameW/2,hedY+frameH/2);
				canvas.drawBitmap(bmpAngerHed[frameIndex], hedX, hedY, paint);
				canvas.restore();
			}else canvas.drawBitmap(bmpAngerHed[frameIndex],hedX,hedY,paint);
			canvas.drawBitmap(bmpNumber[11],hedX+bmpAngerHed[frameIndex].getWidth()/2-20,hedY-40,paint);
			canvas.drawBitmap(bmpNumber[5],hedX+bmpAngerHed[frameIndex].getWidth()/2+1,hedY-40,paint);
			break;
		}
	}
	
	public void logic(){
		switch(hedState){
		case HED_RUN:
			if(lastState!=hedState){
				frameH=this.bmpHed[0].getHeight();
				frameW=this.bmpHed[0].getWidth();
				angerCount=0;
				frameIndex=0;
				lastState=hedState;
			}else{
				frameIndex++;
				if(frameIndex>=bmpHed.length) frameIndex=0;
			}
			hedX+=speed;
			break;
		case HED_ANGER:
			if(lastState!=hedState){
				frameH=this.bmpAngerHed[0].getHeight();
				frameW=this.bmpAngerHed[0].getWidth();
				frameIndex=0;
				lastState=hedState;
			}else{
				angerCount++;
				if(angerCount<angerTime){
					if(angerCount%angerDelay==0){
						frameIndex++;
						if(frameIndex>=bmpAngerHed.length) frameIndex=0;
					}
				}
				else hedState=HED_RUN;
			}
			break;
		}
		if(hedX+frameW<-10 || hedX>DTEView.screenW+10) isDead=true;
	}
	
}