package org.example.asteroides;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlmacenPuntuacionesSQLite extends SQLiteOpenHelper implements
		AlmacenPuntuaciones {

	public AlmacenPuntuacionesSQLite(Context context) {
		super(context, "puntuaciones", null, 5);
	
	}

	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre, AlmacenPuntuacionesArray.getDateFromMilis(milis));
		
	}

	Boolean bTrucos=false;
	public void guardarPuntuacion(int puntos, String nombre, String sFecha) {
		SQLiteDatabase db=getWritableDatabase();
		db.execSQL("INSERT INTO puntuaciones VALUES (null, "+
				puntos+",'"+nombre+"','"+sFecha+"')");
		
		if(bTrucos)
		{
			ContentValues nuevoRegistro=new ContentValues();
			nuevoRegistro.put("puntos",puntos+100);
			nuevoRegistro.put("nombre", "pp");
			nuevoRegistro.put("fecha", sFecha);
			db.insert("puntuaciones",null,nuevoRegistro);
			
			db.update("puntuaciones", nuevoRegistro, "nombre='?'", new String[]{"pp"});
			
			db.delete("puntuaciones","nombre='enemigo'",null);
		}
	}

	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		Vector<Puntuacion> result=new Vector<Puntuacion>();
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT puntos,  nombre,fecha FROM "+
				"puntuaciones ORDER BY puntos DESC LIMIT "+cantidad,null);
//		Cursor cursor2=db.query
//				("puntuaciones", 
//				new String[]{"puntos",  "nombre","fecha"}, 
//				null, // where con ?
//				null, // where argumentos 
//				null, // group by 
//				null, // having
//				"puntos",
//				Integer.toString(cantidad));
	
		while(cursor.moveToNext())
			result.add(new Puntuacion(cursor.getString(1), // Nombre
					cursor.getInt(0), //puntos
					cursor.getString(2) // fecha
					));
		cursor.close();
		return result;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE puntuaciones ("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"puntos INTEGER,nombre TEXT,fecha TEXT)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion)
		{
		case 1:
			//db.execSQL("ALTER TABLE puntuaciones...")
			break;
		case 2:
			//db.execSQL("CREATE TABLE puntuaciones2...")
			break;
		}
		
	}

}
