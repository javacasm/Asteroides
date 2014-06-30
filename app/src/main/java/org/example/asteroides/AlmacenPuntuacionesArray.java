package org.example.asteroides;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import android.text.format.DateFormat;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones
{
	private Vector<Puntuacion> puntuaciones;
	
	public AlmacenPuntuacionesArray()
	{
		puntuaciones=new Vector<Puntuacion>();

		guardarPuntuacion(1000, "pepeito", "10/7/2011");
		guardarPuntuacion(1100, "juanito", "10/2/2012");
		guardarPuntuacion(1300, "joseito", "12/3/2012");
		guardarPuntuacion(1060, "pepeito", "13/7/2010");
		guardarPuntuacion(1500, "luisito", "10/6/2012");
		guardarPuntuacion(1900, "luisito", "10/6/2012");
		guardarPuntuacion(1400, "luisita", "15/6/2012");
		guardarPuntuacion(1550, "luisita", "10/8/2012");
		guardarPuntuacion(1010, "juanito", "11/7/2022");
		guardarPuntuacion(2400, "luisita", "19/6/2012");
		guardarPuntuacion(2550, "luisita", "20/8/2012");
		guardarPuntuacion(2010, "juanito", "11/7/2022");
	}

	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		Collections.sort(puntuaciones);
		//Collections.reverse(puntuaciones);
		return puntuaciones;
	}
	
	public void guardarPuntuacion(int puntos, String nombre, String fecha) {
		
		Puntuacion puntocion=new Puntuacion(nombre, puntos, fecha);
		puntuaciones.add(0,puntocion);
	}

	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre, getDateFromMilis(milis));
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
