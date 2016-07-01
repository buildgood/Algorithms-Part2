import edu.princeton.cs.algs4.*;
import java.util.*;
import java.lang.*;

public class WordNet {
	
	private Digraph G;
	private SAP sap;
	private int countSyn = 0;
	private Map<String, ArrayList<Integer>> nounsList = new HashMap<String, ArrayList<Integer>>();
	private Map<Integer, String> synsetsList = new HashMap<Integer, String>();
	private Map<Integer, ArrayList<Integer>> edgeList = new HashMap<Integer, ArrayList<Integer>>();
	
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
	   if(synsets == null || hypernyms == null) throw new NullPointerException();
	   getSynsets(synsets);
	   getHypernyms(hypernyms);
	   G = new Digraph(countSyn);
	   
	   for(Map.Entry<Integer, ArrayList<Integer>> entry : edgeList.entrySet()) {
		   for(Integer w : entry.getValue()) {
			   G.addEdge(entry.getKey(), w);
		   }
	   }
	   
	   DirectedCycle cycle = new DirectedCycle(G);
	   if(cycle.hasCycle()) throw new IllegalArgumentException();
	   
	   int root = 0;
	   for(int i = 0; i < G.V(); i++) {
		   if(!G.adj(i).iterator().hasNext()) root++;
	   }
	   if(root != 1) throw new IllegalArgumentException();
	   
	   sap = new SAP(G);
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
	   return nounsList.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
	   if(word == null) throw new NullPointerException();
	   return nounsList.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
	   if(nounA == null || nounB == null) throw new NullPointerException();
	   if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
	   return sap.length(nounsList.get(nounA), nounsList.get(nounB));
	   
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
	   if(nounA == null || nounB == null) throw new NullPointerException();
	   if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
	   int ancestor = sap.ancestor(nounsList.get(nounA), nounsList.get(nounB));
	   return synsetsList.get(ancestor);
   }
   
   private void getSynsets(String synsets) {
	   In in = new In(synsets);
	   String line = null;
	   ArrayList<Integer> list;
	   String synsetsNoun;
	   
	   while((line = in.readLine()) != null) {
		   
		   if(line.equals("")) continue;
		   String[] lineElem = line.split(",");
		   String[] noun = lineElem[1].split(" ");
		   int synsetsId = Integer.parseInt(lineElem[0]);
		   
		   for(String n : noun) {
			   
			   if(nounsList.containsKey(n)) {
				   list = nounsList.get(n);
			   }else {
				   list = new ArrayList<Integer>();
			   }
			   
			   list.add(synsetsId);
			   nounsList.put(n, list);
			   synsetsNoun = lineElem[1];
			   synsetsList.put(synsetsId, synsetsNoun);
		   }
		   countSyn++;
	   }
   }
   
   private void getHypernyms(String hypernyms) {
	   In in = new In(hypernyms);
	   String line = null;
	   ArrayList<Integer> list;
	   
	   while((line = in.readLine()) != null){
		   
		   if(line.equals("")) continue;
		   String[] lineElem = line.split(",");
		   int edgeId = Integer.parseInt(lineElem[0]);
		   
		   if(edgeList.containsKey(edgeId)) {
			   list = edgeList.get(edgeId);
		   }else {
			   list = new ArrayList<Integer>();
		   }
		   
		   for(int i = 1; i < lineElem.length; i++) {
			   list.add(Integer.parseInt(lineElem[i]));
		   }
		   
		   edgeList.put(edgeId, list);
	   }
   }

   // do unit testing of this class
   public static void main(String[] args) {
	   WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
		System.out.println(w.sap("Rameses_the_Great", "Henry_Valentine_Miller")); 
		System.out.println(w.distance("Rameses_the_Great", "Henry_Valentine_Miller")); 
   }
}
