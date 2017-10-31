package com.vikramezhil.dccviewexample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vikramezhil.dccv.CalorieCounterView;
import com.vikramezhil.dccv.OnCalorieCounterListener;

/**
 * Droid Calorie Counter Example Activity
 *
 * @author Vikram Ezhil
 */

public class ActivityDCC extends AppCompatActivity {

    private final String TAG = "ActivityDCC";
    private CalorieCounterView calorieCounterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dccv);

        calorieCounterView = findViewById(R.id.calorieCounterView);
        calorieCounterView.setMinimum(0);
        calorieCounterView.setMaximum(1500);
        calorieCounterView.setProgressWithAnimation(500);
        calorieCounterView.setIgnoreMax(true);
        calorieCounterView.setDangerMaxWarning(true, Color.RED);
        calorieCounterView.setOnCalorieCounterListener(new OnCalorieCounterListener() {
            @Override
            public void onCalorieCounterClicked() {
                Log.i(TAG, "Calorie Counter Clicked");

                // Updating the calorie counter progress
                int currentCalories = Integer.valueOf(calorieCounterView.getFooterTxt());
                calorieCounterView.setProgress(currentCalories + 100);
            }

            @Override
            public void onProgressUpdated(int progress, int remaining) {
                Log.i(TAG, "Calorie Counter progress - " + progress + ", remaining - " + remaining);

                // Setting the calorie counter header and footer
                calorieCounterView.setHeaderTxt(String.valueOf(remaining));
                calorieCounterView.setFooterTxt(String.valueOf(progress));
            }
        });
    }
}
