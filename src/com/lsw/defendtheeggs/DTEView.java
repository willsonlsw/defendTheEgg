package com.lsw.defendtheeggs;

import java.util.Random;
import java.util.Vector;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.media.AudioManager;
import android.media.SoundPool;

public class DTEView extends SurfaceView implements Callback,Runnable{
	public static DTEView ins;
	
	public static int screenW,screenH;
	public static int mouseLowSpeed,mouseHighSpeed;
	public static int recordScore;
	public static int itemNum;

	public static final int GAME_MENU=0;
	public static final int GAMEING=1;
	public static final int GAME_OPTIONS=2;
	public static final int GAME_RANKINGS=3;
	public static final int GAME_PAUSE=4;
	public static final int GAME_ABOUT=5;
	public static final int GAME_END=6;
	public static final int GAME_EXIT=7;
	
	public static int gameState=GAME_MENU;	
	
	private int hitNumber;
	private Resources res=this.getResources();
	private Bitmap bmpBackGround;
	private Bitmap bmpHit[]=new Bitmap[2];
	private Bitmap bmpHedgehog[]=new Bitmap[8];
	private Bitmap bmpAngerHedgehog[]=new Bitmap[2];
	private Bitmap bmpMouseRun[]=new Bitmap[12];
	private Bitmap bmpMouseStand[]=new Bitmap[14];
	private Bitmap bmpMouseHit;
	private Bitmap bmpEgg[]=new Bitmap[2];
	private Bitmap bmpNestEgg[][]=new Bitmap[4][2];
	private Bitmap bmpNumber[]=new Bitmap[12];
	private Bitmap bmpMenu;
	private Bitmap bmpEggHP;
	private Bitmap bmpMenuPress;
	private Bitmap bmpContinue[]=new Bitmap[2];
	private Bitmap bmpBackToMenu[]=new Bitmap[2];
	private Bitmap bmpOptionBg;
	private Bitmap bmpOptionSound;
	private Bitmap bmpOptionDif[]=new Bitmap[2];
	private Bitmap bmpAbout;
	private Bitmap bmpGameEnd;
	private Bitmap bmpScoreBoard;
	private Bitmap bmpButton[]=new Bitmap[6];
	
	private int hitFrameH,hitFrameW;
	
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread dteThread;
	private Canvas canvas; 
	private boolean flag;
	private int revise=10;
	
	public DTEMenu dteMenu;
	public DTEBackground dteBackground;
	public DTEPlayer dtePlayer;
	public DTEMouse dteMouse;
	public DTEPause dtePause;
	public DTEOptions dteOptions;
	public DTEAbout dteAbout;
	public DTEGameEnd dteGameEnd;
	public DTERankings dteRankings;
	public DTEButton dteButton;
	
	public Vector<DTEMouse> vcMouse;
	public Vector<DTEEgg> vcEgg;
	public Vector<DTEHedgehog> vcHed;
	public Vector<DTEHitStar> vcHit;
	public Vector<DTERankingsItem> vcRank;
	
////////////////////////////////////////////////
//产鼠周期，待修改
	private int count=0;
	private int createMouseTime=10;
	private Random random;
////////////////////////////////////////////////
	private int createHedgeHog;
	
	public SharedPreferences sp;
	private SoundPool soundp;
	private int soundId_hit;
	private int soundId_nohit;
	
	public DTEView(Context context){
		super(context);
		sfh=this.getHolder();
		sfh.addCallback(this);
		paint=new Paint();
		paint.setColor(Color.WHITE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setKeepScreenOn(true);
		ins=this;
		sp=context.getSharedPreferences("Rankings", Context.MODE_PRIVATE);
		soundp=new SoundPool(4,AudioManager.STREAM_MUSIC,100);
		soundId_hit=soundp.load(context, R.raw.hit,1);
		soundId_nohit=soundp.load(context, R.raw.nohit,1);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenW=this.getWidth();
		screenH=this.getHeight();
		initGame();
		dteOptions=new DTEOptions(bmpOptionBg,bmpOptionSound,bmpOptionDif);
		flag=true;
		dteThread=new Thread(this);
		dteThread.start();
	}
	
	void loadRanking(){
		int score,year,month,day,hour,minute;
		itemNum=sp.getInt("ItemNum", -1);
		if(itemNum!=-1){
			for(int i=0;i<itemNum;i++){
				score=sp.getInt("score"+i, -1);
				if(i==0) recordScore=score;
				year=sp.getInt("year"+i,-1);
				month=sp.getInt("month"+i,-1);
				day=sp.getInt("day"+i,-1);
				hour=sp.getInt("hour"+i,-1);
				minute=sp.getInt("minute"+i,-1);
				vcRank.addElement(new DTERankingsItem(score,year,month,day,hour,minute));
			}			
		}
	}
	
	//初始化函数
	public void initGame(){
		int i,j;
		if(gameState==GAME_MENU){
			bmpBackGround=BitmapFactory.decodeResource(res, R.drawable.bg);
			bmpMouseHit=BitmapFactory.decodeResource(res, R.drawable.mouse_hit);
			bmpMenu=BitmapFactory.decodeResource(res, R.drawable.menu01);
			bmpMenuPress=BitmapFactory.decodeResource(res, R.drawable.menu02);
			bmpContinue[0]=BitmapFactory.decodeResource(res, R.drawable.continue1);
			bmpContinue[1]=BitmapFactory.decodeResource(res, R.drawable.continue2);
			bmpBackToMenu[0]=BitmapFactory.decodeResource(res, R.drawable.backtomenu1);
			bmpBackToMenu[1]=BitmapFactory.decodeResource(res, R.drawable.backtomenu2);
			bmpEggHP=BitmapFactory.decodeResource(res, R.drawable.egg_hp);
			bmpOptionBg=BitmapFactory.decodeResource(res, R.drawable.optionsbg);
			bmpOptionSound=BitmapFactory.decodeResource(res, R.drawable.sounds1);
			bmpOptionDif[0]=BitmapFactory.decodeResource(res, R.drawable.dif01);
			bmpOptionDif[1]=BitmapFactory.decodeResource(res, R.drawable.dif02);
			bmpAbout=BitmapFactory.decodeResource(res, R.drawable.about);
			bmpGameEnd=BitmapFactory.decodeResource(res, R.drawable.game_end);
			bmpScoreBoard=BitmapFactory.decodeResource(res, R.drawable.score_board);
			bmpButton[0]=BitmapFactory.decodeResource(res, R.drawable.pause1);
			bmpButton[1]=BitmapFactory.decodeResource(res, R.drawable.pause2);
			bmpButton[2]=BitmapFactory.decodeResource(res, R.drawable.continue1);
			bmpButton[3]=BitmapFactory.decodeResource(res, R.drawable.continue2);
			bmpButton[4]=BitmapFactory.decodeResource(res, R.drawable.backtomenu1);
			bmpButton[5]=BitmapFactory.decodeResource(res, R.drawable.backtomenu2);
			for(i=0;i<bmpHit.length;i++)
				bmpHit[i]=BitmapFactory.decodeResource(res, R.drawable.hit01+i);
			for(i=0;i<bmpHedgehog.length;i++)
				bmpHedgehog[i]=BitmapFactory.decodeResource(res, R.drawable.hedgehog01+i);
			for(i=0;i<bmpAngerHedgehog.length;i++)
				bmpAngerHedgehog[i]=BitmapFactory.decodeResource(res,R.drawable.anger_hedgehog01+i);
			for(i=0;i<bmpMouseRun.length;i++)
				bmpMouseRun[i]=BitmapFactory.decodeResource(res, R.drawable.mouse_run01+i);
			for(i=0;i<bmpMouseStand.length;i++)
				bmpMouseStand[i]=BitmapFactory.decodeResource(res,R.drawable.mouse_stand01+i);
			for(i=0;i<bmpEgg.length;i++)
				bmpEgg[i]=BitmapFactory.decodeResource(res,R.drawable.egg01+i);
			for(i=0;i<bmpNumber.length;i++)
				bmpNumber[i]=BitmapFactory.decodeResource(res, R.drawable.num_0+i);
			bmpNumber[10]=BitmapFactory.decodeResource(res, R.drawable.add);
			bmpNumber[11]=BitmapFactory.decodeResource(res, R.drawable.sub);
			for(j=0;j<4;j++)
				for(i=0;i<bmpNestEgg[j].length;i++)
					bmpNestEgg[j][i]=BitmapFactory.decodeResource(res, R.drawable.nest_egg01+j*2+i);
		}
		
		dteMenu=new DTEMenu(bmpMenu,bmpMenuPress);
		dteBackground=new DTEBackground(bmpBackGround,bmpNestEgg);
		dtePlayer=new DTEPlayer(bmpNumber,bmpEggHP);
		dtePause=new DTEPause(bmpContinue,bmpBackToMenu);
		dteAbout=new DTEAbout(bmpAbout);
		dteGameEnd=new DTEGameEnd(bmpGameEnd,bmpOptionBg);
		dteRankings=new DTERankings(bmpOptionBg,bmpScoreBoard);
		dteButton=new DTEButton(bmpButton);
		
		vcMouse=new Vector<DTEMouse>();
		vcHed=new Vector<DTEHedgehog>();
		vcEgg=new Vector<DTEEgg>();
		vcHit=new Vector<DTEHitStar>();
		vcRank=new Vector<DTERankingsItem>();
		
		vcEgg.addElement(new DTEEgg(bmpEgg,screenW/2-bmpNestEgg[0][0].getWidth()/2,screenH/2-bmpNestEgg[0][0].getHeight()/2,0));
		vcEgg.addElement(new DTEEgg(bmpEgg,screenW/2+bmpNestEgg[0][0].getWidth()/2-bmpEgg[0].getWidth(),screenH/2-bmpNestEgg[0][0].getHeight()/2,1));
		vcEgg.addElement(new DTEEgg(bmpEgg,screenW/2,screenH/2,2));
		
		random=new Random();
		hitFrameH=bmpHit[0].getHeight();
		hitFrameW=bmpHit[0].getWidth();
		
		hitNumber=0;
		
		this.loadRanking();
	}
	
	boolean hitMouse(MotionEvent event){
		if(gameState!=GAMEING) return false;
		boolean hit=false;
		boolean hitMouse=false;
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			int pointX=(int)event.getX();
			int pointY=(int)event.getY();
			DTEMouse dteMouse;
			for(int i=0;i<vcMouse.size();i++)
				if(!vcMouse.elementAt(i).isDead){
					if(vcMouse.elementAt(i).mouseState!=DTEMouse.MOUSE_DIZZY){
						dteMouse=vcMouse.elementAt(i);
						if(dteMouse.mouseX-revise<pointX && pointX<dteMouse.mouseX+dteMouse.frameW+revise && dteMouse.mouseY-revise<pointY && pointY<dteMouse.mouseY+dteMouse.frameH+revise){
							if(dteMouse.mouseState==DTEMouse.MOUSE_STAND){
								dteMouse.dteAimEgg.eggState=DTEEgg.ON_GROUND;
								dteMouse.dteAimEgg.dteMouse=null;
							}
							dteMouse.mouseState=DTEMouse.MOUSE_DIZZY;
							if(dteMouse.toRight) vcHit.addElement(new DTEHitStar(bmpHit,dteMouse.mouseX-hitFrameW/3,dteMouse.mouseY-hitFrameH/3));
							else  vcHit.addElement(new DTEHitStar(bmpHit,dteMouse.mouseX,dteMouse.mouseY-hitFrameH/3));	
							if(hitMouse) hitNumber++;
							dtePlayer.setHitNumber(hitNumber);
							dtePlayer.scoreIncrement();
							dteMouse.setHitScore(dtePlayer.getHitScore());
							if(dteOptions.gameSounds) soundp.play(soundId_hit,1f,1f,0,0,1);
							hit=true;
							hitMouse=true;
						}							
					}
				}
		}
		return hit;
	}
	
	boolean hitHed(MotionEvent event){
		if(gameState!=GAMEING) return false;
		boolean hit=false;
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			int pointX=(int)event.getX();
			int pointY=(int)event.getY();
			for(int i=0;i<vcHed.size();i++){
				DTEHedgehog dteHed=vcHed.elementAt(i);
				if(dteHed.hedX-revise<pointX && pointX<dteHed.hedX+dteHed.frameW+revise && dteHed.hedY-revise<pointY && pointY<dteHed.hedY+dteHed.frameH+revise){
					dteHed.hedState=DTEHedgehog.HED_ANGER;
					dtePlayer.scoreDecrement();
					hitNumber++;
					hit=true;
				}
			}
		}
		return hit;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(gameState){
		case GAME_MENU:
			dteMenu.onTouchEvent(event);
			dteButton.onTouchEvent(event);
			break;
		case GAMEING:
			dteButton.onTouchEvent(event);
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				hitNumber++;
				hitHed(event);
				hitMouse(event);				
			}
			break;
		case GAME_OPTIONS:
			dteOptions.onTouchEvent(event);
			dteButton.onTouchEvent(event);
			break;
		case GAME_RANKINGS:
			dteButton.onTouchEvent(event);
			break;
		case GAME_PAUSE:
			dtePause.onTouchEvent(event);
			dteButton.onTouchEvent(event);
			break;
		case GAME_ABOUT:
			dteButton.onTouchEvent(event);
			break;
		case GAME_END:
			dteButton.onTouchEvent(event);
			break;
		}		
		return true;
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(gameState==GAMEING){
				gameState=GAME_PAUSE;
			}else if(gameState==GAME_MENU){
				DefendTheEggs.instance.finish();
				System.exit(0);
			}else if(gameState==GAME_PAUSE){
				gameState=GAMEING;
			}else if(gameState==GAME_OPTIONS){
				gameState=GAME_MENU;
			}else if(gameState==GAME_ABOUT){
				gameState=GAME_MENU;
			}else if(gameState==GAME_RANKINGS){
				gameState=GAME_MENU;
			}else if(gameState==GAME_END){
				gameState=GAME_MENU;
				initGame();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	public void dteDraw(){
		try{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				canvas.drawColor(Color.WHITE);
				switch(gameState){
				case GAME_MENU:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas, paint);
					dteMenu.draw(canvas, paint);
					break;
				case GAMEING:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					for(int i=0;i<vcMouse.size();i++)
						vcMouse.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcEgg.size();i++)
						vcEgg.elementAt(i).draw(canvas,paint);
					for(int i=0;i<vcHed.size();i++)
						vcHed.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcHit.size();i++)
						vcHit.elementAt(i).draw(canvas, paint);
					dteButton.draw(canvas, paint);
					break;
				case GAME_OPTIONS:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					dteOptions.draw(canvas, paint);
					dteButton.draw(canvas, paint);
					break;
				case GAME_RANKINGS:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					dteRankings.draw(canvas, paint);
					dteButton.draw(canvas, paint);
					break;
				case GAME_PAUSE:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					for(int i=0;i<vcMouse.size();i++)
						vcMouse.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcEgg.size();i++)
						vcEgg.elementAt(i).draw(canvas,paint);
					for(int i=0;i<vcHed.size();i++)
						vcHed.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcHit.size();i++)
						vcHit.elementAt(i).draw(canvas, paint);
					dtePause.draw(canvas);
					dteButton.draw(canvas, paint);
					break;
				case GAME_ABOUT:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					dteAbout.draw(canvas,paint);
					dteButton.draw(canvas, paint);
					break;
				case GAME_END:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas, paint);
					for(int i=0;i<vcMouse.size();i++)
						vcMouse.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcEgg.size();i++)
						vcEgg.elementAt(i).draw(canvas,paint);
					for(int i=0;i<vcHed.size();i++)
						vcHed.elementAt(i).draw(canvas, paint);
					for(int i=0;i<vcHit.size();i++)
						vcHit.elementAt(i).draw(canvas, paint);
					dteGameEnd.draw(canvas, paint);
					dteButton.draw(canvas, paint);
					break;
				case GAME_EXIT:
					dteBackground.draw(canvas, paint);
					dtePlayer.draw(canvas,paint);
					break;
				}
			}
		}catch(Exception e){
			//do nothing
		} finally{
			if(canvas!=null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	
	private void logic(){
		int y;
		boolean left;
		dteButton.btnState(gameState);
		switch(gameState){
		case GAME_MENU:
			dteBackground.logic();
			break;
		case GAMEING:
			count++;
			dteBackground.logic();
			dtePlayer.logic();
			dteButton.logic();
/////////////////////////////////////////////////////
//清鼠
			for(int i=0;i<vcMouse.size();i++){
				if(vcMouse.elementAt(i).isDead) vcMouse.removeElementAt(i);
				else vcMouse.elementAt(i).logic();
			}
/////////////////////////////////////////////////////
//产鼠
			if(vcMouse.size()<dtePlayer.maxMouseNum && count%createMouseTime==0){
				mouseLowSpeed=dtePlayer.lowestSpeed;
				mouseHighSpeed=dtePlayer.highestSpeed;
				left=random.nextBoolean();
				y=random.nextInt(screenH*5/6)+40;
				if(left){
					vcMouse.addElement(new DTEMouse(bmpMouseRun,bmpMouseStand,bmpMouseHit,bmpNumber,-bmpMouseRun[0].getWidth(),y));
				}else{
					vcMouse.addElement(new DTEMouse(bmpMouseRun,bmpMouseStand,bmpMouseHit,bmpNumber,screenW,y));
				}
			}
/////////////////////////////////////////////////////
//清刺猬，产刺猬			
			for(int i=0;i<vcHed.size();i++){
				if(vcHed.elementAt(i).isDead) vcHed.removeElementAt(i);
				else vcHed.elementAt(i).logic();
			}
			createHedgeHog=random.nextInt(100);
			if(createHedgeHog==1 && vcHed.size()<4){
				left=random.nextBoolean();
				y=random.nextInt(screenH*5/6)+40;
				if(left){
					vcHed.addElement(new DTEHedgehog(bmpHedgehog,bmpAngerHedgehog,bmpNumber,-bmpHedgehog[0].getWidth(),y));
				}else{
					vcHed.addElement(new DTEHedgehog(bmpHedgehog,bmpAngerHedgehog,bmpNumber,screenW,y));
				}
			}
/////////////////////////////////////////////////////
//清打击效果
			for(int i=0;i<vcHit.size();i++){
				if(vcHit.elementAt(i).playEnd) vcHit.removeElementAt(i);
				else vcHit.elementAt(i).logic();
			}
/////////////////////////////////////////////////////
//清蛋
			for(int i=0;i<vcEgg.size();i++){
				if(vcEgg.elementAt(i).isDead) vcEgg.removeElementAt(i);
				else vcEgg.elementAt(i).logic();
			}
///////////////////////////////////////////////////////
//碰撞处理
			for(int i=0;i<vcMouse.size();i++){
				for(int j=0;j<vcEgg.size();j++){
					if(vcMouse.elementAt(i).collsionWith(vcEgg.elementAt(j))){
						//nothing;
					}
				}
			}
			break;
		case GAME_OPTIONS:
			dteBackground.logic();
			dteOptions.logic();
			dteButton.logic();
			break;
		case GAME_RANKINGS:
			dteBackground.logic();
			dteRankings.logic();
			dteButton.logic();
			break;
		case GAME_PAUSE:
			dteButton.logic();
			break;
		case GAME_ABOUT:
			dteBackground.logic();
			dteAbout.logic();
			dteButton.logic();
			break;
		case GAME_END:
			dteBackground.logic();
			dtePlayer.logic();
			dteGameEnd.logic();
			dteButton.logic();
			break;
		case GAME_EXIT:
			DefendTheEggs.instance.finish();
			DefendTheEggs.instance.stopBgMusicSer();
			dteOptions.gameMusic=false;
			System.exit(0);
			break;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag){
			long start=System.currentTimeMillis();
			dteDraw();
			logic();
			long end=System.currentTimeMillis();
			try{
				if(end-start<40) Thread.sleep(40-(end-start));
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag=false;
	}

}