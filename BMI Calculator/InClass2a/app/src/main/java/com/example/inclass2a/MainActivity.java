package com.example.inclass2a;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Body Mass Index Calculator");

        final TextView txtResult = findViewById(R.id.txtResultBMI);
        txtResult.setVisibility(View.INVISIBLE);

        final TextView txtResMain = findViewById(R.id.txtResultMain);
        txtResMain.setVisibility(View.INVISIBLE);

        Button btnBMICalc = findViewById(R.id.btnCalculateBMI);


        btnBMICalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView weight = findViewById(R.id.txtWeightValue);
                TextView heightFeet = findViewById(R.id.txtHeightFeet);
                TextView heightInches = findViewById(R.id.txtHeightInch);

                txtResult.setVisibility(View.INVISIBLE);
                txtResMain.setVisibility(View.INVISIBLE);

                if(weight.getText().toString().trim().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter Weight.",Toast.LENGTH_LONG).show();
                }
                else if(heightFeet.getText().toString().trim().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter height in Feet.",Toast.LENGTH_LONG).show();
                }
                else if(heightInches.getText().toString().trim().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter height in Inches.",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(heightInches.getText().toString().trim()) >= 12){
                    Toast.makeText(MainActivity.this,"Please enter Height less than 12 inches ",Toast.LENGTH_LONG).show();
                } else {

                    double heightFeetToInch = Double.parseDouble(heightFeet.getText().toString()) * 12.0;
                    double totalHeight = heightFeetToInch + Double.parseDouble(heightInches.getText().toString());

                    //log
                    Log.d("str", totalHeight + "");

                    double weightDouble = Double.parseDouble(weight.getText().toString());
                    double bmiResult = (weightDouble / (totalHeight * totalHeight)) * 703.0;

                    String bmiVal = "";
                    if (bmiResult <= 18.5) {
                        bmiVal = "You are Underweight!";
                    } else if (18.5 < bmiResult && bmiResult < 24.9) {
                        bmiVal = "You have Normal Weight!";
                    } else if (25 < bmiResult && bmiResult < 29.9) {
                        bmiVal = "You are Overweight!";
                    } else if (bmiResult >= 30) {
                        bmiVal = "You are Obese!";
                    }

                    Toast toast = Toast.makeText(MainActivity.this, "Body Mass Index Calculated!", Toast.LENGTH_LONG);

                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                    txtResult.setVisibility(View.VISIBLE);
                    txtResMain.setVisibility(View.VISIBLE);

                    txtResult.setText("Your BMI: " + Math.round(bmiResult * 10D) / 10D);
                    txtResMain.setText(bmiVal);

                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
