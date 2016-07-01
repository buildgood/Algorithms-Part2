import java.util.*;
import edu.princeton.cs.algs4.*;;

public class MoveToFront {
	private static final int R = 256;
	
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	LinkedList<Character> ch = new LinkedList<Character>();
    	for(char i = 0; i < R; i++) {
    		ch.add(i);
    	}
    	while(!BinaryStdIn.isEmpty()) {
    		char c = BinaryStdIn.readChar();
    		int index = ch.indexOf(c);
    		ch.remove(index);
    		BinaryStdOut.write((char)index);
    		ch.addFirst(c);
    	}
    	BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	LinkedList<Character> ch = new LinkedList<Character>();
    	for(char i = 0; i < R; i++) {
    		ch.add(i);
    	}
    	while(!BinaryStdIn.isEmpty()) {
    		char index = BinaryStdIn.readChar();
    		char c = ch.remove(index);
    		BinaryStdOut.write(c);
    		ch.addFirst(c);
    	}
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
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