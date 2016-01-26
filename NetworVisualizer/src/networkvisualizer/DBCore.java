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
    private final String dbPort = "1111";//5432 1111
    private final String dbIp = "localhost";//127.0.0.1 localhost
    
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
        System.out.println("!!!!!!!!!!!!!!!!DB should be resetet!!!!!!!!!!");
        //System.out.println(this.cleanDb());
        
        //Insert some defoult data if not exist
        insetDefNetworkTypes();
        insertDefCommunicationProtocol();
        
        
        addNodesToPanelFromDb();
        
        
    }
    
    public ArrayList<CommunicationProtocol> getAllCommunicationProtocol() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from comunicationprotocol;";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList<CommunicationProtocol> output = new ArrayList<CommunicationProtocol>();
            while(res.next())  {
                output.add(new CommunicationProtocol(res.getInt("protocol_id"), res.getString("name"), res.getInt("level"), res.getString("description")));
            }
            stmt.close();
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
/*    
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
    */
    
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
    
   
    public boolean deleteNetworkType(NetworkType nt) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = " select * from network where networktype_id_net_type = '"+nt.getId()+"';";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                throw new Exception("This networktype is still linkt to a network");
            }
            
            sql = "delete from networktype where id_net_type = '"+nt.getId()+"';";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    public boolean updateNetworkType(NetworkType nt) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update networktype set description = '"+nt.getDescription()+"' where id_net_type = '"+nt.getId()+"';";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    
    public int addCommunicationProtocol(CommunicationProtocol comProt) {
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
    
    public boolean deleteCommunicationProtocol(CommunicationProtocol cp) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from network where comunicationprotocol_id = '"+cp.getId()+"';";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                throw new Exception("This communication protocol is still linkt to a network");
            }
            
            sql = "delete from comunicationprotocol where protocol_id = '"+cp.getId()+"';";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean updateCommunicationProtocol(CommunicationProtocol cp) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update comunicationprotocol set name = '"+cp.getName()+"', level = "+cp.getLevel()+", description = '"+cp.getDescription()+"' where protocol_id = "+cp.getId()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
   
    public ArrayList<CommunicationProtocol> getAllComunicationProtocol() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from comunicationprotocol;";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList<CommunicationProtocol> output = new ArrayList<CommunicationProtocol>();
            while(res.next())  {
                output.add(new CommunicationProtocol(res.getInt("protocol_id"), res.getString("name"), res.getInt("level"), res.getString("description")));
            }
            stmt.close();
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    public ArrayList<Network> getAllNetwork() {
        try {
            
            ArrayList<Network> output = new ArrayList<Network>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select * from network";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                Network net = new Network();
                net.setParams(res.getString("name"), res.getString("description"), res.getString("networktype_id_net_type"), res.getInt("comunicationprotocol_id"));
                net.setId(res.getInt("id_network"));
                net.setColor(SColor.getColorFromHex(res.getString("color")));
                output.add(net);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
            
             //Cheching if networktype exists
            sql = "select * from networktype where id_net_type = '"+net.getNet_type_id()+"';";
            res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new Exception("Networktype not found");
            }
            
            sql = "insert into network(name, description, color, networktype_id_net_type, comunicationprotocol_id) values('"+net.getName()+"', '"+net.getDescription()+"', '"+SColor.hexFromColor(net.getColor())+"', '"+net.getNet_type_id()+"', "+net.getNet_com_protocol()+") returning id_network;";
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
    
    public boolean deleteNetwork(Network net) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from node where network_id_network = "+net.getId()+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                throw new Exception("A node is stil linked to this Network");
            }
            
            sql = "delete from network where id_network = "+net.getId()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch(SQLException ex) {
            return false;
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
    
    public boolean updateNodeConnection(NodeConnection nc) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update nodeconnection set type = '"+nc.getType()+"', velocity = "+nc.getVelocity()+" where id1 = "+getSmaller(nc.n1.getId(), nc.n2.getId())+" and id2 = "+getBigger(nc.n1.getId(), nc.n2.getId())+";";
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
            String sql = "INSERT into node(ip_address, network_id_network, angle, distance) values('"+n.getLabel()+"', "+n.getNetworkId()+","+n.getAngle()+", "+n.getDistance()+") returning(id_node);";
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
            String sql = "update node set ip_address = '"+n.getLabel()
                    + "', angle="+n.getAngle()+", distance="+n.getDistance()+
                    ", network_id_network="+n.getNetwork().getId() + ", status='"+n.getStatus()+"' where id_node = "+n.getId()+";";
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
    
    public Network getNetworkById(int id) {
        Network net = NetworkVisualizer.panel.getNetworkById(id);
        if(net != null) {
            return net;
        }
        
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from network where id_network = "+id+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                net = new Network();
                net.setParams(res.getString("name"), res.getString("description"), res.getString("networktype_id_net_type"), res.getInt("comunicationprotocol_id"));
                net.setId(res.getInt("id_network"));
                net.setColor(SColor.getColorFromHex(res.getString("color")));
                return net;
            } else {
                return null;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }   
    }
    
    public boolean updateNetwork(Network net) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update network set name  = '"+net.getName()+"', description = '"+net.getDescription()+"', color = '"+SColor.hexFromColor(net.getColor())+"', networktype_id_net_type = '"+net.getNet_type_id()+"', comunicationprotocol_id = "+net.getNet_com_protocol()+" where id_network = "+net.getId()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean addNodesToPanelFromDb() {
        try {
            //Add all Nodes of the DB to the panel
            Statement stmt = connection.createStatement();
            String sql = "select * from node no, network net where no.network_id_network = net.id_network;";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                NetworkVisualizer.panel.addNodeFromDB(getNetworkById(res.getInt("network_id_network")), res.getInt("id_node"), res.getDouble("angle"), res.getDouble("distance"), res.getString("ip_address"));
            }
            stmt.close();
            
            //Connenct all nodes at the panel
            stmt = connection.createStatement();
            sql = "Select * from nodeconnection;";
            res = stmt.executeQuery(sql);
            while(res.next()) {
                NetworkVisualizer.panel.connectNodesById(res.getInt("id1"), res.getInt("id2"),res.getString("type"),res.getDouble("velocity"));
            }
            stmt.close();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public int addUser(User us) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into users(username, type, usage, pwhash) values('"+us.getUsername()+"', '"+us.isIsAdmin()+"', "+us.getUsage()+", "+us.getPwHash()+") returning id_user ;";
            ResultSet res = stmt.executeQuery(sql);
            res.next(); //By calling one time next the first tuple became selected
            int id = res.getInt(1); //The number passing the get method represents the collum.
            stmt.close();
            us.setUserID(id);
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public boolean updateUser(User us) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update users set username = '"+us.getUsername()+"', type = '"+us.isIsAdmin()+"', usage = "+us.getUsage()+", pwhash = "+us.getPwHash()+" where id_user = "+us.getUserID()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteUser(User us) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from consumedby where user_id_user = "+us.getUserID()+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                throw new Exception("User is stil linked in the consumed by table");
            }
            
            sql = "delete from users where id_user = "+us.getUserID()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public User getUserById(int id) {
        try {
            Statement stmt = connection.createStatement();
            User us = null;
            String sql = "select * from users where id_user = "+id+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                us = new User(res.getInt("id_user"), res.getString("username"), res.getBoolean("type"));
                us.setUsage(res.getInt("usage"));
                us.setPwHash(res.getInt("pwhash"));
            }
            
            stmt.close();
            
            return us;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public ArrayList<User> getAllUser() {
        try {
            
            ArrayList<User> output = new ArrayList<User>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select * from users";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                User us = new User(res.getInt("id_user"), res.getString("username"), res.getBoolean("type"));
                us.setUsage(res.getInt("usage"));
                us.setPwHash(res.getInt("pwhash"));
                
                output.add(us);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public User checkUser(String usn, String pw) {
        try {
            Statement stmt = connection.createStatement();
            User us = null;
            String sql = "select * from users where username = '"+usn+"' and pwhash = "+pw.hashCode()+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                us = new User(res.getInt("id_user"), res.getString("username"), res.getBoolean("type"));
                us.setUsage(res.getInt("usage"));
                us.setPwHash(res.getInt("pwhash"));
            }
            
            stmt.close();
            
            return us;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    
    public boolean addOffer(Network net, Service ser) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from service where code=" + ser.getCOD() + ";";
            ResultSet res = stmt.executeQuery(sql);
            if (!res.next()) {
                throw new Exception("Service not found");
            }

            sql = "select * from network where id_network = " + net.getId() + ";";
            res = stmt.executeQuery(sql);
            if (!res.next()) {
                throw new Exception("Network not found");
            }

            sql = "insert into offers(network_id_network, service_code) values(" + net.getId() + "," + ser.getCOD() + ")";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteOffer(Network net, Service ser) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from offers where network_id_network = "+net.getId()+" and service_code = "+ser.getCOD()+";";
            ResultSet res = stmt.executeQuery(sql);
            if (!res.next()) {
                throw new Exception("offer not found");
            }

            sql = "delete from offers where network_id_network = "+net.getId()+" and service_code = "+ser.getCOD()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<Network> getAllNetworkOfferingService(Service ser) {
        try {
            
            ArrayList<Network> output = new ArrayList<Network>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select n.name, n.description, n.networktype_id_net_type, n.comunicationprotocol_id, n.id_network from offers o, service s, network n where o.network_id_network = n.id_network and o.service_code = s.code and s.code = "+ser.getCOD()+";";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                Network net = new Network();
                net.setParams(res.getString("name"), res.getString("description"), res.getString("networktype_id_net_type"), res.getInt("comunicationprotocol_id"));
                net.setId(res.getInt("id_network"));
                output.add(net);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<Service> getAllServiceOfferdByNetwork(Network net) {
        try {
            
            ArrayList<Service> output = new ArrayList<Service>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select s.code, s.description, s.service, s.permission, s.cost from offers o, service s, network n where o.network_id_network = n.id_network and o.service_code = s.code and n.id_network = "+net.getId()+";";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                Service ser = new Service(res.getString("description"), res.getString("service"), res.getString("permission"), res.getFloat("cost"));
                ser.setCOD(res.getInt("code"));
                output.add(ser);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean addConsumedBy(User us, Service ser) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from users where id_user = "+us.getUserID()+";";
            ResultSet res = stmt.executeQuery(sql);
            if (!res.next()) {
                throw new Exception("user not found");
            }
            
            sql = "select * from service where code = "+ser.getCOD()+";";
            res = stmt.executeQuery(sql);
            if (!res.next()) {
                throw new Exception("service not found");
            }

            sql = "insert into consumedby(service_code, user_id_user) values("+ser.getCOD()+","+us.getUserID()+");";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deletConsumedBy(User us, Service ser) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "delete from consumedby where service_code = "+ser.getCOD()+" and user_id_user = "+us.getUserID()+";";
            ResultSet res = stmt.executeQuery(sql);
            
            stmt.close();
            return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<User> getAllUserConsumingService(Service ser) {
        try {
            
            ArrayList<User> output = new ArrayList<User>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select u.id_user, u.username, u.type, u.usage from users u, service s, consumedby c where c.service_code = s.code and c.user_id_user = u.id_user and s.code = "+ser.getCOD()+";";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                User us = new User(res.getInt("id_user"), res.getString("username"), res.getBoolean("type"));
                us.setUsage(res.getInt("usage"));
                output.add(us);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<Service> getAllServiceConsumedByUser(User us) {
        try {
            
            ArrayList<Service> output = new ArrayList<Service>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select s.code, s.description, s.service, s.permission, s.cost from users u, service s, consumedby c where c.service_code = s.code and c.user_id_user = u.id_user and u.id_user = "+us.getUserID()+";";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                Service ser = new Service(res.getString("description"), res.getString("service"), res.getString("permission"), res.getFloat("cost"));
                ser.setCOD(res.getInt("code"));
                output.add(ser);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int addService(Service serv) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into service(description, service, permission, cost) values('"+serv.getDescription()+"', '"+serv.getService()+"', '"+serv.getPermission()+"', "+serv.getCost()+") returning code;";
            ResultSet res = stmt.executeQuery(sql);
            res.next(); //By calling one time next the first tuple became selected
            int id = res.getInt(1); //The number passing the get method represents the collum.
            stmt.close();
            serv.setCOD(id);
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public boolean updateService(Service ser) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "update service set description = '"+ser.getDescription()+"', service = '"+ser.getService()+"', permission = '"+ser.getPermission()+"', cost = "+ser.getCost()+" where code = "+ser.getCOD()+";";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    public boolean deleteServcie(Service ser) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from offers where service_code = "+ser.getCOD()+";";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()) {
                throw new Exception("This Service is still linked to offers");
            }
            
            sql = "delete from service where code = "+ser.getCOD()+";";
            stmt.executeUpdate(sql);
            
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public ArrayList<Service> getAllService() {
        try {
            
            ArrayList<Service> output = new ArrayList<Service>();
            
            Statement stmt = connection.createStatement();
            
            String sql = "select * from service;";
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                Service ser = new Service(res.getString("description"), res.getString("service"), res.getString("permission"), res.getFloat("cost"));
                ser.setCOD(res.getInt("code"));
                output.add(ser);
            }
            
            stmt.close();
            
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
                    + "   level INT NOT NULL,\n"
                    + "   description  VARCHAR(300) NULL,\n"
                    + "  PRIMARY KEY ( protocol_id )\n"
                    + ");\n"
                    + " \n"
                    /*
                    + "CREATE TABLE IF NOT EXISTS NetworkTopology (\n"
                    + "  name VARCHAR(15) NOT NULL,\n"
                    + "  structure VARCHAR(300) NULL,\n"
                    + "  PRIMARY KEY (name)\n"
                    + ");\n"
                    + " \n"
                    */
                    + "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "  id_user SERIAL NOT NULL,\n"
                    + "  username VARCHAR(45) NOT NULL,\n"
                    + "  type BOOLEAN NULL,\n"
                    + "  usage INT NULL,\n"
                    + "  pwhash INT NOT NULL,\n"
                    + "  PRIMARY KEY (id_user)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS Service (\n"
                    + "  code SERIAL,\n"
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
                    /*
                    + "CREATE TABLE IF NOT EXISTS NetworkLink (\n"
                    + "  id_net_link INT NOT NULL,\n"
                    + "  type VARCHAR(45) NULL,\n"
                    + "  velocity VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (id_net_link)\n"
                    + ");\n"
                    + " \n"
                    */
                    + "CREATE TABLE IF NOT EXISTS Network (\n"
                    + "  id_network SERIAL,\n"
                    + "  Name VARCHAR(45) NULL,\n"
                    + "  Description VARCHAR(300) NULL,\n"
                    + "  Color VARCHAR(20) NULL,\n"
                    + "  NetworkType_id_net_type VARCHAR(3) NOT NULL,\n"
                    + "  ComunicationProtocol_id INT NOT NULL,\n"
                    + "  PRIMARY KEY(id_network),\n"
                    + "    FOREIGN KEY (NetworkType_id_net_type)\n"
                    + "    REFERENCES NetworkType(id_net_type),\n"
                    + "  FOREIGN KEY (ComunicationProtocol_id)\n"
                    + "  REFERENCES ComunicationProtocol(protocol_id)\n"
                    + ");\n"
                    + " \n"
                     + "CREATE TABLE IF NOT EXISTS Node (\n"
                    + "  id_node SERIAL,\n"
                    + "  ip_address VARCHAR(15) NOT NULL,\n"
                    + "  network_id_network INT NOT NULL,\n"
                    + "  angle REAL,\n"
                    + "  distance REAL,\n"
                    + "  status VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (id_node),\n"
                    + "    FOREIGN KEY (network_id_network)\n"
                    + "    REFERENCES Network(id_network)\n"
                    + ");\n"
                    + " \n"
                    + "CREATE TABLE IF NOT EXISTS NodeConnection (\n"
                    + "  id1 INT NOT NULL,\n"
                    + "  id2 INT NOT NULL,\n"
                    + "  type VARCHAR(20) NULL,\n"
                    + "  velocity REAL NULL,\n"
                    + "  PRIMARY KEY (id1, id2),\n"
                    + "   \n"
                    + "    FOREIGN KEY (id1)\n"
                    + "    REFERENCES Node(id_node),\n"
                    + "     \n"
                    + "    FOREIGN KEY (id1)\n"
                    + "    REFERENCES Node(id_node)\n"
                    + ");\n"
                    + "   \n"
                    + "CREATE TABLE IF NOT EXISTS Offers (\n"
                    + "  Network_id_network INT NOT NULL,\n"
                    + "  Service_code INT NOT NULL,\n"
                    + "  PRIMARY KEY (Network_id_network, Service_code),\n"
                    + "    FOREIGN KEY (Network_id_network)\n"
                    + "    REFERENCES Network (id_network),\n"
                    + "    FOREIGN KEY (Service_code)\n"
                    + "    REFERENCES Service (code)\n"
                    + ");\n"
                    + " \n";
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
            String sql = "DROP TABLE Offers;\n"
                    + "DROP TABLE NodeConnection;\n"
                    + "DROP TABLE Node;\n"
                    
                    + "DROP TABLE Network;\n"
                    /*
                    + "DROP TABLE NetworkLink;\n"
                    */
                    + "DROP TABLE ConsumedBy;\n"
                    + "DROP TABLE Service;\n"
                    + "DROP TABLE Users;\n"
                    /*
                    + "DROP TABLE NetworkTopology;\n"
                    */
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
    
    private void insetDefNetworkTypes() {
        insertNetworkType("LAN", "Local Area Network");
        insertNetworkType("PAN", "Personal Area Network");
        insertNetworkType("MAN", "Metropolitan Area Network");
        insertNetworkType("WAN", "Wide Area Network");
        insertNetworkType("GAN", "Global Area Network");
    }
    
    private void insertNetworkType(String name, String desc) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from networktype where id_net_type = '"+name+"';";
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                sql = "insert into networktype(id_net_type, description) values('"+name+"', '"+desc+"');";
                stmt.executeUpdate(sql);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private void insertDefCommunicationProtocol() {
        insertCommunicationProtocol("TCP", 1, "Transmission Control Protoco");
        insertCommunicationProtocol("UDP", 1, "User Datagram Protocol");
        insertCommunicationProtocol("ICMP", 1, "Internet Control Message Protocol");
        insertCommunicationProtocol("HTTP", 1, "Hypertext Transfer Protocol");
        insertCommunicationProtocol("DNS", 1, "Domain Name System");
        insertCommunicationProtocol("SMTP", 1, "Simple Mail Transfer Protocol");
        
    }
    
    private void insertCommunicationProtocol(String name, int level, String desc) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from comunicationprotocol where name = '"+name+"';";
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                sql = "insert into comunicationprotocol(name, level, description) values('"+name+"', "+level+", '"+desc+"');";
                stmt.executeUpdate(sql);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertExampleData() {
        addNetworkType(new NetworkType("LAN", "local"));
        //addNetworkTopology(new NetworkTopology("star", "normal"));
        addCommunicationProtocol(new CommunicationProtocol("TCP", 1, "def")); 
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
