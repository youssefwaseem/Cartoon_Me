package com.app.cartoonme;

public class story {
    private int storyImageResource;
    private String storyname;
    private int numofpages;
    public story(int simg,String sn,int nofpages)
    {
        storyImageResource=simg;
        storyname=sn;
        numofpages=nofpages;
    }
    public int getStoryImageResource()
    {
        return  storyImageResource;
    }
    public String getStoryname()
    {
        return storyname;
    }
    public int getNumofpages()
    {
        return numofpages;
    }
}
