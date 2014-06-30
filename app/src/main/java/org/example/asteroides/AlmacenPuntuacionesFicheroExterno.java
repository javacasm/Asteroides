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
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class AlmacenPuntuacionesFicheroExterno implements AlmacenPuntuaciones {

	private static String FICHERO=Environment.getExternalStorageDirectory()+ "/puntuaciones.txt";
	private Context context;
	
	public AlmacenPuntuacionesFicheroExterno(Context contexto)
	{ context=contexto;}
	
	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre, getDateFromMilis(milis));
	}

	public void guardarPuntuacion(int puntos, String nombre, String sFecha) {
		String estadoSD=Environment.getExternalStorageState();
		if(estadoSD.equals(Environment.MEDIA_MOUNTED))
		{
			try {
				FileOutputStream f= new FileOutputStream(FICHERO, true);
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
		else
		{
			Log.d(MainActivity.AsteroidesTAG,"No se puede escribir el almacenamiento externo");
		}
	}

	
	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		Vector<Puntuacion> result=new Vector<Puntuacion>();
		String estadoSD=Environment.getExternalStorageState();
		if(estadoSD.equals(Environment.MEDIA_MOUNTED) ||
				estadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			FileInputStream f;
			try {
				f = new FileInputStream(FICHERO);
	
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
		}
		else
		{
			Log.d(MainActivity.AsteroidesTAG,"No se puede leer el almacenamiento externo");
		}
		return result;
	}
	
	public static String getDateFromMilis(long milliSeconds)
	{return getDateFromMilis(milliSeconds,"dd/MM/yyyy");	}
	
	public static String getDateFromMilis(long milliSeconds, String dateFormat)
	{     return  DateFormat.format("dd/MM/yyyy", milliSeconds).toString();	}

}
