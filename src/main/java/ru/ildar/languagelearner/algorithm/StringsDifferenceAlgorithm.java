package ru.ildar.languagelearner.algorithm;

/**
 * Algorithm that calculates the difference between two strings.
 * The difference is found considering the next allowed string modifications:
 * <ul>
 *     <li>A symbol can be deleted from the string</li>
 *     <li>A symbol can be inserted into the string</li>
 *     <li>A symbol in the string can be changed to another symbol</li>
 * </ul>
 */
public interface StringsDifferenceAlgorithm
{
    /**
     * Calculates the difference between two strings.
     * Method returns the modifications needed to turn sentence2 into sentence1.
     * @param sentence1 String for which modifications need to be computed to turn it into the sentence2
     * @param sentence2 The correct string
     */
    StringsDifference calculateDifference(String sentence1, String sentence2);
}
