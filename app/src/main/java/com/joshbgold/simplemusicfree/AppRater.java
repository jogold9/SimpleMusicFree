package com.joshbgold.simplemusicfree;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppRater {
    private final static String APP_TITLE = "Simple Music Free";// App Name
    private final static String APP_PNAME = "com.joshbgold.simplemusicfree";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days
    private static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(mContext);
        textView.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        textView.setWidth(240);
        textView.setPadding(4, 0, 4, 10);
        linearLayout.addView(textView);

        Button rateButton = new Button(mContext);
        rateButton.setText("Rate " + APP_TITLE);
        rateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        linearLayout.addView(rateButton);

        Button remindLaterButton = new Button(mContext);
        remindLaterButton.setText("Remind me later");
        remindLaterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LAUNCHES_UNTIL_PROMPT = LAUNCHES_UNTIL_PROMPT + 3;
                dialog.dismiss();
            }
        });
        linearLayout.addView(remindLaterButton);

        Button noThanksButton = new Button(mContext);
        noThanksButton.setText("No, thanks");
        noThanksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        linearLayout.addView(noThanksButton);

        dialog.setContentView(linearLayout);
        dialog.show();
    }
}
