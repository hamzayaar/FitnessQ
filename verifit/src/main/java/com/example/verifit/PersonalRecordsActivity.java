package com.example.verifit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;


public class PersonalRecordsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ArrayList<ExercisePersonalStats> exerciseStats = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_records);

        onCreateStuff();

        calculatePersonalRecords();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ExerciseStatsAdapter exerciseStatsAdapter = new ExerciseStatsAdapter(this, exerciseStats);
        recyclerView.setAdapter(exerciseStatsAdapter);

    }


    public void onCreateStuff()
    {
        // Bottom Navigation Bar Intents
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.me);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    public void calculatePersonalRecords()
    {
        // Calculate PRs before parsing Hashmaps
        MainActivity.calculatePersonalRecords();

        for (HashMap.Entry<String,Double> entry : MainActivity.VolumePRs.entrySet())
        {
            String currentExerciseName = entry.getKey();
            String exerciseCategory = MainActivity.getExerciseCategory(currentExerciseName);

            ExercisePersonalStats exercisePersonalStats = new ExercisePersonalStats();
            exercisePersonalStats.exerciseName = currentExerciseName;
            exercisePersonalStats.exerciseCategory = exerciseCategory;
            exercisePersonalStats.maxVolume = MainActivity.VolumePRs.get(currentExerciseName);
            exercisePersonalStats.maxSetVolume = MainActivity.SetVolumePRs.get(currentExerciseName).first * MainActivity.SetVolumePRs.get(currentExerciseName).second;
            exercisePersonalStats.maxSetVolumeReps = MainActivity.SetVolumePRs.get(currentExerciseName).first; // First = Reps
            exercisePersonalStats.MaxSetVolumeWeight = MainActivity.SetVolumePRs.get(currentExerciseName).second; // Second = Weight
            exercisePersonalStats.maxReps = MainActivity.MaxRepsPRs.get(currentExerciseName);
            exercisePersonalStats.maxWeight = MainActivity.MaxWeightPRs.get(currentExerciseName);
            exercisePersonalStats.actual1RM = MainActivity.ActualOneRepMaxPRs.get(currentExerciseName);
            exercisePersonalStats.estimated1RM = MainActivity.EstimatedOneRMPRs.get(currentExerciseName);
            exercisePersonalStats.isFavorite = MainActivity.isExerciseFavorite(currentExerciseName);


            if(MainActivity.VolumePRs.get(currentExerciseName) == 0.0)
            {
                // Skip this exercise, it was not even performed
            }
            else
            {
                exerciseStats.add(exercisePersonalStats);
            }

//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onCreateStuff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.personal_records_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.settings)
        {
            Intent in = new Intent(this,SettingsActivity.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    // Navigates to given activity based on the selected menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        if(item.getItemId() == R.id.home)
        {
            Intent in = new Intent(this,MainActivity.class);
            startActivity(in);
            overridePendingTransition(0,0);
        }
        else if(item.getItemId() == R.id.exercises)
        {
            Intent in = new Intent(this,ExercisesActivity.class);
            startActivity(in);
            overridePendingTransition(0,0);
        }
        else if(item.getItemId() == R.id.diary)
        {
            Intent in = new Intent(this,DiaryActivity.class);
            startActivity(in);
            overridePendingTransition(0,0);
        }
        else if(item.getItemId() == R.id.charts)
        {
            Intent in = new Intent(this,ChartsActivity.class);
            startActivity(in);
            overridePendingTransition(0,0);
        }
        else if(item.getItemId() == R.id.me)
        {
            Intent in = new Intent(this, PersonalRecordsActivity.class);
            startActivity(in);
            overridePendingTransition(0,0);
        }
        return true;
    }
}