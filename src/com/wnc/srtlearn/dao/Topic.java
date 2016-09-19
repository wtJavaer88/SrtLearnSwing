package com.wnc.srtlearn.dao;

public class Topic
{
    private String topic_base_word;
    private String bookName;
    private String topic_id;
    private String topic_word;
    private String mean_cn;
    private String state;

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public String getTopic_id()
    {
        return topic_id;
    }

    public void setTopic_id(String topic_id)
    {
        this.topic_id = topic_id;
    }

    public String getTopic_word()
    {
        return topic_word;
    }

    public void setTopic_word(String topic_word)
    {
        this.topic_word = topic_word;
    }

    public String getMean_cn()
    {
        return mean_cn;
    }

    public void setMean_cn(String mean_cn)
    {
        this.mean_cn = mean_cn;
    }

    @Override
    public String toString()
    {
        return "Topic [topic_base_word=" + topic_base_word + ", bookName="
                + bookName + ", topic_id=" + topic_id + ", topic_word="
                + topic_word + ", mean_cn=" + mean_cn + ", state=" + state
                + "]";
    }

    public String getTopic_base_word()
    {
        return topic_base_word;
    }

    public void setTopic_base_word(String topic_base_word)
    {
        this.topic_base_word = topic_base_word;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }
}
