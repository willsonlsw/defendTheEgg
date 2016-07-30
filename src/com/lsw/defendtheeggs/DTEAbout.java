package com.lsw.defendtheeggs;

import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DTEAbout{
	private Bitmap bmpBg;
	
	private boolean entAbout;
	private Paint paintBg;
	
	private int dis;
	
	public DTEAbout(Bitmap bmpB){
		bmpBg=bmpB;
		paintBg=new Paint();
		paintBg.setARGB(0xb0, 0xff, 0x00, 0x00);
	}
	
	public void enterAbout(){
		entAbout=true;
		dis=-bmpBg.getHeight();
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpBg,(DTEView.screenW-bmpBg.getWidth())/2,dis, paintBg);
	}
	
	public void logic(){
		if(entAbout){
			dis+=25;
		}
		if(dis+bmpBg.getHeight()>DTEView.screenH*9/10) entAbout=false;
	}
	
}