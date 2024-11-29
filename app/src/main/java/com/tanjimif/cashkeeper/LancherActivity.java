package com.tanjimif.cashkeeper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LancherActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefstanjimifcashkeeper";
    private static final String KEY_FIRST_RUN = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean(KEY_FIRST_RUN, true);

        if (!isFirstRun) {
            Intent intent = new Intent(LancherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_lancher);
        ImageView draggableIcon = findViewById(R.id.draggable_icon);

        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.updown);
        animator.setTarget(draggableIcon);
        animator.start();

        Button confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_RUN, false);
            editor.apply();

            Intent intent = new Intent(LancherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
