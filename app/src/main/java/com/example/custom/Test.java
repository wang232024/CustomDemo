package com.example.custom;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Test {
    private static final String TAG = "wtx_Test";

    // 测试机器传感器
    private void init_MAGNETIC_FIELD(Context context) {
        SensorManager mSensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(listener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
//                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // 注意赋值时要调用clone()方法
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // 注意赋值时要调用clone()方法
                magneticValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues,
                    magneticValues);
            SensorManager.getOrientation(R, values);
            Log.i("MainActivity", "value[0] is " + Math.toDegrees(values[0]));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                    tv.setText("" + Math.toDegrees(values[0]) + "\n" + Math.toDegrees(values[1]) + "\n" + Math.toDegrees(values[2]));
//                }
//            });
//            if (false) {
//                try {
//                    if ((values[0] >= 0 && values[0] <= 180) || (values[0] < -180 && values[0] > -360)) {
//                        if (0 == cmd_left_already) {    // avoid cmd send frequencily.
//                            ProcessBuilder left = new ProcessBuilder(cmd_aw_dev_3_switch_left);
//                            mLogProc = left.start();
//                            cmd_left_already = 1;
//                            cmd_right_already = 0;
//                            Toast.makeText(mContext, "tinymix aw_spin_switch spin_0", Toast.LENGTH_SHORT).show();
//                        }
//                    } else if (values[0] > 180 && values[0] <= 360 || (values[0] < 0 && values[0] >= -180)) {
//                        if (0 == cmd_right_already) {
//                            ProcessBuilder right = new ProcessBuilder(cmd_aw_dev_3_switch_right);
//                            mLogProc = right.start();
//                            cmd_left_already = 0;
//                            cmd_right_already = 1;
//                            Toast.makeText(mContext, "tinymix aw_spin_switch spin_270", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } catch (Exception exc) {
//                    exc.printStackTrace();
//                }
//            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    // 测试传感器
    private void test(Context context) {
        SensorManager mSensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        SensorEventListener lsn = new SensorEventListener() {
            public void onSensorChanged(SensorEvent e) {
//                Log.i(TAG, "values[0]:" + e.values[0]);   // TYPE_ORIENTATION shuiping
//                Log.i(TAG, "values[1]:" + e.values[1]);   // TYPE_ORIENTATION shuzhi
//                Log.i(TAG, "values[2]:" + e.values[2]);   // TYPE_ORIENTATION zuoyou
//                try {
//                    if ((e.values[0] >= 0 && e.values[0] <= 180) || (e.values[0] < -180 && e.values[0] >= -360)) {
//                        if (0 == cmd_left_already) {    // avoid cmd send frenquencily.
//                            cmd_left_already = 1;
//                            cmd_right_already = 0;
//                            Log.e(TAG, "left");
//                        }
//                    } else if (e.values[0] > 180 && e.values[0] <= 360 || (e.values[0] < 0 && e.values[0] >= -180)) {
//                        if (0 == cmd_right_already) {
//                            cmd_left_already = 0;
//                            cmd_right_already = 1;
//                            Log.e(TAG, "right");
//                        }
//                    }
//                } catch (Exception exc) {
//                    exc.printStackTrace();
//                }
            }
            public void onAccuracyChanged(Sensor s, int accuracy) {
                Log.i(TAG, "accuracy:" + accuracy);
            }
        };
        mSensorManager.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
