package ru.ildar.languagelearner.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;

import static ru.ildar.languagelearner.algorithm.Modification.*;

/** Calculates the difference between strings based on dynamic programming table algorithm */
@Component
public class StringsDifferenceAlgorithmImplDynamicProg implements StringsDifferenceAlgorithm
{
    @Override
    public StringsDifference calculateDifference(String sentence1, String sentence2)
    {
        int n = sentence1.length();
        int m = sentence2.length();

        //Table for counting minimal distance between a part of the sentence 1 and a part of the sentence 2
        int[][] dist = new int[n + 1][m + 1];
        //Modification needed to be made at the specific index of sentence 1 with respect to the part
        //of the correct sentence
        Modification[][] modif = new Modification[n + 1][m + 1];
        //Information for back tracing the modification steps minimal path
        Entry[][] cameFrom = new Entry[n + 1][m + 1];

        modif[0][0] = new Modification(ModifOperation.SYMBOLS_EQUAL);
        for(int i = 1;i <= n;i++)
        {
            //delete i symbols
            dist[i][0] = i;
            modif[i][0] = new Modification(ModifOperation.DELETE_ALL);
        }
        for(int j = 1;j <= m;j++)
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


        /* Gathering information about the minimal modification path
           by tracing back through the table
         */
        int modificationsCount = 0;
        Integer howMuchAdd = null;
        Map<Integer, IndexModification> modifications = new TreeMap<>();
        Entry currEntry = new Entry(n, m);
        do
        {
            //Trace the way back to gather the list of string changes
            int i = currEntry.getY();
            int j = currEntry.getX();

            if(modif[i][j].getModifOperation() == ModifOperation.DELETE_ALL)
            {
                //Delete all symbol before and including this one
                IndexModification im = modifications.getOrDefault(i - 1, new IndexModification(i - 1));
                im.getModifications().add(0, modif[i][j]);
                modifications.put(i - 1, im);

                modificationsCount += (i - 1);
            }
            else if(modif[i][j].getModifOperation() == ModifOperation.INSERT_ALL)
            {
                //Insert a number of symbols in the beginning of the string
                IndexModification im = modifications.getOrDefault(0, new IndexModification(0));
                im.getModifications().add(0, modif[i][j]);
                modifications.put(0, im);
                howMuchAdd = j;

                modificationsCount += howMuchAdd;
            }
            else
            {
                /* INSERT, REPLACE, DELETE or SYMBOLS_EQUAL operation.
                * We don't omit the SYMBOLS_EQUAL case so a user can construct the string wholly from
                * the modifications list. If the user needs only "real" modifications, he can filter them
                * himself.
                * */
                IndexModification im = modifications.getOrDefault(i - 1, new IndexModification(i - 1));
                im.getModifications().add(0, modif[i][j]);
                modifications.put(i - 1, im);

                if(modif[i][j].getModifOperation() != ModifOperation.SYMBOLS_EQUAL)
                {
                    modificationsCount++;
                }
            }

            currEntry = cameFrom[i][j];
        }
        while(currEntry != null);

        //Convert the map to set. The indexes are stored in the IndexModification objects
        //so a user doesn't need key-value pairs with keys as indexes
        Set<IndexModification> set = new TreeSet<>(indexComparator());
        set.addAll(modifications.values());
        return new StringsDifference(sentence2, modificationsCount, set, howMuchAdd);
    }

    private Comparator<IndexModification> indexComparator()
    {
        return (o1, o2) -> o1.getIndex() - o2.getIndex();
    }
}

/** Coordinates at the table */
class Entry
{
    private int x;
    private int y;

    public Entry(int y, int x)
    {
        this.x = x;
        this.y = y;
    }

    /** Column number */
    public int getX()
    {
        return x;
    }

    /** Row number */
    public int getY()
    {
        return y;
    }
}





