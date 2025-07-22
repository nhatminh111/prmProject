package edu.com.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import edu.com.project.R;
import edu.com.project.model.CartItem;
import com.bumptech.glide.Glide;

public class BillingCartAdapter extends RecyclerView.Adapter<BillingCartAdapter.ViewHolder> {
    private final List<CartItem> cartItems;

    public BillingCartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billing_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textAuthor.setText("by " + item.getAuthor());
        holder.textPrice.setText("Price: $" + item.getPrice());
        holder.textQuantity.setText("Quantity: " + item.getQuantity());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl()) // Make sure CartItem has getImageUrl()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageBook);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook;
        TextView textTitle, textAuthor, textPrice, textQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBook);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
        }
    }
}