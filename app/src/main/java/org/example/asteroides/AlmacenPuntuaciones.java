package org.example.asteroides;

import java.util.Vector;

public interface AlmacenPuntuaciones {

	public void guardarPuntuacion(int puntos,String nombre,long milis);
	public Vector<Puntuacion> listaPuntuaciones(int cantidad);
}
