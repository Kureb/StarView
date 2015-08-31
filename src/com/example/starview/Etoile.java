package com.example.starview;


import android.graphics.Color;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Etoile{
	private AscensionDroite ascension;
	private String grec,nom;
	private Declinaison declinaison;
	private String magnitude;
	private Triplet triplet,tripletcoo;
	private MainActivity main;
	
	public Etoile(String grec,AscensionDroite ascension,Declinaison declinaison,String magnitude,String nom,MainActivity mainActivity ){
		this.grec=grec;
		this.declinaison = declinaison;
		this.ascension = ascension;
		this.magnitude=magnitude;
		this.nom=nom;
		this.main = mainActivity;
		coordonnee();
	}
	
	public Etoile(String grec,AscensionDroite ascension,Declinaison declinaison,String magnitude,MainActivity m){
		this.grec=grec;
		this.declinaison = declinaison;
		this.ascension = ascension;
		this.magnitude=magnitude;
		this.main = m;
		coordonnee();
	}
	
	public Etoile(AscensionDroite ascension,Declinaison declinaison,String magnitude,MainActivity m){
		this.declinaison = declinaison;
		this.ascension = ascension;
		this.magnitude=magnitude;
		this.main = m;
		coordonnee();
	}
	
	public Etoile(AscensionDroite ascension,Declinaison declinaison,String magnitude,String nom,MainActivity m){
		this.declinaison = declinaison;
		this.ascension = ascension;
		this.magnitude=magnitude;
		this.nom=nom;
		this.main = m;
		coordonnee();
	}
	
	public float calculDeclinaison(){
		float dec = this.declinaison.calculenDegre();
		float res = (float) (48.6833 * Math.tan(45-dec/2));
		return res;
	}
	
	public float coordonneZ(){
		return (float) Math.sin(this.declinaison.calculenDegre());
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public float getascension(){
		return this.ascension.calculEnDegres();
	}
	
	public void coordonnee(){
		double x,y,z;
		x = Math.cos(Math.toRadians(this.getascension()))*Math.cos(Math.toRadians(this.calculDeclinaison()));
		y =  Math.cos(Math.toRadians(this.getascension()))*Math.sin(Math.toRadians(this.calculDeclinaison()));
		z = Math.sin(Math.toRadians(this.getascension()));
		Matrice m = new Matrice("ry",-90);
		Matrice m1=new Matrice("rz",-90);
		Matrice m2=new Matrice("ry",main.getSideral());
		Matrice m3 = new Matrice("rx",main.getLatitude());
		
		Triplet t = new Triplet(x,y,z);
		
		t=t.Multiplication(m);
		t=t.Multiplication(m1);
		t=t.Multiplication(m2);
		t=t.Multiplication(m3);
		this.tripletcoo =t;
	}
	
	public void resultat(){
		Matrice m4 = new Matrice("rz",Math.toRadians(main.getZ()));
		Matrice m5 = new Matrice("rx",Math.toRadians(main.getY()));
		Matrice m6 = new Matrice("ry",Math.toRadians(main.getX()));
		Triplet t1 = this.tripletcoo;
		t1=t1.Multiplication(m4);
		t1=t1.Multiplication(m5);
		t1=t1.Multiplication(m6);
		this.triplet=t1;
	}
	
	
	public float abscisse(){
		return (float)this.triplet.getX();
		
	}
	
	public float ordonnee(){
		
		return (float)this.triplet.getY();
	}
	
	public float profondeur(){
		return (float)this.triplet.getZ();
	}
}
