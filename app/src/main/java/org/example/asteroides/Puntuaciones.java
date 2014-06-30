package org.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Puntuaciones extends ListActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);
        
//        setListAdapter(new ArrayAdapter<String>(this, 
//        		R.layout.elemento_lista,
//        		R.id.titulo,
//        		MainActivity.almacen.listaPuntuaciones(10)));
        
        setListAdapter(new MiAdaptador(this, 
        		MainActivity.almacen.listaPuntuaciones(10)));
        setResult(RESULT_OK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.puntuaciones, menu);
        return true;
    }

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Object o =getListAdapter().getItem(position);
		
		Puntuacion puntos=(Puntuacion)o;
		
		String sMensajeJav="Seleccion:" +Integer.toString(position)+" - " + o.toString();
		String sMensaje="Seleccion: " +Integer.toString(position)+" - "+
				"Nombre: "+puntos.nombre+ " puntos: "+puntos.puntos+" fecha: "+puntos.fecha;
		
		Toast.makeText(this, sMensaje, Toast.LENGTH_LONG).show();
	}
    
}
