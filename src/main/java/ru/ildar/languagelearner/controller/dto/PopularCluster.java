package ru.ildar.languagelearner.controller.dto;

public class PopularCluster
{
    private String language1;
    private String language2;
    private long clusterCount;

    public PopularCluster() { }
    public PopularCluster(String language1, String language2, long clusterCount)
    {
        this.language1 = language1;
        this.language2 = language2;
        this.clusterCount = clusterCount;
    }

    public String getLanguage1()
    {
        return language1;
    }

    public void setLanguage1(String language1)
    {
        this.language1 = language1;
    }

    public String getLanguage2()
    {
        return language2;
    }

    public void setLanguage2(String language2)
    {
        this.language2 = language2;
    }

    public long getClusterCount()
    {
        return clusterCount;
    }

    public void setClusterCount(long clusterCount)
    {
        this.clusterCount = clusterCount;
    }
}
