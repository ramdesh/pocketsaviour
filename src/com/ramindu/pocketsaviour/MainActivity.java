package com.ramindu.pocketsaviour;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private SensorManager mSensorManager;
	private Sensor mProximitySensor;
	public float mProximity;
	protected static final int REQUEST_ENABLE = 0;
    DevicePolicyManager devicePolicyManager;
    ComponentName adminComponent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adminComponent = new ComponentName(MainActivity.this, MyDeviceAdminReceiver.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        
		SensorEventListener mProximitySensorListener = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				mProximity = event.values[0];
				Log.i("Proximity", String.valueOf(mProximity));
				if ( mProximity < 5 ) {
					if (!devicePolicyManager.isAdminActive(adminComponent)) {

			            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
			            startActivityForResult(intent, REQUEST_ENABLE);
			        } else {
			            devicePolicyManager.lockNow();
			        }
				}
				
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mSensorManager.registerListener(mProximitySensorListener, mProximitySensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ENABLE == requestCode) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
