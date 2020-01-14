package com.example.hw5;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        AddEditExpenseFragment addEditExpenseFragment = (AddEditExpenseFragment) getActivity().getSupportFragmentManager().findFragmentByTag("add_or_edit_expense_fragment");

        String monthTag = "";

        switch (month) {
            case 0:
                monthTag = "Jan";
                break;
            case 1:
                monthTag = "Feb";
                break;
            case 2:
                monthTag = "Mar";
                break;
            case 3:
                monthTag = "Apr";
                break;
            case 4:
                monthTag = "May";
                break;
            case 5:
                monthTag = "Jun";
                break;
            case 6:
                monthTag = "Jul";
                break;
            case 7:
                monthTag = "Aug";
                break;
            case 8:
                monthTag = "Sep";
                break;
            case 9:
                monthTag = "Oct";
                break;
            case 10:
                monthTag = "Nov";
                break;
            case 11:
                monthTag = "Dec";
                break;
        }
        addEditExpenseFragment.setDateDisplayTextView(monthTag + " " + day + ", " + year, month, day, year);
    }
}