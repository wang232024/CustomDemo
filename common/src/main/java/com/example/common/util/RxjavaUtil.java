package com.example.common.util;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxjavaUtil {
    public static final String TAG = "RxjavaUtil";

    public interface RxjavaUtilListener {
        void onClick(View v);
    }

    // 按键去抖动
    public static void throttleFirstClick(View view, int second, RxjavaUtilListener listener) {
        Observable<View> observable = Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(ObservableEmitter<View> e) throws Exception {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        e.onNext(v);
                    }
                });
            }
        });
        Observer<View> observer = new Observer<View>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(View view) {
                listener.onClick(view);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .throttleFirst(second, TimeUnit.MILLISECONDS)
                .subscribe(observer);
    }

}
