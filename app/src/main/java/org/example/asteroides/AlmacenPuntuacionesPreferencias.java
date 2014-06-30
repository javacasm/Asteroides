package org.example.asteroides;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones {

	private static String PREFERENCIAS ="puntuaciones";
	private Context context;
	
	public AlmacenPuntuacionesPreferencias(Context contexto)
	{this.context=contexto;}
	
	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre, getDateFromMilis(milis));
	}

	public void guardarPuntuacion(int puntos, String nombre, String sFecha) {
		SharedPreferences preferencias=
				context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=preferencias.edit();
		Puntuacion p=new Puntuacion(nombre, puntos, sFecha);
		editor.putString("puntuacion", p.convierteString());
		editor.commit();
	}

	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		Vector<Puntuacion> result=new Vector<Puntuacion>();
		SharedPreferences preferencias=
				context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
		String s=preferencias.getString("puntuacion", "");
		if(s!="")
		{
			Puntuacion p=new Puntuacion(s);
			result.add(p);
//			// JAV
////			int iSeparador1=s.indexOf('|');
////			int iSeparador2=s.indexOf(s,iSeparador1);
////			String sPuntos=s.substring(0,iSeparador1);
////			String sNombre=s.substring(iSeparador1, iSeparador2-iSeparador1);
////			String sFecha=s.substring(iSeparador2);
////			int puntos=Integer.valueOf(sPuntos);
////			Puntuacion p=new Puntuacion(sNombre, puntos, sFecha);
////			result.add(p);
//			// CESAR
//			String []campos=s.split("|");
//			Puntuacion p=new Puntuacion(campos[1], Integer.valueOf(campos[0]), campos[2]);

		}
			
	
		return result;
	}

	public static String getDateFromMilis(long milliSeconds)
	{return getDateFromMilis(milliSeconds,"dd/MM/yyyy");	}
	
	public static String getDateFromMilis(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
//	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//
//	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
//	     Calendar calendar = Calendar.getInstance();
//	     calendar.setTimeInMillis(milliSeconds);
//	     return formatter.format(calendar.getTime());
	     
	     return  DateFormat.format("dd/MM/yyyy", milliSeconds).toString();
	}
}
