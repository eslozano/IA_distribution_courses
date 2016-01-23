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
public class Materia {
    private final int id;
    private final String nombre;
    private final int cupo;
    private final int paralelo;
    private final int semestre;
    private final int horasSemanales;    
    
    public Materia(int id, String nomb, int cupo, int paral,int semes,int horasSem){
        this.id=id;
        this.nombre=nomb;
        this.cupo=cupo;
        this.paralelo=paral;
        this.semestre=semes;
        this.horasSemanales=horasSem;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public int getCupo(){
        return this.cupo;
    }
    
    public int getParalelo(){
        return this.paralelo;
    }
    
    public int getSemestre(){
        return this.semestre;
    }
    
    public int getHorasSemanales(){
        return this.horasSemanales;
    }
      
    @Override
    public String toString() {
        return "ID:" + this.id + " Nombre:"+this.nombre + ","
                +" Cupo:" + this.cupo+","
                +" Paralelo:" + this.paralelo+","
                +" Semestre:" + this.semestre+","
                +" Horas_Semanales:" + this.horasSemanales;
        
    }
    
}
