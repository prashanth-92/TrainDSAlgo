package algo;

import java.util.ArrayList;
import java.util.List;


public class CellCompete {
    public static void main(String[] args) {

        CellCompete cellCompete = new CellCompete();
        System.out.println(
                cellCompete.cellCompete(new int[]{1,0,0,0,0,1,0,0}, 100000000)
        );

    }
    public List<Integer> cellCompete(int[] states, int days)
    {
        List<Integer> statesList = arrayToList(states);
        for(int count = 1; count<=days; count++){
            statesList = compute(statesList);
        }
        return statesList;
    }

    private List<Integer> compute(List<Integer> statesList){
        List<Integer> newState = new ArrayList<>();
        for(int i=0; i< statesList.size(); i++){
            if(i == 0){
                newState.add(computeNewState(0, statesList.get(i+1)));
            }else if(i == statesList.size() - 1){
                newState.add(computeNewState(statesList.get(i-1), 0));
            }else {
                newState.add(computeNewState(statesList.get(i - 1), statesList.get(i + 1)));
            }
        }
        return newState;
    }

    private List<Integer> arrayToList(int[] states){
        List<Integer> statesList = new ArrayList<>();
        for(int i=0; i<states.length;i++){
            statesList.add(states[i]);
        }
        return statesList;
    }

    private int computeNewState(int left, int right){
        if(left == right)
            return 0;
        else
            return 1;
    }
}
