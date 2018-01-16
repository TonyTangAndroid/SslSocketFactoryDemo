package com.github.tonytangandroid.client.certificate.demo;

import android.app.Application;

public class DemoApplication extends Application {

    private static DemoApplication app;

    public static DemoApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;

    }
}
