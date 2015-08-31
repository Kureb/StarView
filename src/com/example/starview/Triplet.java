package com.example.starview;


public class Triplet {
	private double x,y,z;
	public Triplet(double x,double y,double z){
		this.x = x;
		this.y=y;
		this.z=z;
	}
	
	public Triplet Multiplication(Matrice m){
		double x1,y1,z1;
		x1= m.getA11()*this.x+m.getA12()*this.y+m.getA13()*this.z;
		y1= m.getA21()*this.x+m.getA22()*this.y+m.getA23()*this.z;
		z1= m.getA31()*this.x+m.getA32()*this.y+m.getA33()*this.z;
		Triplet res = new Triplet(x1,y1,z1);
		return res;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	    
	    
	    
	
	
}
