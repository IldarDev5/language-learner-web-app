package ru.ildar.languagelearner.controller.dto;

/** Stores information about popular clusters - their language pairs and count of them among
 * different users
 * @param T Type of count variable - can be any type descending from <code>java.lang.Number</code>
 * */
public class PopularCluster<T extends Number>
{
    private String language1;
    private String language2;
    private T count;

    public PopularCluster() { }
    public PopularCluster(String language1, String language2, T count)
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

    public T getCount()
    {
        return count;
    }
}
