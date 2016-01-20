/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.util.ArrayList;
import java.util.Iterator;
  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.xpath.XPathConstants;  
import javax.xml.xpath.XPathExpressionException;  
import javax.xml.xpath.XPathFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException; 
/**
 *
 * @author Henry
 */
public class Datos {
    ArrayList<Profesor>profesores;
    ArrayList<Materia>materias;
    ArrayList<Aula>aulas;
    ArrayList<Clase>clases;
    

    public Datos() {
        profesores= new ArrayList();
        materias= new ArrayList();
        aulas= new ArrayList();
        clases= new ArrayList();
    }
    
    public void cargarProfesores() throws ParserConfigurationException, SAXException, IOException{          
         // Implementación DOM por defecto de Java  
         // Construimos nuestro DocumentBuilder  
         DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
         // Procesamos el fichero XML y obtenemos nuestro objeto Document  
         Document doc = documentBuilder.parse(new InputSource(new FileInputStream("profesores.xml")));  
           
        
           
         // Buscamos una etiqueta dentro del XML  
         
        NodeList lista_profesores = doc.getElementsByTagName("Profesor");  
        ArrayList<String>lista_contenido=new ArrayList<String>();
        for(int i=0;i<lista_profesores.getLength();i++){  
            Node profesor = lista_profesores.item(i);
            NodeList informacion=profesor.getChildNodes();
            int id= Integer.parseInt(profesor.getAttributes().getNamedItem("id").getTextContent());
            
            
                
            for(int j=0;j<informacion.getLength();j++){
                Node contenido= informacion.item(j);
                String nombre=contenido.getTextContent();
                lista_contenido.add(nombre);    
            }
            Profesor pro= new Profesor(id,lista_contenido.get(1));
            profesores.add(pro);
            lista_contenido.clear();
        }
        Iterator<Profesor> nIterator= profesores.iterator();
        while(nIterator.hasNext()){
            Profesor elemento=nIterator.next();
            System.out.println("ID:"+elemento.getid()+"Nombre:"+elemento.getNombre());
        }
        
    }
    
    public void cargarMaterias() throws ParserConfigurationException, SAXException, IOException{          
         // Implementación DOM por defecto de Java  
         // Construimos nuestro DocumentBuilder  
         DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
         // Procesamos el fichero XML y obtenemos nuestro objeto Document  
         Document doc = documentBuilder.parse(new InputSource(new FileInputStream("materias.xml")));  
           
        
           
         // Buscamos una etiqueta dentro del XML  
         
        NodeList lista_materias = doc.getElementsByTagName("Materia");
        
        ArrayList<String>lista_contenido=new ArrayList<String>();
        for(int i=0;i<lista_materias.getLength();i++){  
            Node materia = lista_materias.item(i);
            NodeList informacion=materia.getChildNodes();
            int id= Integer.parseInt(materia.getAttributes().getNamedItem("id").getTextContent());
            
            
                
            for(int j=0;j<informacion.getLength();j++){
                Node contenido= informacion.item(j);
                String dato=contenido.getTextContent();
                System.out.println(dato);
                lista_contenido.add(dato);    
            }
            String nombre=lista_contenido.get(1);
            System.out.println(nombre);
           
            int cupo=Integer.parseInt(lista_contenido.get(3));
            int paralelo=Integer.parseInt(lista_contenido.get(5));
            int semestre=Integer.parseInt(lista_contenido.get(7));
            int horas=Integer.parseInt(lista_contenido.get(9));
            
            Materia mat= new Materia(id,nombre,cupo,paralelo,semestre,horas);
            materias.add(mat);
            lista_contenido.clear();
            
        }
        Iterator<Materia> nIterator= materias.iterator();
        while(nIterator.hasNext()){
            Materia elemento=nIterator.next();
            System.out.println("ID:"+elemento.getId()+"Nombre:"+elemento.getNombre()+"Cupo:"+elemento.getCupo());
        }
        
    }
    public void cargarAulas() throws ParserConfigurationException, SAXException, IOException{          
         // Implementación DOM por defecto de Java  
         // Construimos nuestro DocumentBuilder  
         DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
         // Procesamos el fichero XML y obtenemos nuestro objeto Document  
         Document doc = documentBuilder.parse(new InputSource(new FileInputStream("aulas.xml")));  
           
        
           
         // Buscamos una etiqueta dentro del XML  
         
        NodeList lista_aulas = doc.getElementsByTagName("Aula");
        
        ArrayList<String>lista_contenido=new ArrayList<String>();
        for(int i=0;i<lista_aulas.getLength();i++){  
            Node aula = lista_aulas.item(i);
            NodeList informacion=aula.getChildNodes();
            int id= Integer.parseInt(aula.getAttributes().getNamedItem("id").getTextContent());
            
            
                
            for(int j=0;j<informacion.getLength();j++){
                Node contenido= informacion.item(j);
                String dato=contenido.getTextContent();
                
                lista_contenido.add(dato);    
            }
            String nombre=lista_contenido.get(1);
            
            int capacidad=Integer.parseInt(lista_contenido.get(3));
            boolean laboratorio=Boolean.parseBoolean(lista_contenido.get(5));
            
            Aula aul= new Aula(id,nombre,capacidad,laboratorio);
            aulas.add(aul);
            lista_contenido.clear();
            
        }
        Iterator<Aula> nIterator= aulas.iterator();
        while(nIterator.hasNext()){
            Aula elemento=nIterator.next();
            System.out.println("ID:"+elemento.getId()+"Nombre:"+elemento.getNombre()+"Capacidad:"+elemento.getCapacidad());
        }
    }
    public void cargarClases() throws ParserConfigurationException, SAXException, IOException{          
         // Implementación DOM por defecto de Java  
         // Construimos nuestro DocumentBuilder  
         DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
         // Procesamos el fichero XML y obtenemos nuestro objeto Document  
         Document doc = documentBuilder.parse(new InputSource(new FileInputStream("clases.xml")));  
           
        
           
         // Buscamos una etiqueta dentro del XML  
         
        NodeList lista_clases = doc.getElementsByTagName("Clase");
        
        ArrayList<String>lista_contenido=new ArrayList<String>();
        for(int i=0;i<lista_clases.getLength();i++){  
            Node clase = lista_clases.item(i);
            NodeList informacion=clase.getChildNodes();
            int id= Integer.parseInt(clase.getAttributes().getNamedItem("id").getTextContent());
            
            
                
            for(int j=0;j<informacion.getLength();j++){
                Node contenido= informacion.item(j);
                String dato=contenido.getTextContent();
                
                lista_contenido.add(dato);    
            }
            
            int duracion=Integer.parseInt(lista_contenido.get(1));
            int materia=Integer.parseInt(lista_contenido.get(3));
            int profesor=Integer.parseInt(lista_contenido.get(5));
            boolean laboratorio=Boolean.parseBoolean(lista_contenido.get(7));
            Materia ma=materias.get(materia-1);
            Profesor prof=profesores.get(profesor-1);
            Clase clas= new Clase(id,duracion,ma,prof,laboratorio);
            clases.add(clas);
            lista_contenido.clear();
            
        }
        Iterator<Clase> nIterator= clases.iterator();
        while(nIterator.hasNext()){
            Clase elemento=nIterator.next();
            System.out.println("ID:"+elemento.getId()+"Duracion:"+elemento.getDuracion()+"Materia:"+elemento.getMateria().getId()+"Profesor:"+elemento.getProfesor().getid());
        }
    }
}  
