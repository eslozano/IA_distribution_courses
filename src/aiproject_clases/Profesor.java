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
public class Profesor {
    private int id;
    private String nombre;
    
    public Profesor(int id,String nombre){
        this.id=id;
        this.nombre=nombre;
    }
    
    public int getid(){
        return id;
    }
    
    public String getNombre(){
        return nombre;
    }  
    
}
