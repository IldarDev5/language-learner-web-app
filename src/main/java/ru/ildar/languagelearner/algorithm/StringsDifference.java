package ru.ildar.languagelearner.algorithm;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Object that carries the information about modifications needed to
 * turn the string to another string. */
public class StringsDifference
{
    private String correctSentence;
    private Set<IndexModification> modifications;
    private Integer howMuchAdd;

    public StringsDifference() { }
    public StringsDifference(String correctSentence,
                             Set<IndexModification> modifications, Integer howMuchAdd)
    {
        this.correctSentence = correctSentence;
        this.modifications = modifications;
        this.howMuchAdd = howMuchAdd;
    }

    /** The correct sentence */
    public String getCorrectSentence()
    {
        return correctSentence;
    }

    /** Modifications needed to perform on the entered string to match it
     * to the correct sentence, and in a minimal amount of steps.
     * Key is the index of the string to which to perform the corresponding list of modifications.<br />
     * Modifications for the key are performed in a row as they are put in this list.
     * The index for performing modification is not static, e.g. if the list for key '1'
     * contains 3 INSERT modifications, the first insertion is made after the index 1, the
     * second insertion is made after the index 2, and the third insertion is made after the
     * index 3. The other keys modification lists must also adjust their indexes, i.e, their keys. */
    public Set<IndexModification> getModifications()
    {
        return modifications;
    }

    /** A counter for INSERT_ALL modification, that shows how much symbols we need to insert
     * in the beginning of the sentence. Symbols are taken from the beginning of the correct sentence. */
    public Integer getHowMuchAdd()
    {
        return howMuchAdd;
    }
}