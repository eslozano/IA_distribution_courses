/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject_clases;

import aiproject_genetic_algorithms.AIProject_Genetic_Algorithms;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Kevin
 */
public class MenuActionListener implements ActionListener{
    private int i;
    
    public MenuActionListener(int i){
        this.i = i;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AIProject_Genetic_Algorithms.container.revalidate();
        AIProject_Genetic_Algorithms.container.removeAll();
        JTable aux = (JTable) AIProject_Genetic_Algorithms.tablas[this.i];
        JScrollPane scrollPane = new JScrollPane(aux);
        AIProject_Genetic_Algorithms.container.add(scrollPane, BorderLayout.CENTER);
    }
    
}
