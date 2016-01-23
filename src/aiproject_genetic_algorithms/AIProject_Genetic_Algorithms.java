/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_genetic_algorithms;

import aiproject_clases.Datos;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author estef
 */
public class AIProject_Genetic_Algorithms {
    //private static final Logger LOG = Logger.getLogger(AIProject_Genetic_Algorithms.class);
    private static final int NUMERO_EVOLUCIONES = 5000;
    // El tamaño de la poblaion (numero de cromosomas en el genotipo)    
    private static final int TAMANIO_POBLACION = 50;
    
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
    private Class[] clases;
    //Sumatoria total de las horas de todas las clases por asignar
    private double totalHorasClases = 0.0D;

    public AIProject_Genetic_Algorithms(int seed) throws Exception {
        
        d = new Datos();
        d.cargarProfesores();
        d.cargarMaterias();
        d.cargarAulas();
        d.cargarClases();
        
        inicializarVariables();
        
        
        
        //this.createBoxes(seed);
        //Genotype genotype = this.configureJGAP();
       // this.evolve(genotype);
    }
    
   
    /*
     private Genotype configureJGAP() throws InvalidConfigurationException {
        // Genotype genotype=new Genotype();
         
         return NULL;
     }*/
     
     private void evolve(Genotype a_genotype) {
         
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
    
    
}
