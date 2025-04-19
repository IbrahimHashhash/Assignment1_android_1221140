package com.example.zaitoona.Adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zaitoona.Activities.DetailedActivity;
import com.example.zaitoona.Manager;
import com.example.zaitoona.R;
import com.example.zaitoona.dataaccess.OliveOilProduct;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<OliveOilProduct> products;

    public ProductAdapter(List<OliveOilProduct> products) {
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.olive_oil,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.productImage);
        OliveOilProduct product = products.get(position);
        Drawable dr = ContextCompat.getDrawable(cardView.getContext(), product.getImageResId());
        imageView.setImageDrawable(dr);

        TextView pName = (TextView) cardView.findViewById(R.id.productName);
        TextView pType = (TextView) cardView.findViewById(R.id.productType);
        TextView pPrice = (TextView) cardView.findViewById(R.id.productPrice);
        TextView pQuantity = (TextView) cardView.findViewById(R.id.productQuantity);
        TextView pLiter = (TextView) cardView.findViewById(R.id.liter);
        Button buyNow = (Button) cardView.findViewById(R.id.buyNowBtn);
        Button addToCart = (Button) cardView.findViewById(R.id.addToCartBtn);

        pName.setText(product.getName());
        pType.setText(product.getType());
        pLiter.setText("Liters: " + String.valueOf(product.getLiters()));
        pPrice.setText("Price: " + String.valueOf(product.getPrice()) + " $");
        pQuantity.setText("Quantity: " + String.valueOf(product.getQuantity()));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), DetailedActivity.class);
                intent.putExtra("productId", product.getProductId());
                intent.putExtra("name", product.getName());
                intent.putExtra("type", product.getType());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("quantity", product.getQuantity());
                intent.putExtra("imageResId", product.getImageResId());
                intent.putExtra("liters", product.getLiters());
                cardView.getContext().startActivity(intent);
            }
        });

        buyNow.setOnClickListener(v -> {
            if (product.getQuantity() > 0) {
                product.setQuantity(product.getQuantity() - 1);
                notifyItemChanged(holder.getAdapterPosition());
                Manager.updateProductInPrefs(cardView.getContext(), product);
            }
        });

        addToCart.setOnClickListener(v -> {
            if (product.getQuantity() > 0) {
                // create a new product object with quantity of 1 for the cart
                OliveOilProduct cartProduct = new OliveOilProduct(
                        product.getProductId(),
                        product.getName(),
                        product.getType(),
                        product.getPrice(),
                        1, // add just 1 to cart
                        product.getImageResId(),
                        product.getLiters()
                );

                Manager.addToCart(cardView.getContext(), cartProduct);
                Toast.makeText(cardView.getContext(), "1 " + product.getName() + " added to cart",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(cardView.getContext(), "Item out of stock",
                        Toast.LENGTH_SHORT).show();
            }
        });    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}