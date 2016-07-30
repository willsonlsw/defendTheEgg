package com.lsw.defendtheeggs;

import java.util.Comparator;

public class DTERankingsItem{
	public int year,month,day,hour,minute;
	public int score;
	public int rank;
	
	public DTERankingsItem(int sc,int y,int m,int d,int h,int min){
		score=sc;
		//rank=ra;
		year=y;
		month=m;
		day=d;
		hour=h;
		minute=min;
	}
	
	public int compare(Object e1,Object e2){
		DTERankingsItem o1=(DTERankingsItem)e1;
		DTERankingsItem o2=(DTERankingsItem)e2;
		if(o1.score>o2.score){
			return -1;
		}else if(o1.score<o2.score){
			return 1;
		}else return 0;
	}
}