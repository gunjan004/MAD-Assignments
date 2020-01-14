package com.example.hw5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class AddEditExpenseFragment extends Fragment {

    public static int PICKED_IMAGE = 1;

    private OnAddExpenseFragmentInteractionListener mListener;

    private TextView dateDisplayTextView;
    private int monthInt, dayInt, yearInt;
    private ImageView receiptImageView;
    private String downloadUrl = null;

    public AddEditExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.isOnMainScreen = false;
        getActivity().invalidateOptionsMenu();

        final View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        EditText nameEditText = view.findViewById(R.id.expense_name_editText);
        EditText amountEditText = view.findViewById(R.id.amount_editText);
        Button addOrSaveExpenseButton = view.findViewById(R.id.add_expense_button);
        dateDisplayTextView = view.findViewById(R.id.date_display_textView);
        receiptImageView = view.findViewById(R.id.receipt_imageView);

        if(this.getArguments() != null) {
            getActivity().setTitle("Edit Expense");

            Expense expense = (Expense) this.getArguments().getSerializable(ShowExpenseFragment.EXPENSE_KEY);
            final int index = this.getArguments().getInt(ShowExpenseFragment.INDEX_KEY);

            nameEditText.setText(expense.name);
            amountEditText.setText(expense.amount + "");
            addOrSaveExpenseButton.setText(R.string.save_changes_text);
            dateDisplayTextView.setText(expense.date);
            monthInt = expense.month;
            dayInt = expense.day;
            yearInt = expense.year;
            downloadUrl = expense.receiptUrl;
            Picasso.get().load(downloadUrl).placeholder(R.drawable.loading).into(receiptImageView);

            dateDisplayTextView = view.findViewById(R.id.date_display_textView);

            view.findViewById(R.id.date_picker_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                }
            });

            receiptImageView = view.findViewById(R.id.receipt_imageView);

            view.findViewById(R.id.receipt_picker_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chooseImageIntent = new Intent();
                    chooseImageIntent.setType("image/*");
                    chooseImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Intent chooserIntent = Intent.createChooser(chooseImageIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

                    startActivityForResult(chooserIntent, PICKED_IMAGE);
                }
            });
            addOrSaveExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText expenseNameEditText = view.findViewById(R.id.expense_name_editText);
                    EditText amountEditText = view.findViewById(R.id.amount_editText);

                    if (expenseNameEditText.length() > 0 && amountEditText.length() > 0 && dateDisplayTextView.length() > 0 && downloadUrl != null) {
                        Expense expense = new Expense();
                        expense.name = expenseNameEditText.getText().toString();
                        expense.amount = Double.parseDouble(amountEditText.getText().toString());
                        expense.date = dateDisplayTextView.getText().toString();
                        expense.receiptUrl = downloadUrl;
                        expense.month = monthInt;
                        expense.day = dayInt;
                        expense.year = yearInt;

                        mListener.updateExpenseInList(expense, index);
                        } else {
                        if (expenseNameEditText.length() == 0) {
                            Toast.makeText(getContext(), "Please enter the name of your expense", Toast.LENGTH_LONG).show();
                        } else if (amountEditText.length() == 0) {
                            Toast.makeText(getContext(), "Please enter the amount of your expense", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Please select the date of your expense", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        } else {

            getActivity().setTitle("Add Expense");
            dateDisplayTextView = view.findViewById(R.id.date_display_textView);
            view.findViewById(R.id.date_picker_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getActivity()
                            .getSupportFragmentManager(),
                            "datePicker");
                }
            });
            receiptImageView = view.findViewById(R.id.receipt_imageView);
            view.findViewById(R.id.receipt_picker_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chooseImageIntent = new Intent();
                    chooseImageIntent.setType("image/*");
                    chooseImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Intent chooserIntent = Intent.createChooser(chooseImageIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

                    startActivityForResult(chooserIntent, PICKED_IMAGE);
                }
            });

            addOrSaveExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText expenseNameEditText = view.findViewById(R.id.expense_name_editText);
                    EditText amountEditText = view.findViewById(R.id.amount_editText);

                    if (expenseNameEditText.length() > 0 && amountEditText.length() > 0 && dateDisplayTextView.length() > 0 && downloadUrl != null) {
                        Expense expense = new Expense();
                        expense.name = expenseNameEditText.getText().toString();
                        expense.amount = Double.parseDouble(amountEditText.getText().toString());
                        expense.date = dateDisplayTextView.getText().toString();
                        expense.receiptUrl = downloadUrl;
                        expense.month = monthInt;
                        expense.day = dayInt;
                        expense.year = yearInt;

                        mListener.addExpenseToList(expense);

                    } else {
                        if (expenseNameEditText.length() == 0) {
                            Toast.makeText(getContext(), "Please enter the name of your expense", Toast.LENGTH_LONG).show();
                        } else if (amountEditText.length() == 0) {
                            Toast.makeText(getContext(), "Please enter the amount of your expense", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Please select the date of your expense", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("demo", "onActivityResult: " + data.getExtras());

        Log.d("demo", "requestCode: " + requestCode + " resultCode: " + resultCode + " RESULT_OK: " + RESULT_OK);
        if(resultCode == RESULT_OK && requestCode == PICKED_IMAGE) {
            if(data.getExtras() != null) {

                final Bitmap bitmap = data.getExtras().getParcelable("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] byteData = baos.toByteArray();

                MainActivity mainActivityContext = (MainActivity) getContext();

                final StorageReference imageRef = mainActivityContext.storageRef.child("images/" + bitmap.toString());
                UploadTask uploadTask = imageRef.putBytes(byteData);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("demo", "onFailure: " + byteData);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("demo", "onSuccess: " + imageRef.getDownloadUrl());
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Log.d("demo", "onSuccess: " + downloadUrl);
                                Picasso.get().load(downloadUrl).placeholder(R.drawable.loading).into(receiptImageView);
                            }
                        });
                    }
                });
            } else {
                final Uri filePath = data.getData();

                MainActivity mainActivityContext = (MainActivity) getContext();

                final StorageReference imageRef = mainActivityContext.storageRef.child("images/" + filePath.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(filePath);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("demo", "onFailure: " + filePath);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("demo", "onSuccess: " + imageRef.getDownloadUrl());
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Log.d("demo", "onSuccess: " + downloadUrl);
                                Picasso.get().load(downloadUrl).placeholder(R.drawable.loading).into(receiptImageView);
                            }
                        });
                    }
                });
            }
        }

    }

    public void setDateDisplayTextView(String date, int monthInt, int dayInt, int yearInt) {
        dateDisplayTextView.setText(date);
        this.monthInt = monthInt;
        this.dayInt = dayInt;
        this.yearInt = yearInt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddExpenseFragmentInteractionListener) {
            mListener = (OnAddExpenseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseAppFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddExpenseFragmentInteractionListener {
        // TODO: Update argument type and name
        void addExpenseToList(Expense expense);
        void updateExpenseInList(Expense expense, int index);
    }
}
