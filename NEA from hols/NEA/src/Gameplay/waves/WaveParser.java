package Gameplay.waves;

import CfgReader.CfgReader;

import java.util.ArrayList;
import java.util.HashMap;

public class WaveParser {

    public static ArrayList<Wave> enemiesBetweenGaps (String[] fns) //This method gives how many eneemies to give between each 1s gap given by the gamemanger
    {
        ArrayList<String> wavesRaw = new ArrayList<>();
        for(String fn : fns){

        }

        ArrayList<Wave> enemiesBetweenFinal = new ArrayList<>();

        for (String enemies : releases) { //At this point, we only have a string like EEEESSEEE, with each different character denoting a different enemy
            char[] chars = enemies.toCharArray();

            HashMap<Character, Integer> hashMap = new HashMap<>();

            for (char c : chars) {
                int current = 1; //Starting value is one if it doesn't contain it, or 1 + the before if it is new.
                if(hashMap.containsKey(c)) {
                    current += hashMap.remove(c);

                }

                hashMap.put(c, current);
            }

            Wave w = new Wave()

            enemiesBetweenFinal.add();
        }

        return enemiesBetweenFinal;
    }

}
