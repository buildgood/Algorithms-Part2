import edu.princeton.cs.algs4.*;
import java.lang.*;
import java.util.*;
import java.awt.*;

public class SeamCarver {
	
   private double[][] energy;
   private int[][] color;
   private int width;
   private int height;
   
   public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
   {
	   if(picture == null) throw new NullPointerException();
	   width = picture.width();
	   height = picture.height();
	   energy = new double[width][height];
	   color = new int[width][height];
	   
	   for(int i = 0; i < width; i++)
		   for(int j = 0; j < height; j++) {
			   color[i][j] = parseColor(picture.get(i, j));
		   } 
	   
	   for(int i = 0; i < width; i++)
		   for(int j = 0; j < height; j++) {
			   energy[i][j] = energy(i, j);
		   }
	   
   }
   
   private int parseColor(Color color) {
	   return (color.getRed() << 16) | (color.getGreen() << 8) | (color.getBlue());
   }
   
   private int getRed(int color) {
	   return color >> 16;
   }
   
   private int getGreen(int color) {
	   return (color >> 8) & 0xFF;
   }
   
   private int getBlue(int color) {
	   return color & 0xFF;
   }
   
   private Color toColor(int color) {
	   return new Color(getRed(color), getGreen(color), getBlue(color));
   }
   
   public Picture picture()                          // current picture
   {
	   Picture picture = new Picture(width, height);
	   for(int i = 0; i < width; i++)
		   for(int j = 0; j < height; j++) {
			   picture.set(i, j, toColor(color[i][j]));
		   }
	   return picture;
   }
   
   public int width()                            	// width of current picture
   {
	   return width;
   }
   
   public int height()                           	// height of current picture
   {
	   return height;
   }
   
   public double energy(int x, int y)               // energy of pixel at column x and row y
   {
	   if(x < 0 || x >= width || y < 0 || y >= height) throw new IndexOutOfBoundsException();
	   if(x == 0 || x == width-1 || y == 0 || y == height-1) return 1000.0;
	   
	   int rx = Math.abs(getRed(color[x-1][y]) - getRed(color[x+1][y]));
	   int gx = Math.abs(getGreen(color[x-1][y]) - getGreen(color[x+1][y]));
	   int bx = Math.abs(getBlue(color[x-1][y]) - getBlue(color[x+1][y]));
	   
	   int ry = Math.abs(getRed(color[x][y-1]) - getRed(color[x][y+1]));
	   int gy = Math.abs(getGreen(color[x][y-1]) - getGreen(color[x][y+1]));
	   int by = Math.abs(getBlue(color[x][y-1]) - getBlue(color[x][y+1]));
	   
	   return Math.sqrt((double)(rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by));
   }
   
   public int[] findHorizontalSeam()               	// sequence of indices for horizontal seam
   {
	   int[] seam = new int[width];
	   if(height == 1) return seam;
	   
	   double[][] distTo = new double[width][height];
	   int[][] pathFrom = new int[width][height];
	   
	   for(int i = 0; i < height; i++) {
		   distTo[0][i] = energy[0][i];
		   pathFrom[0][i] = 0;
	   }
	    
	   for(int i = 1; i < width; i++)
		   for(int j = 0; j < height; j++) {
			   
			   if(j == 0){
				   distTo[i][j] = distTo[i-1][j] + energy[i][j];
				   pathFrom[i][j] = j;
				   if(distTo[i][j] > distTo[i-1][j+1] + energy[i][j]) {
					   distTo[i][j] = distTo[i-1][j+1] + energy[i][j];
					   pathFrom[i][j] = j+1;
				   }
				   
			   }else if(j == height-1) {
				   distTo[i][j] = distTo[i-1][j-1] + energy[i][j];
				   pathFrom[i][j] = j-1;
				   if(distTo[i][j] > distTo[i-1][j] + energy[i][j]) {
						distTo[i][j] = distTo[i-1][j] + energy[i][j];
						pathFrom[i][j] = j;
				   }
					   
			   }else {
				   distTo[i][j] = distTo[i-1][j-1] + energy[i][j];
				   pathFrom[i][j] = j-1;
				   if(distTo[i][j] > distTo[i-1][j] + energy[i][j]) {
					   distTo[i][j] = distTo[i-1][j] + energy[i][j];
					   pathFrom[i][j] = j;
			       }
				   if(distTo[i][j] > distTo[i-1][j+1] + energy[i][j]) {
						distTo[i][j] = distTo[i-1][j+1] + energy[i][j];
						pathFrom[i][j] = j+1;
				   }
			   }
		   }
	   
	   double min = distTo[width-1][0];
	   seam[width-1] = 0;
	   for(int i = 1; i < height; i++) {
		   if(distTo[width-1][i] < min) {
			   min = distTo[width-1][i];
			   seam[width-1] = i;
		   }
	   }
	   
	   for(int i = width-1; i > 0; i--) {
		   seam[i-1] = pathFrom[i][seam[i]];
	   }
	   return seam;
   }
   
   public int[] findVerticalSeam()                	// sequence of indices for vertical seam
   {
	   transpose();
	   int[] seam = findHorizontalSeam();
	   transpose();
	   return seam;
   }
   
   private void transpose() {
	   double[][] tEnergy = new double[height][width];
	   int[][] tColor = new int[height][width];
	   for(int i = 0; i < width; i++)
		   for(int j = 0; j < height; j++) {
			   tEnergy[j][i] = energy[i][j];
			   tColor[j][i] = color[i][j];
		   }
	   int temp = width;
	   width = height;
	   height = temp;
	   energy = tEnergy;
	   color = tColor;
   }
   
   public void removeHorizontalSeam(int[] seam)   	// remove horizontal seam from current picture
   {
	   checkCorner(seam);
	   if(height <= 1) throw new IllegalArgumentException();
	   if(seam.length != width) throw new IllegalArgumentException();
	   for(int i = 0; i < width; i++) {
		   if(seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException();
	   }
	   int[][] nColor = new int[width][height-1];
	   double[][] nEnergy = new double[width][height-1];
	   
	   for(int i = 0; i < width; i++) {
		   int j = seam[i];
		   System.arraycopy(color[i], 0, nColor[i], 0, j);
		   System.arraycopy(color[i], j+1, nColor[i], j, height-j-1);
		   System.arraycopy(energy[i], 0, nEnergy[i], 0, j);
		   System.arraycopy(energy[i], j+1, nEnergy[i], j, height-j-1);
	   }
	   color = nColor;
	   energy = nEnergy;
	   height--;
	   
	   for(int i = 0; i < width; i++) {
		   int j = seam[i];
		   if(j == 0) {
			   energy[i][j] = energy(i, j);
		   }else if(j == height) {
			   energy[i][j-1] = energy(i, j-1);
		   }else {
			   energy[i][j-1] = energy(i, j-1);
			   energy[i][j] = energy(i, j);
			   
		   }
	   }
	   
   }
   
   public void removeVerticalSeam(int[] seam)     	// remove vertical seam from current picture
   {
	   checkCorner(seam);
	   if(width <= 1) throw new IllegalArgumentException();
	   if(seam.length != height) throw new IllegalArgumentException();
	   for(int i = 0; i < height; i++) {
		   if(seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException();
	   }
	   transpose();
	   removeHorizontalSeam(seam);
	   transpose();
	   
   }
   
   private void checkCorner(int[] seam) {
	   if(seam == null) throw new NullPointerException();
	   for(int i = 1; i < seam.length; i++) {
		   if(Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException();
	   }
   }
   
}