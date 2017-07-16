package com.casic.bluebot.http;

/**
 * Created by admin on 2015/4/29.
 */
public class NetUrl
{

    public static String URL_LOGIN;
    public static String URL_POSTARTICLE;
    public static String URL_POSTCOMMETN;
    public static String URL_GETCOMMENTS;

    public static String URL_CURRENTUSER;//更新当前用户的信息
    public static String URL_REGISTER;//注册用户信息
    public static String URL_CAPTCHA;
    public static String URL_LOGINWITHCAPTCHA;
    public static String URL_SENDEMAILANDPASSWORD;
    public static String HOST="http://124.42.118.86:8080/iot/";
    public static String URL_SENDMSG;
    public static String URL_SENDIMAGE;
    public static String URL_USERINFO;
    public static String URL_MESSAGE;
    public static String URL_MARKMSG;
    public static String URL_TEMPLATEST;




    // String HOST_USER_REGISTER = Global.HOST_API + "/register";
    // private static String HOST_NEED_CAPTCHA = Global.HOST_API + "/captcha/register";


    static {
        NetUrl.URL_TEMPLATEST = HOST+"rs/temp/listLatest";
        NetUrl.URL_LOGIN = HOST+"rs/user/login";
        NetUrl.URL_POSTARTICLE = HOST+"article/post.do";
        NetUrl.URL_POSTCOMMETN = HOST+"comment/post.do";
        NetUrl.URL_GETCOMMENTS = HOST+"rs/article/list";

        NetUrl.URL_CURRENTUSER = HOST+"rs/user/currentUser";
      //  NetUrl.URL_REGISTER = "http://192.168.1.104:9080/iot/rs/user/register";
      //  NetUrl.URL_CAPTCHA = "http://192.168.1.104:9080/iot/rs/user/captcha";
        NetUrl.URL_REGISTER = HOST+"user/register.do";
        NetUrl.URL_CAPTCHA = HOST+"user/jcaptcha.do";
        NetUrl.URL_LOGINWITHCAPTCHA=HOST+"user/loginWithCaptcha.do";
        NetUrl.URL_SENDEMAILANDPASSWORD=HOST+"user/sendEmailAndPassword.do";
        NetUrl.URL_SENDMSG=HOST+"user/sendMessage.do";
        NetUrl.URL_SENDIMAGE=HOST+"user/sendImage.do";
        NetUrl.URL_USERINFO=HOST+"user/getUserInfoByKey.do";
        NetUrl.URL_MESSAGE=HOST+"user/pageQueryMsg.do";
        NetUrl.URL_MARKMSG=HOST+"user/markMsg.do";


    }
}
