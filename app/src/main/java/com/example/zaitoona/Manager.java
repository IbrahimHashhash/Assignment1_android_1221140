package com.example.zaitoona;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.zaitoona.dataaccess.OliveOilProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
// this is the manager class that is used to handle shared preference and saving the actions of the user to the local storage of the device
public class Manager {

    public Manager() {
    }

    public static List<OliveOilProduct> getAllSavedProducts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyOliveOilApp", Context.MODE_PRIVATE);
        String json = prefs.getString("products", "");
        if (json.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<List<OliveOilProduct>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
    // this method is specifically used to update the items in the shared preference after a user has done an action like buying a n time
    public static void updateProductInPrefs(Context context, OliveOilProduct updatedProduct) {
        SharedPreferences prefs = context.getSharedPreferences("MyOliveOilApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        List<OliveOilProduct> allProducts = getAllSavedProducts(context);
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == updatedProduct.getProductId()) {
                allProducts.set(i, updatedProduct);
                break;
            }
        }
        String updatedJson = gson.toJson(allProducts);
        editor.putString("products", updatedJson);
        editor.apply();
        Toast.makeText(context, updatedProduct.getName() + " has been purchased successfully", Toast.LENGTH_SHORT).show();
    }
    // this method is used to add an item to the cart
    public static void addToCart(Context context, OliveOilProduct product) {
        SharedPreferences prefs = context.getSharedPreferences("cartPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = prefs.getString("cart", "");
        List<OliveOilProduct> cart = new ArrayList<>();
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<OliveOilProduct>>() {}.getType();
            cart = gson.fromJson(json, type);
        }
        boolean exists = false;
        for (OliveOilProduct p :cart) {
            if (p.getProductId() == product.getProductId()) {
                p.setQuantity(p.getQuantity() +product.getQuantity());
                exists = true;
                break;
            }
        }
        if (!exists) {
            cart.add(product);
        }
        String updatedJson = gson.toJson(cart);
        editor.putString("cart", updatedJson);
        editor.apply();
    }

    // we get the items from the shared preference and display it
    public static List<OliveOilProduct> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cartPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("cart", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<OliveOilProduct>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
    // this method is used to save the item in the cart into shared preference
    public static void saveCart(Context context, List<OliveOilProduct> cartItems) {
        SharedPreferences prefs = context.getSharedPreferences("cartPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString("cart", gson.toJson(cartItems));
        editor.apply();
    }
}