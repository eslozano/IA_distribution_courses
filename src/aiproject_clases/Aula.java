/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estef
 */
public class Aula {
    private int id;
    private final String nombre;
    private final int capacidad;
    private final Boolean lab;
    private double horasAsignadas = 0.0D;
    
    private static final double HORAS_DIA = 12;
    private static final double HORAS_SEMANA = 60;
    
    private final ArrayList<Clase> horasSemana ; //Sera un arraylist de 12 * 5 casillas, 12 horas 5 dias
    //private ArrayList horas ;   
    
    public Aula(int id,String nombre,int capacidad,Boolean lab){
        this.id=id;
        this.nombre=nombre;
        this.capacidad=capacidad;
        this.lab=lab;
        
        horasSemana = new ArrayList();
        
        inicializarHorasSemana();
    }
    
    private void inicializarHorasSemana(){
        for(int i=0;i<HORAS_SEMANA;i++){
            horasSemana.add(null);
        }
    }
    
    public boolean addClass(Clase clase,int posicion){
        int dia=posicion/(int)HORAS_DIA;
        //int hora=posicion%(int)HORAS_DIA;
        
        //60 son las horas maximas que pueden ser asignadas a un aula
         if (this.horasAsignadas + clase.getDuracion() > HORAS_SEMANA) {
            return false;
        }
        if(horasSemana.get(posicion)!=null){
            return false;
        }
        for(int i=0;i<clase.getDuracion();i++){
            if(horasSemana.get(i+posicion)!=null){
                return false;
            }
        }
        int diaDuracion=(int)(posicion+clase.getDuracion())/(int)HORAS_DIA;
        if(dia!=diaDuracion){
            return false;
        }
        for(int i=0;i<clase.getDuracion();i++){
            horasSemana.add(posicion+i,clase);
        }
        this.horasAsignadas += clase.getDuracion();
        
        return true;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public Boolean getLab() {
        return lab;
    }
    
    @Override
    public String toString() {
        return "ID:" + this.id + " Nombre:"+this.nombre
                + " Capacidad:"+this.capacidad
                + " Lab:"+this.lab;
        //return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
  
}
