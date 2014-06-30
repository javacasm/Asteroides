package org.example.asteroides;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.content.Context;
import android.util.Log;

	public class AlmacenPuntuacionesXML_DOM implements AlmacenPuntuaciones {
		private static String FICHERO = "puntuaciones.xml";
		private Context contexto;
		private Document documento;
		private boolean cargadoDocumento;
		Vector<Puntuacion> lista;

	public AlmacenPuntuacionesXML_DOM(Context contexto) {
			this.contexto = contexto;
			lista = new Vector<Puntuacion>();
			cargadoDocumento = false;
		}
	public void guardarPuntuacion(int puntos, String nombre, String fecha) {
		
		try {
			if (!cargadoDocumento){
				leerXML (contexto.openFileInput (FICHERO));
			}
		} catch (FileNotFoundException e) {
			crearXML();
		} catch (Exception e){
			Log.e("Asteroides", e.getMessage(), e);
		}
		nuevo(puntos, nombre, fecha);
		try {
			escribirXML(contexto.openFileOutput(FICHERO, Context.MODE_PRIVATE));
		} catch (Exception e) {
			Log.e ("Asteroides", e.getMessage(), e);
		}

	}
	
	public Vector<Puntuacion> listaPuntuaciones(int cantidad) {

		try {
			if (!cargadoDocumento) {
				leerXML(contexto.openFileInput(FICHERO));
			}
		} catch (FileNotFoundException e) {
			Log.e("Asteroides", e.getMessage(), e);
		} catch (Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}

		return aVectorPuntuacion();

	}
	
	
		
	public Vector<Puntuacion> aVectorPuntuacion() {
		
		Vector<Puntuacion> result = new Vector<Puntuacion>();
		String nombre = "";
		int puntos = 0;
		String fecha = "";
		Element raiz = documento.getDocumentElement();
		NodeList puntuaciones = raiz.getElementsByTagName("puntuacion");
		for (int i = 0; i < puntuaciones.getLength(); i++) {
			
			Node puntuacion = puntuaciones.item(i);
			NodeList propiedades = puntuacion.getChildNodes();
			for (int j = 0; j < propiedades.getLength(); j++) {
				
				Node propiedad = propiedades.item(j);
				String etiqueta = propiedad.getNodeName();
				if( etiqueta.equals("nombre")){
					nombre = propiedad.getFirstChild().getNodeValue();
				}else if (etiqueta.equals("puntos")){
					puntos = Integer.valueOf(propiedad.getFirstChild().getNodeValue());
				}else if (etiqueta.equals("fecha")){
					fecha = propiedad.getFirstChild().getNodeValue();
				}
			}
			Puntuacion p = new Puntuacion(nombre, puntos, fecha);
			result.add(p);
		}
	
		return result;
	}
	
	private void crearXML() {
		try {
			DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructor = fabrica.newDocumentBuilder();
			documento = constructor.newDocument();
			Element raiz =  documento.createElement("lista_puntuaciones");
			documento.appendChild(raiz);
			cargadoDocumento = true;
		} catch (Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}
		
	}
	
	private void leerXML(InputStream entrada) throws Exception {
		
		DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = fabrica.newDocumentBuilder();
		documento = constructor.parse(entrada);
		cargadoDocumento = true;
	}
	
	
	private void nuevo(int puntos, String nombre, String fecha) {
		Element puntuacion = documento.createElement("puntuacion");
		puntuacion.setAttribute("fecha", fecha);
		Element e_nombre = documento.createElement("nombre");
		Text texto = documento.createTextNode(nombre);
		e_nombre.appendChild(texto);
		puntuacion.appendChild(e_nombre);
		Element e_puntos = documento.createElement("puntos");
		texto = documento.createTextNode(String.valueOf(puntos));
		e_puntos.appendChild(texto);
		puntuacion.appendChild(e_puntos);
		Element raiz = documento.getDocumentElement();
		raiz.appendChild(puntuacion);
		
	}
	
	
	private void escribirXML(OutputStream salida) throws Exception {
		
		TransformerFactory fabrica = TransformerFactory.newInstance();
		Transformer transformador = fabrica.newTransformer();
		transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformador.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource fuente = new DOMSource(documento);
		StreamResult resultado = new StreamResult(salida);
		transformador.transform(fuente, resultado);
		
	}
	
	
	
	
	
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {
		// TODO Auto-generated method stub
		
	}

}