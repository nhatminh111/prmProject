package edu.com.project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.com.project.R;
import edu.com.project.adapter.CartAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.CartItem;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;
    private int userId;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button buttonCheckout = view.findViewById(R.id.buttonCheckout);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(getContext(), "Please log in first.", Toast.LENGTH_SHORT).show();
            return view;
        }
        dbHelper = new DatabaseHelper(getContext());
        userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
            return view;
        }
        loadCart();

        buttonCheckout.setOnClickListener(v -> {
            // Navigate to BillingFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, new BillingFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadCart() {
        cartItems = dbHelper.getCartItemsForUser(userId);
        adapter = new CartAdapter(cartItems, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCartChanged() {
        loadCart();
        // Update bottom nav badge after cart changes
        if (getActivity() instanceof edu.com.project.MainActivity) {
            int cartCount = dbHelper.getCartItemsForUser(userId).size();
            ((edu.com.project.MainActivity) getActivity()).setBadge(cartCount);
        }
    }
}