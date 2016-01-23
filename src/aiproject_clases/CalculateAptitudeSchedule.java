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

    public void setDatos(Datos d) {
        this.d = d;
    }
    
    public void setHorasSemanas(double HORAS_SEMANA_AULA){
        this.HORAS_SEMANA_AULA=HORAS_SEMANA_AULA;
    }    

    @Override
    protected double evaluate(IChromosome ic) {
        double puntuacionCromosoma=0;
         Clase claseActual=null;
        
        int tamChromosome=ic.size();
        
		/*for (int i = 0; i < boxes.length; i++) {
			int index = (Integer) a_subject.getGene(i).getAllele();
			if ((sizeInVan + this.boxes[index].getVolume()) <= vanCapacity) {
				sizeInVan += this.boxes[index].getVolume();
			} else {
				// Compute the difference
				numberOfVansNeeded++;
				wastedVolume += Math.abs(vanCapacity - sizeInVan);
				// Make sure we put the box which did not fit in this van in the next van
				sizeInVan = this.boxes[index].getVolume();
			}
		}*/
                /*
                int index=1679;
                int aula=index/60;
                int dia=(index%60)/12;
                int hora=(index%60)%12;
                
                */
                
                for(int i=0;i<tamChromosome;i++){
                    int clase = (Integer) ic.getGene(i).getAllele();
                    
                    if(clase!=0){
                        Iterator<Clase> nIterator= d.getClases().iterator();
                        while(nIterator.hasNext()){
                            Clase elemento=nIterator.next();
                            if(elemento.getId()==clase){
                                claseActual=elemento;
                                puntuacionCromosoma+=verificarDuracion(ic,i,claseActual);
                                break;
                            }
                        }
                    }
                    
                    
                    
                }
                
		// Take into account the number of vans needed. More vans produce a higher fitness value.
		//return wastedVolume * numberOfVansNeeded;
                return puntuacionCromosoma;
                        
        
    }
    
    public int verificarDuracion(IChromosome ic,int index,Clase claseactual){
        for(int i=1;i<claseactual.getDuracion();i++){
            if((Integer)ic.getGene(index+i).getAllele()!=0){
                return 0;
            }
        }
        return 1;        
    }
    
    
}
