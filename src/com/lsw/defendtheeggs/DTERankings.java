package com.lsw.defendtheeggs;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DTERankings{
	private Bitmap bmpBg;
	private Bitmap bmpScoreBoard;
	private Paint paintText;
	private Paint paintBg;
	
	private int dis;
	private boolean entRanking;
	private int topy;
	private DTERankingsItem dteR;
	
	public DTERankings(Bitmap bmpB,Bitmap bmpSB){
		bmpBg=bmpB;
		bmpScoreBoard=bmpSB;
		dis=-bmpBg.getHeight();
		paintBg=new Paint();
		paintBg.setARGB(0xb0, 0xff, 0x00, 0x00);
		paintText=new Paint();
		paintText.setColor(Color.DKGRAY);
		paintText.setTextSize(24);
	}
	
	public void enterRankings(){
		entRanking=true;
		dis=-bmpBg.getHeight();
	}
	
	public void draw(Canvas canvas,Paint paint){
		topy=10;
		canvas.drawBitmap(bmpBg,(DTEView.screenW-bmpBg.getWidth())/2,dis, paintBg);
		if(!entRanking){
			canvas.drawBitmap(bmpScoreBoard,(DTEView.screenW-bmpScoreBoard.getWidth())/2,topy,paint);
			topy+=bmpScoreBoard.getHeight()+20;
			canvas.drawText("Score", DTEView.screenW/2-140, topy, paintText);
			canvas.drawText("Date",DTEView.screenW/2+80,topy,paintText);
			topy+=30;
			for(int i=0;i<DTEView.ins.vcRank.size();i++){
				dteR=DTEView.ins.vcRank.elementAt(i);
				canvas.drawText(" "+dteR.score,DTEView.screenW/2-120,topy,paintText);
				canvas.drawText(" "+dteR.month+'/'+dteR.day+'/'+dteR.year+' '+dteR.hour+':'+dteR.minute,DTEView.screenW/2,topy,paintText);
				topy+=30;				
			}
		}
	}
	
	public void logic(){
		if(entRanking){
			dis+=25;
		}
		if(dis+bmpBg.getHeight()>DTEView.screenH*9/10) entRanking=false;
	}
}