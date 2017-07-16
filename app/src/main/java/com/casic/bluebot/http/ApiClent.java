package com.casic.bluebot.http;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;


public class ApiClent {
    public final static String message_error = "服务器连接有问题";

    public interface ClientCallback {
        abstract void onSuccess(Object data);

        abstract void onFailure(String message);

        abstract void onError(Exception e);
    }

    //获取当前用户的信息
    public static void updateUser(Context appContext, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_LOGIN)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }


    //是否需要更新图片
    public static void updateCaptcha(Context appContext, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_CAPTCHA)
                .asByteArray()
                .setCallback(new FutureCallback<byte[]>() {
                    @Override
                    public void onCompleted(Exception e, byte[] result) {

                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }



    public static void login(Context appContext, String userName, String password, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_LOGIN)
                .setBodyParameter("userName", userName)
                .setBodyParameter("password", password)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    public static void loginWithCaptcha(Context appContext, String userName, String password,
                                        String j_captcha,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_LOGINWITHCAPTCHA)
                .setBodyParameter("userName", userName)
                .setBodyParameter("password", password)
                .setBodyParameter("j_captcha", j_captcha)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    public static void register(Context appContext, String email, String global_key, String j_captcha, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_REGISTER)
                .setBodyParameter("email", email)
                .setBodyParameter("global_key", global_key)
                .setBodyParameter("j_captcha", j_captcha)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });

    }

    public static void postArticle(Context appContext, String title, String body, String userId, File file, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_POSTARTICLE)
                .setMultipartParameter("title", title)
                .setMultipartParameter("body", body)
                .setMultipartParameter("userId", userId)
                .setMultipartFile("file", file)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });

    }

    public static void postComment(Context appContext, String articleId, String message,
                                   String userId, File file, String cid, final ClientCallback callback) {


        Ion.with(appContext)
                .load(NetUrl.URL_POSTCOMMETN)
                .setMultipartParameter("articleId", articleId)
                .setMultipartParameter("message", message)
                .setMultipartParameter("userId", userId)
                .setMultipartParameter("cid", cid)
                .setMultipartFile("file", file)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    public static void getComments(Context appContext, String articleId, String userId, String page, String rows, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_GETCOMMENTS)
                .setBodyParameter("articleId", articleId)
                .setBodyParameter("userId", userId)
                .setBodyParameter("page", page)
                .setBodyParameter("rows", rows)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });

    }

    public static void getLatestTemp(Context appContext,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_TEMPLATEST)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    //找回密码
    public static void sendEmailAndPassword(Context appContext, String email, String j_captcha, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_SENDEMAILANDPASSWORD)
                .setBodyParameter("email", email)
                .setBodyParameter("j_captcha", j_captcha)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });

    }

    //发送消息
    public static void sendMsg(Context appContext, String content,
                               String receiverId,String senderId,
                               final long creatTime,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_SENDMSG)
                .setBodyParameter("content", content)
                .setBodyParameter("receiverId", receiverId)
                .setBodyParameter("senderId", senderId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
/*
                            callback.onError(e);
*/
                            callback.onFailure(creatTime + "");
                            return;
                        }
                        callback.onSuccess(result);
                    }
                });

    }


    //发送消息
    public static void sendImage(Context appContext, File imageFile,
                               String receiverId,String senderId,
                               final long creatTime,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_SENDMSG)
                .setMultipartFile("imageFile", imageFile)
                .setMultipartParameter("receiverId", receiverId)
                .setMultipartParameter("senderId", senderId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
/*
                            callback.onError(e);
*/
                            callback.onFailure(creatTime+"");
                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    //TODO LIST：服务器端API开发
    public static void getUserInfo(Context appContext, String globalKey,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_USERINFO)
                .setBodyParameter("globalKey", globalKey)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    //TODO LIST:服务器端API待开发
    public static void deleteMsg(Context appContext, String msgId,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_USERINFO)
                .setBodyParameter("msgId", msgId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    //TODO LIST:服务器端API待开发
    public static void pageQueryMsg(Context appContext,String globalKey, String pageIndex,String pageSize,final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_MESSAGE)
                .setBodyParameter("globalKey", globalKey)
                .setBodyParameter("pageIndex", pageIndex)
                .setBodyParameter("pageSize", pageSize)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

    //TODO LIST:标记已经读取的信息
    public static void markMsg(Context appContext,String globalKey, final ClientCallback callback) {

        Ion.with(appContext)
                .load(NetUrl.URL_MARKMSG)
                .setBodyParameter("globalKey", globalKey)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            callback.onError(e);

                            return;
                        }
                        callback.onSuccess(result);
                    }
                });
    }

}
