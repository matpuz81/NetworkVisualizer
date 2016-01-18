/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

/**
 *
 * @author matthiaskeim
 */
public class ComunicationProtocol {
    
    private int protocol_id;
    private String name;
    private String level;
    private String description;
    
    public ComunicationProtocol(String name, String level, String desc) {
        this.name = name;
        this.level = level;
        this.description = desc;
    }
    
    public ComunicationProtocol(int id, String name, String level, String desc) {
        this.protocol_id = id;
        this.name = name;
        this.level = level;
        this.description = desc;
    }
    
    public void setId(int id) {
        this.protocol_id = id;
    }
    
    public int getId() {
        return this.protocol_id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getLevel() {
        return this.level;
    }
    
    public String getDescription() {
        return this.description;
    }
    
}
