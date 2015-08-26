package ru.ildar.languagelearner.controller.dto;

/** Stores information about popular clusters - their language pairs and count of them among
 * different users */
public class PopularCluster
{
    private String language1;
    private String language2;
    private long count;

    public PopularCluster() { }
    public PopularCluster(String language1, String language2, long count)
    {
        this.language1 = language1;
        this.language2 = language2;
        this.count = count;
    }

    public String getLanguage1()
    {
        return language1;
    }

    public String getLanguage2()
    {
        return language2;
    }

    public long getCount()
    {
        return count;
    }
}
