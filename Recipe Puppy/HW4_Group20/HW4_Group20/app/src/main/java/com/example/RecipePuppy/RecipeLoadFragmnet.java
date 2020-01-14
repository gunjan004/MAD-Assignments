package com.example.RecipePuppy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class RecipeLoadFragmnet extends Fragment implements GetRecipesAsyncTask.RecipeInfo {

    private RecipeLoadFragmnetInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private ProgressBar loadingBar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public RecipeLoadFragmnet() {

    }

    public static RecipeLoadFragmnet newInstance(String param1, String param2) {
        RecipeLoadFragmnet fragment = new RecipeLoadFragmnet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_load, container, false);
        loadingBar = view.findViewById(R.id.progressBar_Loading);
        return view;
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeLoadFragmnetInteractionListener) {
            mListener = (RecipeLoadFragmnetInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void recipeData(ArrayList<Recipe> recipes) {
        mListener.displayRecipesFragmentFunction(recipes);
    }

    @Override
    public void updateProgress(int progress) {
        loadingBar.setProgress(progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Recipes");
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        loadingBar.setMax(maxProgress);
    }

    public interface RecipeLoadFragmnetInteractionListener {
        void displayRecipesFragmentFunction(ArrayList<Recipe> recipes);
    }
}
