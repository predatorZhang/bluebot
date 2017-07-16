package com.casic.bluebot.bean;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.casic.bluebot.common.LoginBackground;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class AccountInfo {

    private static final String ACCOUNT = "ACCOUNT";
    private static final String USER_RELOGIN_INFO = "USER_RELOGIN_INFO";
    private static final String BACKGROUNDS = "BACKGROUNDS";
    private static final String GLOBAL_LAST_LOGIN_NAME = "GLOBAL_LAST_LOGIN_NAME";
    private static String KEY_NEED_PUSH = "KEY_NEED_PUSH"; //是否需要推送
    private static final String MESSAGE_DRAFT = "MESSAGE_DRAFT";
    private static String FILE_PUSH = "FILE_PUSH";

    public static UserObject loadAccount(Context ctx) {
        UserObject data = null;
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(ctx.openFileInput(ACCOUNT));
                data = (UserObject) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new UserObject();
        }

        return data;
    }

    public static String loadMessageDraft(Context ctx, String globalKey) {
        ArrayList<String> data = new DataCache<String>().load(ctx, MESSAGE_DRAFT + globalKey);
        if (data.isEmpty()) {
            return "";
        } else {
            return data.get(0);
        }
    }

    public static void saveMessages(Context ctx, String globalKey, ArrayList<Message.MessageObject> data) {
        if (ctx == null) {
            return;
        }

        File file = new File(ctx.getFilesDir(), globalKey);
        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ctx.openFileOutput(globalKey, Context.MODE_PRIVATE));
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Message.MessageObject> loadMessages(Context ctx, String globalKey) {
        ArrayList<Message.MessageObject> data = null;
        File file = new File(ctx.getFilesDir(), globalKey);
        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(ctx.openFileInput(globalKey));
                data = (ArrayList<Message.MessageObject>) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new ArrayList<Message.MessageObject>();
        }

        return data;
    }

    // input 为 "" 时，删除上次的输入
    public static void saveMessageDraft(Context ctx, String input, String globalkey) {
        if (input.isEmpty()) { //
            new DataCache<String>().delete(ctx, MESSAGE_DRAFT + globalkey);
        } else {
            ArrayList<String> data = new ArrayList<>();
            data.add(input);
            new DataCache<String>().save(ctx, data, MESSAGE_DRAFT + globalkey);
        }
    }

    public static boolean isLogin(Context ctx) {
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        return file.exists();
    }

    public static String[] loadAllRelogininfo(Context ctx) {
        ArrayList<Pair> listData = new DataCache<Pair>().loadGlobal(ctx, USER_RELOGIN_INFO);
        String[] s = new String[listData.size() * 2];
        for (int i = 0; i < listData.size(); ++i) {
            Pair item = listData.get(i);
            s[i * 2] = item.first;
            s[i * 2 + 1] = item.second;
        }

        return s;
    }

    public static ArrayList<LoginBackground.PhotoItem> loadBackgrounds(Context ctx) {
        return new DataCache<LoginBackground.PhotoItem>().loadGlobal(ctx, BACKGROUNDS);
    }

    public static String loadRelogininfo(Context ctx, String key) {
        ArrayList<Pair> listData = new DataCache<Pair>().loadGlobal(ctx, USER_RELOGIN_INFO);
        if (key.contains("@")) {
            for (Pair item : listData) {
                if (item.first.equals(key)) {
                    return item.second;
                }
            }

        } else {
            for (Pair item : listData) {
                if (item.second.equals(key)) {
                    return item.second;
                }
            }
        }

        return "";
    }

    public static String loadLastLoginName(Context context) {
        String s = new DataCache<String>().loadGlobalObject(context, GLOBAL_LAST_LOGIN_NAME);
        if (s == null) {
            return "";
        }

        return s;
    }

    public static void saveAccount(Context ctx, UserObject data) {
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ctx.openFileOutput(ACCOUNT, Context.MODE_PRIVATE));
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveReloginInfo(Context ctx, String email, String globayKey) {
        DataCache<Pair> dateCache = new DataCache<>();
        ArrayList<Pair> listData = dateCache.loadGlobal(ctx, USER_RELOGIN_INFO);
        for (int i = 0; i < listData.size(); ++i) {
            if (listData.get(i).second.equals(globayKey)) {
                listData.remove(i);
                --i;
            }
        }

        listData.add(new Pair(email, globayKey));
        dateCache.saveGlobal(ctx, listData, USER_RELOGIN_INFO);
    }

    public static boolean getNeedPush(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_PUSH, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_NEED_PUSH, true);
    }

    public static void setNeedPush(Context ctx, boolean push) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_PUSH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_NEED_PUSH, push);
        editor.commit();
    }

    public static void loginOut(Context ctx) {
        File dir = ctx.getFilesDir();
        String[] fileNameList = dir.list();
        for (String item : fileNameList) {
            File file = new File(dir, item);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
        }

        AccountInfo.setNeedPush(ctx, true);

        NotificationManager notificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    static class DataCache<T> {

        public final static String FILDER_GLOBAL = "FILDER_GLOBAL";

        public void save(Context ctx, ArrayList<T> data, String name) {
            save(ctx, data, name, "");
        }

        public void saveGlobal(Context ctx, Object data, String name) {
            save(ctx, data, name, FILDER_GLOBAL);
        }

        public void delete(Context ctx, String name) {
            File file = new File(ctx.getFilesDir(), name);
            if (file.exists()) {
                file.delete();
            }
        }


        private void save(Context ctx, Object data, String name, String folder) {
            if (ctx == null) {
                return;
            }

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                file.delete();
            }

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(data);
                oos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ArrayList<T> load(Context ctx, String name) {
            return load(ctx, name, "");
        }

        public ArrayList<T> loadGlobal(Context ctx, String name) {
            return load(ctx, name, FILDER_GLOBAL);
        }

        public T loadGlobalObject(Context ctx, String name) {
            String folder = FILDER_GLOBAL;
            T data = null;

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (T) ois.readObject();
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return data;
        }

        private ArrayList<T> load(Context ctx, String name, String folder) {
            ArrayList<T> data = null;

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (ArrayList<T>) ois.readObject();
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (data == null) {
                data = new ArrayList<T>();
            }

            return data;
        }
    }

    static class Pair implements Serializable {
        public String first;
        public String second;

        Pair(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }


}
