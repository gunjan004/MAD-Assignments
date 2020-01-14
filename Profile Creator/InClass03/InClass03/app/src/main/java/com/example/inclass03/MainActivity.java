package com.example.inclass03;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static String STUDENT_KEY = "STUDENT";
    private static TextView fName;
    private static TextView lName;
    private static TextView studentId;
    private static ImageView myAvatar;
    private static RadioGroup dptGroup;
    private static RadioButton dpt;
    private static int imageId;
    static public int REQUEST_CODE = 500;
    static public String REQUEST_VALUE = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fName = findViewById(R.id.firstNameTxt);
        lName = findViewById(R.id.lastNameTxt);
        studentId = findViewById(R.id.studentIdTxt);
        dptGroup = findViewById(R.id.radioGroup);
        myAvatar = findViewById(R.id.selectImg);
        imageId = R.drawable.select_image;

        findViewById(R.id.selectImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSelectAvtar = new Intent(MainActivity.this, SelectAvatarActivity.class);
                startActivityForResult(intentToSelectAvtar, REQUEST_CODE);
            }
        });

        dptGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dpt = findViewById(checkedId);
            }
        });


        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fName.getText().toString().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter First Name.",Toast.LENGTH_LONG).show();
                } else  if(lName.getText().toString().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter Last Name.",Toast.LENGTH_LONG).show();
                } else  if(studentId.getText().toString().length() <= 0){
                    Toast.makeText(MainActivity.this,"Please enter Student ID.",Toast.LENGTH_LONG).show();
                } else if(studentId.getText().toString().trim().length() != 9){
                    Toast.makeText(MainActivity.this,"Student ID should be a 9-digit numeric value.",Toast.LENGTH_LONG).show();
                } else if(dptGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(MainActivity.this,"Please select a department.",Toast.LENGTH_LONG).show();
                }else{

                    //If all data is entered properly then redirect to display profile activity
                    Intent intent = new Intent(MainActivity.this, DisplayMyProfile.class);

                    //StudentData studentData = new StudentData(fName.getText().toString(), lName.getText().toString(), studentId.getText().toString(), selectedDept.getText().toString());

                    intent.putExtra(STUDENT_KEY, new StudentData(fName.getText().toString(), lName.getText().toString(), studentId.getText().toString(), dpt.getText().toString(), imageId));

                    startActivity(intent);
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("demo", imageId + " ");
                imageId = data.getExtras().getInt(REQUEST_VALUE);
                Log.d("demo", imageId + " " + R.id.selectImg);
                myAvatar.setImageResource(imageId);
            }
        }
    }
}
