package com.example.custom;

import com.example.common.KLog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Test {
    private static Disposable disposable;

    private static int count = 10;
    public static void start() {
        disposable = Observable
                .just(1)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        KLog.d("doOnNext accept, integer:" + integer);
                        KLog.d("--->" + Thread.currentThread().getName());
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        while(0 < count) {
                            Thread.sleep(1000);
                            KLog.i("map accept, integer:" + integer);
                            KLog.i("--->" + Thread.currentThread().getName());
                            KLog.i("count:" + count);
                            count--;
                        }
                        return "test_" + integer;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String integer) throws Exception {
                        KLog.w("subscribe accept, integer:" + integer);
                        KLog.w("--->" + Thread.currentThread().getName());
                    }
                });
    }

    public static void stop() {
        disposable.dispose();
    }

}
