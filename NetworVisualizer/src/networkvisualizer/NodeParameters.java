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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author chef 
 */
public class NodeParameters extends JFrame {
    
    JPanel mainPanel = new JPanel();
    JList nodesList;
    GraphPanel parentPanel;
    Node node;
    DefaultListModel nodeListModel = new DefaultListModel();
    JPanel listPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel parameterPanel = new JPanel();
    JPanel parameterLeftPanel = new JPanel();
    JPanel parameterRightPanel = new JPanel();
    LinkedList<Node> nodesToRemove = new LinkedList();
    JButton cancelButton, saveButton,networkParamButton,addNetworkButton;
    JLabel labelInputLabel, networkLabel;
    JTextField labelInput;
    
    public NodeParameters(Node node)
    {
        this.parentPanel=NetworkVisualizer.panel;
        this.node=node;
        this.setTitle("Parameters - " + node.getLabel() + " - id:" + node.getId());
        
        mainPanel.setLayout(new BorderLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        parameterPanel.setLayout(new GridLayout(3,2,20,0));

        labelInputLabel = new JLabel("Label:");
        networkLabel = new JLabel("Network:");
        labelInput = new JTextField(20);
        labelInput.setText(node.getLabel());
        
        networkParamButton = new JButton(node.getNetwork().getName());
        networkParamButton.setActionCommand("openNetworkParam");
        networkParamButton.addActionListener(new NodeParametersListener());
        
        addNetworkButton = new JButton("Add Network");
        addNetworkButton.setActionCommand("addNetwork");
        addNetworkButton.addActionListener(new NodeParametersListener());
        
        
        
        parameterPanel.add(labelInputLabel);
        parameterPanel.add(networkLabel);
        parameterPanel.add(labelInput);
        parameterPanel.add(networkParamButton);
        
        addList();
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(new NodeParametersListener());
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(new NodeParametersListener());
        
        southPanel.add(cancelButton);
        southPanel.add(saveButton);
        
        leftPanel.add(parameterPanel);
        
        centerPanel.add(leftPanel);
        centerPanel.add(listPanel);
        
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(southPanel,BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
        
        getRootPane().setDefaultButton(saveButton);
        pack();
        setVisible(true);
    }
    
    final void addList()
    {
        listPanel.setBorder(BorderFactory.createTitledBorder("Connected Nodes:"));

        nodesList = new JList(nodeListModel);
        nodesList.setPreferredSize(new Dimension(300,150));
        nodesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                nodesList.setSelectedIndex(nodesList.locationToIndex(e.getPoint()));
                checkMousePress(e);
            }
        });
        listPanel.add(nodesList);
        
        refreshList();
    }
    
    final void refreshList()
    {
        
        nodeListModel.clear();
        if(node.getConnectedNodes().isEmpty() || node.getConnectedNodes().size() == nodesToRemove.size())
        {
            nodeListModel.addElement("No connected nodes");
            nodesList.setEnabled(false);
        }
        
        else
        {
            for(Node n:node.getConnectedNodes()){
                if(!nodesToRemove.contains(n))
                    nodeListModel.addElement(n.getLabel() + " Type: " + NetworkVisualizer.panel.getConnection(node, n).getType()
                     + " \tVelocity: " + NetworkVisualizer.panel.getConnection(node, n).getVelocity());
            }
        }
    }
    
    void save() {
        /*
        if(node.getNetwork()==null)
        {
            
        }
        else*/
        {
            node.setParams(labelInput.getText());
            //if(!parentPanel.nodes.contains(node))
            {
            }


            for(Node n:nodesToRemove) {
                System.out.println(n.getLabel());
                NetworkVisualizer.panel.removeConnection(node,n);
            }

            parentPanel.repaint();
            close();
        }
    }
    void close()
    {
        this.setVisible(false);
        this.dispose();
    }
    
    Node getSelectedNode() {
        return node.getConnectedNodes().get(nodesList.getSelectedIndex());
    }
    
    void checkMousePress(MouseEvent e)
    {    
        if(SwingUtilities.isRightMouseButton(e))
        { 
            if(nodesList.getSelectedIndex() != -1 && nodesList.isEnabled()) {
                NodeParametersMenu paramMenu = new NodeParametersMenu();
                paramMenu.show(nodesList,e.getX(),e.getY());
            }
        }
    }
    
    class NodeParametersMenu extends JPopupMenu
    {
        JMenuItem disconnectNode,nodeProperties;

        public NodeParametersMenu(){
            disconnectNode = new JMenuItem("Disconnect");
            disconnectNode.setActionCommand("disconnectNode");
            nodeProperties = new JMenuItem("Properties");
            nodeProperties.setActionCommand("nodeProperties");

            disconnectNode.addActionListener(new NodeParametersListener());
            nodeProperties.addActionListener(new NodeParametersListener());
            
            add(disconnectNode);
            add(nodeProperties);
        }
    }
    
    class NodeParametersListener implements ActionListener
    {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand())
            {
                case "addNetwork":
                    
                    Network tmpNet = new Network();
                    node.setNetwork(tmpNet);
                    NetworkParameters addNetworkFrame = new NetworkParameters(tmpNet);
                    addNetworkFrame.setVisible(true);
                    save();
                    break;
                case "changeNetwork":
                    
                    break;
                case "openNetworkParam":
                    NetworkParameters netParamsFrame = new NetworkParameters(node.getNetwork());
                    netParamsFrame.setVisible(true);
                    save();
                    break;
                case "disconnectNode": 
                    nodesToRemove.add(getSelectedNode());
                    refreshList();
                    break;
                case "nodeProperties":
                    NodeParameters paramsFrame = new NodeParameters(getSelectedNode());
                    paramsFrame.setVisible(true);
                    close();
                    break;
                case "save":
                    save();
                    break;
                case "cancel":
                    close();
                    break;
            }
        }
        
    }
}


