/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

/**
 *
 * @author estef
 */
public class Clase {
    private int id;
    private final int duracion;
    private final Materia materia;
    private final Profesor profesor;
    private final Boolean lab;
    
    public Clase(int id,int durac, Materia mat,Profesor prof, Boolean lab ) {
        this.id=id;
        this.duracion=durac;
        this.materia = mat;
        this.profesor=prof;
        this.lab=lab;        
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public double getDuracion() {
        return this.duracion;
    }
    
    public Profesor getProfesor(){
        return this.profesor;
    }
    
    public Materia getMateria(){
        return this.materia;
    }
    
    public Boolean getLab(){
        return this.lab;
    }
    
    @Override
    public String toString() {
        return "Clase:" + this.getId() + ","
                + " Materia:" + this.materia.getId() + ","
                + " Profesor:" + this.profesor.getId() + ","
                + " Laboratorio:" + this.lab + ","
                + " Duracion :" + this.duracion + " horas.";
    }
    
}
