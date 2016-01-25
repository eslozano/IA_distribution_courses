/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_genetic_algorithms;

import aiproject_clases.CalculateAptitudeSchedule;
import aiproject_clases.Clase;
import aiproject_clases.Datos;
import aiproject_clases.MenuActionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;

import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.SwappingMutationOperator;

/**
 *
 * @author estef
 */
public class AIProject_Genetic_Algorithms {
    //private static final Logger LOG = Logger.getLogger(AIProject_Genetic_Algorithms.class);
    private static final int NUMERO_EVOLUCIONES = 5000;
    // El tamaño de la poblaion (numero de cromosomas en el genotipo)    
    private static final int TAMANIO_POBLACION = 100;
    
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

    public static Object[] tablas = new Object[10];
    public static JFrame principal = new JFrame("Course Distribution");
    public static Container container = principal.getContentPane();
    
    public static JFrame mainFrame = new JFrame("Evolution Progress");
    public static Box box = Box.createVerticalBox();
    public static JScrollPane pane = new JScrollPane(box);
    public static JButton showResults = new JButton("Results");
    
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
            Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
            
            gaConf.getGeneticOperators().clear();
            SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
            gaConf.addGeneticOperator(swapper);
            gaConf.setPreservFittestIndividual(true);
            gaConf.setPopulationSize(TAMANIO_POBLACION);
            gaConf.setKeepPopulationSizeConstant(false);
        
       
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
        
        int valorFitness=1;
        System.out.println("NumeroOptimo:"+valorFitness);
        
        double previousFittest = a_genotype.getFittestChromosome().getFitnessValue();
        box.setSize(400,700);
        mainFrame.setSize(400,700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.add(pane);
        
        for (int i = 0; i < NUMERO_EVOLUCIONES; i++) {
             
            a_genotype.evolve();
            double fittness = a_genotype.getFittestChromosome().getFitnessValue();            
            
            if (fittness > previousFittest ) {
                this.printFittest(a_genotype.getFittestChromosome(),i);
                previousFittest = fittness;
            }
            if (fittness>=valorFitness) {
                    break;
            }
        }       
        showResults.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                principal.setVisible(true);
            }
        });
        showResults.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(showResults);
        box.revalidate();
        
        IChromosome fittest = a_genotype.getFittestChromosome();
        this.printSolution2(fittest);     
        //this.printSolution(fittest);
     }
     
     private void printFittest(IChromosome fittest,int i) {
        Gene[] genes = fittest.getGenes();
        String fittestStr = "\t Evolution:   ["+i+"]    \t     Fitness Value:   [" + fittest.getFitnessValue() + "]";
        JLabel label = new JLabel(fittestStr);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        box.add(label);
        System.out.println("Evolution:["+i+"]    \t   Fitness Value: [" + fittest.getFitnessValue() + "]");
        box.revalidate();
     }
     

     
    private void printSolution2(IChromosome fittest) {
         
        JMenuBar menu = new JMenuBar();
        JMenu rooms = new JMenu("Rooms");
        menu.add(rooms);
        Object days[] = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        principal.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int tableIndex = 0;
        
        int gene=0, id=0;
        int [][] aula1Matriz = new int[12][5];
        for (int x=0; x < 12; x++) {
            for (int y=0; y < 5; y++) {
              aula1Matriz[x][y]=0;
            }
        }
        
        while(gene < fittest.size()){
            for(int indicedia=0;indicedia<5;indicedia++){          
                for(int indicehora=0;indicehora<12;indicehora++){          
                    id=(int)fittest.getGene(gene).getAllele();
                    if(aula1Matriz[indicehora][indicedia]==0){
                        aula1Matriz[indicehora][indicedia]=id;
                    }
                    if(id!=0){    
                        Clase c=d.getClases().get(id-1);
                        for (int j=1;j<c.getDuracion();j++){
                            if(indicehora+j<12){
                                aula1Matriz[indicehora+j][indicedia]=id;
                            }
                        }
                    }
                    gene++;
                }
            }
            System.out.println("AULA:"+(gene-1)/60);
            JTable table = new JTable();
            DefaultTableModel model = new DefaultTableModel(days, 0);
                 for (int x=0; x < aula1Matriz.length; x++) {
                    //System.out.print("|");
                    Object [] fila = new Object[5];
                    for (int y=0; y < aula1Matriz[x].length; y++) {
                      //System.out.print (aula1Matriz[x][y]);
                      Clase c=d.getClase(aula1Matriz[x][y]);
                      if(c!=null){
                          fila[y] = c.getMateria().getNombre();
                      }
                      //if (y!=aula1Matriz[x].length-1) 
                          //System.out.print("\t");
                    }
                    model.addRow(fila); 
                    //System.out.println("|");
                }
            table.setModel(model);
            tablas[tableIndex] = table;
            tableIndex++;
            //System.out.println(gene);
            
            for (int x=0; x < 12; x++) {
                for (int y=0; y < 5; y++) {
                  aula1Matriz[x][y]=0;
                }
            }
 
        }
        
        for (int i = 0; i < 9; i++){
            MenuActionListener aux = new MenuActionListener(i);
            JMenuItem room = new JMenuItem("Room "+(i+1));
            room.addActionListener(aux);
            rooms.add(room);
        }
        principal.setJMenuBar(menu);
        principal.setSize(500, 300);
        principal.setVisible(false);
     }
     
     private void printSolution(IChromosome fittest) {
        int aula=0, aulaAnterior=0, dia=0,hora=0;
        for(int index=0;index<TOTAL_HORAS;index++){            
            int clase=(int)fittest.getGene(index).getAllele();
            
            Clase c=d.getClase(clase);
            if(c!=null){
               //Cuando encuentro la clase que busco con el id del allelo
                aula=index/(int)HORAS_SEMANA_AULA;
                if(aulaAnterior != aula){
                    System.out.println();
                }      
                aulaAnterior=aula;
                dia=(index%(int)HORAS_SEMANA_AULA)/(int)HORAS_DIA;
                hora=(index%(int)HORAS_SEMANA_AULA)%(int)HORAS_DIA;
                System.out.print(" Clase[" + clase+"-"+c.getDuracion() + "] Materia["+c.getMateria().getId()+"] Aula["+aula+"] Dia["+dia+"] Hora["+hora+"] //");
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
