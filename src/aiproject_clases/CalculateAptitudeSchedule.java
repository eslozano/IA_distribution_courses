/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import java.util.Iterator;
import org.jgap.FitnessFunction;
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
        int tamChromosome=ic.size();
        /*
        int index=1679;
        int aula=index/60;
        int dia=(index%60)/12;
        int hora=(index%60)%12;

        */
        for(int i=0;i<tamChromosome;i++){
            if((Integer) ic.getGene(i).getAllele()!=0){
                Iterator<Clase> nIterator= d.getClases().iterator();
                while(nIterator.hasNext()){
                    Clase claseActual=nIterator.next();
                    if(claseActual.getId()==(Integer) ic.getGene(i).getAllele()){
                        
                        puntuacionCromosoma+=(verificarDuracion(ic,i,claseActual)+
                                verificarMateriaDia(ic,i,claseActual)+
                                verificarProfesorHora(ic,i,claseActual));
                        break;
                    }
                }
            }
        }

        //Como se implementarion 7 validaciones(puntos-score) posibles. Se tiene que multiplicar el numero de clases *7
        fitnnessValueChromosoma= puntuacionCromosoma/(2*(d.getClases().size()));
        // Take into account the number of vans needed. More vans produce a higher fitness value.
        //return wastedVolume * numberOfVansNeeded;
        return fitnnessValueChromosoma;

        
    }
    
    /*
    Funcion: verificarDuracion
    Retorna: int
    Descripcion: Esta funcion verifica si en la duración de la clase es decir por ejemplo tres horas no hay ninguna
    otra clase asignada. Si no hay ninguna clase es decir es algun gen diferente de cero retorna 0 sino retorna 1
    */
    public int verificarDuracion(IChromosome ic,int index,Clase claseactual){
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }
        for(int i=1;i<claseactual.getDuracion();i++){
            if((Integer)ic.getGene(index+i).getAllele()!=0){
                return 0;
            }
        }
        return 1;        
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
            int posicionDia=(diaActual*(int)HORAS_DIA)+indexAula;
            for(int hora=0;hora<HORAS_DIA;hora++){
                 //obtengo el alelo de ese aula,dia y hora
                int id=(Integer)ic.getGene(posicionDia+hora).getAllele();
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
        
        return 1;
        
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
        
        /*
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }*/
        
        for(int aula=0;aula<(d.getAulas().size()+HORAS_SEMANA_AULA);aula++){
            
            //Tengo que encontrar la posicion del dia a comparar en todas las aulas
            //Ejemplo diaActual=3;HORAS_DIA=12;aula=0; entonces posicionDia=3*12+0=36 (indice en el cromosoma)
            int posicionDia=(diaActual*(int)HORAS_DIA)+aula;
            
            for(int hora=0;hora<HORAS_DIA;hora++){
                //obtengo el alelo de ese dia, aula y hora
                int id=(Integer)ic.getGene(posicionDia+hora).getAllele();
                //si encuentro un id en el gen y ese gen es diferente al id de la clase actual entro
                if(id!=0 && id!=claseactual.getId()){
                    Iterator<Clase> nIterator= d.getClases().iterator();
                    while(nIterator.hasNext()){
                       Clase elemento=nIterator.next();
                       //Cuando encuentro la clase que busco con el id del allelo
                       if(elemento.getId()==id){
                           //si el id de la materia de la clase es el mismo id de la materia de la clase actual retorno cero
                           //Quiere decir que en un mismo dia hay dos clases que estan dictando la misma materia
                           if(elemento.getMateria().getId()==claseactual.getMateria().getId()){
                                return 0;   
                           }
                       }
                    }
                }  
            }       
        }        
        return 1;
        
    }
    public int verificarAula(int index,Clase claseactual){
        int aula=index/60;
        String laboratorio="0";
        if (aula>25){
            laboratorio="1";
        }
        if(claseactual.getLab() == Boolean.parseBoolean(laboratorio)){
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
        int numero_espacios=0;
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }
        
        return 1;
    }
    
     
}
