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
public class NetworkTopology {
    
    private String name;
    private String structure;
    
    public NetworkTopology (String name, String description) {
        this.name = name;
        this.structure = description;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getStructure() {
        return this.structure;
    }
}
