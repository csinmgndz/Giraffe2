package com.example.sinem.giraffe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by sinem on 12.9.2017.
 */

public class BaseTaskService extends Service {

    private int mNumTask = 0;

    public void taskStarted() {
        changeNumberOfTasks(1);
    }

    public void taskCompleted() {
        changeNumberOfTasks(-1);
    }

    private synchronized void changeNumberOfTasks(int delta) {
        mNumTask += delta;

        if (mNumTask <= 0) {
            stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }
}