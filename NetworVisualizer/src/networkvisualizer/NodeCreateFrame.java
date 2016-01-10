/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author chef
 */
public class NodeCreateFrame extends JFrame {
    
    
    JPanel mainPanel;
    GraphPanel parentPanel;
    JTextField labelInput;
    JButton saveButton, cancelButton;
    JPanel lowerPanel, centerPanel;
    JLabel labelInputLabel;
    Node node;
    
    public NodeCreateFrame(GraphPanel parentPanel, Node node)
    {
        this.parentPanel=parentPanel;
        this.node=node;
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(300,200));
        mainPanel.setSize(300,200);
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1,2));
        lowerPanel = new JPanel();
        
        labelInputLabel = new JLabel("Label:");
        labelInput = new JTextField(20);
        labelInput.setText(node.label);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(new NodeCreatePanelListener());
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(new NodeCreatePanelListener());
        
        
        centerPanel.add(labelInputLabel);
        centerPanel.add(labelInput);
        lowerPanel.add(cancelButton);
        lowerPanel.add(saveButton);
        
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(lowerPanel,BorderLayout.PAGE_END);
        
        getRootPane().setDefaultButton(saveButton);
        getContentPane().add(mainPanel);
        pack();
        setVisible(true);
    }
    
    private void save()
    {
        parentPanel.addNodeToList(node, labelInput.getText());
        this.setVisible(false);
        this.dispose();
    }
    
    private class NodeCreatePanelListener implements ActionListener {

    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand())
        {
            case "cancel":
                break;
            case "save":
                save();
                break;
        }
    }
}

}

