package com.bijoykochar.launcher.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bijoykochar.launcher.R;

/**
 * The view for the home taskbar
 * Created by bijoy on 10/17/15.
 */
public class TaskbarView extends LinearLayout {

    public ImageView[] apps = new ImageView[4];
    public ImageView menu;

    public TaskbarView(Context context) {
        super(context);
    }

    public TaskbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.taskbar, this, true);

        menu = (ImageView) rootView.findViewById(R.id.apps);
        menu.setColorFilter(Color.WHITE);

        apps[0] = (ImageView) rootView.findViewById(R.id.taskbar_1);
        apps[1] = (ImageView) rootView.findViewById(R.id.taskbar_2);
        apps[2] = (ImageView) rootView.findViewById(R.id.taskbar_3);
        apps[3] = (ImageView) rootView.findViewById(R.id.taskbar_4);
    }

    public void createAppList(final Context context) {
        apps[0].setImageResource(R.drawable.ic_phone_white_48dp);
        apps[1].setImageResource(R.drawable.ic_email_white_48dp);
        apps[2].setImageResource(R.drawable.ic_message_white_48dp);
        apps[3].setImageResource(R.drawable.ic_photo_camera_white_48dp);

        // "android.media.action.IMAGE_CAPTURE";

        apps[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DIAL");
                context.startActivity(intent);
            }
        });

        apps[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_MAIN);
                    emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    context.startActivity(emailIntent);
                } catch (Exception e) {
                    Log.e(TaskbarView.class.getSimpleName(), e.getMessage(), e);
                }
            }
        });

        apps[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_MAIN);
                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(smsIntent);
                } catch (Exception e) {
                    Log.e(TaskbarView.class.getSimpleName(), e.getMessage(), e);
                }
            }
        });

        apps[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    PackageManager manager = context.getPackageManager();

                    ResolveInfo info = manager.resolveActivity(imageIntent, 0);
                    Intent imageActivity = new Intent();
                    imageActivity.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                    imageActivity.setAction(Intent.ACTION_MAIN);
                    imageActivity.addCategory(Intent.CATEGORY_LAUNCHER);
                    context.startActivity(imageActivity);
                } catch (Exception e) {
                    Log.e(TaskbarView.class.getSimpleName(), e.getMessage(), e);
                }
            }
        });


    }
}
