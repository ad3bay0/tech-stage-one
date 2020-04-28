package techstageone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecondChallenge {

    public static int getMaximumPairOfSocks(int noOfWashes, int[] cleanPile, int[] dirtyPile) {

        int pair = 0;
        Set<Integer> unpairedCleanSocks = new HashSet<>();

        for (int cleanSock : cleanPile) {

            if(!unpairedCleanSocks.remove(cleanSock)){

                unpairedCleanSocks.add(cleanSock);

            }else{

                pair += 1;
            }

        }

        if (noOfWashes<=0){
        
            return pair;
        }

        Map<Integer,Integer> dirtySocks = new HashMap<>();

        for(int color:dirtyPile){

            if(dirtySocks.containsKey(color)){

                dirtySocks.put(color, dirtySocks.get(color) +1);

            }else{

                dirtySocks.put(color, 1);
            }

        }

        for(int color: unpairedCleanSocks){

            if(dirtySocks.containsKey(color) && dirtySocks.get(color)>0){

                dirtySocks.put(color, dirtySocks.get(color)  - 1);
                pair +=1;
                noOfWashes -= 1;

                if(noOfWashes == 0){

                    return pair;
                }

            }

        }

        for(int count: dirtySocks.values()){

            int socks = Math.min(count/2, noOfWashes/2);

            noOfWashes -= socks * 2;

            pair += socks;

            if(noOfWashes <=1){

                return pair;

            }

        }

        return pair;

    }


}