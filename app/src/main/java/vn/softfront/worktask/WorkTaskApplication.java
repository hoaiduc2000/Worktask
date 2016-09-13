package vn.softfront.worktask;

import android.app.Application;

import vn.softfront.worktask.util.DialogUntil;

/**
 * Created by nguyen.hoai.duc on 7/29/2016.
 */
public class WorkTaskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DialogUntil.init(this);
    }
}
