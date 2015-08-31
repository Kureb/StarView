package com.example.starview;

public class Declinaison {
	private String un,deux,trois;
	public Declinaison(String un,String deux,String trois){
		this.un = un;
		this.deux=deux;
		this.trois=trois;
	}
	
	public float calculenDegre(){
		float res = (Float.parseFloat(this.un))+(Float.parseFloat(this.deux)/60)+(Float.parseFloat(this.trois)/3600);
		return res;
	}
}
