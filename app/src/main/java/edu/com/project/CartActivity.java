package edu.com.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.com.project.adapter.CartAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.CartItem;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;
    private int userId;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button buttonCheckout = findViewById(R.id.buttonCheckout);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dbHelper = new DatabaseHelper(this);
        userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadCart();

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, BillingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCart() {
        cartItems = dbHelper.getCartItemsForUser(userId);
        adapter = new CartAdapter(cartItems, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCartChanged() {
        loadCart();
    }
}
