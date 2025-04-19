package com.example.zaitoona.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zaitoona.Manager;
import com.example.zaitoona.R;
import com.example.zaitoona.dataaccess.OliveOilProduct;

public class DetailedActivity extends AppCompatActivity {

    private int selectedQuantity = 1;
    private TextView quantityTextView, nameTextView, priceTextView, quantityInfoTextView, typeTextView, literTextView;
    private ImageView productImage;
    private ImageButton plusBtn, minusBtn;
    private Button buyNowBtn, addToCartBtn;
    private OliveOilProduct product;
    private ImageView cart;
    private int availableQuantity; //store the available inventory quantity separately

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        Intent intent = getIntent();
        int id = intent.getIntExtra("productId", 0);
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        double price = intent.getDoubleExtra("price", 0);
        availableQuantity = intent.getIntExtra("quantity", 0); // This is inventory quantity
        int imageResId = intent.getIntExtra("imageResId", 0);
        double liters = intent.getDoubleExtra("liters", 0.0);
        initializeViews();
        handleBuyNow();
        handleAddToCart();
        handlePlusMinus();

        product = new OliveOilProduct(id, name, type, price, availableQuantity, imageResId, liters);
        productImage.setImageResource(imageResId);
        quantityTextView.setText(String.valueOf(selectedQuantity)); // Show selected quantity (1 by default)
        nameTextView.setText("Name: " + name);
        priceTextView.setText("Price: " + price + "$");
        typeTextView.setText("Type: " + type);
        quantityInfoTextView.setText("Available: " + availableQuantity); // Show available inventory
        literTextView.setText("Liters: " + liters);
    }

    private void initializeViews(){
        productImage = findViewById(R.id.imageView);
        quantityTextView = findViewById(R.id.textView);
        nameTextView = findViewById(R.id.detailName);
        priceTextView = findViewById(R.id.detailPrice);
        typeTextView = findViewById(R.id.detailType);
        quantityInfoTextView = findViewById(R.id.detailQuantity);
        literTextView = findViewById(R.id.detailLiter);
        plusBtn = findViewById(R.id.plus);
        minusBtn = findViewById(R.id.minus);
        buyNowBtn = findViewById(R.id.button3);
        addToCartBtn = findViewById(R.id.button2);
        cart = findViewById(R.id.cartButton);
    }

    private void handleBuyNow(){
        cart.setOnClickListener(v -> startActivity(new Intent(DetailedActivity.this, CartActivity.class)));
        buyNowBtn.setOnClickListener(v -> {
            if (selectedQuantity > 0 && selectedQuantity <= availableQuantity) {
                int remaining = availableQuantity - selectedQuantity;
                product.setQuantity(remaining);
                Manager.updateProductInPrefs(DetailedActivity.this, product); // update inventory

                Toast.makeText(DetailedActivity.this, "Purchased " + selectedQuantity + " items", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(DetailedActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleAddToCart(){
        addToCartBtn.setOnClickListener(v -> {
            if (selectedQuantity > 0 && selectedQuantity <= availableQuantity) {
                //create a product for the cart with the selected quantity
                OliveOilProduct productForCart = new OliveOilProduct(
                        product.getProductId(),
                        product.getName(),
                        product.getType(),
                        product.getPrice(),
                        selectedQuantity,  // This is the quantity to add to cart
                        product.getImageResId(),
                        product.getLiters()
                );

                //save to cart
                Manager.addToCart(DetailedActivity.this, productForCart);

                //update product inventory
                int remaining = availableQuantity - selectedQuantity;
                product.setQuantity(remaining);
                availableQuantity = remaining; // Update local variable too
                //update the UI to show new available quantity
                quantityInfoTextView.setText("Available: " + availableQuantity);

                quantityTextView.setText(String.valueOf(selectedQuantity));

                Toast.makeText(DetailedActivity.this, selectedQuantity + " item(s) added to cart", Toast.LENGTH_SHORT).show();
                //reset selected quantity to 1 after adding to cart
                selectedQuantity = 1;
            } else {
                Toast.makeText(DetailedActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePlusMinus(){
        plusBtn.setOnClickListener(v -> {
            if (selectedQuantity < availableQuantity) { // Check against available inventory
                selectedQuantity++;
                quantityTextView.setText(String.valueOf(selectedQuantity));
            } else {
                Toast.makeText(DetailedActivity.this, "Maximum available quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        minusBtn.setOnClickListener(v -> {
            if (selectedQuantity > 1) {
                selectedQuantity--;
                quantityTextView.setText(String.valueOf(selectedQuantity));
            }
        });
    }
}