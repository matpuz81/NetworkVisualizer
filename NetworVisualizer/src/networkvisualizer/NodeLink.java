/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

/**
 *
 * @author chef
 */
public class NodeLink {
    
    Node n1, n2;
    String type;
    double velocity;
    
    public NodeLink(Node n1, Node n2, String type, double velocity)
    {
        this.n1=n1;
        this.n2=n2;
        this.type=type;
        this.velocity=velocity;
    }
}
