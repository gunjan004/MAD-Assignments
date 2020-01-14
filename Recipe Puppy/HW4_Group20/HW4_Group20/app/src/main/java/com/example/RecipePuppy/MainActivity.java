package com.example.RecipePuppy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeFragment.RecipeSearchFragmentInteractionListener,RecipeLoadFragmnet.RecipeLoadFragmnetInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isConnected() == true) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new RecipeFragment(), "RecipeFragment")
                    .commit();
        }
    }


    @Override
    public void displayRecipesFragmentFunction(ArrayList<Recipe> recipes) {
        getSupportFragmentManager().popBackStack();

        if(recipes.size() > 0) {

            RecipeDisplayFragment showRecipesFragment = new RecipeDisplayFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeDisplayFragment.RECIPES_LIST_KEY, recipes);
            showRecipesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, showRecipesFragment, "show_recipes_fragment")
                    .addToBackStack(null)
                    .commit();
        } else {

            Toast.makeText(MainActivity.this, "No Recipes Were Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void recipeLoadFragmentFunction(String url) {
        RecipeLoadFragmnet loadingFragment = new RecipeLoadFragmnet();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loadingFragment, "loading_fragment")
                .addToBackStack(null)
                .commit();

        new GetRecipesAsyncTask(loadingFragment).execute(url);
        return;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


}