package com.casic.bluebot.bean;

import com.google.gson.Gson;

/**
 * Created by admin on 2015/5/3.
 */
public class UserInfo
{
    private int id;
    private String username;
    private String password;
    private String nickName;
    private int credits;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public int getCredits()
    {
        return credits;
    }

    public void setCredits(int credits)
    {
        this.credits = credits;
    }

    public static UserInfo parseJson(String json)
    {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        return userInfo;
    }
}
