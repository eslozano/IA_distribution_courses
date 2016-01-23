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
    
    public int getId(){
        return id;
    }
    
    public String getNombre(){
        return nombre;
    }  

    @Override
    public String toString() {
        return "ID:" + this.id + " Nombre:"+this.nombre;
        //return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
