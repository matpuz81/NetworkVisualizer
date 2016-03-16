/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author chef
 */
public class NetworkVisualizer {
    
    public static final GraphPanel panel = new GraphPanel();
    //This is the global DB Object. Every ineracction with the db should be done over this object.
    public static final DBCore DB = new DBCore();
    
    public static final Toolbar toolbar = new Toolbar();
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setJMenuBar(buildMenu());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(toolbar,BorderLayout.NORTH);
        mainPanel.add(panel);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().add(mainPanel); 
        frame.pack();
        frame.setVisible(true);
    }
 
    public static JMenuBar buildMenu() {
        JMenuBar menuBar;
        JMenu userMenu, networkMenu, serviceMenu;
        JMenuItem userItem, servicesItem, 
                topologiesItem, networkTypeItem, communicationProtocolItem;
        
        menuBar = new JMenuBar();
        
        userMenu = new JMenu("User");
        userItem = new JMenuItem("User List");
        userItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UserList user = new UserList();
            }
        });
        userMenu.add(userItem);
        
        networkMenu = new JMenu("Network");
        networkTypeItem = new JMenuItem("Network Types");
        networkTypeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NetworkTypeList networkType = new NetworkTypeList();
            }
        });
        networkMenu.add(networkTypeItem);
        communicationProtocolItem = new JMenuItem("Communication Protocols");
        communicationProtocolItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CommunicationProtocolList communicationProtocol = new CommunicationProtocolList();
            }
        });
        networkMenu.add(communicationProtocolItem);
        
        serviceMenu = new JMenu("Service");
        servicesItem = new JMenuItem("Services");
        servicesItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceList service = new ServiceList();
            }
        });
        serviceMenu.add(servicesItem);
        
        menuBar.add(userMenu);
        menuBar.add(serviceMenu);
        menuBar.add(networkMenu);
        
        return menuBar;
    }
}
