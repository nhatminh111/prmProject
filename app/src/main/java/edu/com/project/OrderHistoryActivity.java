package edu.com.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.com.project.adapter.OrderAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Order;

public class OrderHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        List<Order> orderList = dbHelper.getOrdersForUser(userId);
        // Pass null as the click listener if you do not need click handling here
        OrderAdapter adapter = new OrderAdapter(orderList, null);
        recyclerView.setAdapter(adapter);
    }
}