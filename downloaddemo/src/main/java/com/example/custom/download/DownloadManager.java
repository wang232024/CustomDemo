package com.example.custom.download;

import android.annotation.SuppressLint;

import com.example.common.KLog;
import com.example.common.util.UrlUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class DownloadManager {
    private static final String TAG = DownloadManager.class.getName();
    private DownloadInfo mDownloadInfo;
    private Disposable disposable;
    private DownloadService service;
    private static final String SAVE_PATH = "/sdcard/Download/";
    private static final int BUFF_SIZE = 1024 * 16;     // responseBody.byteStream().read()方法触发DownloadResponseBody中的read，该处缓存大小需>=其bytesRead
    private int mDisposeReason = 0; // 停止原因
    private ObservableEmitter<DownloadInfo> mObservableEmitter;
    private ObservableEmitter<DownloadInfo.STATE> mDelayEmitter;

    private WeakReference<OnDownloadListener> mOnDownloadListenerWeakReference;
    public interface OnDownloadListener {
        void onStart(DownloadInfo downloadInfo);
        void onPause(DownloadInfo downloadInfo);
        void onContinue(DownloadInfo downloadInfo);
        void onStop(DownloadInfo downloadInfo);
        void onFinish(DownloadInfo downloadInfo);
        void onProgress(DownloadInfo downloadInfo);
        void onError(DownloadInfo downloadInfo);
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        mOnDownloadListenerWeakReference = new WeakReference<>(listener);
    }

    @SuppressLint("CheckResult")
    public DownloadManager() {
        // 更新UI
        Observable.create(new ObservableOnSubscribe<DownloadInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<DownloadInfo> e) throws Exception {
                        mObservableEmitter = e;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        if (null != mOnDownloadListenerWeakReference.get()) {
                            OnDownloadListener onDownloadListener = mOnDownloadListenerWeakReference.get();
                            KLog.d("accept, downloadInfo.getState():" + downloadInfo.getState());
                            switch (downloadInfo.getState()) {
                                case NONE:
                                    break;
                                case START:
                                    onDownloadListener.onStart(mDownloadInfo);
                                    break;
                                case PAUSE:
                                    onDownloadListener.onPause(mDownloadInfo);
                                    break;
                                case CONTINUE:
                                    onDownloadListener.onContinue(mDownloadInfo);
                                    break;
                                case STOP:
                                    onDownloadListener.onStop(mDownloadInfo);
                                    break;
                                case FINISH:
                                    onDownloadListener.onFinish(mDownloadInfo);
                                    break;
                                case PROGRESS:
                                    onDownloadListener.onProgress(mDownloadInfo);
                                    break;
                                case ERROR:
                                    onDownloadListener.onError(mDownloadInfo);
                                    break;
                            }
                        }
                    }
                });

        // 暂停/停止后，延迟150ms发送状态，防止被PROGRESS改变
        Observable.create(new ObservableOnSubscribe<DownloadInfo.STATE>() {
                    @Override
                    public void subscribe(ObservableEmitter<DownloadInfo.STATE> e) throws Exception {
                        mDelayEmitter = e;
                    }
                }).delay(150, TimeUnit.MILLISECONDS).subscribe(new Consumer<DownloadInfo.STATE>() {
                    @Override
                    public void accept(DownloadInfo.STATE state) throws Exception {
                        mDownloadInfo.setState(state);
                        mObservableEmitter.onNext(mDownloadInfo);
                    }
                });
    }

    private final DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
        @SuppressLint("CheckResult")
        @Override
        public void progress(long read, long contentLength, boolean done) {
            KLog.d(TAG, "contentLength:" + contentLength + ", read:" + read + ", info.getContentLength():" + mDownloadInfo.getContentLength());
            // 该方法仍然是在子线程，如果想要调用进度回调，需要切换到主线程，否则的话，会在子线程更新UI，直接错误
            // 如果断电续传，重新请求的文件大小是从断点处到最后的大小，不是整个文件的大小，info中的存储的总长度是
            // 整个文件的大小，所以某一时刻总文件的大小可能会大于从某个断点处请求的文件的总大小。此时read的大小为
            // 之前读取的加上现在读取的
            long readLen = read;
            if (mDownloadInfo.getContentLength() > contentLength) {
                readLen = read + (mDownloadInfo.getContentLength() - contentLength);
            } else {
                mDownloadInfo.setContentLength(contentLength);
            }
            mDownloadInfo.setReadLength(readLen);

            if (done) {
                mDownloadInfo.setState(DownloadInfo.STATE.FINISH);
                mObservableEmitter.onNext(mDownloadInfo);
            } else {
                mDownloadInfo.setState(DownloadInfo.STATE.PROGRESS);
                mObservableEmitter.onNext(mDownloadInfo);
            }
        }
    };

    private void download() {
        disposable = service.download("bytes=" + mDownloadInfo.getReadLength() + "-", mDownloadInfo.getUrl())
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, DownloadInfo>() {
                    @Override
                    public DownloadInfo apply(ResponseBody responseBody) {  // responseBody读完才会触发subscribe
                        try {
                            KLog.w(TAG, "responseBody, downloadInfo:" + mDownloadInfo);
                            KLog.w(TAG, "responseBody.contentLength:" + responseBody.contentLength());
                            InputStream is = responseBody.byteStream();
                            RandomAccessFile randomAccessFile = new RandomAccessFile(mDownloadInfo.getSavePath(), "rwd");
                            randomAccessFile.seek(mDownloadInfo.getReadLength());
                            byte[] buf = new byte[BUFF_SIZE];
                            int len;
                            long count = mDownloadInfo.getReadLength();
                            int wr = 0;
                            while (0 < (len = is.read(buf))) {
                                randomAccessFile.write(buf, 0, len);
                                wr += len;
                            }
                            count += wr;
                            is.close();
                            randomAccessFile.close();
                        } catch (Exception e) {
                            KLog.e("e:" + e);
                            if (e instanceof FileNotFoundException) {
                                mDownloadInfo.setState(DownloadInfo.STATE.ERROR);
                                mDownloadInfo.setThrowable(e);
                                mObservableEmitter.onNext(mDownloadInfo);
                            }
                            if (0 == mDisposeReason) {

                            } else if (1 == mDisposeReason) {
                                mDelayEmitter.onNext(DownloadInfo.STATE.STOP);  // 延迟发送stop和pause，防止progress状态后过来
                            } else if (2 == mDisposeReason) {
                                mDelayEmitter.onNext(DownloadInfo.STATE.PAUSE);
                            }
                        }
                        return mDownloadInfo;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) {
                        KLog.w(TAG, "subscribe accept, downloadInfo:" + downloadInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        KLog.w(TAG, "subscribe accept, throwable:" + throwable);
                        mDownloadInfo.setState(DownloadInfo.STATE.ERROR);
                        mDownloadInfo.setThrowable(throwable);
                        mObservableEmitter.onNext(mDownloadInfo);
                    }
                });
    }

    public void downloadStart(String url) {
        KLog.w(TAG, "start url:" + url);
        mDownloadInfo = new DownloadInfo();
        mDownloadInfo.setReadLength(0);
        mDownloadInfo.setContentLength(0);
        mDownloadInfo.setSavePath(SAVE_PATH + UrlUtil.getUrlName(url));
        mDownloadInfo.setUrl(url);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);
        builder.addInterceptor(new DownloadInterceptor(downloadProgressListener));

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(UrlUtil.getBaseUrl(url))
                .build();
        service = retrofit.create(DownloadService.class);

        mDownloadInfo.setState(DownloadInfo.STATE.START);
        mObservableEmitter.onNext(mDownloadInfo);

        download();
    }

    public void downloadStop() {
        if (DownloadInfo.STATE.PAUSE == mDownloadInfo.getState()) {  // 已经暂停时，直接发送STOP
            mDownloadInfo.setState(DownloadInfo.STATE.STOP);
            mObservableEmitter.onNext(mDownloadInfo);
        } else {
            mDisposeReason = 1;
        }
        if (null != disposable) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void downloadPause() {
        mDisposeReason = 2;
        if (null != disposable) {
            disposable.dispose();
        }
    }

    public void downloadContinue() {
        mDownloadInfo.setState(DownloadInfo.STATE.CONTINUE);
        mObservableEmitter.onNext(mDownloadInfo);

        download();
    }

    /**
     * 下载的Service
     */
    private interface DownloadService {
        /**
         * 请求方式GET
         * @param start     从某个字节开始下载数据
         * @param url       文件下载的url
         * @return          Observable
         * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
         */
        @Streaming
        @GET
        Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
    }

}
