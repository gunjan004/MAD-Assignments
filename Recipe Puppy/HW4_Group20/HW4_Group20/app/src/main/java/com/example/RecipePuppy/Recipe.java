package com.example.RecipePuppy;

import java.io.Serializable;

public class Recipe implements Serializable {
    String title, image, ingredients, url;

    public Recipe() {
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
