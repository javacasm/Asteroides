package org.example.asteroides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	MediaPlayer mp;
	public static AlmacenPuntuaciones almacen; 
	public static String AsteroidesTAG="Asteroides";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvTitle=(TextView)findViewById(R.id.tvTitle);
        Animation animacion = AnimationUtils.loadAnimation(this,R.anim.animacion);
        tvTitle.startAnimation(animacion);
      //  Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        
        //almacen=new AlmacenPuntuacionesFicheroInterno(this);
        
        creaAlmacen();
        
        Log.i("Estado","oncreate");
        if(mp==null)
        {
        	mp=MediaPlayer.create(this, R.raw.audio);
        }
        else
        {
        	if(savedInstanceState != null&& mp!= null) {
                int pos = savedInstanceState.getInt("posicion");
                mp.seekTo(pos);
        	}
        }
        if(mp!=null)
        {
        	SharedPreferences pref = getSharedPreferences(
    				"org.example.asteroides_preferences", Context.MODE_PRIVATE);

    		if(pref.getBoolean("musica", false))
    			mp.start();
        }
    }
   
    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
           super.onSaveInstanceState(estadoGuardado);
           if(mp!= null) {
                  int pos = mp.getCurrentPosition();
                  estadoGuardado.putInt("posicion", pos);
           }
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
           super.onRestoreInstanceState(estadoGuardado);
           if(estadoGuardado != null&& mp!= null) {
                  int pos = estadoGuardado.getInt("posicion");
                  mp.seekTo(pos);
           }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        
        return true;
    }
    void creaAlmacen()
    {
		SharedPreferences pref = getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);

		String sAlmacen = pref.getString("almacen", "0");
		int TipoAlmacen = Integer.valueOf(sAlmacen);
		switch (TipoAlmacen) {
		case 0:
			almacen = new AlmacenPuntuacionesArray();
			break;
		case 1:
			almacen = new AlmacenPuntuacionesPreferencias(this);
			break;
		case 2:
			almacen = new AlmacenPuntuacionesFicheroInterno(this);
			break;
		case 3:
			almacen = new AlmacenPuntuacionesFicheroExterno(this);
			break;
		case 4:
			almacen =new AlmacenPuntuacionesXML_SAX(this);
			break;
		case 5:
			almacen =new AlmacenPuntuacionesXML_DOM(this);
			break;
		case 6:
			almacen =new AlmacenPuntuacionesSQLite(this);
			break;
		}
    }
    
    public void jugar(View v)
    {
    	Intent i=new Intent(this,Juego.class);
    	startActivityForResult(i, 1234);
    }
    public void verPreferencias(View v)
    {
    	Intent i=new Intent(this, Preferencias.class);
		startActivityForResult(i, 1235);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       	super.onActivityResult(requestCode, resultCode, data);
       	switch(requestCode)
       	{
       		case 1234:
       	       	if(resultCode==RESULT_OK && data!=null)
       	       	{
       	       		puntuacion=data.getIntExtra("puntuacion", 0);
       	       		showDialog(ID_DIALOG_PEDIR_NOMBRE);
       	       	}
       			break;
       		case 1235:
       	       	if(resultCode==RESULT_OK)
       	       	{
       	       		creaAlmacen();
       	       	}
       			break;
       	}

    } 

    public final int ID_DIALOG_PEDIR_NOMBRE=100;
    public final int ID_DIALOG_MOSTRAR_AVISO=200;
    public String sNombreUsuario;
    public int puntuacion;

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
    	final Dialog dialogo;
    	switch(id)
    	{
    		case ID_DIALOG_PEDIR_NOMBRE:
    			dialogo=new Dialog(this);
    			dialogo.setContentView(R.layout.dialogo_nombre);
    			dialogo.setTitle("Record de puntuacion!!");
    			Button btOK=(Button)dialogo.findViewById(R.id.btOKDialogo);
    			final EditText et=(EditText)dialogo.findViewById(R.id.etUsuario);
    			if(sNombreUsuario!="")
    				et.setText(sNombreUsuario);
    			btOK.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						sNombreUsuario=et.getText().toString();
						dialogo.dismiss();
				  		almacen.guardarPuntuacion(puntuacion, sNombreUsuario, System.currentTimeMillis());
				  		verPuntuaciones(null);
					}
				});
    			break;
    			
    		case ID_DIALOG_MOSTRAR_AVISO:
    			AlertDialog.Builder builder=new AlertDialog.Builder(this);
    			builder.setTitle(args.getString("Titulo"));
    			builder.setMessage(args.getShort("Mensaje"));
    			builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
    		        public void onClick(DialogInterface dialog, int which) {
    		            dialog.cancel();
    		        }
    		    });
    			dialogo=builder.create();
    			break;
			default:
				dialogo=super.onCreateDialog(id, args);
				break;
    	}
    	
    	return dialogo;
    }

    
    public void lanzarAcercaDe(View v)
    {
    	Intent i=new Intent(this,AcercaDe.class);
    	startActivity(i);
    }
    

    
    public void verPuntuaciones(View v)
    {
    	//finish();
    	//mostrarPreferencias();
    	Intent i =new Intent(this, Puntuaciones.class);
    	startActivity(i);
    }

    public void salirAplicacion(View v)
    {
    	finish();
    }
    
    public void mostrarPreferencias()
    {
    	SharedPreferences pref=getSharedPreferences("org.example.asteroides_preferences", MODE_PRIVATE);
    	String s="música: "+pref.getBoolean("musica", true)+" gráficos: "+pref.getString("graficos","?")+ 
    			" fragmentos: "+pref.getString("fragmentos", "0");
    	String s2="usuario:"+pref.getString("usuario","login");
    	Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
     public boolean onOptionsItemSelected(MenuItem item) {
	
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.acercade:
			lanzarAcercaDe(null);
			break;
		case R.id.config:
			verPreferencias(null);
			break;
		default:
			super.onOptionsItemSelected(item);
			break;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
	//	Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onDestroy");
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		//Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onPause");
		super.onPause();
		mp.pause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		//Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onRestart");
		super.onRestart();
		mp.start();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
	//	Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onResume");
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
	//	Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onStart");
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
	//	Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
		Log.i("Estado","onStop");
		super.onStop();
	}
}
