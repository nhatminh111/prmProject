package edu.com.project.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
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
    private final double SHIPPING_FEE = 10.10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        ImageView buttonBack = view.findViewById(R.id.buttonBack);
        TextView textSubtotal = view.findViewById(R.id.textSubtotal);
        TextView textShipping = view.findViewById(R.id.textShipping);
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

        // 1. Tính subtotal
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotal();
        }

        // 2. Shipping cố định
        double shipping = SHIPPING_FEE;

        // 3. Tổng
        total = subtotal + shipping;

        // 4. Hiển thị định dạng từng dòng
        setStyledPrice(textSubtotal, subtotal);
        setStyledPrice(textShipping, shipping);
        setStyledPrice(textTotal, total);

        buttonPay.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang UserInfoFragment trước khi lưu dữ liệu
            UserInfoFragment userInfoFragment = new UserInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("userId", userId);
            bundle.putDouble("total", total);
            bundle.putSerializable("cartItems", (Serializable) cartItems);
            userInfoFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, userInfoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        buttonBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void setStyledPrice(TextView textView, double amount) {
        String priceStr = String.format(Locale.US, "$%.2f", amount);
        SpannableString styled = new SpannableString(priceStr);

        // Ký hiệu $ màu cam (#FF5722), phần còn lại màu đen
        styled.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styled.setSpan(new ForegroundColorSpan(Color.BLACK), 1, styled.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(styled);
    }
}