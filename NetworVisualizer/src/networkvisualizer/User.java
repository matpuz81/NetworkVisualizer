/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

/**
 *
 * @author Jessica
 */
public class User {
    
    private String username;
    private boolean isAdmin;
    private int userID, usage;
    private int pwHash;
    
    public User() {
        
    }
    
    public User(int userID, String username, boolean isAdmin) {
        this.userID = userID;
        this.username = username;
        this.isAdmin = isAdmin;
    }
    
    public void setPwHash(int pw) {
        this.pwHash = pw;
    }
    
    public int getPwHash() {
        return this.pwHash;
    }

    public int getUserID() {
        return userID;
    }
    
    public void setUsage(int us) {
        this.usage = us;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getUsage() {
        return usage;
    }

    public void incrementServiceUsage() {
        usage++;
    }  
    
    public void decrementServiceUsage() {
        usage--;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;   
    }
}
