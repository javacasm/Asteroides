package org.example.asteroides;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class VistaJuego extends View {
	
	private int puntuacion=0;
	private Activity padre;
	
	Handler handler = new Handler(); 
	
    // //// ASTEROIDES //////
    private Vector<Grafico> Asteroides; // Vector con los Asteroides
    private Grafico nave;
    private int giroNave;
    private float aceleracionNave;
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE=0.5f;
    
    private int numAsteroides= 5; // Número inicial de asteroides
    private int numFragmentos= 3; // Fragmentos en que se divide
    private float mX=0, mY=0;
    private boolean disparo=false;
    private ThreadJuego thread = new ThreadJuego();
    private static int PERIODO_PROCESO = 50;
    private long ultimoProceso=0;
    
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo=false;
    private int tiempoMisil;
    
   // MediaPlayer mpDisparo,mpExplosion;

    SoundPool soundPool;
    int idDisparo,idExplosion;

    // Fondo
    int iNumeroEstrellas=50;
    float estrellasPosicion[];
    float estrellasTamanio[];
    Paint paintEstrellas;
    float vx=1;
    float vy=1;
    Paint pFondo;
    
    private Drawable drawableAsteroide[]=new Drawable[3]; 
    
    public VistaJuego(Context context, AttributeSet attrs) {

          super(context, attrs);
          Drawable drawableNave,  drawableMisil;
    	  SharedPreferences pref = context.getSharedPreferences(
       		   "org.example.asteroides_preferences", Context.MODE_PRIVATE);
          if(isInEditMode()|| !pref.getString("graficos", "0").equals("0"))
          {
        	  drawableNave=context.getResources().getDrawable(R.drawable.nave);
        	  
        	  drawableAsteroide[0]=context.getResources().getDrawable(R.drawable.asteroide1);
        	  drawableAsteroide[1]=context.getResources().getDrawable(R.drawable.asteroide2);
        	  drawableAsteroide[2]=context.getResources().getDrawable(R.drawable.asteroide3);
        	  drawableMisil =(AnimationDrawable)getResources().getDrawable(R.drawable.animacionmisil);
        	  ((AnimationDrawable)drawableMisil).start();
        			 // context.getResources().getDrawable(R.drawable.asteroide1);
        	  setBackgroundResource(R.drawable.fondo);
          }
          else
          {
        	  // Nave
        	  Path pathNave=new Path();
        	  pathNave.moveTo(0f, 0f);
        	  pathNave.lineTo(1f, 0.5f);
        	  pathNave.lineTo(0f, 1f);
        	  pathNave.lineTo(0f, 0f);
        	  
        	  ShapeDrawable dNave=new ShapeDrawable(new PathShape(pathNave, 1f, 1f));
        	  dNave.getPaint().setColor(Color.GREEN);
        	  dNave.getPaint().setStyle(Style.STROKE);
        	  dNave.setIntrinsicHeight(20);
        	  dNave.setIntrinsicWidth(15);
        	  drawableNave=dNave;
        	  
        	  //Asteroide
		      Path pathAsteroide = new Path();
		      pathAsteroide.moveTo((float) 0.3, (float) 0.0);
		      pathAsteroide.lineTo((float) 0.6, (float) 0.0);
		      pathAsteroide.lineTo((float) 0.6, (float) 0.3);
		      pathAsteroide.lineTo((float) 0.8, (float) 0.2);
		      pathAsteroide.lineTo((float) 1.0, (float) 0.4);
		      pathAsteroide.lineTo((float) 0.8, (float) 0.6);
		      pathAsteroide.lineTo((float) 0.9, (float) 0.9);
		      pathAsteroide.lineTo((float) 0.8, (float) 1.0);
		      pathAsteroide.lineTo((float) 0.4, (float) 1.0);
		      pathAsteroide.lineTo((float) 0.0, (float) 0.6);
		      pathAsteroide.lineTo((float) 0.0, (float) 0.2);
		      pathAsteroide.lineTo((float) 0.3, (float) 0.0);

		      for(int i =0;i<3;i++)
		      {
			      ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
			      dAsteroide.getPaint().setColor(Color.WHITE);
			      dAsteroide.getPaint().setStyle(Style.STROKE);
			      dAsteroide.setIntrinsicWidth(50-i*14);
			      dAsteroide.setIntrinsicHeight(50-i*14);
			      drawableAsteroide[i]=dAsteroide;
		      }
		      
		      // Misil
		      ShapeDrawable dMisil=new ShapeDrawable(new RectShape());
		      dMisil.getPaint().setColor(Color.WHITE);
		      dMisil.getPaint().setStyle(Style.STROKE);
		      dMisil.setIntrinsicHeight(3);
		      dMisil.setIntrinsicWidth(15);
		      drawableMisil=dMisil;
		      
		     
		      // Background
		      setBackgroundColor(Color.BLACK); 
		      estrellasPosicion=new float[2*iNumeroEstrellas];
		      estrellasTamanio=new float[iNumeroEstrellas];
		      
		      paintEstrellas=new Paint();
		      paintEstrellas.setStyle(Style.FILL_AND_STROKE);
		      paintEstrellas.setColor(Color.WHITE);
		      
		      pFondo=new Paint();
		      pFondo.setColor(Color.BLACK);
		      pFondo.setStyle(Style.FILL);
          }

          Bitmap bmExplosion=BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
          
          Asteroides = new Vector<Grafico>();

          for (int i = 0; i < numAsteroides; i++) {
                Grafico asteroide = new Grafico(this, drawableAsteroide[(int)(Math.random()*3)]);
                asteroide.setIncY(Math.random() * 4 - 2);
                asteroide.setIncX(Math.random() * 4 - 2);
                asteroide.setAngulo((int) (Math.random() * 360));
                asteroide.setRotacion((int) (Math.random() * 8 - 4));
                asteroide.setAnimacion(bmExplosion);
                //asteroide.setFrame(0);
                asteroide.setNFrame(20);
                Asteroides.add(asteroide);
          }
          
          nave=new Grafico(this, drawableNave);
          
          nave.setAnimacion(bmExplosion);
         // nave.setFrame(0);
          nave.setNFrame(20);
          
          misil=new Grafico(this, drawableMisil);
          
          // Sonido
        //  mpDisparo=MediaPlayer.create(context, R.raw.disparo);
        //  mpExplosion=MediaPlayer.create(context, R.raw.explosion);
          
          soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
          idDisparo=soundPool.load(context, R.raw.disparo,0);
          idExplosion=soundPool.load(context,R.raw.explosion,0);
    }

    protected void actualizaFisica() {

        long ahora = System.currentTimeMillis();

        // No hagas nada si el período de proceso no se ha cumplido.
        if(ultimoProceso + PERIODO_PROCESO > ahora) {
              return;
        }

        // Para una ejecución en tiempo real calculamos retardo          
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez

        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        double nIncX = nave.getIncX() + aceleracionNave *
                             Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave *
                            Math.sin(Math.toRadians(nave.getAngulo())) * retardo;

        // Actualizamos si el módulo de la velocidad no excede el máximo
        if(Math.hypot(nIncX,nIncY) <= Grafico.getMaxVelocidad()){
              nave.setIncX(nIncX);
              nave.setIncY(nIncY);
        }

        // Actualizamos posiciones X e Y
        nave.incrementaPos(retardo);
        for (Grafico asteroide : Asteroides) {
              asteroide.incrementaPos(retardo);
        }
        
        if(misilActivo)
        {
        	misil.incrementaPos(retardo);
        	tiempoMisil--;
        	if(tiempoMisil<0){
        		misilActivo=false;
        	}
        	else{
        		for(int i=0;i<Asteroides.size();i++)
        		{
        			if(misil.verificaColision(Asteroides.elementAt(i)))
        			{
        				destruyendoAsteroide(i);
        				break;
        			}
        		}
        	}
        }
        
        if(estrellasPosicion!=null)
        {
        // 	Movemos el fondo
	        for(int i=0;i<iNumeroEstrellas;i++)
	        {
	        	estrellasPosicion[2*i]=(estrellasPosicion[2*i]+vx)%getWidth();
	        	estrellasPosicion[2*i+1]=(estrellasPosicion[2*i+1]+vy)%getHeight();
	        }
	        //vx=(Math.random()-0.5)/10;
	        //vy=(Math.random()-0.5)/10;
	        vx=(float) (-nave.getIncX()/10.0f);
	        vy=(float) (-nave.getIncY()/10.0f);
        }
        for(Grafico asteroide : Asteroides)
        	if(asteroide.verificaColision(nave))
        	{
        		bAnimacion=true;
        		nave.setIncX(0);
        		nave.setIncY(0);
        		nave.anima(true);
    	    	handler.postDelayed(new Runnable() { 
    	           public void run() {
    	        	   nave.anima(false);
    	        	   salir();
    	           }
    	    	}, 2000); 
        		
        	}
    }
    Boolean bAnimacion=false;
//    void anima(final Grafico grafico)
//    {
//    	if(bAnimacion)
//    	{
//    		grafico.setFrame((nave.getFrame()+1)%nave.getNFrame());
//	    	
//	    	handler.postDelayed(new Runnable() { 
//	           public void run() {
//	        	  anima(grafico);
//	           }
//	    	}, 100); 
//    	}
//    }
    
    private synchronized  void destruyendoAsteroide(int i)
    {
    	//mpExplosion.start();
    	soundPool.play(idExplosion, 1, 1, 0, 0, 1);
    	Grafico asteroideDestruido=Asteroides.get(i);
    	int tam=0;
    	if(asteroideDestruido.getDrawable()!=drawableAsteroide[2])
    	{
    		if(asteroideDestruido.getDrawable()!=drawableAsteroide[1])
    			tam=1;
    	   	else
    	   		tam=2;
    		for(int n=0;n<numFragmentos;n++)
    		{
    			Grafico asteroideNuevo=new Grafico(this, drawableAsteroide[tam]);
    			asteroideNuevo.setPosX(asteroideDestruido.getPosX());
    			asteroideNuevo.setPosY(asteroideDestruido.getPosY());
    			asteroideNuevo.setIncX(Math.random()*7-2-tam);
    			asteroideNuevo.setIncY(Math.random()*7-2-tam);
    			asteroideNuevo.setAngulo((int)(Math.random()*360));
    			asteroideNuevo.setRotacion((int)(Math.random()*8-4));
    			Asteroides.add(asteroideNuevo);
    		}
    		Asteroides.remove(i);
    	}
    	else
    	{
    	//	asteroideDestruido.anima(true);
    		asteroideParaBorrar=asteroideDestruido;
    		//handler.postDelayed(new Runnable() {
				
			//	public void run() {
				//	asteroideParaBorrar.anima(false);
					Asteroides.remove(asteroideParaBorrar);
				//}
		//	}, 2000);
    	}
    	
    	puntuacion+=1000;
    	
    	misilActivo=false;
    	if(Asteroides.isEmpty())
    		salir();
    }
    Grafico asteroideParaBorrar;

    private void salir() {
		Intent intent =new Intent();
		intent.putExtra("puntuacion", puntuacion);
		padre.setResult(Activity.RESULT_OK,intent);
		padre.finish();
		
	}

	private void activaMisil()
    {
    	//mpDisparo.start();
    	soundPool.play(idDisparo, 1,1,1, 0, 1);
    	
       misil.setPosX(nave.getPosX()+ nave.getAncho()/2-misil.getAncho()/2);
       misil.setPosY(nave.getPosY()+ nave.getAlto()/2-misil.getAlto()/2);
       misil.setAngulo(nave.getAngulo());
       misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))*PASO_VELOCIDAD_MISIL);
       misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))*PASO_VELOCIDAD_MISIL);
       tiempoMisil = (int) Math.min(this.getWidth() / Math.abs( misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
       misilActivo = true;
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event) {
       super.onTouchEvent(event);
       float x = event.getX();
       float y = event.getY();
       switch(event.getAction()) {
       case MotionEvent.ACTION_DOWN:
              disparo=true;
              Log.i("touch","down");
              break;

       case MotionEvent.ACTION_MOVE:
    	   	  Log.i("touch","move");
              float dx = Math.abs(x - mX);
              float dy = Math.abs(y - mY);
              if(dy<6 && dx>6){
                     giroNave = Math.round((x - mX) / 2);
                     disparo= false;
              } else if(dx<6 && dy>6){
                     aceleracionNave = Math.round((mY - y) / 25);
                     disparo= false;
              }
              break;

       case MotionEvent.ACTION_UP:
    	   Log.i("touch","up");
              giroNave= 0;
              aceleracionNave = 0;
              if(disparo){
            	  activaMisil();
              }
              break;
       }

       mX=x; mY=y;       
       return true;
    }
    
    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
          super.onKeyDown(codigoTecla, evento);
           // Suponemos que vamos a procesar la pulsación
          boolean procesada = true;
          switch (codigoTecla) {
          case KeyEvent.KEYCODE_Q:
          case KeyEvent.KEYCODE_DPAD_UP:
                 aceleracionNave = +PASO_ACELERACION_NAVE;
                 Log.i("KeyUp","aceleracion:"+aceleracionNave);
                 break;

          case KeyEvent.KEYCODE_DPAD_LEFT:
                 giroNave = -PASO_GIRO_NAVE;
                 Log.i("KeyLeft","aceleracion:"+aceleracionNave);
                 break;

          case KeyEvent.KEYCODE_DPAD_RIGHT:
                 giroNave = +PASO_GIRO_NAVE;
                 Log.i("KeyRight","aceleracion:"+aceleracionNave);
                 break;

          case KeyEvent.KEYCODE_S:
        	  Toast.makeText(this.getContext(), "Trucos", Toast.LENGTH_SHORT).show();
        	  break;
          case KeyEvent.KEYCODE_DPAD_CENTER:
          case KeyEvent.KEYCODE_ENTER:
                 activaMisil();
        	  	Log.i("KeyEnter","aceleracion:"+aceleracionNave);
                 break;

          default:
                 // Si estamos aquí, no hay pulsación que nos interese
                 procesada = false;
                 break;
          }

          return procesada;
    }
    
    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
          super.onKeyUp(codigoTecla, evento);
          // Suponemos que vamos a procesar la pulsación
          boolean procesada = true;
          switch (codigoTecla) {
          case KeyEvent.KEYCODE_DPAD_UP:
                 aceleracionNave = 0;
                 break;

          case KeyEvent.KEYCODE_DPAD_LEFT:
          case KeyEvent.KEYCODE_DPAD_RIGHT:
                 giroNave = 0;
                 break;

          default:
                 // Si estamos aquí, no hay pulsación que nos interese
                 procesada = false;
                 break;
          }
          return procesada;
    }
     
    @Override protected void onSizeChanged(int ancho, int alto,
                                                         int ancho_anter, int alto_anter) {
          super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
          nave.setPosX(ancho/2-nave.getAncho()/2);
          nave.setPosY(alto/2-nave.getAlto()/2);
          
          // Una vez que conocemos nuestro ancho y alto.
          for (int i=0;i< Asteroides.size();i++) {
        	  Grafico asteroide=Asteroides.get(i);
        	  do
        	  {
                asteroide.setPosX(Math.random()*(ancho-asteroide.getAncho()));
                asteroide.setPosY(Math.random()*(alto-asteroide.getAlto()));
        	  }while(asteroide.distancia(nave)<(ancho+alto)/5 || CC(asteroide,i));
          }
          
          // Fondo
          if(estrellasPosicion!=null)
          {
	          for(int i=0;i<iNumeroEstrellas;i++)
	          {
	        	  estrellasPosicion[2*i]=(float)(Math.random()*ancho);
	        	  estrellasPosicion[2*i+1]=(float)(Math.random()*alto);
	        	  estrellasTamanio[i]=(float)(Math.random()*5);
	          }
          }
          // Animamos el juego con un hilo
          ultimoProceso = System.currentTimeMillis();
          thread.start();
    }
    
    private boolean CC(Grafico asteroide, int i) {
		for(int j=0;j<i;j++)
		{
			if(Asteroides.get(j).distancia(asteroide)<100)
				return true;
		}
		return false;
	}

	@Override protected synchronized void onDraw(Canvas canvas) {
          super.onDraw(canvas);
    
          if(estrellasPosicion!=null)
          {
        	  canvas.drawRect(0, 0, getWidth(), getHeight(),  pFondo);
          
        	  for(int i=0;i<iNumeroEstrellas;i++)
        		  canvas.drawCircle(estrellasPosicion[2*i],estrellasPosicion[2*i+1],estrellasTamanio[i], paintEstrellas);
          }
          
          
          for (Grafico asteroide: Asteroides) {
              asteroide.dibujaGrafico(canvas);
          }
          
          nave.dibujaGrafico(canvas);
          if(misilActivo)
        	  misil.dibujaGrafico(canvas);
    }

	
	class ThreadJuego extends Thread
	{
		private boolean pausa;
		private boolean corriendo;

		@Override
		public void run()
		{
			corriendo=true;
			while(corriendo)
			{
				actualizaFisica();
				synchronized (this) {
					while(pausa){
						try{
							wait();
						}
						catch(Exception ex)
						{
							Log.e("Asteroides","Error en threadJuego.Run:"+Log.getStackTraceString(ex));
						}
					}
				}
			}
		}

		public void pausar() {
			pausa=true;
			
		}

		public synchronized void reanudar() {
			pausa=false;
			notify();
			
		}

		public void detener() {
			corriendo=false;
			if(pausa) reanudar();
			
		}
	}

	public void setPadre(Activity activity)
	{
		this.padre=activity;
	}
	
	/**
	 * @return the thread
	 */
	public ThreadJuego getThread() {
		return thread;
	}
}
