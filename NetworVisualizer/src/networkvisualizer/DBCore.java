/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author matthiaskeim
 */
public class DBCore {
    
    public static void test() {
      Connection c = null;
      try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testDB", "postgres", "password");
      } catch (Exception e) {
         System.out.println("Connection to DB faild!");
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");
    }
    
}
