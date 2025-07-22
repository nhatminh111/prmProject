package edu.com.project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.OrderDetail;

public class OrderDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        int orderId = getIntent().getIntExtra("order_id", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<OrderDetail> details = dbHelper.getOrderDetails(orderId);

        StringBuilder sb = new StringBuilder();
        for (OrderDetail detail : details) {
            sb.append(detail.getBookTitle())
              .append(" x")
              .append(detail.getQuantity())
              .append(" - $")
              .append(detail.getPrice())
              .append("\n");
        }
        TextView textOrderDetails = findViewById(R.id.textOrderDetails);
        textOrderDetails.setText(sb.toString());
    }
}
