/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

/**
 *
 * @author chef
 */
public class NodeConnection {
    
    Node n1, n2;
    private String type;
    private double velocity;
    
    public NodeConnection(Node n1, Node n2, String type, double velocity)
    {
        this.n1=n1;
        this.n2=n2;
        this.type=type;
        this.velocity=velocity;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
