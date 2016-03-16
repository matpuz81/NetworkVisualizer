/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import com.bric.swing.ColorPicker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author chef
 */
public class NetworkParameters extends JFrame {
    
    JPanel mainPanel = new JPanel();
    JList nodesList;
    GraphPanel parentPanel;
    final Network net;
    DefaultListModel lm = new DefaultListModel();
    JPanel listPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel parameterPanel = new JPanel();
    LinkedList<Node> nodesToRemove = new LinkedList();
    JSlider colorSlider;
    JButton cancelButton, saveButton, colorButton;
    JLabel nameInputLabel, colorLabel, topologyLabel, networkTypeLabel, comProtoLabel;
    JTextArea descriptionInput;
    JTextField nameInput;
    JComboBox networkTypeInput, comProtoInput;
    
    public NetworkParameters(Network net)
    {
        this.parentPanel=NetworkVisualizer.panel;
        this.net=net;
        this.setTitle("Parameters - " + net.getName() + " - id:" + net.getId());
        //this.setPreferredSize(new Dimension());
        mainPanel.setLayout(new BorderLayout());
        leftPanel.setLayout(new GridLayout(2,1));
        parameterPanel.setLayout(new GridLayout(4,2,10,0));
        parameterPanel.setPreferredSize(new Dimension(500,150));
        
        nameInputLabel = new JLabel("Label:");
        colorLabel = new JLabel("Color:");
        nameInput = new JTextField(20);
        nameInput.setText(net.getName());
        
        topologyLabel = new JLabel(net.getNet_topology());
        colorButton = new JButton();
        colorButton.setPreferredSize(new Dimension(25,25));
        colorButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorButton.setBackground(net.getColor());
        colorButton.setActionCommand("openColorPopup");
        colorButton.addActionListener(new NetworkParametersListener());
        
        comProtoInput = new JComboBox();
        for(CommunicationProtocol p : NetworkVisualizer.DB.getAllCommunicationProtocol())
        {
            comProtoInput.addItem(p.getName());
        }
        
        comProtoInput.setSelectedIndex(net.getNet_com_protocol()-1);

        networkTypeInput = new JComboBox();
        for(NetworkType t : NetworkVisualizer.DB.getAllNetworkType())
        {
            networkTypeInput.addItem(t.getId());
        }
        networkTypeInput.setSelectedItem(net.getNet_type_id());
        
        System.out.println(net.getNet_com_protocol()); 

        descriptionInput = new JTextArea(5,20);
        descriptionInput.setText(net.getDescription());
        
        addList();
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(new NetworkParametersListener());
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(new NetworkParametersListener());
        
        southPanel.add(cancelButton);
        southPanel.add(saveButton);
        
        parameterPanel.add(nameInputLabel);
        JPanel colorTopologyLabelPanel = new JPanel();
        colorTopologyLabelPanel.setLayout(new GridLayout(1,2));
        colorTopologyLabelPanel.add(colorLabel);
        colorTopologyLabelPanel.add(new JLabel("Topology:"));
        parameterPanel.add(colorTopologyLabelPanel);
        parameterPanel.add(nameInput);
        JPanel colorTopologyPanel = new JPanel();
        colorTopologyPanel.setLayout(new GridLayout(1,2));
        JPanel colorButtonPanel = new JPanel();
        colorButtonPanel.add(colorButton);
        colorTopologyPanel.add(colorButtonPanel);
        colorTopologyPanel.add(topologyLabel);
        parameterPanel.add(colorTopologyPanel);
        
        parameterPanel.add(comProtoInput);
        parameterPanel.add(networkTypeInput);
        parameterPanel.add(new JLabel("Description:"));
        leftPanel.add(parameterPanel);
        leftPanel.add(descriptionInput);
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

        nodesList = new JList(lm);
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
        lm.clear();
        if(net.getNodes().isEmpty() || net.getNodes().size() == nodesToRemove.size())
        {
            lm.addElement("No connected nodes");
            nodesList.setEnabled(false);
        }
        
        else
        {
            for(Node n:net.getNodes()){
                if(!nodesToRemove.contains(n))
                    lm.addElement(n.getLabel());
            }
        }
    }
    
    void save() {
        
        net.setParams(nameInput.getText(), descriptionInput.getText(), networkTypeInput.getSelectedItem().toString(), comProtoInput.getSelectedIndex()+1);
        net.setColor(colorButton.getBackground());
        
        NetworkVisualizer.DB.updateNetwork(net);
        
        
        parentPanel.repaint();
        close();
    }
    void close()
    {
        this.setVisible(false);
        this.dispose();
    }
    
    Node getSelectedNode() {
        return net.getNodes().get(nodesList.getSelectedIndex());
    }
    
    void checkMousePress(MouseEvent e)
    {    
        if(SwingUtilities.isRightMouseButton(e))
        { 
            if(nodesList.getSelectedIndex() != -1 && nodesList.isEnabled()) {
                NetworkParametersMenu paramMenu = new NetworkParametersMenu();
                paramMenu.show(nodesList,e.getX(),e.getY());
            }
        }
    }
    
    class NetworkParametersMenu extends JPopupMenu
    {
        JMenuItem nodeProperties;

        public NetworkParametersMenu(){
            nodeProperties = new JMenuItem("Properties");
            nodeProperties.setActionCommand("nodeProperties");

            nodeProperties.addActionListener(new NetworkParametersListener());
            
            add(nodeProperties);
        }
    }
    
    class NetworkParametersListener implements ActionListener
    {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand())
            {
                case "openColorPopup":
                    colorButton.setBackground(ColorPicker.showDialog(null, colorButton.getBackground()));
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


