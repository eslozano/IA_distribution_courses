/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    
    Map<Clase,Integer>nivel2= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel3= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel4= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel5= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel6= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel7= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel8= new HashMap<Clase,Integer>();
    Map<Clase,Integer>nivel9= new HashMap<Clase,Integer>();
    Map<Clase,Integer>Mapclases= new HashMap<Clase,Integer>();
        
    
    Boolean[] clasesEncontradas;
    
    

    public void setDatos(Datos d) {
        this.d = d;
        clasesEncontradas= new Boolean[this.d.getClases().size()];
        for(int i=0; i<clasesEncontradas.length; i++){
            clasesEncontradas[i]=false;
        }
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
                    clasesEncontradas[idClase-1]=true;
                    puntuacionCromosoma+=(verificarClaseNoRepetida(ic,index,claseActual));//4
                    puntuacionCromosoma+=(verificarDuracion(ic,index,claseActual));//4
                    puntuacionCromosoma+=(verificarClaseDia(index,claseActual));//3
                    puntuacionCromosoma+=(verificarMateriaDia(ic,index,claseActual));//3
                } 
            }
            index++;            
        }
        puntuacionCromosoma+=(verificarExistenTodasLasClases());//4
        for(int i=0; i<clasesEncontradas.length; i++){
            clasesEncontradas[i]=false;
        }
        
        fitnnessValueChromosoma= puntuacionCromosoma/((d.getClases().size()*12)+(d.getClases().size()*6));
        return fitnnessValueChromosoma;
    }
    /*
    Funcion: verificarClaseNoRepetida
    Retorna: int
    Descripcion: Esta funcion verifica si una clase no se encuentra repetida en el cromosoma, si no esta repetida retorna 4
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
        return 4;        
    }
    /*
    Funcion: verificarExistenTodasLasClases
    Retorna: int
    Descripcion: Esta funcion verifica que todas las clases existan en el cromosoma, retorna como maximo valor 4*numero_de_clases
    si la clase existe es 4, sino es 0
    */
    public int verificarExistenTodasLasClases(){
        int acumulador=0;
        for(int i=0; i<clasesEncontradas.length; i++){
            if(clasesEncontradas[i]==true){
                acumulador+=4;
            }
        }
        return acumulador;        
    }
    /*
    Funcion: verificarClaseDia
    Retorna: int
    Descripcion: Esta funcion verifica que el dia de la ultima hora de la clase no sea diferente al dia de la primera hora de la clase.
    Asegurandonos que una clase no se pueda dividir en dos dias. Retorna 3 si la clase no esta dividida, 0 si lo está
    */
    public int verificarClaseDia(int index,Clase claseactual){
        if(index+claseactual.getDuracion()>TAM_CROMOSOME){
            return 0;
        }
        int diaPrimeraHora=(index%60)/12;
        int diaUltimaHora=(int)((index+claseactual.getDuracion())%60)/12;
        if(diaPrimeraHora!=diaUltimaHora){
            return 0;
        }
        return 3;
    }
    /*
    Funcion: verificarMateriaDia
    Retorna: int
    Descripcion: Esta funcion verifica si dos clases de una misma materia no son dictadas el mismo dia en cualquier aula,
    si son dictadas el mismo dia retorna 0, sino retorna 2
    */
    public int verificarMateriaDia(IChromosome ic,int index,Clase claseactual){
      
        
        int aulaActual=(index/(int)HORAS_SEMANA_AULA);
        int indexAula=aulaActual*60;
        int diaActual=(index%(int)HORAS_SEMANA_AULA)/(int)HORAS_DIA;
        int horaActual=(index%(int)HORAS_SEMANA_AULA)%(int)HORAS_DIA;
        do{
            //Tengo que encontrar la posicion del dia a comparar en todas las aulas
            //Ejemplo diaActual=3;HORAS_DIA=12;indexAula=180(aula3); entonces posicionDia=3*12+180= 216(indice en el cromosoma)
            int indexDiaComparacion=indexAula+(diaActual*(int)HORAS_DIA);
            for(int hora=(horaActual+1);hora<HORAS_DIA;hora++){
                 //obtengo el alelo de ese aula,dia y hora
                int id=(Integer)ic.getGene(indexDiaComparacion+hora).getAllele();
                //si encuentro un id en el gen y ese gen es diferente al id de la clase actual entro
                if(id!=0 && id!=claseactual.getId()){
                    //Recorrer la lista de clases para poder obtener la materia
                    Clase clase=d.getClase(id);
                    if (clase!=null){
                        //si el id de la materia de la clase es el mismo id de la materia de la clase actual retorno cero
                        //Quiere decir que en un mismo dia hay dos clases que estan dictando la misma materia
                        if(clase.getMateria().getId()==claseactual.getMateria().getId()){
                             return 0;   
                        }
                    }
                }
            }
            indexAula+=HORAS_SEMANA_AULA;
        }while(indexAula<(d.getAulas().size()*HORAS_SEMANA_AULA));
        return 3;
        
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
    
    public void LlenarMapaClasesNiveles(IChromosome ic){
        for(int i=0;i<TAM_CROMOSOME;i++){
            if((Integer) ic.getGene(i).getAllele()!=0){
                Iterator<Clase> nIterator= d.getClases().iterator();
                while(nIterator.hasNext()){
                    Clase claseActual=nIterator.next();
                    if(claseActual.getId()==(Integer) ic.getGene(i).getAllele()){
                        if(claseActual.getMateria().getSemestre()==2){
                            nivel2.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==3){
                            nivel3.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==4){
                            nivel4.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==5){
                            nivel5.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==6){
                            nivel6.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==7){
                            nivel7.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==8){
                            nivel8.put(claseActual, i);
                        }if(claseActual.getMateria().getSemestre()==9){
                            nivel9.put(claseActual, i);
                        }
                    }
                }
            }
        }
    }
    
    public int recorrerMapaSemestre(Map mapa){
        int hora1=-1,hora2=-1,hora3=-1;
        int hora1temp=-1,hora2temp=-1,hora3temp=-1;
        int cruces=0;
        Iterator<Clase> iterador = mapa.keySet().iterator();
        Iterator<Clase> iterador2 = mapa.keySet().iterator();
        while(iterador.hasNext()){
            Clase cla = iterador.next();
            if(cla.getDuracion()==2){
                hora1=(int) mapa.get(cla);
                hora2=(int)mapa.get(cla)+1;
            }
            if(cla.getDuracion()==3){
                hora1=(int) mapa.get(cla);
                hora2=(int)mapa.get(cla)+1;
                hora3=(int)mapa.get(cla)+2;
                
            }
            while(iterador2.hasNext()){
                Clase cla2 = iterador2.next();
                if(cla2.getId()!=cla.getId()){
                    
                    if(cla2.getDuracion()==2 && cla.getDuracion()==2){
                        hora1temp=(int) mapa.get(cla2);
                        hora2temp=(int)mapa.get(cla2)+1;
                        
                        if(hora1==hora1temp || hora1==hora2temp){
                            cruces+=1;
                        }
                        if(hora2==hora1temp || hora2==hora2temp){
                            cruces+=1;
                        }
                    }
                    if(cla2.getDuracion()==3 && cla.getDuracion()==3){
                        hora1temp=(int) mapa.get(cla2);
                        hora2temp=(int)mapa.get(cla2)+1;
                        hora3temp=(int)mapa.get(cla2)+2;
         
                        if(hora1==hora1temp || hora1==hora2temp || hora1==hora3temp){
                            cruces+=1;
                        }
                        if(hora2==hora1temp || hora2==hora2temp || hora2==hora3temp){
                            cruces+=1;
                        }
                        if(hora3==hora1temp || hora3==hora2temp || hora3==hora3temp){
                            cruces+=1;
                        }
                    }
                    
                    if(cla2.getDuracion()==2 && cla.getDuracion()==3){
                        hora1temp=(int) mapa.get(cla2);
                        hora2temp=(int)mapa.get(cla2)+1;
         
                        if(hora1==hora1temp || hora1==hora2temp ){
                            cruces+=1;
                        }
                        if(hora2==hora1temp || hora2==hora2temp ){
                            cruces+=1;
                        }
                        if(hora3==hora1temp || hora3==hora2temp ){
                            cruces+=1;
                        }
                    }
                    if(cla2.getDuracion()==3 && cla.getDuracion()==2){
                        hora1temp=(int) mapa.get(cla2);
                        hora2temp=(int)mapa.get(cla2)+1;
                        hora3temp=(int)mapa.get(cla2)+2;
         
                        if(hora1==hora1temp || hora1==hora2temp || hora1==hora3temp){
                            cruces+=1;
                        }
                        if(hora2==hora1temp || hora2==hora2temp || hora2==hora3temp){
                            cruces+=1;
                        }
                    }
                }
            }   
        }
        return cruces;
    }
    
    public int verificarClasesNiveles(IChromosome ic, Clase claseActual){
        int numero_cruces=0;
        LlenarMapaClasesNiveles(ic);
        
        if (claseActual.getMateria().getSemestre()==2){
            numero_cruces=recorrerMapaSemestre(nivel2);
        }if (claseActual.getMateria().getSemestre()==3){
            numero_cruces=recorrerMapaSemestre(nivel3);
        }if (claseActual.getMateria().getSemestre()==4){
            numero_cruces=recorrerMapaSemestre(nivel4);
        }if (claseActual.getMateria().getSemestre()==5){
            numero_cruces=recorrerMapaSemestre(nivel5);
        }if (claseActual.getMateria().getSemestre()==6){
            numero_cruces=recorrerMapaSemestre(nivel6);
        }if (claseActual.getMateria().getSemestre()==7){
            numero_cruces=recorrerMapaSemestre(nivel7);
        }if (claseActual.getMateria().getSemestre()==8){
            numero_cruces=recorrerMapaSemestre(nivel8);
        }if (claseActual.getMateria().getSemestre()==9){
            numero_cruces=recorrerMapaSemestre(nivel9);
        }
        if(numero_cruces > 0){
            return 0;
        }else{
            return 1;
        } 
    }
    
    public void LlenarMapClases(IChromosome ic){
        
        for(int i=0;i<TAM_CROMOSOME;i++){
            if((Integer) ic.getGene(i).getAllele()!=0){
                Iterator<Clase> nIterator= d.getClases().iterator();
                while(nIterator.hasNext()){
                    Clase claseActual=nIterator.next();
                    if(claseActual.getId()==(Integer) ic.getGene(i).getAllele()){
                        Mapclases.put(claseActual, i);
                    }
                }
            }
        }
    }
    
    public int crucesClaseProfesor(IChromosome ic ,Clase ClaseActual){
        int hora1=-1,hora2=-1,hora3=-1;
        int hora1temp=-1,hora2temp=-1,hora3temp=-1;
        int cruces=0;
        Iterator<Clase> iterador2 = Mapclases.keySet().iterator();
            if(ClaseActual.getDuracion()==2){
                hora1=(int) Mapclases.get(ClaseActual);
                hora2=(int)Mapclases.get(ClaseActual)+1;
            }
            if(ClaseActual.getDuracion()==3){
                hora1=(int) Mapclases.get(ClaseActual);
                hora2=(int)Mapclases.get(ClaseActual)+1;
                hora3=(int)Mapclases.get(ClaseActual)+2;
                
            }
            while(iterador2.hasNext()){
                Clase cla2 = iterador2.next();
                if (ClaseActual.getProfesor().getId()==cla2.getProfesor().getId()){
                    if(cla2.getId()!=ClaseActual.getId()){

                        if(cla2.getDuracion()==2 && ClaseActual.getDuracion()==2){
                            hora1temp=(int) Mapclases.get(cla2);
                            hora2temp=(int)Mapclases.get(cla2)+1;

                            if(hora1==hora1temp || hora1==hora2temp){
                                cruces+=1;
                            }
                            if(hora2==hora1temp || hora2==hora2temp){
                                cruces+=1;
                            }
                        }
                        if(cla2.getDuracion()==3 && ClaseActual.getDuracion()==3){
                            hora1temp=(int) Mapclases.get(cla2);
                            hora2temp=(int)Mapclases.get(cla2)+1;
                            hora3temp=(int)Mapclases.get(cla2)+2;

                            if(hora1==hora1temp || hora1==hora2temp || hora1==hora3temp){
                                cruces+=1;
                            }
                            if(hora2==hora1temp || hora2==hora2temp || hora2==hora3temp){
                                cruces+=1;
                            }
                            if(hora3==hora1temp || hora3==hora2temp || hora3==hora3temp){
                                cruces+=1;
                            }
                        }

                        if(cla2.getDuracion()==2 && ClaseActual.getDuracion()==3){
                            hora1temp=(int) Mapclases.get(cla2);
                            hora2temp=(int)Mapclases.get(cla2)+1;

                            if(hora1==hora1temp || hora1==hora2temp ){
                                cruces+=1;
                            }
                            if(hora2==hora1temp || hora2==hora2temp ){
                                cruces+=1;
                            }
                            if(hora3==hora1temp || hora3==hora2temp ){
                                cruces+=1;
                            }
                        }
                        if(cla2.getDuracion()==3 && ClaseActual.getDuracion()==2){
                            hora1temp=(int) Mapclases.get(cla2);
                            hora2temp=(int)Mapclases.get(cla2)+1;
                            hora3temp=(int)Mapclases.get(cla2)+2;

                            if(hora1==hora1temp || hora1==hora2temp || hora1==hora3temp){
                                cruces+=1;
                            }
                            if(hora2==hora1temp || hora2==hora2temp || hora2==hora3temp){
                                cruces+=1;
                            }
                        }
                    }
                }
            }
        
        return cruces;
    }
    
    public int verificarClaseProfesor(IChromosome ic,Clase claseactual){
        int numero_cruces=0;
        LlenarMapClases(ic);
        
        if(crucesClaseProfesor(ic,claseactual) > 0){
            return 0;
        }else{
            return 1;
        }
    }
    
}
