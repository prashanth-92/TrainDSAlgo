package algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompareAnagrams {

    public int makeAnagrams(final String first, final String second){

        char[] firstChars = first.toCharArray();
        char[] secondChars = first.toCharArray();
        List<Character> firstCharsList = getList(firstChars);
        List<Character> secondCharsList = getList(secondChars);
        Iterator<Character> secondCharsListIterator;
        int count = 0;
        for(int i=0; i< firstCharsList.size(); i++){
            secondCharsListIterator = secondCharsList.iterator();
            while(secondCharsListIterator.hasNext()){
                Character item = secondCharsListIterator.next();
                if(item.equals(firstCharsList.get(i))){
                    secondCharsListIterator.remove();
                    count++;
                }
            }
        }
        return (first.length() - count) + (second.length() - count);

    }
    private List<Character> getList(char[] chars){
        List<Character> charsList = new ArrayList<>();
        for(int count = 0; count< chars.length; count++){
            charsList.add(chars[count]);
        }
        return charsList;
    }
}
