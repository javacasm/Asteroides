package org.example.asteroides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

import android.Manifest.permission;
import android.content.Context;
import android.database.CursorJoiner.Result;
import android.text.format.DateFormat;
import android.util.Log;

public class AlmacenPuntuacionesFicheroInterno implements AlmacenPuntuaciones {

	private static String FICHERO="puntuaciones.txt";
	private Context context;
	
	public AlmacenPuntuacionesFicheroInterno(Context contexto)
	{ context=contexto;}
	
	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre, getDateFromMilis(milis));
	}

	public void guardarPuntuacion(int puntos, String nombre, String sFecha) {
		try {
			FileOutputStream f= context.openFileOutput(FICHERO, Context.MODE_APPEND);
			Puntuacion p=new Puntuacion(nombre, puntos, sFecha);
			String texto=p.convierteString()+"\n";
			f.write(texto.getBytes());
			f.close();
		} catch (FileNotFoundException e) {
			Log.e("asteroides","Error abriendo fichero:" + FICHERO+ e.getStackTrace());
		} catch (IOException e) {
			Log.e("asteroides","Error escribiendo fichero:" + FICHERO+ e.getStackTrace());
		}
	}

	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		Vector<Puntuacion> result=new Vector<Puntuacion>();
		FileInputStream f;
		try {
			f = context.openFileInput(FICHERO);

			BufferedReader entrada=new BufferedReader(new InputStreamReader(f));
			int n=0;
			String linea=null;
			do
			{
				linea=entrada.readLine();

				if(linea!=null)
				{
					result.add(new Puntuacion(linea));
					n++;
				}

			}
			while(n<cantidad && linea!=null);
			f.close();
			 
		}
		catch (FileNotFoundException e) {
			Log.e("asteroides","Error abriendo fichero:" + FICHERO+ e.getStackTrace());
		} 
		catch (IOException e) {
			Log.e("asteroides","Error leyendo fichero:" + FICHERO+ e.getStackTrace());
		}
		Collections.sort(result);
		return result;
	}
	
	public static String getDateFromMilis(long milliSeconds)
	{return getDateFromMilis(milliSeconds,"dd/MM/yyyy");	}
	
	public static String getDateFromMilis(long milliSeconds, String dateFormat)
	{     return  DateFormat.format("dd/MM/yyyy", milliSeconds).toString();	}

}
