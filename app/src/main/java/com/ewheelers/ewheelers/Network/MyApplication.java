package com.ewheelers.ewheelers.Network;

import android.app.Application;

public class MyApplication extends Application {
    // Gloabl declaration of variable to use in whole app

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReciever.ConnectivityRecieverListener listener) {
        ConnectivityReciever.connectivityReceiverListener = (ConnectivityReciever.ConnectivityRecieverListener) listener;
    }

}
