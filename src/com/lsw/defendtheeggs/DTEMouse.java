package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;
import java.lang.Math;

public class DTEMouse{
	public static final int MOUSE_RUN=1;
	public static final int MOUSE_STAND=2;
	public static final int MOUSE_DIZZY=3;
	
	public int mouseState=MOUSE_RUN;
	private int lastState=MOUSE_RUN;
	public boolean isDead;
	
	private Bitmap bmpMouseRun[];
	private Bitmap bmpMouseStand[];
	private Bitmap bmpMouseDizzy;
	private Bitmap bmpNumber[];
	private int frameIndex;
	
	public int frameH,frameW;
	
	public int mouseX,mouseY;
	private int speedX,speedY;
	private Random random;
	public boolean toRight=false;
	private int delay=0;
	private int hitScore;
	private int sc[]=new int[12];
	
	public DTEEgg dteAimEgg;
	
	public DTEMouse(Bitmap bmpMR[],Bitmap bmpMS[],Bitmap bmpMD,Bitmap bmpN[],int X,int Y){
		bmpMouseRun=bmpMR;
		bmpMouseStand=bmpMS;
		bmpMouseDizzy=bmpMD;
		bmpNumber=bmpN;
		mouseX=X;mouseY=Y;
		frameH=bmpMouseRun[0].getHeight();
		frameW=bmpMouseRun[0].getWidth();
		frameIndex=0;
		isDead=false;
		random=new Random();
		if(X<0) toRight=true;
		else toRight=false;
		getAimEgg();
		getOriSpeed(X,Y);
	}
	
	void getOriSpeed(int X,int Y){
		speedX=random.nextInt(DTEView.mouseHighSpeed-DTEView.mouseLowSpeed)+DTEView.mouseLowSpeed;
		if(!toRight) speedX=-speedX;
		if(dteAimEgg!=null && dteAimEgg.isDead==false){
			speedY=(int)Math.abs((float)(dteAimEgg.eggY-mouseY)/(float)(dteAimEgg.eggX-mouseX)*(float)speedX);
			if(dteAimEgg.eggY<mouseY) speedY=-speedY;
			if(Math.abs(speedY)>Math.abs(speedX)*3/4) speedY=0;
		}	
	}
	
	int distance(int x1,int y1,int x2,int y2){
		return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
	}
	
	void getAimEgg(){
		int dis=0x7fffffff;
		dteAimEgg=null;
		for(int i=0;i<DTEView.ins.vcEgg.size();i++)
			if(!DTEView.ins.vcEgg.elementAt(i).isDead){
				DTEEgg dteEgg=DTEView.ins.vcEgg.elementAt(i);
				if(dteEgg.eggState!=DTEEgg.STOLEN){
					if(distance(dteEgg.eggX,dteEgg.eggY,this.mouseX,this.mouseY)<dis){
						dis=distance(dteEgg.eggX,dteEgg.eggY,this.mouseX,this.mouseY);
						dteAimEgg=dteEgg;
					}
				}
			}
	}
	
	public void setHitScore(int s){
		hitScore=s;
	}
	
	void draw(Canvas canvas,Paint paint){
		switch(mouseState){
		case MOUSE_RUN:
			if(toRight){
				canvas.save();
				canvas.scale(-1,1,mouseX+bmpMouseRun[frameIndex].getWidth()/2,mouseY+bmpMouseRun[frameIndex].getHeight()/2);
				canvas.drawBitmap(bmpMouseRun[frameIndex], mouseX, mouseY, paint);
				canvas.restore();
			}else canvas.drawBitmap(bmpMouseRun[frameIndex], mouseX, mouseY, paint);
			break;
		case MOUSE_STAND:
			if(toRight){
				canvas.save();
				canvas.scale(-1,1,mouseX+bmpMouseStand[frameIndex].getWidth()/2,mouseY+bmpMouseStand[frameIndex].getHeight()/2);
				canvas.drawBitmap(bmpMouseStand[frameIndex], mouseX, mouseY, paint);
				canvas.restore();
			}else canvas.drawBitmap(bmpMouseStand[frameIndex], mouseX, mouseY, paint);
			break;
		case MOUSE_DIZZY:
			if(toRight){
				canvas.save();
				canvas.scale(-1,1,mouseX+bmpMouseDizzy.getWidth()/2,mouseY+bmpMouseDizzy.getHeight()/2);
				canvas.drawBitmap(bmpMouseDizzy, mouseX, mouseY, paint);
				canvas.restore();
			}else canvas.drawBitmap(bmpMouseDizzy, mouseX, mouseY, paint);
			drawScore(canvas,paint,mouseX+bmpMouseDizzy.getWidth()/2,mouseY-40);
		}
	}
	
	public void drawScore(Canvas canvas, Paint paint,int x,int y){
		int i=0,l=x;
		if(hitScore>0){
			int tmp=hitScore;
			while(tmp>0){
				sc[i++]=(int)tmp%10;
				tmp/=10;
			}
			l=x-i*10-20;
			canvas.drawBitmap(bmpNumber[10],l,y,paint);
			l+=20;
			while(i>0){
				i--;
				canvas.drawBitmap(bmpNumber[sc[i]],l,y,paint);
				l+=20;
			}
		}
		else canvas.drawBitmap(bmpNumber[0],l,y,paint);
	}
	
	void logic(){
		switch(mouseState){
		case MOUSE_RUN:
			if(dteAimEgg!=null){
				if(dteAimEgg.eggState==DTEEgg.STOLEN) getAimEgg();
				if(dteAimEgg!=null) getOriSpeed(mouseX,mouseY);
			}else{
				getAimEgg();
				if(dteAimEgg!=null) getOriSpeed(mouseX,mouseY);
			}
			
			if(lastState!=MOUSE_RUN){
				frameIndex=0;
				lastState=MOUSE_RUN;
			}else{
				frameIndex++;
				if(frameIndex>=12)frameIndex=0;
			}
			mouseX+=speedX;
			mouseY+=speedY;
			break;
		case MOUSE_STAND:
			if(lastState!=MOUSE_STAND){
				frameIndex=0;
				frameH=bmpMouseStand[0].getHeight();
				frameW=bmpMouseStand[0].getWidth();
				lastState=MOUSE_STAND;
				if(mouseY<DTEView.screenH/3){
					if(random.nextInt(3)==1 && speedY<0) speedY=-speedY;
				}else if(mouseY>DTEView.screenH*4/5){
					if(random.nextInt(3)==1 && speedY>0) speedY=-speedY;
				}
			}else{
				frameIndex++;
				if(frameIndex>=14)frameIndex=0;
			}
			mouseX+=speedX;
			mouseY+=speedY;
			dteAimEgg.eggX=mouseX;
			dteAimEgg.eggY=mouseY;
			break;
		case MOUSE_DIZZY:
			delay++;
			if(delay>=12) isDead=true;
			break;
		}
		if(mouseY<DTEView.screenH/7) speedY=0;
		if(mouseX+frameW<-10 || mouseX>DTEView.screenW+10 || mouseY>DTEView.screenH+10) isDead=true;
	}	
	
	public boolean collsionWith(DTEEgg dteEgg){
		if(dteAimEgg==null) return false;
		if(dteEgg.eggIndex!=dteAimEgg.eggIndex) return false;
		if(dteEgg.eggState==DTEEgg.STOLEN) return false;
		if(mouseState!=MOUSE_RUN) return false;
		
		if(mouseX>=dteEgg.eggX+dteEgg.frameW) return false;
		else if(mouseX+frameW<=dteEgg.eggX) return false;
		else if(mouseY>=dteEgg.eggY+dteEgg.frameH) return false;
		else if(mouseY+frameH<=dteEgg.eggY) return false;
		
		mouseState=MOUSE_STAND;
		if(dteAimEgg.eggState==DTEEgg.IN_NEST){
			DTEView.ins.dteBackground.eggStolenOne();
		}
		dteAimEgg.eggState=DTEEgg.STOLEN;
		dteAimEgg.dteMouse=this;
		return true;
	}
}