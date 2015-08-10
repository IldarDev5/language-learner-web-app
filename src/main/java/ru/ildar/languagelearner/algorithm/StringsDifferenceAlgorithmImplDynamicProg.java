package ru.ildar.languagelearner.algorithm;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

import static ru.ildar.languagelearner.algorithm.Modification.*;

@Component
/** Calculates the difference between strings based on dynamic programming table algorithm */
public class StringsDifferenceAlgorithmImplDynamicProg implements StringsDifferenceAlgorithm
{
    @Override
    public StringsDifference calculateDifference(String sentence1, String sentence2)
    {
        int n = sentence1.length();
        int m = sentence2.length();

        int[][] dist = new int[n + 1][m + 1];
        Modification[][] modif = new Modification[n + 1][m + 1];
        Entry[][] cameFrom = new Entry[n + 1][m + 1];

        for(int i = 0;i <= n;i++)
        {
            //delete i symbols
            dist[i][0] = i;
            modif[i][0] = new Modification(ModifOperation.DELETE_ALL);
        }
        for(int j = 0;j <= m;j++)
        {
            //insert j first symbols from sentence2
            dist[0][j] = j;
            modif[0][j] = new Modification(ModifOperation.INSERT_ALL);
        }

        for(int i = 1;i <= n;i++)
        {
            char chI = sentence1.charAt(i - 1);
            for(int j = 1;j <= m;j++)
            {
                char chJ = sentence2.charAt(j - 1);
                if(chI == chJ)
                {
                    //symbol i in sen1 is equal to symbol j at sen2
                    dist[i][j] = dist[i - 1][j - 1];
                    modif[i][j] = new Modification(ModifOperation.SYMBOLS_EQUAL);
                    cameFrom[i][j] = new Entry(i - 1, j - 1);
                }
                else
                {
                    //symbol i must be deleted from sentence1
                    dist[i][j] = dist[i - 1][j] + 1;
                    modif[i][j] = new Modification(ModifOperation.DELETE);
                    cameFrom[i][j] = new Entry(i - 1, j);

                    if (dist[i][j] > dist[i][j - 1] + 1)
                    {
                        //symbol j must be inserted after symbol i
                        dist[i][j] = dist[i][j - 1] + 1;
                        modif[i][j] = new Modification(ModifOperation.INSERT, chJ);
                        cameFrom[i][j] = new Entry(i, j - 1);
                    }

                    if(dist[i][j] > dist[i - 1][j - 1] + 1)
                    {
                        //symbol i in sen1 is changed to symbol j in sen2
                        dist[i][j] = dist[i - 1][j - 1] + 1;
                        modif[i][j] = new Modification(ModifOperation.REPLACE, chJ);
                        cameFrom[i][j] = new Entry(i - 1, j - 1);
                    }
                }
            }
        }

        Integer howMuchAdd = null;
        Map<Integer, Modification> modifications = new TreeMap<>();
        Entry currEntry = new Entry(n, m);
        do
        {
            //Trace the way back to gather the list of string changes
            int i = currEntry.getY();
            int j = currEntry.getX();

            if(modif[i][j].getModifOperation() != ModifOperation.SYMBOLS_EQUAL)
            {
                if(modif[i][j].getModifOperation() == ModifOperation.DELETE_ALL)
                {
                    for(int t = i;t > 0;t--)
                        modifications.put(t - 1, modif[i][j]);
                }
                else if(modif[i][j].getModifOperation() == ModifOperation.INSERT_ALL)
                {
                    modifications.put(0, modif[i][j]);
                    howMuchAdd = j;
                }
                else
                {
                    modifications.put(i - 1, modif[i][j]);
                }
            }

            currEntry = cameFrom[i][j];
        }
        while(currEntry != null);

        return new StringsDifference(sentence2, modifications, howMuchAdd);
    }
}

class Entry
{
    private int x;
    private int y;

    public Entry(int y, int x)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}





