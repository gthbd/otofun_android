package com.vn.otofun.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;

import com.vn.otofun.data.Prefs;
import com.vn.otofun.utils.Commons;

/**
 * Created by AnhVT on 8/5/15.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Prefs p = new Prefs(this);
        p.setLanguage(Commons.LOCALE_VN);

//        Prefs p = new Prefs(this);
//        if (p.isSetLanguage()) {
            enterApp();
//        } else {
//            setupLanguage();
//        }


    }

//    void setupLanguage() {
//        final Activity self = this;
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.labelMessage));
//        builder.setMessage("Chọn ngôn ngữ / Select language");
//        builder.setCancelable(false);
//
//        builder.setPositiveButton(getResources().getString(R.string.english), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Prefs p = new Prefs(self);
//                p.setLanguage(Locale.US);
//                enterApp();
//            }
//        });
//        builder.setNegativeButton(getResources().getString(R.string.vietnamese), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Prefs p = new Prefs(self);
//                p.setLanguage(Commons.LOCALE_VN);
//                enterApp();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

    public void enterApp() {

        Intent intent = null;
        Bundle args = new Bundle();


        if (intent == null) {
            intent = new Intent(this, HomeActivity.class);
        }

        intent.putExtras(args);

        TaskStackBuilder.create(this)
                .addNextIntent(intent)
                .startActivities();

    }
}
