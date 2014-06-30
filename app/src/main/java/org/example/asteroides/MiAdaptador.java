package org.example.asteroides;

import java.util.Vector;

import org.example.asteroides.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MiAdaptador extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Puntuacion> lista;
	
	public MiAdaptador(Activity actividad,Vector<Puntuacion> lista)
	{
		super();
		this.actividad=actividad;
		this.lista=lista;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater=actividad.getLayoutInflater();
		
		View view=inflater.inflate(R.layout.elemento_lista, null,true);
		
		TextView textView=(TextView)view.findViewById(R.id.titulo);
		Puntuacion puntos=lista.elementAt(position);
		textView.setText(puntos.nombre+" "+puntos.puntos);
		
		TextView textViewSub=(TextView)view.findViewById(R.id.subtitulo);
		//textViewSub.setText(""+puntos.puntos);
		textViewSub.setText(Integer.toString(puntos.puntos));
		//textViewSub.setText(puntos.fecha);
		
		ImageView imageView=(ImageView)view.findViewById(R.id.icono);
		
		if(position<lista.size()/3)
			imageView.setImageResource(R.drawable.asteroide1);
		else
		{
			if(position<2*lista.size()/3)
				imageView.setImageResource(R.drawable.asteroide2);
			else
				imageView.setImageResource(R.drawable.asteroide3);
		}
			
//		switch(puntos.puntos%3)
//		{
//			case 0:
//				imageView.setImageResource(R.drawable.asteroide1);
//				break;
//			case 1:
//				imageView.setImageResource(R.drawable.asteroide2);
//				break;
//			case 2:
//				imageView.setImageResource(R.drawable.asteroide3);
//				break;
//		}
		
		return view;
	}

	public int getCount() {
		return lista.size();
	}
	
	public Object getItem(int position) {
		return lista.elementAt(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
}
