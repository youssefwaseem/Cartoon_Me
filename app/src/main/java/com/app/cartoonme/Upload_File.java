package com.app.cartoonme;

public class Upload_File {
    private String mName;
    private String mImageurl;
    public Upload_File()
    {
    }
    public Upload_File(String name,String imageurl)
    {
        if(name.trim().equals(""))
        {
            name ="No Name";
        }
        mName=name;
        mImageurl=imageurl;
    }
    public String getmName()
    {
        return mName;
    }
    public void setmName(String name)
    {
        mName=name;
    }
    public String getmImageurl()
    {
        return mImageurl;
    }
    public void setmImageurl(String imageurl)
    {
        mImageurl=imageurl;
    }

}
