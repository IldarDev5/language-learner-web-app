package ru.ildar.languagelearner.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * List of modifications to perform on the specified string index
 */
public class IndexModification
{
    private Integer index;
    private List<Modification> modifications;

    public IndexModification() { }
    public IndexModification(int index)
    {
        this.index = index;
        this.modifications = new ArrayList<>();
    }
    public IndexModification(Integer index, List<Modification> modifications)
    {
        this.index = index;
        this.modifications = modifications;
    }

    /**
     * String index
     */
    public Integer getIndex()
    {
        return index;
    }

    /**
     * Modifications list for this index
     */
    public List<Modification> getModifications()
    {
        return modifications;
    }
}
