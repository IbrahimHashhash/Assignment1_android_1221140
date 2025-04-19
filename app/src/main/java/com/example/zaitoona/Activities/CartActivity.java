package com.example.zaitoona.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zaitoona.Adapters.CartAdapter;
import com.example.zaitoona.Manager;
import com.example.zaitoona.R;
import com.example.zaitoona.dataaccess.OliveOilProduct;
import java.util.List;
// this is the cart activity, where i store checked out items
public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button buyAllButton;
    private CartAdapter cartAdapter;
    private List<OliveOilProduct> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        buyAllButton = findViewById(R.id.buyAllButton);
        cartItems = Manager.getCart(this);
        cartAdapter = new CartAdapter(this, cartItems, this::updateTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalPrice();
        // buy all the products in the cart
        buyAllButton.setOnClickListener(v -> {
            List<OliveOilProduct> allProducts = Manager.getAllSavedProducts(this);

            for (OliveOilProduct cartItem : cartItems) {
                for (OliveOilProduct product : allProducts) {
                    if (product.getProductId() == cartItem.getProductId()) {
                        int newQuantity = product.getQuantity() - cartItem.getQuantity();
                        product.setQuantity(Math.max(0, newQuantity));
                        Manager.updateProductInPrefs(this, product);
                        break;
                    }
                }
            }

            Toast.makeText(this, "Purchased!", Toast.LENGTH_SHORT).show();

            cartItems.clear();
            Manager.saveCart(this, cartItems);
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });    }
    // this method is used to update the price when a new item is added to the cart
    private void updateTotalPrice() {
        double total = 0;
        for (OliveOilProduct item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPriceTextView.setText("Total: " + total + " $");
    }
}
