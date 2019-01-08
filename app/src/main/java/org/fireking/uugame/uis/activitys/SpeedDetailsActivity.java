package org.fireking.uugame.uis.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.fireking.uugame.R;

public class SpeedDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_details);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("加速游戏");
        setSupportActionBar(toolbar);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SpeedDetailsActivity.class);
        context.startActivity(intent);
    }
}
