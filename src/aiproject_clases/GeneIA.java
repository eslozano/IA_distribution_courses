/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author estef
 */
public class GeneIA extends Genotype{
    
    public GeneIA(Configuration a_configuration, IChromosome[] a_initialChromosomes) throws InvalidConfigurationException {
        super(a_configuration, a_initialChromosomes);
    }

    @Override
    public synchronized void evolve() {
        super.evolve(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
