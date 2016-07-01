import edu.princeton.cs.algs4.*;
import java.util.*;

public class BoggleSolver
{
	private Tries ts;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	ts = new Tries();
    	for(String s : dictionary) {
    		if(s.length() > 2) ts.put(s);
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	int row = board.rows();
    	int col = board.cols();
    	boolean[][] visited = new boolean[row][col];
    	Set<String> words = new HashSet<String>();
    	
    	for(int i = 0; i < row; i++)
    		for(int j = 0; j < col; j++) {
    			String keyFind = getLetter(board, i, j);
    			searchWords(board, i, j, visited, keyFind, words, ts.getNode(keyFind));
    		}
    	
    	return words;
    }
    
    private void searchWords(BoggleBoard board, int row, int col, boolean[][] visited, String keyFind, Set<String> words, Node x) {
    	if(x == null) return;
    	if(x.val) words.add(keyFind);
    	
    	visited[row][col] = true;
    	
    	for(int i = Math.max(0, row-1); i <= row+1 && i < board.rows(); i++)
    		for(int j = Math.max(0, col-1); j <= col+1 && j < board.cols(); j++) {
    			if(!visited[i][j]) {
    				char c = board.getLetter(i, j);
    				Node n = x.next[c-'A'];
    				
    				if(n != null) {
    					
    					if(c == 'Q')
    						searchWords(board, i, j, visited, keyFind+"QU", words, n.next['U'-'A']);
    					else
    						searchWords(board, i, j, visited, keyFind+c, words, n);
    				}
    				
    			}
    			
    		}
    	visited[row][col] = false;
    }
    
    private String getLetter(BoggleBoard board, int row, int col) {
    	char c = board.getLetter(row, col);
    	if(c == 'Q') return "QU";
    	else return String.valueOf(c);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if(word.length() < 3 || !ts.contains(word)) return 0;
    	switch(word.length()) {
    	case 3:
    	case 4: return 1;
    	case 5: return 2;
    	case 6: return 3;
    	case 7: return 5;
    	default: return 11;
    	}
    	
    }
    
    //Tries
    private static class Node {
    	private boolean val;
    	private Node[] next = new Node[26];
    }
    
    private class Tries {
    	
    	private Node root;
    	
    	public boolean get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.val;
        }
    	
    	public Node getNode(String key) {
    		return get(root, key, 0);
    	}
    	
    	private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c-'A'], key, d+1);
        }
    	
    	public boolean contains(String key) {
    		return get(key) != false;
    	}
    	
    	public void put(String key) {
            root = put(root, key, true, 0);
        }

        private Node put(Node x, String key, boolean val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c-'A'] = put(x.next[c-'A'], key, val, d+1);
            return x;
        }
    	
    }
    
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
