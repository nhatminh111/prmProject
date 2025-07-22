package edu.com.project.adapter;

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

import java.util.List;
import edu.com.project.model.CartItem;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartChangedListener listener) {
        this.cartItems = cartItems;
        this.dbHelper = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageBook);
        holder.textTitle.setText(item.getTitle());
        holder.textAuthor.setText(item.getAuthor());
        holder.textPrice.setText(holder.itemView.getContext().getString(R.string.cart_price_total, item.getPrice(), item.getTotal()));
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        holder.buttonIncrease.setOnClickListener(v -> {
            dbHelper.updateCartItemQuantity(item.getId(), item.getQuantity() + 1);
            if (listener != null) listener.onCartChanged();
        });
        holder.buttonDecrease.setOnClickListener(v -> {
            dbHelper.updateCartItemQuantity(item.getId(), item.getQuantity() - 1);
            if (listener != null) listener.onCartChanged();
        });
        holder.buttonRemove.setOnClickListener(v -> {
            dbHelper.removeCartItem(item.getId());
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook;
        TextView textTitle, textAuthor, textPrice, textQuantity;
        Button buttonIncrease, buttonDecrease, buttonRemove;
        CartViewHolder(View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBook);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}