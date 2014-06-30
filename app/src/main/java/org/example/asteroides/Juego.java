package org.example.asteroides;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;



public class Juego extends Activity {

	VistaJuego vistaJuego;
	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		Log.i("Estado","juegos.onCreate");
		vistaJuego=(VistaJuego)findViewById(R.id.VistaJuego);
		vistaJuego.setPadre(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.i("Estado","juegos.onDestroy");
		vistaJuego.getThread().detener();
		super.onDestroy();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		Log.i("Estado","juegos.onPause");
		super.onPause();
		vistaJuego.getThread().pausar();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		Log.i("Estado","juegos.onRestart");
		super.onRestart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.i("Estado","juegos.onResume");
		super.onResume();
		vistaJuego.getThread().reanudar();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		Log.i("Estado","juegos.onStart");
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		Log.i("Estado","juegos.onStop");
		super.onStop();
	}
	
}
