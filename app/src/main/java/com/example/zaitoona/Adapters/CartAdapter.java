package com.example.zaitoona.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zaitoona.Activities.DetailedActivity;
import com.example.zaitoona.Manager;
import com.example.zaitoona.R;
import com.example.zaitoona.dataaccess.OliveOilProduct;

import java.util.List;
// this is cart adapter for the specific items in the cart activity
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<OliveOilProduct> cartItems;
    private Context context;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<OliveOilProduct> cartItems, OnCartChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        OliveOilProduct product = cartItems.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.cartItemImage);
        TextView name = cardView.findViewById(R.id.cartItemName);
        TextView price = cardView.findViewById(R.id.cartItemPrice);
        TextView quantity = cardView.findViewById(R.id.cartItemQuantity);
        ImageButton removeButton = cardView.findViewById(R.id.removeButton);

        name.setText(product.getName());
        price.setText("Price: $" + product.getPrice());
        quantity.setText("Qty: " + product.getQuantity());
        imageView.setImageResource(product.getImageResId());
        // this is used to remove an item from an activity
        removeButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            Manager.saveCart(context, cartItems);
            listener.onCartChanged();
        });
        // this is used to go to the detailed activity of the selected item in the cart
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(cardView.getContext(), DetailedActivity.class);
            intent.putExtra("productId", product.getProductId());
            intent.putExtra("name", product.getName());
            intent.putExtra("type", product.getType());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("quantity", product.getQuantity());
            intent.putExtra("imageResId", product.getImageResId());
            intent.putExtra("liters", product.getLiters());
            cardView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
// Cart View Holder class. to interact with the cart
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public CartViewHolder(CardView v) {
            super(v);
            this.cardView = v;
        }
    }
}
