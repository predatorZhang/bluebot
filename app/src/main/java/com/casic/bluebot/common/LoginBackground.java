package com.casic.bluebot.common;

import android.content.Context;

import com.casic.bluebot.bean.AccountInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by chaochen on 15/1/5.
 */
public class LoginBackground {

    static final String TAG = "LoginBackground";

    private Context context;

    public LoginBackground(Context context) {
        this.context = context;
    }

    public PhotoItem getPhoto() {
        ArrayList<PhotoItem> list = AccountInfo.loadBackgrounds(context);
        ArrayList<PhotoItem> cached = new ArrayList();
        for (PhotoItem item : list) {
            if (item.isCached(context)) {
                cached.add(item);
            }
        }

        int max = cached.size();
        if (max == 0) {
            return new PhotoItem();
        }

        int index = new Random().nextInt(max);
        return cached.get(index);
    }

    public static class PhotoItem implements Serializable {
        Group group = new Group();
        String url = "";
        public PhotoItem(JSONObject json) {
            group = new Group(json.optJSONObject("group"));
            url = json.optString("url");
        }

        public PhotoItem() {
        }

        public String getUrl() {
            return url;
        }


        public String getTitle() {
            return String.format("%s ? %s", group.name, group.author);
        }

        private String getCacheName() {
            return String.valueOf(group.id);
        }

        public File getCacheFile(Context ctx) {
            File file = new File(getPhotoDir(ctx), getCacheName());
            return file;
        }

        // 下载的文件先放在这里，下载完成后再放到 getCacheFile 目录下
        public File getCacheTempFile(Context ctx) {
            File fileDir = new File(getPhotoDir(ctx), "temp");
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            File file = new File(fileDir, getCacheName());
            return file;
        }

        public boolean isCached(Context ctx) {
            return getCacheFile(ctx).exists();
        }

        private File getPhotoDir(Context ctx) {
            final String dirName = "BACKGROUND";
            File root = ctx.getExternalFilesDir(null);
            File dir = new File(root, dirName);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }

            return dir;
        }

        class Group implements Serializable {
            String name = "";
            String author = "";
            String link = "";
            String description = "";
            int id;

            Group(JSONObject json) {
                name = json.optString("name");
                author = json.optString("author");
                link = json.optString("link");
                description = json.optString("description");
                id = json.optInt("id");
            }

            Group() {
            }
        }
    }


}
