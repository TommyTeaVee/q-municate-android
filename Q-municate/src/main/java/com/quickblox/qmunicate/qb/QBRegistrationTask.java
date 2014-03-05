package com.quickblox.qmunicate.qb;

import android.app.Activity;

import com.quickblox.module.content.QBContent;
import com.quickblox.module.content.model.QBFile;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.qmunicate.App;
import com.quickblox.qmunicate.core.concurrency.BaseProgressTask;
import com.quickblox.qmunicate.ui.main.MainActivity;

import java.io.File;

public class QBRegistrationTask extends BaseProgressTask<Object, Void, Void> {

    public QBRegistrationTask(Activity activity) {
        super(activity);
    }

    @Override
    public Void performInBackground(Object... params) throws Exception {
        QBUser user = (QBUser) params[0];
        File file = (File) params[1];

        String password = user.getPassword();

        user = QBUsers.signUpSignInTask(user);
        if (file != null) {
            QBFile qbFile = QBContent.uploadFileTask(file, false, (String) null);
            user.setFileId(qbFile.getId());
            user = QBUsers.updateUser(user);
        }
        user.setPassword(password);
        App.getInstance().setUser(user);

        return null;
    }

    @Override
    public void onResult(Void aVoid) {
        super.onResult(aVoid);
        final Activity activity = activityRef.get();
        if (isActivityAlive()) {
            MainActivity.startActivity(activity);
            activity.finish();
        }
    }
}