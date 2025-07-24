package edu.com.project.adapter;

import android.content.Context;
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
import edu.com.project.model.Book;
import edu.com.project.model.OrderDetail;
import edu.com.project.database.DatabaseHelper;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private final List<OrderDetail> orderDetails;
    private final DatabaseHelper dbHelper;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails, OnOrderDetailClickListener listener) {
        this.orderDetails = orderDetails;
        this.dbHelper = new DatabaseHelper(context);
        // If you need the listener, store it as a field (not used here)
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        Book book = dbHelper.getBookByTitle(detail.getBookTitle());
        if (book != null) {
            Glide.with(holder.itemView.getContext())
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.imageBook);
            holder.textTitle.setText(book.getTitle());
            holder.textAuthor.setText(book.getAuthor());
        } else {
            holder.textTitle.setText(detail.getBookTitle());
            holder.textAuthor.setText("");
        }
        holder.textPrice.setText("$" + detail.getPrice());
        holder.textQuantity.setText("Quantity: " + detail.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
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

    // Optional: define the click listener interface if needed
    public interface OnOrderDetailClickListener {
        void onOrderDetailClick(OrderDetail detail);
    }
}