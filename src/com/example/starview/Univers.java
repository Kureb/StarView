package com.example.starview;

import java.io.IOException;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Univers extends View{
	private HashMap<Integer, ListeEtoile> table; 
	private String[] ls;
	private float scaleFactor = 1f;
	private ScaleGestureDetector detector;
	private static float MIN_ZOOM = 0.1f;
	private static float MAX_ZOOM = 2f;
	private float x,xtouch;
	private float y,ytouch;
	private TextView textview ;
    public Univers(Context context,MainActivity mainActivity) throws IOException{
    	super(context);
    	ls = new String [88];
    	ls = context.getAssets().list("");
    	table = new HashMap<Integer, ListeEtoile>();
    	for(int i=0;i<ls.length;i++){
    		table.put(i,new ListeEtoile(context, ls[i],mainActivity));
    	}
		detector = new ScaleGestureDetector(getContext(), new ScaleListener());
		setFocusable(true);
	 }
	 
	 public boolean onTouchEvent(MotionEvent ev) {
		 // Let the ScaleGestureDetector inspect all events.
		 detector.onTouchEvent(ev); 
		 final int action = MotionEventCompat.getActionMasked(ev); 
		 switch (action) { 
		 	case MotionEvent.ACTION_MOVE: {
		 		final int pointerIndex = MotionEventCompat.getActionIndex(ev); 
		 		xtouch = MotionEventCompat.getX(ev, pointerIndex);
		 		ytouch = MotionEventCompat.getY(ev, pointerIndex);
		 		invalidate();
		 		break;
		 	}
		 }
		 return true;
	 }
		 
	 protected void onDraw(Canvas canvas) {
		 Paint p = new Paint();
		 p.setColor(Color.WHITE);
		 super.onDraw(canvas);
		 canvas.save();
		 canvas.translate(xtouch, ytouch);
		 canvas.scale(scaleFactor, scaleFactor,x,y);
		 canvas.drawARGB(255, 0, 0, 0);
		 Paint p1 = new Paint();
		 p1.setColor(Color.RED);
		 for(int i = 0;i<table.size();i++){
			 for(int j = 0;j<table.get(i).size();j++){
				 table.get(i).get(j).resultat();
				 if(table.get(i).get(j).profondeur()>0){
					 canvas.drawCircle(table.get(i).get(j).abscisse()*1000,table.get(i).get(j).ordonnee()*1000,5,p);
				 }
			 }
		 }
		 /*for (int i = 0; i < table.size(); i++) {
			 for(int j = 0;j<table.get(i).size()-1;j++){
				 canvas.drawLine(table.get(i).get(j).abscisse()*1000,table.get(i).get(j).ordonnee()*1000, table.get(i).get(j+1).abscisse()*1000, table.get(i).get(j+1).ordonnee()*1000, p);
			 }
		 }*/
		
		canvas.restore();
		invalidate();
	 }
	 
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		 @Override
		 public boolean onScale(ScaleGestureDetector detector) {
		 x = detector.getFocusX();
		 y = detector.getFocusY();
		 scaleFactor *= detector.getScaleFactor();
		 scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
		 invalidate();
		 return true;
		 }
	 }
	 
	 public ScaleGestureDetector getDetector(){
		 return detector;
	 }
	 
	 public View get(){
		 return this;
	 }
}

