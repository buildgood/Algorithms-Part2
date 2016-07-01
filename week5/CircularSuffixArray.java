import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class CircularSuffixArray {
	
	private final int THRESHOLD = 30;
	private String s;
	private int N;
	private int[] index;
	
    public CircularSuffixArray(String s)  // circular suffix array of s
    {
    	if(s == null) throw new NullPointerException();
    	this.s = s;
    	N = s.length();
    	index = new int[N];
    	
    	for(int i = 0; i < N; i++) {
    		index[i] = i;
    	}
    	sort(0, N - 1, 0);
    }
    
    private void sort(int lo, int hi, int offset) {
    	if(hi - lo <= THRESHOLD) {
    		insertionSort(lo, hi, offset);
    		return;
    	}
    	
    	int lt = lo;
    	int gt = hi;
    	int v = getChar(index[lo], offset);
    	int i = lo + 1;
    	while(i <= gt) {
    		int t = getChar(index[i], offset);
    		if(t < v) exch(lt++, i++);
    		else if(t > v) exch(i, gt--);
    		else i++;
    	}
    	sort(lo, lt - 1, offset);
    	if(v >= 0) sort(lt, gt, offset + 1);
    	sort(gt + 1, hi, offset);
    }
    
    private char getChar(int start, int offset) {
    	return s.charAt((start + offset) % N);
    }
    
    private void exch(int i, int j) {
    	int temp = index[i];
    	index[i] = index[j];
    	index[j] = temp;
    }
    
    private void insertionSort(int lo, int hi, int offset) {
    	for(int i = lo; i <= hi; i++)
    		for(int j = i; j > lo && less(j, j-1, offset); j--) {
    			exch(j, j - 1);
    		}
    }
    
    private boolean less(int a, int b, int offset) {
    	int ai = index[a];
    	int bi = index[b];
    	for(int i = offset; i < N; i++) {
    		int ca = getChar(ai, i);
    		int cb = getChar(bi, i);
    		if(ca < cb) return true;
    		else if(ca > cb) return false;
    	}
    	return false;
    }
    
    public int length()                   // length of s
    {
    	return N;
    }
    
    public int index(int i)               // returns index of ith sorted suffix
    {
    	if(i < 0 || i > N-1) throw new IndexOutOfBoundsException();
    	return index[i];
    }
    
    public static void main(String[] args)// unit testing of the methods (optional)
    {
    	CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i) + " ");
        }
    }
    
}
