import java.util.*;
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {
	
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s = BinaryStdIn.readString();
    	CircularSuffixArray csa = new CircularSuffixArray(s);
    	int first = 0;
    	int N = s.length();
    	
    	while(first < N && csa.index(first) != 0) {
    		first++;
    	}
    	BinaryStdOut.write(first);
    	for(int i = 0; i < s.length(); i++) {
    		BinaryStdOut.write(s.charAt((csa.index(i) + N - 1) % N));
    	}
    	
    	BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    	final int R = 256;
    	int first = BinaryStdIn.readInt();
    	String t = BinaryStdIn.readString();
    	int N = t.length();
    	int[] next = new int[N];
    	int[] count = new int[R+1];
    	
    	for(int i = 0; i < N; i++) {
    		count[t.charAt(i) + 1]++;
    	}
    	for(int i = 1; i < R+1; i++) {
    		count[i] += count[i-1];
    	}
    	for(int i = 0; i < N; i++) {
    		next[count[t.charAt(i)]++] = i;
    	}
    	int c = next[first];
    	for(int i = 0; i < N; i++) {
    		BinaryStdOut.write(t.charAt(c));
    		c = next[c];
    	}
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
    	 if (args.length < 1) {
             return;
         }
         if ("-".equals(args[0])) {
             encode();
         }
         if ("+".equals(args[0])) {
             decode();
         }
    }
}
