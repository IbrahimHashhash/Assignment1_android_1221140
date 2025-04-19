package com.example.zaitoona.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zaitoona.Adapters.ProductAdapter;
import com.example.zaitoona.Manager;
import com.example.zaitoona.R;
import com.example.zaitoona.dataaccess.DAFactory;
import com.example.zaitoona.dataaccess.OliveOilProduct;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private Button search;
    private RecyclerView products;
    private Switch advancedSearch;
    private CardView advancedPanel;
    private Spinner type;
    private EditText searchBar,minPrice,maxPrice,minLiter,maxLiter;
    private CheckBox stockOnly;
    private DAFactory daFactory = new DAFactory();
    private ImageButton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // on create method for the activity
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        populateRecycleView();
    }
    private void initializeViews() { // a method to initialize the views in the activity
       products = findViewById(R.id.productRecyclerView);
       search = findViewById(R.id.searchButton);
       searchBar = findViewById(R.id.searchEditText);
       advancedSearch = findViewById(R.id.advancedSwitch);
       advancedPanel = findViewById(R.id.advancedPanel);
       cart = findViewById(R.id.cartButton);
       minPrice = findViewById(R.id.minPrice);
       maxPrice = findViewById(R.id.maxPrice);
       minLiter = findViewById(R.id.minLiter);
       maxLiter = findViewById(R.id.maxLiter);
       type = findViewById(R.id.typeSpinner);
       stockOnly = findViewById(R.id.inStockOnly);
       populateSpinner();
        cart.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, CartActivity.class)));
        handleSearch();

    }

    // a method to handle the search functionality in main activity.
    public void handleSearch() {
        advancedSearch.setOnCheckedChangeListener((button, isChecked) -> // activates the advanced search
                advancedPanel.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        search.setOnClickListener(v -> { // performs searchs
            List<OliveOilProduct> result = performSearch();

            if (stockOnly.isChecked()) {
                result = handleAvailableItems(result);
            }

            ProductAdapter productAdapter = new ProductAdapter(result);
            products.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
            products.setAdapter(productAdapter);
        });
    }

    private List<OliveOilProduct> performSearch() { // a method to perform the search
        if (advancedSearch.isChecked()) {
            String searchText = searchBar.getText().toString();
            String selectedType = type.getSelectedItem().toString();
            Double minPriceValue =parseDouble(minPrice);
            Double maxPriceValue =parseDouble(maxPrice);
            Double minLiterValue =parseDouble(minLiter);
            Double maxLiterValue =parseDouble(maxLiter);
            if (!isValidRange(minPrice, maxPrice, minPriceValue, maxPriceValue)) return new ArrayList<>();
            if (!isValidRange(minLiter, maxLiter, minLiterValue, maxLiterValue)) return new ArrayList<>();

            return DAFactory.getInstance().getAdvancedFilteredProducts(
                    searchText,
                    selectedType.equals("All Types") ? null : selectedType, minPriceValue, maxPriceValue,
                    minLiterValue, maxLiterValue
            );
        } else {
            String value = searchBar.getText().toString();
            return DAFactory.getInstance().getProducts(value);
        }
    }

    private boolean isValidRange(EditText minField, EditText maxField, Double minValue, Double maxValue) { // checks if the range is valid
        if ((minValue == null && !minField.getText().toString().isEmpty()) ||
                (maxValue == null && !maxField.getText().toString().isEmpty())) {
            return false;
        }

        if (minValue != null && maxValue != null && minValue > maxValue) {
            Toast.makeText(this," Min value cannot be greater than max value", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Double parseDouble(EditText editText) {
        String text = editText.getText().toString().trim();
        try {
            return text.isEmpty() ? null : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private List<OliveOilProduct> handleAvailableItems(List<OliveOilProduct> products) {
        List<OliveOilProduct> inStockItems = new ArrayList<>();
        for (OliveOilProduct product : products) {
            if (product.getQuantity() > 0) {
                inStockItems.add(product);
            }
        }
        return inStockItems;
    }

    private void populateSpinner() { // populating the spinner
        String[] types = daFactory.getInstance().getTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        type.setAdapter(adapter);
    }
    private void populateRecycleView() { // population the recycle view from the shared preference
        List<OliveOilProduct> oliveOilProducts = Manager.getAllSavedProducts(this);
        if (oliveOilProducts.isEmpty()) {
            oliveOilProducts = daFactory.getInstance().allProducts();
            SharedPreferences prefs = getSharedPreferences("MyOliveOilApp", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            editor.putString("products", gson.toJson(oliveOilProducts));
            editor.apply();
        }
        ProductAdapter productAdapter = new ProductAdapter(oliveOilProducts);
        products.setLayoutManager(new LinearLayoutManager(this));
        products.setAdapter(productAdapter);
    }
    @Override
    protected void onResume() { // on resume to keep the activity dynamic and reflect the changes in real time
        super.onResume();
        populateRecycleView();
    }
}
