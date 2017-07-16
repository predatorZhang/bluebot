package com.casic.bluebot.bean;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cc191954 on 14-8-27.
 */
public class TempInfo{

    public String devCode;
    public String cell;
    public String dataValue;
    public String collectTime;
    public String logTime;

    public static List<TempInfo> parseJsons(String json) {
        Gson gson = new Gson();
        List<TempInfo> records = new ArrayList<>();
        try
        {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); ++i) {
                TempInfo tempInfo = gson.fromJson(array.getJSONObject(i).toString(), TempInfo.class);
                records.add(tempInfo);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return records;
    }

    public static TempInfo parseJson(String json) {
        Gson gson = new Gson();
        TempInfo messageObjects = gson.fromJson(json, TempInfo.class);
        return messageObjects;
    }



}
