/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matthiaskeim
 */
public class DBCore {

    private Connection connection = null;
    private final String us = "postgres";
    private final String pw = "password";
    private final String dbName = "testDB";
    private final String dbPort = "5432";
    private final String dbIp = "127.0.0.1";
    
    //Temp variable to enable debugging
    

    //The constructor creates a connection to the DB. There Should be onyl one DB object becouse otherwise the application has several connections simultaneusly.
    public DBCore() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://" + dbIp + ":" + dbPort + "/" + dbName, us, pw);
        } catch (Exception e) {
            System.out.println("Connection to DB faild!");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        System.out.println(createDbStructure());
        //System.out.println(this.cleanDb());
        //insertExampleData();
        addNodesToPanelFromDb();
        
        
    }
    
    public ArrayList<ComunicationProtocol> getAllComunicationProtocol() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from comunicationprotocol;";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList<ComunicationProtocol> output = new ArrayList<ComunicationProtocol>();
            while(res.next())  {
                output.add(new ComunicationProtocol(res.getInt("protocol_id"), res.getString("name"), res.getString("level"), res.getString("description")));
            }
            stmt.close();
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<NetworkTopology> getAllNetworkTopology() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from networktopology;";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList<NetworkTopology> output = new ArrayList<NetworkTopology>();
            while(res.next())  {
                output.add(new NetworkTopology(res.getString("name"), res.getString("structure")));
            }
            stmt.close();
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<NetworkType> getAllNetworkType() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from networktype;";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList<NetworkType> output = new ArrayList<NetworkType>();
            while(res.next())  {
                output.add(new NetworkType(res.getString("id_net_type"), res.getString("description")));
            }
            stmt.close();
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    
    public int addComunicationProtocol(ComunicationProtocol comProt) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into comunicationprotocol(name, level, description) values('"+comProt.getName()+"', '"+comProt.getLevel()+"', '"+comProt.getDescription()+"') returning protocol_id;";
            ResultSet res = stmt.executeQuery(sql);
            res.next(); //By calling one time next the first tuple became selected
            int id = res.getInt(1); //The number passing the get method represents the collum.
            stmt.close();
            comProt.setId(id); //The passed object gets also the id selected by the db
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public boolean addNetworkTopology(NetworkTopology netTop) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into networktopology(name, structure) values('"+netTop.getName()+"', '"+netTop.getStructure()+"');";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }   
    }
    
    public boolean addNetworkType(NetworkType netType) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into networktype(id_net_type, description) values('"+netType.getId()+"', '"+netType.getDescription()+"');";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
    }
    
    public int addNetwork(Network net) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            //Cheching if comunication protocol exists
            String sql = "select * from comunicationprotocol where protocol_id = "+net.getNet_com_protocol()+";";
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new Exception("Comunication protocol not found");
            }
            
            //Cheching if networktopology exists
            sql = "select * from networktopology where name = '"+net.getNet_topology()+"';";
            res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new Exception("Networktopology not found");
            }
            
             //Cheching if networktype exists
            sql = "select * from networktype where id_net_type = '"+net.getNet_type_id()+"';";
            res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new Exception("Networktype not found");
            }
            
            sql = "insert into network(name, description, networktype_id_net_type, networktopology_name, comunicationprotocol_id) values('"+net.getName()+"', '"+net.getDescription()+"', '"+net.getNet_type_id()+"', '"+net.getNet_topology()+"', "+net.getNet_com_protocol()+") returning id_network;";
            res = stmt.executeQuery(sql);
            
            res.next();
            int newNetId = res.getInt(1);
            
            stmt.close();
            
            net.setId(newNetId);
            
            return newNetId;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public boolean deleteNodeConnection(Node n1, Node n2) {
         try {
            Statement stmt = connection.createStatement();
            String sql = "delete from nodeconnection where id1 = "+getSmaller(n1.getId(), n2.getId())+" and id2 = "+getBigger(n1.getId(), n2.getId())+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean addNodeConnection(Node n1, Node n2) {
         try {
            Statement stmt = connection.createStatement();
            String sql = "insert into nodeconnection (id1, id2) values ("+getSmaller(n1.getId(), n2.getId())+","+getBigger(n1.getId(), n2.getId())+");";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //This method takes a Node object and add its into the DB
    public int addNode(Node n) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT into node(ip_address, angle, distance) values('"+n.getLabel()+"',"+n.getAngle()+", "+n.getDistance()+") returning(id_node);";
            ResultSet res = stmt.executeQuery(sql);
            res.next(); //By calling one time next the first tuple became selected
            int id = res.getInt(1); //The number passing the get method represents the collum.
            stmt.close();
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    //You can pass this method node which will be updatet
    public boolean updateNode(Node n) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update node set ip_address = '"+n.getLabel()+"' where id_node = "+n.getId()+";"
                    + "update node set angle = "+n.getAngle()+" where id_node = "+n.getId()+";"
                    + "update node set distance = "+n.getDistance()+" where id_node = "+n.getId()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //You can pass a node which will be deleted
    public boolean deleteNode(Node n) {
         try {
            Statement stmt = connection.createStatement();
            String sql = "delete from node where id_node = "+n.getId();
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //This method deleates all entries from the Db and restore the structure.
    public boolean cleanDb() {
        deleteDbStructure();
        return createDbStructure();
    }
    
    public boolean addNodesToPanelFromDb() {
        try {
            //Add all Nodes of the DB to the panel
            Statement stmt = connection.createStatement();
            String sql = "Select * from node;";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                NetworkVisualizer.panel.addNodeFromDB(null, res.getInt("id_node"), res.getDouble("angle"), res.getDouble("distance"), res.getString("ip_address"));
            }
            stmt.close();
            
            //Connenct all nodes at the panel
            stmt = connection.createStatement();
            sql = "Select * from nodeconnection;";
            res = stmt.executeQuery(sql);
            while(res.next()) {
                NetworkVisualizer.panel.connectNodesById(res.getInt("id1"), res.getInt("id2"));
            }
            stmt.close();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //This method creates the tables which are neccessary for our application
    private boolean createDbStructure() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS NetworkType (\n"
                    + "  id_net_type VARCHAR(3) NOT NULL,\n"
                    + "  description VARCHAR(100) NULL,\n"
                    + "  PRIMARY KEY (id_net_type)\n"
                    + ");\n"
                    + " \n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS ComunicationProtocol  (\n"
                    + "   protocol_id SERIAL,\n"
                    + "   name  VARCHAR(10) NULL,\n"
                    + "   level  VARCHAR(45) NULL,\n"
                    + "   description  VARCHAR(300) NULL,\n"
                    + "  PRIMARY KEY ( protocol_id )\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS NetworkTopology (\n"
                    + "  name VARCHAR(15) NOT NULL,\n"
                    + "  structure VARCHAR(300) NULL,\n"
                    + "  PRIMARY KEY (name)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "  id_user INT NOT NULL,\n"
                    + "  type VARCHAR(45) NULL,\n"
                    + "  usage VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (id_user)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS Service (\n"
                    + "  code INT NOT NULL,\n"
                    + "  description VARCHAR(300) NULL,\n"
                    + "  service VARCHAR(45) NULL,\n"
                    + "  permission VARCHAR(45) NULL,\n"
                    + "  cost DECIMAL NULL,\n"
                    + "  PRIMARY KEY (code)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS ConsumedBy (\n"
                    + "  Service_code INT NOT NULL,\n"
                    + "  User_id_user INT NOT NULL,\n"
                    + "  PRIMARY KEY (Service_code, User_id_user),\n"
                    + " \n"
                    + "    FOREIGN KEY (Service_code)\n"
                    + "    REFERENCES Service(code),\n"
                    + " \n"
                    + "    FOREIGN KEY (User_id_user)\n"
                    + "    REFERENCES Users(id_user)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS NetworkLink (\n"
                    + "  id_net_link INT NOT NULL,\n"
                    + "  type VARCHAR(45) NULL,\n"
                    + "  velocity VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (id_net_link)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS Node (\n"
                    + "  id_node SERIAL,\n"
                    + "  ip_address VARCHAR(15) NOT NULL,\n"
                    + "  angle REAL,\n"
                    + "  distance REAL,\n"
                    + "  status VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (id_node)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS NodeConnection (\n"
                    + "  id1 INT NOT NULL,\n"
                    + "  id2 INT NOT NULL,\n"
                    + "  PRIMARY KEY (id1, id2),\n"
                    + "   \n"
                    + "    FOREIGN KEY (id1)\n"
                    + "    REFERENCES Node(id_node),\n"
                    + "     \n"
                    + "    FOREIGN KEY (id1)\n"
                    + "    REFERENCES Node(id_node)\n"
                    + ");\n"
                    + "   \n"
                    + "CREATE TABLE IF NOT EXISTS Network (\n"
                    + "  id_network SERIAL,\n"
                    + "  Name VARCHAR(45) NULL,\n"
                    + "  Description VARCHAR(300) NULL,\n"
                    + "  NetworkType_id_net_type VARCHAR(3) NOT NULL,\n"
                    + "  NetworkTopology_name VARCHAR(15) NOT NULL,\n"
                    + "  ComunicationProtocol_id INT NOT NULL,\n"
                    + "  PRIMARY KEY(id_network),\n"
                    + "    FOREIGN KEY (NetworkType_id_net_type)\n"
                    + "    REFERENCES NetworkType(id_net_type),\n"
                    + "    FOREIGN KEY (NetworkTopology_name)\n"
                    + "    REFERENCES NetworkTopology(name),\n"
                    + "  FOREIGN KEY (ComunicationProtocol_id)\n"
                    + "  REFERENCES ComunicationProtocol(protocol_id)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS Offers (\n"
                    + "  Network_id_network INT NOT NULL,\n"
                    + "  Service_code INT NOT NULL,\n"
                    + "  PRIMARY KEY (Network_id_network, Service_code),\n"
                    + "    FOREIGN KEY (Network_id_network)\n"
                    + "    REFERENCES Network (id_network),\n"
                    + "    FOREIGN KEY (Service_code)\n"
                    + "    REFERENCES Service (code)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS NetworkConnection (\n"
                    + "  Node_id INT NOT NULL,\n"
                    + "  Network_id_network INT NOT NULL,\n"
                    + "  NetworkLink_id_net_link INT NOT NULL,\n"
                    + "  PRIMARY KEY (Node_id, Network_id_network, NetworkLink_id_net_link),\n"
                    + "    FOREIGN KEY (Node_id)\n"
                    + "    REFERENCES Node(id_node),\n"
                    + "    FOREIGN KEY (Network_id_network)\n"
                    + "    REFERENCES Network(id_network),\n"
                    + "    FOREIGN KEY (NetworkLink_id_net_link)\n"
                    + "    REFERENCES NetworkLink(id_net_link)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    //This method deleats the table used by this application
    private boolean deleteDbStructure() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "DROP TABLE NetworkConnection;\n"
                    + "DROP TABLE Offers;\n"
                    + "DROP TABLE Network;\n"
                    + "DROP TABLE NodeConnection;\n"
                    + "DROP TABLE Node;\n"
                    + "DROP TABLE NetworkLink;\n"
                    + "DROP TABLE ConsumedBy;\n"
                    + "DROP TABLE Service;\n"
                    + "DROP TABLE Users;\n"
                    + "DROP TABLE NetworkTopology;\n"
                    + "DROP TABLE ComunicationProtocol;\n"
                    + "DROP TABLE NetworkType;";
            stmt.executeUpdate(sql);
            stmt.close();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    private void insertExampleData() {
        addNetworkType(new NetworkType("LAN", "local"));
        addNetworkTopology(new NetworkTopology("star", "normal"));
        addComunicationProtocol(new ComunicationProtocol("TCP", "low", "def")); 
    }
    
    private int getSmaller(int a, int b) {
        if(a < b) {
            return a;
        } else {
            return b;
        }
    }
    
    private int getBigger(int a, int b) {
        if(a > b) {
            return a;
        } else {
            return b;
        }
    }
}
