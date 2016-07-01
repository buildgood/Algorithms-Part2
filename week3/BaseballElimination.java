import edu.princeton.cs.algs4.*;
import java.lang.*;
import java.util.*;

public class BaseballElimination {
	
	private int N;
	private HashMap<String, Integer> map;
	private String[] tm;
	private int[] w, l, r;
	private int[][] g;
	
	public BaseballElimination(String filename)        // create a baseball division from given filename in format specified below
	{
		In in = new In(filename);
		N = in.readInt();
		map = new HashMap<String, Integer>();
		tm = new String[N];
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		for(int i = 0; i < N; i++) {
			
			tm[i] = in.readString();
			map.put(tm[i], i);
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for(int j = 0; j < N; j++) {
				g[i][j] = in.readInt();
			}
		}
		
	}
	
	public int numberOfTeams()                        // number of teams
	{
		return N;
	}
	
	public Iterable<String> teams()                    // all teams
	{
		return map.keySet();
	}
	
	public int wins(String team)                      // number of wins for given team
	{
		checkValid(team);
		return w[map.get(team)];
	}
	
	public int losses(String team)                    // number of losses for given team
	{
		checkValid(team);
		return l[map.get(team)];
	}
	
	public int remaining(String team)                 // number of remaining games for given team
	{
		checkValid(team);
		return r[map.get(team)];
	}
	
	public int against(String team1, String team2)    // number of remaining games between team1 and team2
	{
		checkValid(team1);
		checkValid(team2);
		return g[map.get(team1)][map.get(team2)];
	}
	
	public boolean isEliminated(String team)           // is given team eliminated?
	{
		checkValid(team);
		int id = map.get(team);
		
		for(int i = 0; i < N; i++) {
			if(w[id] + r[id] < w[i]) return true;
		}
		
		FlowNetwork fn = constructNet(id);
		int s = 0;
		int t = fn.V() - 1;
		
		FordFulkerson ff = new FordFulkerson(fn, s, t);
		
		if(eliminated(fn, s)) return true;
		
		return false;
	}
	
	private FlowNetwork constructNet(int id) {
		int nVex = N + 2;
		for(int i = 0; i < N; i++)
			for(int j = i + 1; j < N; j++) {
				if(g[i][j] > 0 && i != id && j != id) nVex++;
			}
		int s = 0;
		int t = nVex - 1;
		
		FlowNetwork fn = new FlowNetwork(nVex);
		
		int cur = 1;
		for(int i = 0; i < N; i++)
			for(int j = i + 1; j < N; j++) {
				if(g[i][j] > 0 && i != id && j != id) {
					FlowEdge e = new FlowEdge(s, cur, g[i][j]);
					fn.addEdge(e);
					
					FlowEdge e1 = new FlowEdge(cur, t - N + i, Double.POSITIVE_INFINITY);
					fn.addEdge(e1);
					FlowEdge e2 = new FlowEdge(cur, t - N + j, Double.POSITIVE_INFINITY);
					fn.addEdge(e2);
					
					cur++;
				}
			}
		
		for(int i = 0; i < N; i++) {
			FlowEdge e = new FlowEdge(t - N + i, t, w[id] +r[id] - w[i]);
			fn.addEdge(e);
		}
		
		return fn;
	}
	
	private boolean eliminated(FlowNetwork fn, int s) {
		for(FlowEdge fe : fn.adj(s)) {
			int to = fe.other(s);
			if(fe.residualCapacityTo(to) > 0) return true;
		}
		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		checkValid(team);
		int id = map.get(team);
		ArrayList<String> it = new ArrayList<String>();
		for(int i = 0; i < N; i++) {
			if(w[id] + r[id] < w[i]) {
				it.add(tm[i]);
			}
		}
		if(!it.isEmpty()) return (Iterable<String>)it;
		
		FlowNetwork fn = constructNet(id);
		int s = 0;
		int t = fn.V() - 1;
		
		FordFulkerson ff = new FordFulkerson(fn, s, t);
		
		if(eliminated(fn, s)) {
			for(int i = 0; i < N; i++) {
				if(i != id && ff.inCut(t - N + i)) it.add(tm[i]);
			}
			return (Iterable<String>)it;
		}
		
	    return null;
	}
	
	private void checkValid(String team) {
		if(team == null) throw new IllegalArgumentException();
		if(!map.containsKey(team)) throw new IllegalArgumentException();
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
