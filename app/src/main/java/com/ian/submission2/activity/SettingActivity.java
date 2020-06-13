package com.ian.submission2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ian.submission2.R;
import com.ian.submission2.reminder.ReminderReceiver;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Switch swReminder = findViewById(R.id.swReminder);
        sharedPreferences = this.getSharedPreferences("daily_reminder", Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("reminder", false);
        swReminder.setChecked(isChecked);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.setting));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putBoolean("reminder", true);
                    editor.apply();
                    Toast.makeText(SettingActivity.this, "enabled", Toast.LENGTH_SHORT).show();
                    setReminder(getApplicationContext());
                } else {
                    editor.putBoolean("reminder", false);
                    editor.apply();
                    Toast.makeText(SettingActivity.this, "disabled", Toast.LENGTH_SHORT).show();
                    cancelReminder(SettingActivity.this);
                }
            }
        });
    }

    private void setReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent reminderIntent = new Intent(context, ReminderReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, reminderIntent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}