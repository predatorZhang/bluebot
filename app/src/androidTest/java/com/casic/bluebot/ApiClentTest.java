package com.casic.bluebot;

import android.test.AndroidTestCase;

import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.UserInfo;
import com.casic.bluebot.http.ApiClent;
import com.koushikdutta.async.util.StreamUtility;

import java.io.File;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ApiClentTest extends AndroidTestCase
{


    final Semaphore sLogin = new Semaphore(0);
    private ApiClent.ClientCallback loginCallback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            assertEquals(true, resp.isSuccess());
            if (resp.isSuccess())
            {
                UserInfo userInfo = UserInfo.parseJson(resp.getMessage());
                assertEquals("123456", userInfo.getPassword());
                assertEquals("zhangfan", userInfo.getUsername());
                assertEquals("big1", userInfo.getNickName());
                sLogin.release();
            }
            else
            {
            }

        }

        @Override
        public void onFailure(String message)
        {
            sLogin.release();

        }

        @Override
        public void onError(Exception e)
        {
            sLogin.release();
        }
    };

    public void testLogin() throws Exception
    {
        ApiClent.login(getContext(), "zhangfan", "123456", loginCallback);
        sLogin.acquire();
    }

    final Semaphore sRegisterSucess = new Semaphore(0);
    private ApiClent.ClientCallback registerSuccessCalllback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            assertEquals(resp.getMessage(),"注册成功");
            assertEquals(resp.isSuccess(),true);
            sRegisterSucess.release();
        }

        @Override
        public void onFailure(String message)
        {

        }

        @Override
        public void onError(Exception e)
        {

        }
    };

    public void testRegisterSuccess() throws Exception
    {
        Random random1 = new Random(100);
        String userName = "zhangfan" + random1.nextInt();
        ApiClent.register(getContext(), userName, "123456", "tobias", registerSuccessCalllback);
        sRegisterSucess.acquire();
    }

    final Semaphore sRegisterFailed = new Semaphore(0);
    private ApiClent.ClientCallback registerFailedCallback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            assertEquals(resp.getMessage(),"该账号已注册");
            assertEquals(resp.isSuccess(),false);
            sRegisterFailed.release();

        }

        @Override
        public void onFailure(String message)
        {

        }

        @Override
        public void onError(Exception e)
        {

        }
    };
    public void testrestRegisterFailed() throws Exception
    {
        ApiClent.register(getContext(), "zhangfan", "123456", "predator", registerFailedCallback);
        sRegisterFailed.acquire();
    }


    final Semaphore sPostArticleSuccess = new Semaphore(0);
    private ApiClent.ClientCallback postArticleSuccess = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            assertEquals(resp.getMessage(),"发帖成功");
            assertEquals(resp.isSuccess(),true);
            sPostArticleSuccess.release();
        }

        @Override
        public void onFailure(String message)
        {

        }

        @Override
        public void onError(Exception e)
        {

        }
    };
    public void testPostArticle() throws Exception
    {

        File f = getContext().getFileStreamPath("test.txt");
        StreamUtility.writeFile(f, "hello world");
        ApiClent.postArticle(getContext(), "我的照片", "批示", "1", f,postArticleSuccess);
        sPostArticleSuccess.acquire();

    }

    final Semaphore sPostCommentSuccess = new Semaphore(0);
    private ApiClent.ClientCallback postCommentSuccess = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            assertEquals(resp.getMessage(),"评论成功");
            assertEquals(resp.isSuccess(),true);
            sPostCommentSuccess.release();
        }

        @Override
        public void onFailure(String message)
        {

        }

        @Override
        public void onError(Exception e)
        {

        }
    };
    public void testPostComment() throws Exception
    {
        File f = getContext().getFileStreamPath("test.txt");
        StreamUtility.writeFile(f, "hello world");
        ApiClent.postComment(getContext(), "21", "这是一个测试评论", "1", f, "1", postCommentSuccess);
        sPostCommentSuccess.acquire();

    }


    private Semaphore sGetCommentsSuccess = new Semaphore(0);
    private ApiClent.ClientCallback getCommentsCallback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            sGetCommentsSuccess.release();

        }

        @Override
        public void onFailure(String message)
        {

        }

        @Override
        public void onError(Exception e)
        {

        }
    };
    public void testGetComments() throws Exception
    {
        ApiClent.getComments(getContext(), "1", "1", "1", "10",getCommentsCallback);
        sGetCommentsSuccess.acquire();

    }
}