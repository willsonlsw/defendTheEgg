package com.lsw.defendtheeggs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.Time;
import android.content.SharedPreferences;

public class DTEPlayer{
	public static final int GAME_EASY=1;
	public static final int GAME_MIDIUM=2;
	public static final int GAME_HARD=3;
	
	public int gameDif=GAME_EASY;
	
	public int maxMouseNum;
	public int lowestSpeed,highestSpeed;
	
	private static int eggNum;
	private static boolean eggExist[]=new boolean[3];
	private int score,scoreAim,hitScore;
	private int sc[]=new int[12];
	
	private Bitmap bmpNumber[];
	private Bitmap bmpEggHP;
	private Paint paintText;
	
	private SharedPreferences sp;
	private boolean gameing;
	
	public Time time;
	public int year,month,day,minute,hour;
	
	private int hitNumber,lastHitNumber;
	private int hitTime;
	private int hitMouseTotal;
	
	public DTEPlayer(Bitmap bmpS[],Bitmap bmpHP){
		this.bmpEggHP=bmpHP;
		this.bmpNumber=bmpS;
		eggNum=3;
		score=0;
		scoreAim=0;
		for(int i=0;i<3;i++)
			eggExist[i]=true;
		paintText=new Paint();
		paintText.setColor(Color.DKGRAY);
		paintText.setTextSize(24);
		gameing=true;
		lastHitNumber=-2;
		hitScore=2;
		hitTime=0;
		hitMouseTotal=0;
	}
	
	public int getHitScore(){
		return hitScore;
		//return hitNumber;
	}
	
	public void scoreIncrement(){
		if(lastHitNumber+1==hitNumber && hitTime<=20){
			hitScore++;
		}else{
			hitScore=2;
		}
		lastHitNumber=hitNumber;
		hitTime=0;
		scoreAim+=hitScore;
		hitMouseTotal++;
	}
	
	public void scoreDecrement(){
		scoreAim-=5;
		if(scoreAim<0) scoreAim=0;
	}
	
	public void setHitNumber(int h){
		hitNumber=h;
	}
	
	public void eggMiss(int i){
		eggNum-=1;
	}
	
	private void setDifficulty(){
		switch(gameDif){
		case GAME_EASY:
			maxMouseNum=3;
			lowestSpeed=6;
			highestSpeed=12;
			break;
		case GAME_MIDIUM:
			maxMouseNum=4;
			lowestSpeed=9;
			highestSpeed=15;
			break;
		case GAME_HARD:
			maxMouseNum=5;
			lowestSpeed=12;
			highestSpeed=18;
			break;
		}
		if(hitMouseTotal<=200){
			maxMouseNum+=hitMouseTotal/100;
			lowestSpeed+=hitMouseTotal/50;
			highestSpeed+=hitMouseTotal/40;
		}else if(hitMouseTotal>200 && hitMouseTotal<500){
			maxMouseNum+=2+(hitMouseTotal*2-400)/300;
			lowestSpeed+=5+(hitMouseTotal*2-400)/120;
			highestSpeed+=6+(hitMouseTotal*2-420)/110;
		}else if(hitMouseTotal>=500){
			maxMouseNum+=4+(hitMouseTotal*2-1000)/1000;
			lowestSpeed+=10+(hitMouseTotal*2-1000)/500;
			highestSpeed+=11+(hitMouseTotal*2-1000)/500;
		}
	}
	
	public void rankScore(){
		gameing=false;
        time = new Time();       
        time.setToNow();     
        DTERankingsItem dteRank;
        int year = time.year;      
        int month = time.month+1;      
        int day = time.monthDay;      
        int minute = time.minute;      
        int hour = time.hour;
        int tmp;
		DTEView.ins.vcRank.addElement(new DTERankingsItem((int)scoreAim,year,month,day,hour,minute));
		
		for(int i=0;i<DTEView.ins.vcRank.size();i++){
			for(int j=i+1;j<DTEView.ins.vcRank.size();j++){
				if(DTEView.ins.vcRank.elementAt(j).score>DTEView.ins.vcRank.elementAt(i).score){
					dteRank=DTEView.ins.vcRank.elementAt(j);
					year=dteRank.year;
					month=dteRank.month;
					day=dteRank.day;
					hour=dteRank.hour;
					minute=dteRank.minute;
					tmp=dteRank.score;
					DTEView.ins.vcRank.elementAt(j).year = DTEView.ins.vcRank.elementAt(i).year;
					DTEView.ins.vcRank.elementAt(j).month = DTEView.ins.vcRank.elementAt(i).month;
					DTEView.ins.vcRank.elementAt(j).day = DTEView.ins.vcRank.elementAt(i).day;
					DTEView.ins.vcRank.elementAt(j).hour = DTEView.ins.vcRank.elementAt(i).hour;
					DTEView.ins.vcRank.elementAt(j).minute = DTEView.ins.vcRank.elementAt(i).minute;
					DTEView.ins.vcRank.elementAt(j).score = DTEView.ins.vcRank.elementAt(i).score;
					DTEView.ins.vcRank.elementAt(i).year=year;
					DTEView.ins.vcRank.elementAt(i).month=month;
					DTEView.ins.vcRank.elementAt(i).day=day;
					DTEView.ins.vcRank.elementAt(i).hour=hour;
					DTEView.ins.vcRank.elementAt(i).minute=minute;
					DTEView.ins.vcRank.elementAt(i).score=tmp;
				}
			}
		}
		
		sp=DTEView.ins.sp;
		tmp=DTEView.ins.vcRank.size();
		if(tmp>10)tmp=10;
		sp.edit().putInt("ItemNum",tmp).commit();
		for(int i=0;i<tmp;i++){
			dteRank=DTEView.ins.vcRank.elementAt(i);
			sp.edit().putInt("score"+i, dteRank.score).commit();
			sp.edit().putInt("year"+i, dteRank.year).commit();
			sp.edit().putInt("month"+i, dteRank.month).commit();
			sp.edit().putInt("day"+i, dteRank.day).commit();
			sp.edit().putInt("hour"+i, dteRank.hour).commit();
			sp.edit().putInt("minute"+i, dteRank.minute).commit();
		}
		DTEView.ins.loadRanking();
	}
	
	public void draw(Canvas canvas,Paint paint){
		int i=0,l=10;
		if(score>0){
			long tmp=score;
			while(tmp>0){
				sc[i++]=(int)tmp%10;
				tmp/=10;
			}
			while(i>0){
				i--;
				canvas.drawBitmap(bmpNumber[sc[i]],l,0,paint);
				l+=bmpNumber[sc[i]].getWidth();
			}
		}
		else canvas.drawBitmap(bmpNumber[0],l,0,paint);
		canvas.drawText("Record:"+DTEView.recordScore, 10, bmpNumber[0].getHeight()*3/2, paintText);
		
		l=DTEView.screenW;
		for(i=0;i<DTEView.ins.vcEgg.size();i++){
			canvas.drawBitmap(bmpEggHP,l-bmpEggHP.getWidth(),0,paint);
			l-=bmpEggHP.getWidth();
		}
	}
	
	public void logic(){
		hitTime++;
		if(scoreAim>score+30){
			score+=3;
		}else{
			if(scoreAim>score) score++;
			else if(scoreAim<score) score--;
		}
		if(DTEView.ins.vcEgg.size()==0){
			DTEView.gameState=DTEView.GAME_END;
		}
		setDifficulty();		
		if(gameing && DTEView.gameState==DTEView.GAME_END) rankScore();
	}
}


