package algo;

import java.util.ArrayList;
import java.util.List;

/*Russian Peasant Algo */
public class Multiplication {
	static List<Integer> lst = new ArrayList<>();
	public static void main(String[] args) {
		System.out.println(test(-1599,1577));
	}
	public static boolean test(int num1, int num2) {
		int actual = multiply(num1, num2);
		int expected = num1 * num2;
		System.out.println("Expected : "+expected);
		System.out.println("Actual : "+actual);
		return (actual == expected);
	}
	public static int multiply(int num1, int num2) {
		while(num2>0) {
			if(!isDivisibleBy2(num2)) {
				lst.add(num1);
			}
			num1 = 2*num1;
			num2 = num2/2;	
		}
		return addNum1InList();
	}
	public static int addNum1InList() {
		int ans = 0;
		for(Integer item:lst) {
			ans +=item;
		}
		return ans;
	}
	public static boolean isDivisibleBy2(int num) {
		return (num %2 ==0);
	}
	
}