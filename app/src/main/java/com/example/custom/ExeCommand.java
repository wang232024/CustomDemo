package com.example.custom;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 执行命令的类
 */
public class ExeCommand {
    private static final String TAG = "ExeCommand";
    // shell进程
    private Process mProcess;
    // 对应进程的3个流
    private BufferedReader mErrorResult;
    private BufferedReader mSuccessResult;
    private DataOutputStream mOs;
    // 同步锁
    private ReadWriteLock mLock = new ReentrantReadWriteLock();
    // 保存执行结果
    private StringBuffer mResult = new StringBuffer();
    // 是否同步，true:run会一直阻塞至完成或超时 false:run会立刻返回
    private boolean mSynchronous = true;
    // shell进程是否还在运行
    private boolean mRunning = false;

    public ExeCommand() {
        mSynchronous = true;
    }

    public ExeCommand(boolean synchronous) {
        mSynchronous = synchronous;
    }

    /**
     * 是否正在执行
     *
     * @return true:正在执行 false:还没开始执行，和已经执行完毕
     */
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * @return 返回执行结果
     */
    public String getResult() {
        Lock readLock = mLock.readLock();
        readLock.lock();
        try {
            return new String(mResult);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 执行命令
     *
     * @param commandString     命令语句，如: cat /sdcard/test.txt
     * @param delayExitMillis   最大等待时间(ms)
     * @return  this
     */
    public ExeCommand run(String commandString, final long delayExitMillis) {
        Log.i(TAG, "run command:" + commandString + ", maxtime:" + delayExitMillis);
        if (commandString == null || commandString.length() == 0) {
            return this;
        }
        try {
            mProcess = Runtime.getRuntime().exec("sh");
        } catch (Exception e) {
            return this;
        }
        mRunning = true;
        mSuccessResult = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
        mErrorResult = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
        mOs = new DataOutputStream(mProcess.getOutputStream());;
        try {
            mOs.write(commandString.getBytes());
            mOs.writeBytes("\n");
            mOs.flush();

            mOs.writeBytes("exit\n");
            mOs.flush();

            mOs.close();

            if (delayExitMillis > 0) {
                // 超时就关闭shell进程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(delayExitMillis);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            int ev = mProcess.exitValue();
                            Log.i(TAG, "process exitValue:" + ev);
                        } catch (IllegalThreadStateException e) {
                            Log.i(TAG, "take maxTime, forced to destroy process");
                            mProcess.destroy();
                        }
                    }
                }).start();
            }
            // 开一个线程处理input流
            final Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String readLine;
                    Lock writeLock = mLock.writeLock();
                    try {
                        while ((readLine = mSuccessResult.readLine()) != null) {
                            readLine += "\n";
                            writeLock.lock();
                            mResult.append(readLine);
                            writeLock.unlock();
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "read InputStream exception:" + e);
                    } finally {
                        try {
                            mSuccessResult.close();
                        } catch (Exception e) {
                            Log.i(TAG, "close InputStream exception:" + e);
                        }
                    }
                }
            });
            thread1.start();

            // 开一个线程处理error流
            final Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String readLine;
                    Lock writeLock = mLock.writeLock();
                    try {
                        while ((readLine = mErrorResult.readLine()) != null) {
                            readLine += "\n";
                            writeLock.lock();
                            mResult.append(readLine);
                            writeLock.unlock();
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "read ErrorStream exception:" + e);
                    } finally {
                        try {
                            mErrorResult.close();
                        } catch (Exception e) {
                            Log.i(TAG, "close ErrorStream exception:" + e);
                        }
                    }
                }
            });
            thread2.start();

            Thread thread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 等待执行完毕
                        thread1.join();
                        thread2.join();
                        mProcess.waitFor();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mRunning = false;
                        Log.i(TAG, "run command process end");
                    }
                }
            });
            thread3.start();

            if (mSynchronous) {
                thread3.join();
            }
        } catch (Exception e) {
            Log.i(TAG, "run command process exception:" + e);
        }
        return this;
    }

    public static void testExeCommand() {
        long time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = new ExeCommand().run(
//                "cp /sdcard/Download/pkg.tar.gz /sdcard/Download/" + time + ".tar.gz",
                        "cp /sdcard/DCIM/pkg.tar.gz.png /sdcard/DCIM/" + time + ".tar.gz.png",
                        0
                ).getResult();
                Log.i(TAG, "result:" + result);
            }
        }).start();
    }
}
