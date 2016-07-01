import edu.princeton.cs.algs4.*;

public class Outcast {
	
   private WordNet wordnet;
   
   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
   {
	   this.wordnet = wordnet;
   }
   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
   {
	   String outcast = null;
	   int max = 0;
	   for(String nounA : nouns) {
		   int d = 0;
		   for(String nounB : nouns) {
			   if(nounA != nounB) {
				   d += wordnet.distance(nounA, nounB);
			   }
		   }
		   if(d > max) {
			   max = d;
			   outcast = nounA;
		   }
	   }
	   return outcast;
   }
   public static void main(String[] args)  // see test client below
   {
	   WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
   }
}