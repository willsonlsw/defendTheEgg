package com.lsw.defendtheeggs;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;

public class DTEGameEnd{
	private Bitmap bmpEnd;
	private Bitmap bmpBg;
	private float f;
	private boolean fin;
	Paint paintBg;
	
	public DTEGameEnd(Bitmap bmpE,Bitmap bmpB){
		bmpEnd=bmpE;
		bmpBg=bmpB;
		f=(float)0.08;
		fin=false;
		paintBg=new Paint();
		paintBg.setARGB(0xb0, 0xff, 0x00, 0x00);
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.drawBitmap(bmpBg, (DTEView.screenW-bmpBg.getWidth())/2, (DTEView.screenH-bmpBg.getHeight())/2, paintBg);
		if(!fin){
			canvas.save();
			canvas.scale(f,f,(float)DTEView.screenW/2,(float)DTEView.screenH/2);
			canvas.drawBitmap(bmpEnd, (DTEView.screenW-bmpEnd.getWidth())/2, (DTEView.screenH-bmpEnd.getHeight())/2,paint);
			canvas.restore();
		}else {
			canvas.drawBitmap(bmpEnd,(DTEView.screenW-bmpEnd.getWidth())/2, (DTEView.screenH-bmpEnd.getHeight())/2,paint);
		}
			
	}
	
	public void logic(){
		f+=0.08;
		if(f>=1.0) fin=true;
		if(fin){
			
		}
	}
}