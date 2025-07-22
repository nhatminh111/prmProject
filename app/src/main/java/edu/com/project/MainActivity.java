package edu.com.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.com.project.database.DatabaseHelper;
import edu.com.project.fragment.BookListFragment;
import edu.com.project.fragment.CartFragment;
import edu.com.project.fragment.HomeFragment;
import edu.com.project.fragment.MapFragment;
import edu.com.project.fragment.OrderHistoryFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_POST_NOTIFICATIONS_CART = 1001;
    private int pendingCartCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, new HomeFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_cart) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, new CartFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_orders) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, new OrderHistoryFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_map) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, new MapFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_logout) {
                // Clear user session or preferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                prefs.edit().clear().apply();

                // Redirect to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        // Show notification if cart has products
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            int userId = dbHelper.getUserIdByUsername(username);
            int cartCount = dbHelper.getCartItemsForUser(userId).size();
            if (userId != -1 && cartCount > 0) {
                showCartNotification(cartCount);
                setBadge(cartCount);
            } else {
                setBadge(0);
            }
        } else {
            setBadge(0);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, new BookListFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void showCartNotification(int cartCount) {
        if (android.os.Build.VERSION.SDK_INT >= 33 && checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            pendingCartCount = cartCount;
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQ_CODE_POST_NOTIFICATIONS_CART);
            return;
        }
        postCartNotification(cartCount);
    }

    private void postCartNotification(int cartCount) {
        String channelId = "cart_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Cart Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Cart Reminder")
                .setContentText("You have " + cartCount + " product(s) in your cart!")
                .setNumber(cartCount)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1, builder.build());
    }

    // Change setBadge to public so CartFragment can access it
    public void setBadge(int count) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (count > 0) {
            bottomNav.getOrCreateBadge(R.id.nav_cart).setVisible(true);
            bottomNav.getOrCreateBadge(R.id.nav_cart).setNumber(count);
        } else {
            bottomNav.removeBadge(R.id.nav_cart);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (requestCode == REQ_CODE_POST_NOTIFICATIONS_CART && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingCartCount != -1) {
                    postCartNotification(pendingCartCount);
                    pendingCartCount = -1;
                }
            }
        }
    }
}