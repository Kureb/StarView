package com.example.starview;
public class Matrice {
	
	 private double a11, a12, a13;
	 private double a21, a22, a23;	
	 private double a31, a32, a33;

		  
	 public Matrice(){}
	 
	 public Matrice(String type,double alpha) {
	     if(type=="rx"){
	    	 a11=1; a12=0; a13=0;
	    	 a21=0; a22=Math.cos(alpha); a23=-(Math.sin(alpha));
	    	 a31=0; a32=Math.sin(alpha); a33=Math.cos(alpha);
	     }else if(type=="ry"){
	    	 a11=Math.cos(alpha); a12=0; a13=Math.sin(alpha);
	    	 a21=0; a22=1; a23=0;
	    	 a31=-(Math.sin(alpha)); a32=0; a33=Math.cos(alpha);
	     }
	     else if(type=="rz"){
	    	 a11=Math.cos(alpha); a12=-(Math.sin(alpha)); a13=0;
	    	 a21=Math.sin(alpha); a22=Math.cos(alpha); a23=0;
	    	 a31=0; a32=0; a33=1;
	     }
     }	
		
	public double getA11() {
		return a11;
	}

	public double getA12() {
		return a12;
	}

	public double getA13() {
		return a13;
	}

	public double getA21() {
		return a21;
	}

	public double getA22() {
		return a22;
	}

	public double getA23() {
		return a23;
	}

	public double getA31() {
		return a31;
	}

	public double getA32() {
		return a32;
	}

	public double getA33() {
		return a33;
	}
}