package com.example.starview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements  LocationListener,SensorEventListener {
	
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	float julianDay,sideral,latitude,longitude;
	Calendar dateAndTime = Calendar.getInstance();
	int heure,minutes,secondes,jour,mois,annee;
	LocationManager lManager;
    Location location;
    String choix_source = "";
    AlertDialog.Builder ad;
	private Menu m = null;
	private boolean isCheckedGPS = false;
	private boolean isCheckedConstelation = false;
	private boolean isCheckedManuel  = false;
	//les différentes valeurs de la position
	float x, y, z;
		
	//cf http://fr.openclassrooms.com/informatique/cours/creez-des-applications-pour-android/les-capteurs-de-position
	float[] accelerometreVector = new float[9];
	float[] magneticVector = new float[3];
	float[] resultMatrix = new float[9];
	float[] values = new float[3];
	SensorManager sensorManager;
	Sensor magnetic;
	Sensor accelerometer;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.heure=this.dateAndTime.get(Calendar.HOUR_OF_DAY);
		this.minutes=this.dateAndTime.get(Calendar.MINUTE);
		this.secondes=this.dateAndTime.get(Calendar.SECOND);
		this.jour=this.dateAndTime.get(Calendar.DAY_OF_MONTH);
		this.mois=this.dateAndTime.get(Calendar.MONTH);
		this.annee=this.dateAndTime.get(Calendar.YEAR);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		//On récupère le service de localisation
        lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
        this.setContentView(R.layout.activity_main);
        obtenirPosition();
        
	}

	

	
	
	private float computeSideral() {
		/* D'après le document de monsieur Roegel, je cite :
		 * The sideral time in Greenwich is approximately
		 * GMST = 18.697374558+24.06570982441908 x D
		 * where D is the number of days since 1 January 2000 12 UT ;
		 * D can be computed using the Julian Day;
		 * 
		 * The local sideral time at the current location is
		 * s = LST = GMST + L/15
		 * where L is the longitude in degrees
		 * 
		 * 
		 * Le jour Julien du 01/01/2000 est : 2,451,545 ; (2,451,544 par ma fonction)
		 * 
		 * */
		int jour_julien = computeJulian();
		int jj01jan2000 = 2451545;
		int D = jour_julien - jj01jan2000;
		double GMST = 18.697374558 + 24.06570982441908 * D;
		
		return (float)(GMST + this.latitude/15);
		
	}
	
	public int computeJulian() {
	
		int date_nb = jour+100*mois+10000*annee;//Pour faciliter les calculs YYYYMMDD
		//Algo tiré de http://jean-paul.cornec.pagesperso-orange.fr/formule_jj.htm
		
		/* 1ère étape
		 * Si M = 1 ou 2, Remplacer A par A-1 et M par M+12
		 * Janvier et Février sont considérés comme les 13 et 14ièmes mois
		 * de l'année précédente */
		if (mois == 1 || mois == 2 ) {
			annee -= 1;
			mois +=12;
		}
		
		/* 2ième étape
		 * Si J M A est une date du calendrier grégorien, calculer :
		 * C = ENT[A/100]; B = 2-C + ENT[C/4];
		 * Si J M A est une date du calendrier julien, B = 0 
		 * (inutile de calculer C)
		 * PS : ENT représente la partie entière
		 * Le calendrier julien s'est terminé le 4 octobre 1582.
		 * Le calendrier grégorien a débuté le 15 octobre 1582.
		 * Les dates allant du 5/10/1582 au 14/10/1582 n'existent pas. 
		 * */
		int julien_nb = 15821004;
		int gregorien_nb = 15821015;
		int b = 0, c;
		if (date_nb > gregorien_nb) {
			//calcul grégorien
			c = (annee/100);
			b = 2-c+(int)(c/4);
		}else if (julien_nb > date_nb) {
			b = 0;
		}else {
			//La date n'existe pas, faire un toast ?
		}
		
		/* 3ième étape
		 * Calculer la fraction de jour correspondant à HH MM SS
		 * T = HH / 24 + MM / 1440 + SS / 86400
		 * */
		int t = heure/24 + minutes/1440 + 0;//sec = 0 chez nous, tout le temps
		
		/* 4ième étape
		 * Calcul final du jour julien
		 * JJ = Ent [ 365,25 * ( A + 4716 ) ] + Ent [ 30,6001 * ( M + 1 ) ] + J + T + B - 1524,5
		 * */
		//A vérifier, mais faire attention aux éventuels problèmes d'arrondi.
		int julian_day = (int) ((int)(365.25*(annee+4716)) + (int)(30.6001*(mois+1)) + jour + t + b - 1524.5);
		
		return julian_day;
	}
	



	@Override
	public void onLocationChanged(Location arg0) {
		Log.i("GPS", "La position a changé");
		this.location = arg0;
		try {
			afficherLocation();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lManager.removeUpdates(this);
	}




	@Override
	public void onProviderDisabled(String arg0) {
		Log.i("GPS", "La source a été désactivée");
		//On prévient l'utilisateur qu'il n'y a pu de GPS ou 3G/Wifi
		Toast.makeText(MainActivity.this, String.format("La source \"%s\" a été désactivée", arg0), Toast.LENGTH_LONG).show();
		lManager.removeUpdates(this);
		setProgressBarIndeterminateVisibility(false);
	}



	@Override
	public void onProviderEnabled(String arg0) {
		Log.i("GPS", "La source a été activée");
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.i("GPS","Le statut de la source a changé");
	}
	
	
	private void obtenirPosition() {
		//on démarre le cercle de chargement
		setProgressBarIndeterminateVisibility(true);
 
		//On demande au service de localisation de nous notifier tout changement de position
		//sur la source (le provider) choisie, toute les minutes (60000millisecondes).
		//Le paramètre this spécifie que notre classe implémente LocationListener et recevra
		//les notifications.
		lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, this);
	}
	
	
	private void afficherLocation() throws IOException {
		//On affiche les informations de la position a l'écran
		this.latitude=(float) location.getLatitude();
		this.longitude = (float) location.getLongitude();
		update();
	}
	
	/** Called when the activity is first created. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  MenuInflater inflater = getMenuInflater();
	  //R.menu.main est l'id de notre menu
	  inflater.inflate(R.menu.main, menu);
	  m = menu;
	  return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuItem checkableGps = menu.findItem(R.id.menu_gps);
	    checkableGps.setChecked(isCheckedGPS);
	    MenuItem checkableConst = menu.findItem(R.id.menu_const);
	    checkableConst.setChecked(isCheckedConstelation);
	    MenuItem checkableManuel = menu.findItem(R.id.menu_const);
	    checkableManuel.setChecked(isCheckedManuel);
	    return true;
	}
	
DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			annee=year;
			mois = monthOfYear;
			jour = dayOfMonth;
			Log.i("annee", Integer.toString(annee));
			try {
				update();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("jj", Float.toString(julianDay));
		}
	};
	
	
	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			heure = hourOfDay;
			minute = minute;
		}
	};
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  File imageFile;
	switch(item.getItemId())
	  {
	    case R.id.menu_calendar:
	    	Calendar dateAndTime = Calendar.getInstance();
	    	new DatePickerDialog(MainActivity.this,
					 d,
					 dateAndTime.get(Calendar.YEAR),
					 dateAndTime.get(Calendar.MONTH),
					 dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
		
		new TimePickerDialog(MainActivity.this,
					 t,
					 dateAndTime.get(Calendar.HOUR_OF_DAY),
					 dateAndTime.get(Calendar.MINUTE),
					 true).show();
	      return true;
	      
	    case R.id.menu_share:
	    	Bitmap bitmap = takeScreenshot();
	        saveBitmap(bitmap);
	        return true;
	        
	      
	
	    case R.id.menu_gps:
	    	isCheckedGPS = !item.isChecked();
            if(isCheckedGPS == true){
            	turnGPSOn();
            	Toast toast = Toast.makeText(this, "GPS ON", Toast.LENGTH_SHORT);
            	toast.show();
            	
            }
            else{
            	turnGPSOff();
            	Toast toast = Toast.makeText(this, "GPS OFF", Toast.LENGTH_SHORT);
            	toast.show();
            	
            }
            item.setChecked(isCheckedGPS);
            return true;
        default:
            return false;
            
        case R.id.menu_quit:
        	MainActivity.this.finish();
        	
        case R.id.menu_map:
        	setContentView(R.layout.activity_main);
        	
        case R.id.propos:
        	AlertDialog.Builder a = new AlertDialog.Builder(this);
        	a.setTitle("A Propos");
        	a.setMessage("Cette application a été réalisé dans le cadre d'un projet tutoré de DUT Informatique en 2014 par Alexandre Daussy, Michel Nussbaum, Quentin Jacquemet et Pierre Leriche. Le sujet a été proposé par Denis Roegel.")
        	.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  })
			.setCancelable(false);
        	AlertDialog alertDialog = a.create();
        	a.show();
	  }
	  return super.onOptionsItemSelected(item);
	  
	}
	
	public Bitmap takeScreenshot() {
		   View rootView = findViewById(android.R.id.content).getRootView();
		   rootView.setDrawingCacheEnabled(true);
		   return rootView.getDrawingCache();
		}
	
	public void saveBitmap(Bitmap bitmap) {
	    File imagePath = new File(Environment.getExternalStorageDirectory() + "/Starview"+this.jour+"-"+this.mois+"-"+this.annee+"_"+this.heure+"-"+this.minutes+"-"+this.secondes+".png");
	    FileOutputStream fos;
	    try {
	        fos = new FileOutputStream(imagePath);
	        bitmap.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        Log.e("GREC", e.getMessage(), e);
	    } catch (IOException e) {
	        Log.e("GREC", e.getMessage(), e);
	    }
	}
	
	
	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}

	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);


	   }
	}
	
	protected void onPause() {
		sensorManager.unregisterListener(this, accelerometer);
		sensorManager.unregisterListener(this, magnetic);
		super.onPause();
	}
	
	/* Permet de remettre en route tout quand l'application
	 * revient au premier plan
	 */
	protected void onResume() {
		sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}
	
	public float getSideral(){
		return sideral;
	}
	
	public float getLatitude(){
		return latitude; 
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void onSensorChanged(SensorEvent event) {
		//On récupère les valeurs des 2 sensors s'ils existent
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			accelerometreVector = event.values;
		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			magneticVector = event.values;
		
		
		SensorManager.getRotationMatrix(resultMatrix, null, accelerometreVector, magneticVector);
		SensorManager.getOrientation(resultMatrix, values);
		
		//update the values of the attributes(x, y et z)
		x = (float) Math.toDegrees((int)values[0]);
		y = (float) Math.toDegrees((int)values[1]);
		z = (float) Math.toDegrees((int)values[2]);
	}
	
	public void update() throws IOException{
		this.julianDay = computeJulian();
		this.sideral = computeSideral();
		Univers u = new Univers(this, this);
		this.setContentView(u);
	}
	
	
	
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
}
