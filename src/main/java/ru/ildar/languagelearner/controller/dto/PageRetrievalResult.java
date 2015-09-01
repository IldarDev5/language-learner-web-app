package ru.ildar.languagelearner.controller.dto;

import java.util.List;

public class PageRetrievalResult<T>
{
    private List<T> content;
    private int totalCountOfPages;

    public PageRetrievalResult(List<T> content, int totalCountOfPages)
    {
        this.content = content;
        this.totalCountOfPages = totalCountOfPages;
    }

    public List<T> getContent()
    {
        return content;
    }

    public int getTotalCountOfPages()
    {
        return totalCountOfPages;
    }
}
