import edu.princeton.cs.algs4.*;
import java.lang.*;

public class SAP {
	
	private Digraph G;
	
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
	   if(G == null) throw new NullPointerException();
	   Digraph copy = new Digraph(G); 
	   this.G = copy;
   }
   
   private boolean isValid(int v) {
	   return (0 <= v && v <= G.V()-1);
   }
   
   private boolean isValid(Iterable<Integer> v) {
	   for(Integer i : v) {
		   if(!isValid(i)) return false;
	   }
	   return true;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
	   if(!isValid(v) || !isValid(w)) throw new IndexOutOfBoundsException(); 
	   int[] res = shortest(v, w);
	   return res[0];
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
	   if(!isValid(v) || !isValid(w)) throw new IndexOutOfBoundsException();
	   int[] res = shortest(v, w);
	   return res[1];
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
	   if(v == null || w == null) throw new NullPointerException();
	   if(!isValid(v) || !isValid(w)) throw new IndexOutOfBoundsException();
	   int[] res = shortest(v, w);
	   return res[0];
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
	   if(v == null || w == null) throw new NullPointerException();
	   if(!isValid(v) || !isValid(w)) throw new IndexOutOfBoundsException();
	   int[] res = shortest(v, w);
	   return res[1];
   }
   
   private int[] shortest(int v, int w) {
	   DeluxeBFS bfsV = new DeluxeBFS(G, v);
	   DeluxeBFS bfsW = new DeluxeBFS(G, w);
	   boolean[] vMarked = bfsV.getMarked();
	   boolean[] wMarked = bfsW.getMarked();
	   int shortestLength = Integer.MAX_VALUE;
	   int length = Integer.MAX_VALUE;
	   int ancestor = -1;
	   for(int i = 0; i < vMarked.length; i++) {
		   if(vMarked[i] && wMarked[i]) {
			   length = bfsV.distTo(i) + bfsW.distTo(i);
			   if(length < shortestLength) {
				   shortestLength = length;
				   ancestor = i;
			   }
		   }
	   }
	   int[] res = new int[2];
	   if(ancestor == -1) {
		   res[0] = -1;
		   res[1] = -1;
	   }else {
		   res[0] = shortestLength;
		   res[1] = ancestor;
	   }
	   return res;
   }
   
   private int[] shortest(Iterable<Integer> v, Iterable<Integer> w) {
	   DeluxeBFS bfsV = new DeluxeBFS(G, v);
	   DeluxeBFS bfsW = new DeluxeBFS(G, w);
	   boolean[] vMarked = bfsV.getMarked();
	   boolean[] wMarked = bfsW.getMarked();
	   int shortestLength = Integer.MAX_VALUE;
	   int length = Integer.MAX_VALUE;
	   int ancestor = -1;
	   for(int i = 0; i < vMarked.length; i++) {
		   if(vMarked[i] && wMarked[i]) {
			   length = bfsV.distTo(i) + bfsW.distTo(i);
			   if(length < shortestLength) {
				   shortestLength = length;
				   ancestor = i;
			   }
		   }
	   }
	   int[] res = new int[2];
	   if(ancestor == -1) {
		   res[0] = -1;
		   res[1] = -1;
	   }else {
		   res[0] = shortestLength;
		   res[1] = ancestor;
	   }
	   return res;
   }

   // do unit testing of this class
   public static void main(String[] args) {
	   In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
     }
   }
}