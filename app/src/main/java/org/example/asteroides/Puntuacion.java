package org.example.asteroides;

import android.util.Log;

public class Puntuacion implements Comparable {

	
	public String nombre;
	public int puntos;
	public String fecha;
	
	public Puntuacion()
	{	}
	
	public Puntuacion(String nombre,int puntos,String fecha)
	{
		this.nombre=nombre;
		this.puntos=puntos;
		this.fecha=fecha;
	}
	
	public Puntuacion(String cadena)
	{
		if(cadena.indexOf(separador)==-1)
		{
			Log.e("asteroides","Formato erroneo");
			return;
		}
		String []campos=cadena.split(separador);
		if(campos.length<3)
		{
			Log.e("asteroides","Formato erroneo");
			return;
		}
		try
		{
		puntos=Integer.valueOf(campos[0]);
		
		}
		catch(Exception ex)
		{
			Log.e("asteroides","Error en la conversion");
			puntos=-7;
		}
		nombre=campos[1];
		fecha=campos[2];
	}
	

	
	public int compareTo(Object another) {
		Puntuacion otro=(Puntuacion)another;
		return -Integer.valueOf(this.puntos).compareTo(otro.puntos);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String separador=";";
	public String convierteString()
	{
		return this.puntos+separador+this.nombre+separador+this.fecha;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Nombre: "+nombre+" puntos:"+puntos+" en "+fecha;
	}
	

}
