package org.example.asteroides;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class AlmacenPuntuacionesXML_SAX implements AlmacenPuntuaciones {

	private static String FICHERO = "puntuaciones.xml";
	private Context context;
	private boolean cargadaLista;

	Vector<Puntuacion> lista;

	public AlmacenPuntuacionesXML_SAX(Context contexto) {
		this.context = contexto;
		lista = new Vector<Puntuacion>();
		cargadaLista = false;
	}

	public void guardarPuntuacion(int puntos, String nombre, String fecha) {
		try{
			if(!cargadaLista){
				leerXML(context.openFileInput(FICHERO));
			}
		}catch (FileNotFoundException e){
		}catch (Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}
		lista.add(new Puntuacion(nombre, puntos, fecha));
		try{
			escribirXML(context.openFileOutput(FICHERO, Context.MODE_PRIVATE));
		}catch (Exception e){
			Log.e("Asteroides", e.getMessage(), e);
		}
	}

	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {
		try {
			if (!cargadaLista) {
				leerXML(context.openFileInput(FICHERO));
			}
		} catch (Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}

		return lista;
	}

	private void leerXML(InputStream entrada) throws Exception {
		SAXParserFactory fabrica = SAXParserFactory.newInstance();
		SAXParser parser = fabrica.newSAXParser();
		XMLReader lector = parser.getXMLReader();
		ManejadorXML manejadorXML = new ManejadorXML();
		lector.setContentHandler(manejadorXML);
		lector.parse(new InputSource(entrada));
		cargadaLista = true;
	}

	private void escribirXML(OutputStream salida) {
		XmlSerializer serializador = Xml.newSerializer();
		try {
			serializador.setOutput(salida, "UTF-8");
			serializador.startDocument("UTF-8", true);
			serializador.startTag("", "lista_puntuaciones");
			for (Puntuacion puntuacion : lista) {
				serializador.startTag("", "puntuacion");
				serializador.attribute("", "fecha",
						String.valueOf(puntuacion.fecha));
				serializador.startTag("", "nombre");
				serializador.text(puntuacion.nombre);
				serializador.endTag("", "nombre");
				serializador.startTag("", "puntos");
				serializador.text(String.valueOf(puntuacion.puntos));
				serializador.endTag("", "puntos");
				serializador.endTag("", "puntuacion");
			}
			serializador.endTag("", "lista_puntuaciones");
			serializador.endDocument();
		} catch (Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}
	}

	class ManejadorXML extends DefaultHandler {
		private StringBuilder cadena;
		private Puntuacion puntuacion;

		@Override
		public void startDocument() throws SAXException {
			lista = new Vector<Puntuacion>();
			cadena = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String nombreLocal,
				String nombreCualif, Attributes atr) throws SAXException {
			cadena.setLength(0);
			if (nombreLocal.equals("puntuacion")) {
				puntuacion = new Puntuacion();
				puntuacion.fecha = String.valueOf(atr.getValue("fecha"));
			}
		}

		@Override
		public void characters(char[] ch, int comienzo, int lon)
				throws SAXException {
			cadena.append(ch, comienzo, lon);
		}

		@Override
		public void endElement(String uri, String nombreLocal,
				String nombreCualif) throws SAXException {
			if (nombreLocal.equals("puntos")) {
				puntuacion.puntos = Integer.parseInt(cadena.toString());
			} else if (nombreLocal.equals("nombre")) {
				puntuacion.nombre = cadena.toString();
			} else if (nombreLocal.equals("puntuacion")) {
				lista.add(puntuacion);
			}
		}

		@Override
		public void endDocument() throws SAXException {

		}
	}

	public void guardarPuntuacion(int puntos, String nombre, long milis) {
		guardarPuntuacion(puntos, nombre,
				AlmacenPuntuacionesArray.getDateFromMilis(milis));

	}
}
