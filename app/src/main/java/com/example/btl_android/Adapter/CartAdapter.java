package com.example.btl_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.item.CartItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CartAdapter extends FirebaseRecyclerAdapter<CartItem, CartAdapter.CartViewHolder> {

    private Context context;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartItem> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItem model) {
        holder.productName.setText(model.getName());
        holder.productPrice.setText(String.valueOf(model.getPrice()));
        holder.productQuantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(context)
                .load(model.getImageUrl())
                .into(holder.productImage);

        holder.removeButton.setOnClickListener(v -> getRef(position).removeValue());

        holder.increaseButton.setOnClickListener(v -> updateQuantity(position, model, model.getQuantity() + 1));
        holder.decreaseButton.setOnClickListener(v -> {
            if (model.getQuantity() > 1) { // Đảm bảo số lượng không nhỏ hơn 1
                updateQuantity(position, model, model.getQuantity() - 1);
            }
        });
    }

    private void updateQuantity(int position, CartItem model, int newQuantity) {
        // Cập nhật số lượng trong Firebase
        model.setQuantity(newQuantity);
        getRef(position).setValue(model);
    }



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button removeButton,increaseButton, decreaseButton;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            removeButton = itemView.findViewById(R.id.removeButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}
