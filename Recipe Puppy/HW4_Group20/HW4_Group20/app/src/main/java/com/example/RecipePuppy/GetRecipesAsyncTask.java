package com.example.RecipePuppy;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;


public class GetRecipesAsyncTask extends AsyncTask<String, Integer, ArrayList<Recipe>> {

        ArrayList<Recipe> recipes = new ArrayList<>();
        RecipeInfo recipeInfo;

        public GetRecipesAsyncTask(RecipeInfo recipeInfo) {
                this.recipeInfo = recipeInfo;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
                recipeInfo.updateProgress(values[0]);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... strings) {
                HttpURLConnection connection = null;
                try {
                        URL url = new URL(strings[0]);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                recipes = new ArrayList<>();
                                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                                JSONObject root = new JSONObject(json);
                                JSONArray recipesArray = root.getJSONArray("results");

                                recipeInfo.setMaxProgress(recipesArray.length());
                                for (int i = 0; i < recipesArray.length(); i++) {
                                        JSONObject trackJson = recipesArray.getJSONObject(i);
                                        Recipe recipe = new Recipe();
                                        Log.d("demo", trackJson.toString());
                                        recipe.title = trackJson.getString("title");
                                        recipe.image = trackJson.getString("thumbnail");
                                        recipe.ingredients = trackJson.getString("ingredients");
                                        recipe.url = trackJson.getString("href");
                                        recipes.add(recipe);
                                        publishProgress(i + 1);
                                }
                        }

                } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                } catch (JSONException e) {
                        e.printStackTrace();
                } finally {
                        if (connection != null) {
                                connection.disconnect();
                        }
                }

                return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
                 Log.d("demo", "onPostExecute recipes.size(): " + recipes.size());
                 recipeInfo.recipeData(recipes);
        }

        public interface RecipeInfo {
                void updateProgress(int progress);
                void recipeData(ArrayList<Recipe> recipes);
                void setMaxProgress(int maxProgress);
        }
}