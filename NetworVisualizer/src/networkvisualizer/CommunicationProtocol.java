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
public class CommunicationProtocol {
    
    private int protocol_id;
    private String name;
    private int level;
    private String description;
    
    public CommunicationProtocol(String name, int level, String desc) {
        this.name = name;
        this.level = level;
        this.description = desc;
    }
    
    public CommunicationProtocol(int id, String name, int level, String desc) {
        this(name, level, desc);
        this.protocol_id = id;
    }
    
    public void setId(int id) {
        this.protocol_id = id;
    }
    
    public int getId() {
        return this.protocol_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
