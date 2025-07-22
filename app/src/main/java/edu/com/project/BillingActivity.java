package edu.com.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.CartItem;

public class BillingActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private int userId;
    private List<CartItem> cartItems;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        TextView textSummary = findViewById(R.id.textSummary);
        Button buttonPay = findViewById(R.id.buttonPay);

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
        cartItems = dbHelper.getCartItemsForUser(userId);
        StringBuilder summary = new StringBuilder();
        for (CartItem item : cartItems) {
            summary.append(item.getTitle()).append(" x").append(item.getQuantity())
                    .append(" = $").append(item.getTotal()).append("\n");
            total += item.getTotal();
        }
        summary.append("\nTotal: $").append(total);
        textSummary.setText(summary.toString());

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(BillingActivity.this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                dbHelper.createOrder(userId, total, date, cartItems);
                Toast.makeText(BillingActivity.this, "Payment successful! Order placed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
