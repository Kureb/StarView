package com.example.starview;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;



public class Description extends Dialog {

	public Description(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Description(Context context,String titre,int x,int y) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setTitle(titre);
		TextView t = new TextView(context);
		t.setText("X : "+x+" Y : "+y);
		this.setContentView(t);
	}

	
	
  
}