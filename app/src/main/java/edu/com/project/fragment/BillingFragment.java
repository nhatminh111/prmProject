package edu.com.project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import edu.com.project.R;
import edu.com.project.adapter.BillingCartAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.CartItem;
import edu.com.project.fragment.HomeFragment;

public class BillingFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private int userId;
    private List<CartItem> cartItems;
    private double total = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        TextView textTotal = view.findViewById(R.id.textTotal);
        Button buttonPay = view.findViewById(R.id.buttonPay);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBilling);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(getContext(), "Please log in first.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }
        dbHelper = new DatabaseHelper(getContext());
        userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }
        cartItems = dbHelper.getCartItemsForUser(userId);

        // Set up RecyclerView with BillingCartAdapter
        BillingCartAdapter adapter = new BillingCartAdapter(cartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotal();
        }
        textTotal.setText("Total: $" + total);

        buttonPay.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dbHelper.createOrder(userId, total, date, cartItems);
            Toast.makeText(getContext(), "Payment successful! Order placed.", Toast.LENGTH_LONG).show();

            // Navigate to HomeFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, new HomeFragment())
                    .commit();
        });

        return view;
    }
}