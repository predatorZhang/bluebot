package com.casic.bluebot.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cc191954 on 14-8-27.
 */
public class Message {

    public static class MessageObject implements Serializable {

        public String content = "";
        public int count = 0;
        public long created_at = 0;
        public UserObject friend = new UserObject();
        private int id = 0;
        public int read_at;
        public UserObject sender = new UserObject();
        public int status;
        public int unreadCount;

        public MessageObject(JSONObject json) throws JSONException {
            content = json.optString("content");
            count = json.optInt("count");
            created_at = json.optLong("created_at");

            if (json.has("friend")) {
                friend = new UserObject(json.optJSONObject("friend"));
            }

            id = json.optInt("id");
            read_at = json.optInt("read_at");

            if (json.has("sender")) {
                sender = new UserObject(json.optJSONObject("sender"));
            }

            status = json.optInt("status");
            unreadCount = json.optInt("unreadCount");
        }

        public static List<MessageObject> parseJsons(String json) {
            Gson gson = new Gson();
            List<MessageObject> messageObjects = gson.fromJson(json, List.class);
            return messageObjects;
        }

        public static MessageObject parseJson(String json) {
            Gson gson = new Gson();
            MessageObject messageObjects = gson.fromJson(json, MessageObject.class);
            return messageObjects;
        }



        public int getId() {
            return id;
        }

        public MessageObject() {
        }

    }
}
