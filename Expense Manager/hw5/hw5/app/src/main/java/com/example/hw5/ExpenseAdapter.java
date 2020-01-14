package com.example.hw5;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    ArrayList<Expense> myData;

    public ExpenseAdapter(ArrayList<Expense> myData) {
        this.myData = myData;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_item, viewGroup, false);

        view.setBackground(view.getContext().getApplicationContext().getDrawable(R.drawable.border));

        TextView nameTextView = view.findViewById(R.id.name_in_item_list_textView);
        TextView amountTextView = view.findViewById(R.id.amount_in_item_list_textView);
        TextView dateTextView = view.findViewById(R.id.date_in_item_list_textView);
        ImageView editImageButton = view.findViewById(R.id.edit_in_item_list_imageButton);

        final Expense expense = myData.get(index);

        nameTextView.setText(expense.name);
        amountTextView.setText("Cost: $" + expense.amount);
        dateTextView.setText("Date: " + expense.date);

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityContext = (MainActivity)view.getContext();
                mainActivityContext.goToEditExpenseFragment(expense, index);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityContext = (MainActivity)view.getContext();
                mainActivityContext.goToShowExpenseFragment(expense, index);
            }
        });

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final Expense expense = myData.get(position);
        viewHolder.nameTextView.setText(expense.name);
        viewHolder.amountTextView.setText("Cost: $" + expense.amount + "");
        viewHolder.dateTextView.setText("Date: " + expense.date);

        viewHolder.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityContext = (MainActivity)viewHolder.nameTextView.getContext();
                mainActivityContext.goToEditExpenseFragment(expense, position);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityContext = (MainActivity)viewHolder.nameTextView.getContext();
                mainActivityContext.goToShowExpenseFragment(expense, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, amountTextView, dateTextView;
        ImageView editImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_in_item_list_textView);
            amountTextView = itemView.findViewById(R.id.amount_in_item_list_textView);
            dateTextView = itemView.findViewById(R.id.date_in_item_list_textView);
            editImageButton = itemView.findViewById(R.id.edit_in_item_list_imageButton);
        }
    }
}