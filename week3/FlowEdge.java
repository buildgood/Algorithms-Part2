import edu.princeton.cs.algs4.*;

public class FlowEdge {
    private final int v;             // from
    private final int w;             // to 
    private final double capacity;   // capacity
    private double flow;             // flow

    /**
     * Initializes an edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>capacity</tt> and zero flow.
     * @param v the tail vertex
     * @param w the head vertex
     * @param capacity the capacity of the edge
     * @throws java.lang.IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws java.lang.IllegalArgumentException if <tt>capacity</tt> is negative
     */
    public FlowEdge(int v, int w, double capacity) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (!(capacity >= 0.0)) throw new IllegalArgumentException("Edge capacity must be non-negative");
        this.v         = v;
        this.w         = w;  
        this.capacity  = capacity;
        this.flow      = 0.0;
    }

    /**
     * Initializes an edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>capacity</tt> and <tt>flow</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param capacity the capacity of the edge
     * @param flow the flow on the edge
     * @throws java.lang.IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws java.lang.IllegalArgumentException if <tt>capacity</tt> is negative
     * @throws java.lang.IllegalArgumentException unless <tt>flow</tt> is between 
     *    <tt>0.0</tt> and <tt>capacity</tt>.
     */
    public FlowEdge(int v, int w, double capacity, double flow) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (!(capacity >= 0.0))  throw new IllegalArgumentException("Edge capacity must be non-negative");
        if (!(flow <= capacity)) throw new IllegalArgumentException("Flow exceeds capacity");
        if (!(flow >= 0.0))      throw new IllegalArgumentException("Flow must be non-negative");
        this.v         = v;
        this.w         = w;  
        this.capacity  = capacity;
        this.flow      = flow;
    }

    /**
     * Initializes a flow edge from another flow edge.
     * @param e the edge to copy
     */
    public FlowEdge(FlowEdge e) {
        this.v         = e.v;
        this.w         = e.w;  
        this.capacity  = e.capacity;
        this.flow      = e.flow;
    }

    /**
     * Returns the tail vertex of the edge.
     * @return the tail vertex of the edge
     */
    public int from() {
        return v;
    }  

    /**
     * Returns the head vertex of the edge.
     * @return the head vertex of the edge
     */
    public int to() {
        return w;
    }  

    /**
     * Returns the capacity of the edge.
     * @return the capacity of the edge
     */
    public double capacity() {
        return capacity;
    }

    /**
     * Returns the flow on the edge.
     * @return the flow on the edge
     */
    public double flow() {
        return flow;
    }

    /**
     * Returns the endpoint of the edge that is different from the given vertex
     * (unless the edge represents a self-loop in which case it returns the same vertex).
     * @param vertex one endpoint of the edge
     * @return the endpoint of the edge that is different from the given vertex
     *   (unless the edge represents a self-loop in which case it returns the same vertex)
     * @throws java.lang.IllegalArgumentException if <tt>vertex</tt> is not one of the endpoints
     *   of the edge
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Returns the residual capacity of the edge in the direction
     *  to the given <tt>vertex</tt>.
     * @param vertex one endpoint of the edge
     * @return the residual capacity of the edge in the direction to the given vertex
     *   If <tt>vertex</tt> is the tail vertex, the residual capacity equals
     *   <tt>capacity() - flow()</tt>; if <tt>vertex</tt> is the head vertex, the
     *   residual capacity equals <tt>flow()</tt>.
     * @throws java.lang.IllegalArgumentException if <tt>vertex</tt> is not one of the endpoints
     *   of the edge
     */
    public double residualCapacityTo(int vertex) {
        if      (vertex == v) return flow;              // backward edge
        else if (vertex == w) return capacity - flow;   // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Increases the flow on the edge in the direction to the given vertex.
     *   If <tt>vertex</tt> is the tail vertex, this increases the flow on the edge by <tt>delta</tt>;
     *   if <tt>vertex</tt> is the head vertex, this decreases the flow on the edge by <tt>delta</tt>.
     * @param vertex one endpoint of the edge
     * @throws java.lang.IllegalArgumentException if <tt>vertex</tt> is not one of the endpoints
     *   of the edge
     * @throws java.lang.IllegalArgumentException if <tt>delta</tt> makes the flow on
     *   on the edge either negative or larger than its capacity
     * @throws java.lang.IllegalArgumentException if <tt>delta</tt> is <tt>NaN</tt>
     */
    public void addResidualFlowTo(int vertex, double delta) {
        if      (vertex == v) flow -= delta;           // backward edge
        else if (vertex == w) flow += delta;           // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
        if (Double.isNaN(delta)) throw new IllegalArgumentException("Change in flow = NaN");
        if (!(flow >= 0.0))      throw new IllegalArgumentException("Flow is negative");
        if (!(flow <= capacity)) throw new IllegalArgumentException("Flow exceeds capacity");
    }


    /**
     * Returns a string representation of the edge.
     * @return a string representation of the edge
     */
    public String toString() {
        return v + "->" + w + " " + flow + "/" + capacity;
    }


   /**
     * Unit tests the <tt>FlowEdge</tt> data type.
     */
    public static void main(String[] args) {
        FlowEdge e = new FlowEdge(12, 23, 3.14);
        StdOut.println(e);
    }

}
