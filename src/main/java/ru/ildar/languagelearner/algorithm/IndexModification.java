package ru.ildar.languagelearner.algorithm;

import java.util.ArrayList;
import java.util.List;

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

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

    public List<Modification> getModifications()
    {
        return modifications;
    }

    public void setModifications(List<Modification> modifications)
    {
        this.modifications = modifications;
    }
}
