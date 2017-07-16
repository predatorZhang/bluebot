package com.casic.bluebot.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.ScanDeviceInfo;
import com.casic.bluebot.bean.UserInfo;
import com.casic.bluebot.http.ApiClent;

public class MainActivity0 extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    break;
                case 1:
                    ScanDeviceInfo info = (ScanDeviceInfo)msg.obj;
                    Log.d("MainAciticy", info.getCurrentValue());
                    Log.d("MainAciticy", info.getDevId());
                    Log.d("MainAciticy", info.getIp());
                    Log.d("MainAciticy", info.getSensorId());
                    break;
                case 2:
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //TODO LIST:mina客户端程序测试

      new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params)
            {


                //     UdpClient.send("zhangfanUDP");
           /*     //  new Client("CASIC203", 2013,handler).send("Hello World");
             *//**//*   InetAddress address = null;
                try
                {
                    address = InetAddress.getByName("192.168.1.104");
                    new Client(address, 2013, handler);
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
*/
                return null;
            }
        }.execute();

        /*testLogin();*/

        /*
        DeviceScanTask deviceScanTask = new DeviceScanTask(this, false);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] =2013;
        arrayOfObject[1] = handler;
        Log.d("MainActivity","进入OnCreat函数");
        deviceScanTask.execute(arrayOfObject);
        */


       //TODO LIST:调用so文件返回相关的信息
//        IoTManagerNative ioTManagerNative = new IoTManagerNative();
//        ioTManagerNative.StartSmartConnection("tp1234", "Predator12+", (byte)8);
//        ClientInfo[] clientInfos = ioTManagerNative.QueryClientInfo(1);
//        String ip = clientInfos[0].IPAddress;

    }






    private ApiClent.ClientCallback callback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            Log.d("MainActivity:","成功返回");
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            if (resp.isSuccess())
            {
                Log.d("MainActivity", "登录成功");
                UserInfo userInfo = UserInfo.parseJson(resp.getMessage());
            }
            else
            {
                Log.d("MainActivity", resp.getMessage());
            }

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

    private void testLogin()
    {
        ApiClent.login(getApplicationContext(),"zhangfan","123456",callback);
    }

}
