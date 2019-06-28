package algo;

import java.util.ArrayList;
import java.util.List;

public class SubStrings {
    public static void main(String[] args) {
        String input = "abcbaba";
        check(input);
    }

    private static long check(int n, String input){
        return substring(input)
                .stream()
                .filter(SubStrings::check)
                .count();
    }

    private static List<String> substring(String input){
        int subStringLength = input.length();
        int difference = 1;
        List<String> subStringList = new ArrayList<>();
        for(int i=0; i<=subStringLength; i++){
            for(int j=0; j< subStringLength;j++){
                int start = j;
                int end = start+difference;
                if(end<=subStringLength) {
                    subStringList.add(input.substring(start, end));
                }
            }
            difference++;
        }
        System.out.println(subStringList);
        return subStringList;
    }

    private static boolean check(String input){
        return isPalindrome(input) && isAllCharsExceptMiddleSame(input);
    }

    private static boolean isPalindrome(String input){
        System.out.print(input);
        System.out.print(" ");
        System.out.print(new StringBuilder(input).reverse().toString());
        System.out.print(" ");
        System.out.print(input.equalsIgnoreCase(new StringBuilder(input).reverse().toString()));
        System.out.println();
        return input.equalsIgnoreCase(new StringBuilder(input).reverse().toString());
    }

    private static boolean isAllCharsExceptMiddleSame(String input){
        Character charToCheck = input.charAt(0);
        char[] inputChars = input.toCharArray();
        for(int i=0;i<inputChars.length;i++){
            if(charToCheck.equals(inputChars[i]) || (i == inputChars.length/2)){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }


}
