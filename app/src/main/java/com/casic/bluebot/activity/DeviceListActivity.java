package com.casic.bluebot.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.TempInfo;
import com.casic.bluebot.bean.UserObject;
import com.casic.bluebot.common.BleDeviceManager;
import com.casic.bluebot.http.ApiClent;
import com.casic.bluebot.services.BluetoothLeService;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.model.CardItemView;
import com.dexafree.materialList.view.MaterialListView;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_devicelist)
public class DeviceListActivity extends BaseActivity {

	private static final String TAG = "DeviceListActivity";

	@ViewById
	public MaterialListView lv_devices;

	// Requests to other activities
	private static final int REQ_ENABLE_BT = 0;
	private static final int REQ_DEVICE_ACT = 1;

	private ApiClent.ClientCallback callback = new ApiClent.ClientCallback()
	{
		@Override
		public void onSuccess(Object data)
		{
			try {
				String sData = data.toString();
				RestResponse resp = RestResponse.parseJson(sData);
				if (resp.isSuccess()) {
                    String message=resp.getMessage();
                    List<TempInfo> records = TempInfo.parseJsons(message);
					DeviceListActivity.this.updateMaterialView(records);
                }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(String message) {
			//TODO LIST:
			Log.d(TAG, message);

		}

		@Override
		public void onError(Exception e) {

			Log.d(TAG, e.getMessage());

		}
	};

	//消息推送receiver
	//TODO LIST：在设置推送是否使用的使用，接收广播
	public static final String BroadcastPushStyle = "BroadcastPushStyle";
	BroadcastReceiver mUpdatePushReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateNotifyService();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dl_menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroy");
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.action_add:
				ApiClent.getLatestTemp(this, callback);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateNotifyService() {
		boolean needPush = AccountInfo.getNeedPush(this);

		if (needPush) {
			String globalKey = SensorHubApplication.sUserObject.global_key;
			XGPushManager.registerPush(this, globalKey);
		} else {
			XGPushManager.registerPush(this, "*");
		}
	}

	// 信鸽文档推荐调用，防止在小米手机上收不到推送
	private void pushInXiaomi() {
		Context context = getApplicationContext();
		Intent service = new Intent(context, XGPushService.class);
		context.startService(service);
	}

	@AfterViews
	public void init() {

		//初始化推送消息
		IntentFilter intentFilter = new IntentFilter(BroadcastPushStyle);
		registerReceiver(mUpdatePushReceiver, intentFilter);
		updateNotifyService();
		pushInXiaomi();
		ApiClent.getLatestTemp(this, callback);

		lv_devices.addOnItemTouchListener(new com.dexafree.materialList.controller.RecyclerItemClickListener.OnItemClickListener() {

			@Override
			public void onItemLongClick(CardItemView view, int position) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemClick(CardItemView view, int position) {
				// TODO Auto-generated method stub

			/*	BleDeviceInfo bleDeviceInfo = (BleDeviceInfo) view.getTag();
				if (mScanning)
					stopScan();
				mBluetoothDevice = bleDeviceInfo.getBluetoothDevice();
				if (bleDeviceManager.getmConnIndex() == BleDeviceManager.NO_DEVICE) {
					*//*mScanView.setStatus("Connecting");*//*
					bleDeviceManager.setmConnIndex(position);
					onConnect();
				} else {
*//*
					mScanView.setStatus("Disconnecting");
*//*
					if (bleDeviceManager.getmConnIndex()  != BleDeviceManager.NO_DEVICE) {
						mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
					}
				}
*/

			}
		});

	}


	private void updateMaterialView(List<TempInfo> records ) {

		lv_devices.clear();
		lv_devices.removeAllViews();
		for (TempInfo record : records) {
			SimpleCard card1 = new SmallImageCard(this);
			card1.setTitle(record.devCode);
			card1.setDescription("当前温度:" + record.dataValue + "\n" +
					"电池电量："+record.cell+"\n"+
					"采集时间:" + record.collectTime.toString());
			card1.setDrawable(R.drawable.icon_device);
			card1.setTag(record);
			lv_devices.add(card1);
		}
	}


	// Activity result handling
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

			case REQ_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {

				} else {
				}
				break;
			default:
				Log.e(TAG, "Unknown request code");
				break;
		}
	}
}