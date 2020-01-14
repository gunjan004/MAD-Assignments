package com.example.baccalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static TextView weight;
    public static Switch gender;
    private static RadioGroup drinkSizeGroup;
    private static RadioButton drinkSize;
    private static SeekBar alcoholPercentage;
    private static TextView alcPerDisplay;
    private static TextView bacLevel;
    private static double bacLevelDisplay = 0.00;
    private static double finalBACLevel = 0.00;
    private static TextView finalResult;
    private static int minAlcohol = 5;
    private static int currentAlcohol = 5;
    private static int maxAlcohol = 25;
    private static int stepAlcohol = 5;
    private static Double r = 0.0;
    private static int W = 0;
    private static RadioButton radio1oz ;
    private static String stringWeight;
    private static ArrayList<Integer> alcoholConsumed = new ArrayList<>();
    private ProgressBar progressBar;
    private static int progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("  BAC Calculator");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.wine);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        progressBar = findViewById(R.id.progressBarBACLevel);
        bacLevel = findViewById(R.id.textViewBACLevel);
        drinkSizeGroup = findViewById(R.id.radioGroupDrinkSize);
        radio1oz = findViewById(R.id.radioButton1oz);
        weight = findViewById(R.id.editTextWeight);
        gender = findViewById(R.id.switchGender);
        finalResult = findViewById(R.id.textViewResult);
        finalBACLevel = Math.round(bacLevelDisplay * 100.0) / 100.0;
        bacLevel.setText(Double.toString(finalBACLevel));
        progress = (int) (finalBACLevel * 100);
        progressBar.setMax(25);
        progressBar.setProgress(progress);
        seekbar();
        setResultColor();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClick();
            }
        });
        drinkSizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                drinkSize = findViewById(checkedId);
            }
        });

        findViewById(R.id.buttonAddDrink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weight.getText().toString().length() <=0) {
                    weight.setError("Please enter weight");
                } else {
                    if (W == 0 && r == 0.0) {
                        saveButtonClick();
                    }
                    String stringDrinkSize;
                    if (drinkSize == null) {
                        stringDrinkSize = "1 oz";
                    } else {
                        stringDrinkSize = (String) drinkSize.getText();
                    }
                    stringDrinkSize = stringDrinkSize.replace(" oz", "");
                    alcoholConsumed.add(Integer.parseInt(stringDrinkSize) * currentAlcohol);
                    bacLevelDisplay = bacLevelDisplay + ((alcoholConsumed.get(alcoholConsumed.size() - 1) * 6.24) / (W * r * 100));
                    finalBACLevel = Math.round(bacLevelDisplay * 100.0) / 100.0;
                    progress = (int) (finalBACLevel * 100);
                    progressBar.setProgress(progress);
                    bacLevel.setText(Double.toString(finalBACLevel));
                    setResultColor();
                    enableDisable();
                }
            }
        });
        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bacLevelDisplay = 0.00;
                finalBACLevel = Math.round(bacLevelDisplay * 100.0) / 100.0;
                bacLevel.setText(Double.toString(finalBACLevel));
                weight.setText("");
                W = 0;
                r = 0.0;
                gender.setChecked(false);
                setResultColor();
                enableDisable();

                //reset to 0
                progressBar.setProgress(0);
                radio1oz.setChecked(true);

                //reset to 0
                alcoholPercentage = findViewById(R.id.seekBarAlcohol);
                alcoholPercentage.setProgress(0);
            }
        });
    }
    public void saveButtonClick() {
        if(weight.getText().toString().length() <=0) {
            weight.setError("Please enter weight");
        }else {
            stringWeight = "" + weight.getText();
            W = Integer.parseInt(stringWeight);
            if (gender.isChecked()) {
                r = 0.68;
            } else {

                r = 0.55;
            }
            if (bacLevelDisplay != 0.00) {
                bacLevelDisplay = 0.00;
                calculateBACLevel();
                setResultColor();
            }
            enableDisable();
        }
    }
    public void enableDisable() {
        if(finalBACLevel >= 0.25){
            findViewById(R.id.buttonAddDrink).setEnabled(false);
            Toast.makeText(MainActivity.this, "No more drinks for you", Toast.LENGTH_LONG).show();
        }else{
            findViewById(R.id.buttonAddDrink).setEnabled(true);
        }
    }
    public void setResultColor() {
        if(finalBACLevel <= 0.08) {
            finalResult.setText("You’re safe");
            finalResult.setBackgroundColor(getResources().getColor(R.color.green));
            finalResult.setTextColor(getResources().getColor(R.color.white));
        } else if(finalBACLevel >= 0.08 && finalBACLevel <= 0.20) {
            finalResult.setText("Be careful…");
            finalResult.setBackgroundColor(getResources().getColor(R.color.yellow));
            finalResult.setTextColor(getResources().getColor(R.color.white));
        }else if(finalBACLevel >= 0.20) {
            finalResult.setText("Over the limit!");
            finalResult.setBackgroundColor(getResources().getColor(R.color.red));
            finalResult.setTextColor(getResources().getColor(R.color.white));
        }
    }
    public void calculateBACLevel(){
        String stringDrinkSize;
        if (drinkSize == null) {
            stringDrinkSize = "1 oz";
        } else {
            stringDrinkSize = (String) drinkSize.getText();
        }
        stringDrinkSize = stringDrinkSize.replace(" oz", "");
        for (int i = 0; i < alcoholConsumed.size(); i++) {
            bacLevelDisplay = bacLevelDisplay + ((alcoholConsumed.get(i) * 6.24) / (W * r * 100));
        }
        finalBACLevel = Math.round(bacLevelDisplay * 100.0) / 100.0;
        progress = (int) (finalBACLevel * 100);
        progressBar.setProgress(progress);
        bacLevel.setText(Double.toString(finalBACLevel));
        enableDisable();
    }
    public void seekbar (){
        alcoholPercentage = findViewById(R.id.seekBarAlcohol);
        alcPerDisplay = findViewById(R.id.textViewSeekbarProgress);

        alcoholPercentage.setMax(maxAlcohol - minAlcohol);
        alcoholPercentage.incrementProgressBy(stepAlcohol);
        alcoholPercentage.setProgress(0);
        alcPerDisplay.setText(currentAlcohol + "%");
        alcoholPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 5;
                progress = progress * 5;
                currentAlcohol = progress + minAlcohol;
                alcPerDisplay.setText(currentAlcohol + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alcPerDisplay.setText(currentAlcohol + "%");
            }
        });
    }
}
