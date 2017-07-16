package com.casic.bluebot.fragments;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.bean.BarometerCalibrationCoefficients;
import com.casic.bluebot.bean.Point3D;
import com.casic.bluebot.bean.Sensor;
import com.casic.bluebot.bean.SensorTag;
import com.casic.bluebot.bean.SimpleKeysStatus;
import com.casic.bluebot.services.BluetoothLeService;
import com.casic.bluebot.view.ProgressCircleView;
import com.casic.bluebot.view.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EFragment(R.layout.fragment_index)
public class IndexFragment extends Fragment {
	private static final String LOG_TAG = "IndexFragment";

	//widgets
	@ViewById
	public ProgressCircleView pcv_index;

	@ViewById
	public ProgressWheel pw_search;

	@ViewById
	public ImageButton ib_history;

	@ViewById
	public TextView tv_state;

	@ViewById
	public Button sendButton;

	@ViewById
	public EditText sendText;


	View.OnClickListener onClickHistory = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getActivity(), StatisticPager.class);
			startActivity(i);
		}
	};

	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private static final double PA_PER_METER = 12.0;

	//BLE
	@FragmentArg
	public BluetoothDevice bleDevice = null;

	private List<BluetoothGattService> mServiceList = null;
	private BluetoothLeService mBtLeService = null;
	private List<Sensor> mEnabledSensors = new ArrayList<Sensor>();
	private BluetoothGatt mBtGatt = null;
	private boolean mIsReceiving = false;
	private boolean mServicesRdy = false;
	private static final int GATT_TIMEOUT = 100; // milliseconds



	public void updateContent(String uuidStr, byte[] rawValue) {

		Point3D v;
		String msg;

		if (uuidStr.equals(SensorTag.UUID_ACC_DATA.toString())) {
			v = Sensor.ACCELEROMETER.convert(rawValue);
			msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
			Log.d(LOG_TAG, "加速度：" + msg);
		}

		if (uuidStr.equals(SensorTag.UUID_MAG_DATA.toString())) {
			v = Sensor.MAGNETOMETER.convert(rawValue);
			msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
			Log.d(LOG_TAG, "磁场："+msg);
		}

		if (uuidStr.equals(SensorTag.UUID_GYR_DATA.toString())) {
			v = Sensor.GYROSCOPE.convert(rawValue);
			msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
			Log.d(LOG_TAG, "陀螺仪："+msg);
		}

		//温度
		if (uuidStr.equals(SensorTag.UUID_IRT_DATA.toString())) {
			v = Sensor.IR_TEMPERATURE.convert(rawValue);
			//msg = decimal.format(v.x) + "\n";
			//mAmbValue.setText(msg);
			//msg = decimal.format(v.y) + "\n";
			msg = decimal.format(v.y);
			msg = msg.substring(1,3);
			pcv_index.setProgress(Integer.parseInt(msg));

			//mObjValue.setText(msg);
		}

		if (uuidStr.equals(SensorTag.UUID_HUM_DATA.toString())) {
			v = Sensor.HUMIDITY.convert(rawValue);
			msg = decimal.format(v.x);
			tv_state.setText("湿度:" + msg);
		}

		if (uuidStr.equals(SensorTag.UUID_BAR_DATA.toString())) {
			v = Sensor.BAROMETER.convert(rawValue);
			double h = (v.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
			h = (double) Math.round(-h * 10.0) / 10.0;
			msg = decimal.format(v.x / 100) + "\n" + h;
			Log.d(LOG_TAG, "压力："+msg);

		}

		if (uuidStr.equals(SensorTag.UUID_KEY_DATA.toString())) {
			SimpleKeysStatus s;
			final int img;
			s = Sensor.SIMPLE_KEYS.convertKeys(rawValue);

		}


	}

	public IndexFragment() {

	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					//TODO LIST：显示当前的服务列表信息
					displayServices();
					//checkOad();
				} else {
					//  Toast.makeText(getApplication(), "Service discovery failed", Toast.LENGTH_LONG).show();
					return;
				}
			} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
				// Notification
				final byte  [] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				//TODO LIST:注销当前的值
				getActivity().runOnUiThread(new Runnable() {
					public void run() {				//onCharacteristicChanged(uuidStr, value);

						try {
							//String text = new String(value, "UTF-8");
							Log.i("打印输出:", "温度：" + value[0] + "湿度：" + value[1]);
							tv_state.setText("温度：" + value[0] + "湿度：" + value[1]);
						//	Log.i("打印输出:", "湿度：" + value[0]);

						} catch (Exception e) {
						}
					}
				});

				Log.i("进入Notify方法", "ACTION_DATA_NOTIFY");

			} else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
				// Data written
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				onCharacteristicWrite(uuidStr,status);
				Log.i("进入Write方法", "ACTION_DATA_WRITE");

			} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
				// Data read
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				byte  [] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				onCharacteristicsRead(uuidStr,value,status);
				Log.i("进入Read方法", "ACTION_DATA_READ");

			}
			if (status != BluetoothGatt.GATT_SUCCESS) {
			}
		}
	};

	private void onCharacteristicWrite(String uuidStr, int status) {
		Log.d(LOG_TAG,"onCharacteristicWrite: " + uuidStr);
	}

	private void onCharacteristicChanged(String uuidStr, byte[] value) {

		Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
		if (fragment instanceof IndexFragment) {
				updateContent(uuidStr, value);
		}
	}

	private void onCharacteristicsRead(String uuidStr, byte [] value, int status) {
		Log.i(LOG_TAG, "onCharacteristicsRead: " + uuidStr);
	}

	@AfterViews
	public void init() {
		ib_history.setOnClickListener(onClickHistory);

		mServiceList = new ArrayList<BluetoothGattService>();
		mBtLeService = BluetoothLeService.getInstance();

		updateSensorList();

		// Create GATT object
		mBtGatt = BluetoothLeService.getBtGatt();

		if (!mIsReceiving) {
			getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			mIsReceiving = true;
		}

		// Start service discovery
		if (!mServicesRdy && mBtGatt != null) {
			if (mBtLeService.getNumServices() == 0)
				//TODO LIST：初始化后即开始扫描服务
				discoverServices();
			else
				displayServices();
		}

		//TODO LIST：待删除，绑定发送事件
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = sendText.getText().toString();
				byte[] value;
				try {
					//send data to service
					value = message.getBytes("UTF-8");
					UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
					BluetoothGattService RxService = mBtGatt.getService(RX_SERVICE_UUID);
					if (RxService == null) {
						return;
					}
					UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
					BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
					if (RxChar == null) {

						return;
					}
					RxChar.setValue(value);
					boolean status = mBtGatt.writeCharacteristic(RxChar);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void discoverServices() {
		if (mBtGatt.discoverServices()) {
			Log.i(LOG_TAG, "START SERVICE DISCOVERY");
			mServiceList.clear();
		} else {
			Log.i(LOG_TAG, "Faild SERVICE DISCOVERY");
		}
	}

	private void displayServices() {
		mServicesRdy = true;
		try {
			mServiceList = mBtLeService.getSupportedGattServices();
		} catch (Exception e) {
			e.printStackTrace();
			mServicesRdy = false;
		}

		// Characteristics descriptor readout done
		if (mServicesRdy) {
/*
			setStatus("Service discovery complete");
*/
			//TODO LIST：初始化后，扫描到服务
		//	enableSensors(true);
		//	enableNotifications(true);
			enableNotificationForLock(true);
		}
		else {
/*
            setError("Failed to read services");
*/
		}
	}

	private void enableNotificationForLock(boolean enable) {

		UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
		BluetoothGattService RxService = mBtGatt.getService(RX_SERVICE_UUID);
		if (RxService == null) {
			return;
		}


		UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
		BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
		if (TxChar == null) {
			return;
		}

		mBtGatt.setCharacteristicNotification(TxChar,true);

		UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
		BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
		if (descriptor == null) {
			return;
		}
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		mBtGatt.writeDescriptor(descriptor);

	}

	private void enableNotifications(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			//TODO LIST：data也是一个单独的characteristic
			UUID servUuid = sensor.getService();
			UUID dataUuid = sensor.getData();
			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(dataUuid);

			mBtLeService.setCharacteristicNotification(charac,enable);
			mBtLeService.waitIdle(GATT_TIMEOUT);
		}
	}

	private void enableSensors(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			UUID servUuid = sensor.getService();
			UUID confUuid = sensor.getConfig();

			// Skip keys
			if (confUuid == null)
				break;

			// Barometer calibration
			if (confUuid.equals(SensorTag.UUID_BAR_CONF) && enable) {
				calibrateBarometer();
			}

			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(confUuid);

			//TODO LIST：除了Gyroscope：0：disable，7 enable，其余的均是0disable 1 enable
			byte value =  enable ? sensor.getEnableSensorCode() : Sensor.DISABLE_SENSOR_CODE;
			mBtLeService.writeCharacteristic(charac, value);
			mBtLeService.waitIdle(GATT_TIMEOUT);

		}

	}

	private void calibrateBarometer() {
		Log.i(LOG_TAG, "calibrateBarometer");

		UUID servUuid = Sensor.BAROMETER.getService();
		UUID configUuid = Sensor.BAROMETER.getConfig();
		BluetoothGattService serv = mBtGatt.getService(servUuid);
		BluetoothGattCharacteristic config = serv.getCharacteristic(configUuid);

		// Write the calibration code to the configuration registers
		//TODO LIST：对于需要校正的1个service对应了两个characteristics
		mBtLeService.writeCharacteristic(config,Sensor.CALIBRATE_SENSOR_CODE);
		mBtLeService.waitIdle(GATT_TIMEOUT);
		BluetoothGattCharacteristic calibrationCharacteristic = serv.getCharacteristic(SensorTag.UUID_BAR_CALI);
		mBtLeService.readCharacteristic(calibrationCharacteristic);
		mBtLeService.waitIdle(GATT_TIMEOUT);
	}

	private void updateSensorList() {
		mEnabledSensors.clear();

		for (int i=0; i<Sensor.SENSOR_LIST.length; i++) {
			Sensor sensor = Sensor.SENSOR_LIST[i];
			//TODO LIST:需要判断各种类型的传感器是否被使能
			mEnabledSensors.add(sensor);
		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
		return fi;
	}

	@Override
	public void onResume() {

		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "onResume");

		pw_search.setVisibility(View.VISIBLE);
		pcv_index.clearAnimation();
		pcv_index.clearFocus();
		pcv_index.destroyDrawingCache();

		super.onResume();
		if (!mIsReceiving) {
			getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			mIsReceiving = true;
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d(LOG_TAG, "onPause");
		super.onPause();
		if (mIsReceiving) {
			getActivity().unregisterReceiver(mGattUpdateReceiver);
			mIsReceiving = false;
		}
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "onPause");
		super.onPause();
		if (mIsReceiving) {
			getActivity().unregisterReceiver(mGattUpdateReceiver);
			mIsReceiving = false;
		}
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

}
