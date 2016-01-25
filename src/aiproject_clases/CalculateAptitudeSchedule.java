/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import java.util.ArrayList;
import java.util.Iterator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 *
 * @author estef
 */
public class CalculateAptitudeSchedule extends FitnessFunction{
    
    private Datos d;
    private double HORAS_SEMANA_AULA;
    private double DIAS_SEMANA;
    private double HORAS_DIA;
    private double TAM_CROMOSOME;

    public void setDatos(Datos d) {
        this.d = d;
    }
    
    public void setHorasSemanas(double HORAS_SEMANA_AULA){
        this.HORAS_SEMANA_AULA=HORAS_SEMANA_AULA;
    }    

    public void setdiasSemana(double DIAS_SEMANA) {
        this.DIAS_SEMANA = DIAS_SEMANA;
    }

    public void setHorasDia(double HORAS_DIA) {
        this.HORAS_DIA = HORAS_DIA;
    }
    
    public void setTamCrom(double TAM_CROMOSOME) {
        this.TAM_CROMOSOME = TAM_CROMOSOME;
    }

    @Override
    protected double evaluate(IChromosome ic) {
        double puntuacionCromosoma=0;
        double fitnnessValueChromosoma=0;
        /*
        int index=1679;int aula=index/60;int dia=(index%60)/12;int hora=(index%60)%12;
        */
        int index = 0;
        Gene[] genes = ic.getGenes();
        for (Gene gene : genes) {
            int idClase = (Integer) gene.getAllele();
            if(idClase!=0){
                Clase claseActual=d.getClase(idClase);
                if(claseActual!=null){
                    puntuacionCromosoma+=(verificarClaseNoRepetida(ic,index,claseActual));
                    puntuacionCromosoma+=(verificarDuracion(ic,index,claseActual));
                } 
            }
            index++;            
        }
        //Como se implementarion 7 validaciones(puntos-score) posibles. Se tiene que multiplicar el numero de clases *7
        fitnnessValueChromosoma= puntuacionCromosoma/((d.getClases().size()*4)+(d.getClases().size()*3));
        return fitnnessValueChromosoma;
    }
    
    /*
    Funcion: verificarDuracion
    Retorna: int
    Descripcion: Esta funcion verifica si en la duración de la clase es decir por ejemplo tres horas no hay ninguna
    otra clase asignada. Si no hay ninguna clase es decir es algun gen diferente de cero retorna 0 sino retorna 3
    */
    public int verificarDuracion(IChromosome ic,int index,Clase claseactual){
        if(index+(claseactual.getDuracion())>TAM_CROMOSOME){
            return 0;
        }
        for(int i=1;i<claseactual.getDuracion();i++){
            if((Integer)ic.getGene(index+i).getAllele()!=0){
                return 0;
            }
        }
        return 3;        
    }
    /*
    Funcion: verificarClaseNoRepetida
    Retorna: int
    Descripcion: Esta funcion verifica si una clase no se encuentra repetida en el cromosoma, si no esta repetida retorna 2
    si no retorna 0
    */
    public int verificarClaseNoRepetida(IChromosome ic,int index,Clase claseactual){
        for(int i=index+1;i<TAM_CROMOSOME;i++){
            int id=(Integer)ic.getGene(i).getAllele();
            if(id!=0){
                Clase claseEncontrada=d.getClase(id);
                if(claseEncontrada!=null){
                    if(claseEncontrada.getId()==claseactual.getId()){
                        return 0;
                    }
                }
            }
        }
        return 4;        
    }
    /*
    Funcion: verificarMateriaDia
    Retorna: int
    Descripcion: Esta funcion verifica si dos clases de una misma materia no son dictadas el mismo dia en cualquier aula,
    si son dictadas el mismo dia retorna 0, sino retorna 1
    */
    public int verificarMateriaDia(IChromosome ic,int index,Clase claseactual){
        //int aulaActual=index/(int)HORAS_SEMANA_AULA;
        int diaActual=(index%(int)HORAS_SEMANA_AULA)/(int)HORAS_DIA;
        
        int indexAula=0;
        do{
            //Tengo que encontrar la posicion del dia a comparar en todas las aulas
            //Ejemplo diaActual=3;HORAS_DIA=12;indexAula=180(aula3); entonces posicionDia=3*12+180= 216(indice en el cromosoma)
            int indexDia=(diaActual*(int)HORAS_DIA)+indexAula;
            for(int hora=0;hora<HORAS_DIA;hora++){
                 //obtengo el alelo de ese aula,dia y hora
                int id=(Integer)ic.getGene(indexDia+hora).getAllele();
                //si encuentro un id en el gen y ese gen es diferente al id de la clase actual entro
                if(id!=0 && id!=claseactual.getId()){
                    //Recorrer la lista de clases para poder obtener la materia
                    Iterator<Clase> nIterator= d.getClases().iterator();
                    while(nIterator.hasNext()){
                       Clase c=nIterator.next();
                       //Cuando encuentro la clase que busco con el id del allelo
                       if(c.getId()==id){
                           //si el id de la materia de la clase es el mismo id de la materia de la clase actual retorno cero
                           //Quiere decir que en un mismo dia hay dos clases que estan dictando la misma materia
                           if(c.getMateria().getId()==claseactual.getMateria().getId()){
                                return 0;   
                           }
                           
                       }
                    }
                }
            
            }
            indexAula+=HORAS_SEMANA_AULA;
        }while(indexAula<(d.getAulas().size()*HORAS_SEMANA_AULA));
        
        return 2;
        
    }
    
    /*
    Funcion: verificarProfesorHora
    Retorna: int
    Descripcion: Esta funcion verifica si un profesor tiene dos clases el mismo dia a la misma hora,
    si tiene dos clases a la misma hora el mismo dia retorna 0, sino retorna 1
    */
    public int verificarProfesorHora(IChromosome ic,int index,Clase claseactual){
        //int aulaActual=index/(int)HORAS_SEMANA_AULA;
        int diaActual=(index%(int)HORAS_SEMANA_AULA)/(int)HORAS_DIA;
        int horaActual=(index%(int)HORAS_SEMANA_AULA)%(int)HORAS_DIA;
        
        ArrayList<Integer> horasClaseActual=new ArrayList();
        
        for(int i=0;i<claseactual.getDuracion();i++){
            //Tengo que impedir que se salga del tamaño del cromosoma
             if(index<TAM_CROMOSOME){
                 //Esto tiene la poscion de la hora por ejemplo 5,6,7 si es una clase de 3 horas que comienza a la hora 5
                 horasClaseActual.add(horaActual);  
                 horaActual++;                 
                 index++;
             }                
        }
        
        int indexAula=0;
        int indexHora=0;
        do{
            
            int indexDia=(diaActual*(int)HORAS_DIA)+indexAula;
            for (int hora : horasClaseActual) {
                indexHora=indexAula+indexDia+hora;       
                int id=(Integer)ic.getGene(indexHora).getAllele();
                if(id!=0 && id!=claseactual.getId()){
                    
                    Iterator<Clase> nIterator= d.getClases().iterator();
                    while(nIterator.hasNext()){
                       Clase c=nIterator.next();
                       //Cuando encuentro la clase que busco con el id del allelo
                       if(c.getId()==id){
                           //si el id de la materia de la clase es el mismo id de la materia de la clase actual retorno cero
                           //Quiere decir que en un mismo dia hay dos clases que estan dictando la misma materia
                           if(c.getProfesor().getId()==claseactual.getProfesor().getId()){
                                return 0;   
                           }
                       }
                    }   
                }
                else{
                    
                }
                
            }
            
            indexAula+=HORAS_SEMANA_AULA;
        }while(indexAula<(d.getAulas().size()*HORAS_SEMANA_AULA));
       
        return 1;
        
    }
    public int verificarAula(int index,Clase claseactual){
        int aula=index/60;
        if(claseactual.getLab() && d.getAulas().get(aula).getLab()){
            return 1;
        }else if (!(claseactual.getLab() || d.getAulas().get(aula).getLab())){
            return 1;
        }else{
            return 0;
        }
    }
    public int verificarClaseDia(int index,Clase claseactual){
        int diaPrimeraHora=(index%60)/12;
        
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }
        int diaUltimaHora=(int)((index+(claseactual.getDuracion()))%60)/12;
        
        if(diaPrimeraHora==diaUltimaHora){
            return 1;
        }else{
            return 0;
        }
    }
    
    public int verificarClaseEspacio(IChromosome ic,int index,Clase claseactual){
        int posicion_inicial=(int)(index+claseactual.getDuracion());
        
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }
        if((int)ic.getGene(posicion_inicial).getAllele()!=0){
                return 1;
            }else{
                return 0;
            }
    }
    
}
