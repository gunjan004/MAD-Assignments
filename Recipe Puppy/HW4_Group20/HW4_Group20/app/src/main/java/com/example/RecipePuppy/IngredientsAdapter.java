package com.example.RecipePuppy;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    ArrayList<String> ingredientData;
    private static int MAX_INGREDIENTS = 5;

    public IngredientsAdapter(ArrayList<String> ingredientData) {
        this.ingredientData = ingredientData;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredients_list, viewGroup, false);

        EditText editText = view.findViewById(R.id.ingredientsEditText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientData.set(index, s.toString());
            }
        });

        final FloatingActionButton addRemoveFab = view.findViewById(R.id.add_removeFloatingActionButton);

        addRemoveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(index == getItemCount() - 1 && getItemCount() < MAX_INGREDIENTS) {
                    if (getItemCount() < MAX_INGREDIENTS) {

                        addRemoveFab.setImageResource(R.drawable.remove);
                        ingredientData.add("");
                        IngredientsAdapter.this.notifyDataSetChanged();
                    }
                } else {

                    ingredientData.remove(index);
                    IngredientsAdapter.this.notifyDataSetChanged();
                }
            }
        });

        Log.d("demo", "onCreateViewHolder: " + index);
        Log.d("demo", "onCreateViewHolder: " + getItemCount());

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.editText.setText(ingredientData.get(i));


        if(i == getItemCount() - 1 && getItemCount() < MAX_INGREDIENTS) {

            viewHolder.addRemoveFab.setImageResource(R.drawable.add);
        } else {
            viewHolder.addRemoveFab.setImageResource(R.drawable.remove);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText editText;
        FloatingActionButton addRemoveFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.ingredientsEditText);

            addRemoveFab = itemView.findViewById(R.id.add_removeFloatingActionButton);
        }
    }
}
