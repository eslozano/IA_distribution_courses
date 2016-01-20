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
public class Aula {
    private int id;
    private final String nombre;
    private final int capacidad;
    private final Boolean lab;
    
    public Aula(int id,String nombre,int capacidad,Boolean lab){
        this.id=id;
        this.nombre=nombre;
        this.capacidad=capacidad;
        this.lab=lab;
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
    
  
}
