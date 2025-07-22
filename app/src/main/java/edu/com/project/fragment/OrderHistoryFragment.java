package edu.com.project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.com.project.R;
import edu.com.project.adapter.OrderAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Order;

public class OrderHistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(getContext(), "Please log in first.", Toast.LENGTH_SHORT).show();
            return view;
        }
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
            return view;
        }
        List<Order> orderList = dbHelper.getOrdersForUser(userId);

        OrderAdapter adapter = new OrderAdapter(orderList, order -> {
            Bundle args = new Bundle();
            args.putInt("order_id", order.getId());
            Fragment detailFragment = new OrderDetailFragment();
            detailFragment.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, detailFragment) // Use your container ID
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}