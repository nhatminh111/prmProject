package edu.com.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Book;

public class BookDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        int bookId = getIntent().getIntExtra("book_id", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Book book = dbHelper.getBookById(bookId);

        TextView title = findViewById(R.id.textTitle);
        TextView author = findViewById(R.id.textAuthor);
        TextView price = findViewById(R.id.textPrice);
        TextView description = findViewById(R.id.textDescription);
        findViewById(R.id.imageBook);
        Button buttonAddToCart = findViewById(R.id.buttonAddToCart);

        if (book != null) {
            title.setText(book.getTitle());
            author.setText("By " + book.getAuthor());
            price.setText("$" + book.getPrice());
            description.setText(book.getDescription());
            // If you have image loading logic, add it here (e.g., Glide/Picasso)
        }

        buttonAddToCart.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String username = prefs.getString("username", null);
            if (username == null) {
                Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = dbHelper.getUserIdByUsername(username);
            if (userId == -1) {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
                return;
            }
            dbHelper.addOrUpdateCartItem(userId, bookId);
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            // Update cart badge in MainActivity if this activity is parented by MainActivity
            if (getParent() instanceof edu.com.project.MainActivity) {
                int cartCount = dbHelper.getCartItemsForUser(userId).size();
                ((edu.com.project.MainActivity) getParent()).setBadge(cartCount);
            }
        });
    }
}
