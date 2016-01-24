/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_genetic_algorithms;

import aiproject_clases.CalculateAptitudeSchedule;
import aiproject_clases.Clase;
import aiproject_clases.Datos;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;

import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;

/**
 *
 * @author estef
 */
public class AIProject_Genetic_Algorithms {
    //private static final Logger LOG = Logger.getLogger(AIProject_Genetic_Algorithms.class);
    private static final int NUMERO_EVOLUCIONES = 5000;
    // El tamaño de la poblaion (numero de cromosomas en el genotipo)    
    private static final int TAMANIO_POBLACION = 25;
    
    // Los dias de la semana, y el total de horas diarias
    private static final double DIAS_SEMANA = 5;
    private static final double HORAS_DIA = 12;
    private static double HORAS_SEMANA_AULA;
    
    //El total de clases usada (leido desde el archivo).
    private static double NUMERO_DE_CLASES;
    //El total de clases usada (leido desde el archivo).
    private static double NUMERO_DE_AULAS;
    //El total de clases usada (leido desde el archivo).
    private static double NUMERO_DE_PROFESORES;
     //El total de clases usada (leido desde el archivo).
    private static double NUMERO_DE_MATERIAS;
    //Num_horas_totales tendrá el numero de aulas * numero de dias de la semana * numero de horas diarias
    private static double TOTAL_HORAS;
    
    //Variable que usamos para cargar los datos de los archivos XML
    private final Datos d;
    //Esta sera la variable del cromosoma
    //private Class[] horario;
    //Sumatoria total de las horas de todas las clases por asignar
    private double totalHorasClases = 0.0D;

    public AIProject_Genetic_Algorithms(int seed) throws Exception {
       
        
        //System.out.println("aula:"+aula+"dia:"+dia+"hora:"+hora);
        
        d = new Datos();
        d.cargarProfesores();
        d.cargarMaterias();
        d.cargarAulas(HORAS_DIA,HORAS_SEMANA_AULA);
        d.cargarClases();
        
        inicializarVariables();
        
        Genotype genotype = this.configureJGAP();
        this.evolve(genotype);
    }
    
     private Genotype configureJGAP() throws InvalidConfigurationException {
       
         Configuration gaConf = new DefaultConfiguration();
                 
         
         /*
         
        // Here we specify a fitness evaluator where lower values means a better fitness
        Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
        gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());

        // Only use the swapping operator. Other operations makes no sense here
        // and the size of the chromosome must remain constant
        //Examples of genetic operators are reproduction, crossover, and mutation.
        gaConf.getGeneticOperators().clear();
        SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
        gaConf.addGeneticOperator(swapper);

        // We are only interested in the most fittest individual
        gaConf.setPreservFittestIndividual(true);
        gaConf.setKeepPopulationSizeConstant(false);
        
        */

        gaConf.setPopulationSize(TAMANIO_POBLACION);
        // The number of chromosomes is the number of boxes we have. 
        int chromeSize = (int)TOTAL_HORAS;
        Genotype genotype;

        // Setup the structure with which to evolve the solution of the problem.
        // An IntegerGene is used. This gene represents the index of a box in the boxes array.
        IChromosome sampleChromosome = new Chromosome(gaConf, new IntegerGene(gaConf), chromeSize);
        gaConf.setSampleChromosome(sampleChromosome);
        // Setup the fitness function
        CalculateAptitudeSchedule fitnessFunction = new CalculateAptitudeSchedule();
        fitnessFunction.setDatos(this.d);
        fitnessFunction.setHorasSemanas(HORAS_SEMANA_AULA);
        fitnessFunction.setdiasSemana(DIAS_SEMANA);
        fitnessFunction.setHorasDia(HORAS_DIA);
        fitnessFunction.setTamCrom(TOTAL_HORAS);
        gaConf.setFitnessFunction(fitnessFunction);

        // Because the IntegerGenes are initialized randomly, it is neccesary to set the values to the index. Values range from 0..boxes.length
        genotype = Genotype.randomInitialGenotype(gaConf);
        List chromosomes = genotype.getPopulation().getChromosomes();

        for (Object chromosome : chromosomes) {
            IChromosome chrom = (IChromosome) chromosome;
            for (int j = 0; j < chrom.size(); j++) {
                Gene gene = chrom.getGene(j);
                gene.setAllele(0);
            }
        }
        for (Object chromosome : chromosomes) {
            IChromosome chrom = (IChromosome) chromosome;
            
            Iterator<Clase> nIterator= d.getClases().iterator();
            while(nIterator.hasNext()){
                Clase elemento=nIterator.next();
                Random rand = new Random(); 
                int index = rand.nextInt((int)TOTAL_HORAS); 
                //Obtengo el gen del cromosoma en la posicion index
                Gene gene = chrom.getGene(index);
                if((Integer)gene.getAllele()==0){
                    gene.setAllele(elemento.getId());
                    //System.out.println("Clase:"+gene.getAllele());
                }                
            }  
        }
        return genotype;        
     }
    
     private void evolve(Genotype a_genotype) {
        //int horasTotales=calcularHorasTodasClases();
        int valorFitness=1;
        
        System.out.println("NumeroOptimo:"+valorFitness);
        
        double previousFittest = a_genotype.getFittestChromosome().getFitnessValue();
        
        for (int i = 0; i < NUMERO_EVOLUCIONES; i++) {
            /*
             if (i % 250 == 0) {
                     System.out.println("Number of evolutions [" + i + "]");
                     System.out.println("Valor fitness[" + previousFittest + "]");
             }
             */

           System.out.println("Number of evolutions [" + i + "]");
           System.out.println("Valor fitness[" + previousFittest + "]");

            a_genotype.evolve();
            double fittness = a_genotype.getFittestChromosome().getFitnessValue();
            //System.out.println("Valor nuevo fitness[" + fittness + "]");
            if (fittness > previousFittest ) {
                this.printFittest(a_genotype.getFittestChromosome());
                previousFittest = fittness;
            }
            // No more optimal solutions
            if (valorFitness < fittness) {
                    break;
            }
        }       
        IChromosome fittest = a_genotype.getFittestChromosome();
        this.printSolution(fittest);        
     }
     
     private void printFittest(IChromosome fittest) {
        Gene[] genes = fittest.getGenes();
        System.out.println("Fitness value [" + fittest.getFitnessValue() + "]");
		
     }
     
     private void printSolution(IChromosome fittest) {
        int aula=0, aulaAnterior=0, dia=0,hora=0;
        for(int index=0;index<TOTAL_HORAS;index++){            
            int clase=(int)fittest.getGene(index).getAllele();
            
            Iterator<Clase> nIterator= d.getClases().iterator();
            while(nIterator.hasNext()){
               Clase c=nIterator.next();
               //Cuando encuentro la clase que busco con el id del allelo
               aula=index/(int)HORAS_SEMANA_AULA;
               if(c.getId()==clase){
                   if(clase!=0){
                       
                        dia=(index%(int)HORAS_SEMANA_AULA)/(int)HORAS_DIA;
                        hora=(index%(int)HORAS_SEMANA_AULA)%(int)HORAS_DIA;
                        System.out.print(" Clase[" + clase+"-"+c.getDuracion() + "] Aula["+aula+"] Dia["+dia+"] Hora["+hora+"] //");
                    }
               }
               if(aulaAnterior != aula){
                   System.out.println();
               }
               aulaAnterior=aula;
            }
        }        
     }
     
    

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        int seed = 37;
        if (args.length == 1) {
                seed = Integer.parseInt(args[0]);
        }
        new AIProject_Genetic_Algorithms(seed);
    }
    
    
    
    
     private void inicializarVariables(){
        NUMERO_DE_AULAS=d.getAulas().size();
        NUMERO_DE_PROFESORES=d.getProfesores().size();
        NUMERO_DE_MATERIAS=d.getMaterias().size();
        NUMERO_DE_CLASES=d.getClases().size();
        
        HORAS_SEMANA_AULA=HORAS_DIA*DIAS_SEMANA;
        TOTAL_HORAS=NUMERO_DE_AULAS*DIAS_SEMANA*HORAS_DIA;
    }
     
      private int calcularHorasTodasClases(){
        Iterator<Clase> nIterator= d.getClases().iterator();
        
        int horasAcumuladas=0;
        
        while(nIterator.hasNext()){
            Clase elemento=nIterator.next();
            horasAcumuladas+=elemento.getDuracion();
        }
        return horasAcumuladas;
     }
    
    
}
