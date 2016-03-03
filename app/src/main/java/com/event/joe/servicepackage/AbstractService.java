package com.event.joe.servicepackage;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joe Aimee and Baby on 10/02/2016.
 */
public class AbstractService implements Runnable, Serializable {

    private ArrayList<ServiceListener> listeners;
    private boolean error;

    public AbstractService() {
        listeners = new ArrayList<ServiceListener>();
    }

    public void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ServiceListener listener) {
        listeners.remove(listener);
    }

    public boolean hasError() {
        return error;
    }

    public void serviceCallComplete(boolean error) {
        this.error = error;

        Message m = _handler.obtainMessage();
        Bundle b = new Bundle();
        b.putSerializable("service", this);
        m.setData(b);
        _handler.sendMessage(m);

    }

    final Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AbstractService service = (AbstractService) msg.getData()
                    .getSerializable("service");

            for (ServiceListener listener : service.listeners) {
                listener.ServiceComplete(service);
            }
        }
    };

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

}
