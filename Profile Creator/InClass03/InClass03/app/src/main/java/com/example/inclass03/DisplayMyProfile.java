package com.example.inclass03;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView txtDisplayName = findViewById(R.id.displayName);
        final TextView txtStudId = findViewById(R.id.displayStudId);
        final TextView txtDept = findViewById(R.id.displayDept);
        final ImageView displayImage = findViewById(R.id.imageView7);

        if(getIntent() != null && getIntent().getExtras() != null){

            StudentData studentData = getIntent().getExtras().getParcelable(MainActivity.STUDENT_KEY);
            //Toast.makeText(this, studentData.toString(), Toast.LENGTH_LONG).show();

            txtDisplayName.setVisibility(View.VISIBLE);
            txtStudId.setVisibility(View.VISIBLE);
            txtDept.setVisibility(View.VISIBLE);
            displayImage.setVisibility(View.VISIBLE);

            txtDisplayName.setText(studentData.firstName + " " + studentData.lastName);
            txtStudId.setText(studentData.studentId);
            txtDept.setText(studentData.department);
            displayImage.setImageResource(studentData.imageId);
        }

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
