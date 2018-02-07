package algo;

import java.util.ArrayList;
import java.util.List;

public class PascalTriangle {
	public static List<Integer[]> pascalTriangleRows = new ArrayList<>();
	public static void main(String[] args) {
		int n = 25;
		generatePascalTriangle(n);
		printPascalTriangle();
	}
	private static void generatePascalTriangle(int n) {
		Integer[] row;
		for(int i=0; i<n; i++) {
			if(i==0)
				row = new Integer[] {1};
			else if(i==1)
				row = new Integer[] {1,1};
			else
				row = getCurrentRow(pascalTriangleRows.get(i-1));
			pascalTriangleRows.add(row);
		}
	}
	private static Integer[] getCurrentRow(Integer prevRow[]) {
		Integer[] row = new Integer[prevRow.length+1];
		row[0] = row[row.length-1] = 1;
		for(int i=1; i<row.length-1; i++) {
			row[i] = prevRow[i-1] + prevRow[i];
		}
		return row;
	}
	private static void printPascalTriangle() {
		for(int i=0; i<pascalTriangleRows.size(); i++) {
			printRow(pascalTriangleRows.get(i));
			System.out.println();
		}
	}
	private static void printRow(Integer[] row) {
		for(int i=0; i<row.length; i++) {
			System.out.print(row[i]+"\t");
		}
	}
}
