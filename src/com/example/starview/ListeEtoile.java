package com.example.starview;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ListeEtoile extends ArrayList<Etoile>{
	
	public ListeEtoile(Context context,String file,MainActivity mainActivity){
		try {
			BufferedReader objBufferReader = new BufferedReader(new InputStreamReader(context.getAssets().open(file)));
			String strLine;
			String[] etoile = new String [1000];
			while ((strLine = objBufferReader.readLine()) != null){
				etoile = strLine.split(" ");
				if(Character.toString(etoile[0].charAt(0)).matches("[A-Za-z]")){
					if(Character.toString(etoile[6].charAt(0)).matches("[A-Za-z]")){
					this.add(new Etoile(etoile[0], new AscensionDroite(etoile[1],etoile[2],etoile[3]),new Declinaison(etoile[4], etoile[5], etoile[6]), etoile[7],etoile[8],mainActivity));
					}
					else{
						this.add(new Etoile(etoile[0], new AscensionDroite(etoile[1],etoile[2],etoile[3]),new Declinaison(etoile[4], etoile[5], etoile[6]), etoile[7],mainActivity));
					}
				}
				else{
					if(Character.toString(etoile[5].charAt(0)).matches("[A-Za-z]")){
						this.add(new Etoile(new AscensionDroite(etoile[0],etoile[1],etoile[2]),new Declinaison(etoile[3], etoile[4], etoile[5]), etoile[6],etoile[7],mainActivity));
					}
					else{
						this.add(new Etoile(new AscensionDroite(etoile[0],etoile[1],etoile[2]),new Declinaison(etoile[3], etoile[4], etoile[5]), etoile[6],mainActivity));
					}
				}
			}
			objBufferReader.close();
		}
		catch (FileNotFoundException objError) {
			Log.d("Max", "Fichier non trouvé");
		}
		catch (IOException objError) {
			Log.d("Max", "Erreur");
		}
		
		
		
		
		
		
		/*try{
			String etoile[] = new String[60];
			String ligne;
			InputStream is = context.getResources().openRawResource(R.raw.etoile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while((ligne = br.readLine()) != null){
				ligne = br.readLine();
				etoile = ligne.split(" ");
				this.add(new Etoile(etoile[0], etoile[1],Float.parseFloat(etoile[2]),Float.parseFloat(etoile[3]),Float.parseFloat(etoile[4]), etoile[5],  Float.parseFloat(etoile[6]),  Float.parseFloat(etoile[8]),  Float.parseFloat(etoile[9]),new AscensionDroite(Integer.parseInt(etoile[10]), Integer.parseInt(etoile[11])),Float.parseFloat(etoile[12])));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		/*String essai = Uri.parse("file:///android_asset/Etoile").toString();
		String etoile[] = new String [60];
		etoile = essai.split(" ");
		Log.i("test", essai);
		this.add(new Etoile(etoile[0], 
				etoile[1],
				Float.parseFloat(etoile[2]),
				Float.parseFloat(etoile[3]),
				Float.parseFloat(etoile[4]), 
				etoile[5],
				Float.parseFloat(etoile[6]),
				Float.parseFloat(etoile[8]),
				Float.parseFloat(etoile[9]),
				new AscensionDroite(Integer.parseInt(etoile[10]),Integer.parseInt(etoile[11])),
				Float.parseFloat(etoile[12])));
		
	}*/
	}
	
	/*public String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuilder fileData = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }*/
}
