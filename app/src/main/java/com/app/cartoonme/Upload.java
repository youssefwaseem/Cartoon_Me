package com.app.cartoonme;

public class Upload
{

    private String mImageurl;
    public Upload()
    {
//empty constructor needed
    }
    public Upload(String imageurl)
    {
        mImageurl=imageurl;
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
