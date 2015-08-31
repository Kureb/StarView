package com.example.starview;

import android.util.Log;

public class AscensionDroite {
	private String heure;
	private String minutes;
	private String seconde;
	
	public AscensionDroite(String heure,String minutes,String seconde){
		this.heure = heure;
		this.minutes = minutes;
		this.seconde = seconde;
	}
	
	public float calculEnDegres(){
		float res = 0;
		final float DEGRES = (float) 0.250 ;
		float res_en_minutes = Float.parseFloat(this.heure)*60+Float.parseFloat(this.minutes)+Float.parseFloat(this.seconde)/60;
		res = res_en_minutes * DEGRES;
		//Log.i("test", Float.toString(res));
		return res;
	}
}
