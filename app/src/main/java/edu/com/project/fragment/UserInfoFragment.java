package edu.com.project.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.com.project.R;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.CartItem;
import edu.com.project.fragment.HomeFragment;

public class UserInfoFragment extends Fragment {
    private EditText etName, etPhone, etEmail, etAddress, etCity, etZipCode, etNote;
    private RadioGroup radioGroupShipping;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;

    private int userId;
    private double total;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        // Ánh xạ các trường
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etZipCode = view.findViewById(R.id.etZipCode);
        etNote = view.findViewById(R.id.etNote);
        radioGroupShipping = view.findViewById(R.id.radioGroupShipping);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        dbHelper = new DatabaseHelper(getContext());

        // Lấy dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId");
            total = args.getDouble("total");
            cartItems = (List<CartItem>) args.getSerializable("cartItems");
        }

        btnConfirm.setOnClickListener(v -> confirmOrder());

        // Back button (nếu dùng ImageView)
        ImageView backBtn = view.findViewById(R.id.buttonBack);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        return view;
    }

    private void confirmOrder() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String zip = etZipCode.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        int selectedShippingId = radioGroupShipping.getCheckedRadioButtonId();
        if (selectedShippingId == -1) {
            Toast.makeText(getContext(), "Please select a shipping method", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra dữ liệu
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(address) || TextUtils.isEmpty(city) || TextUtils.isEmpty(zip)) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isDigitsOnly(zip) || zip.length() < 4) {
            Toast.makeText(getContext(), "Invalid zip code", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thành công
        Toast.makeText(getContext(), "🎉 Order placed successfully!", Toast.LENGTH_LONG).show();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dbHelper.createOrder(userId, total, date, cartItems);

        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.main_content, new HomeFragment());
        transaction.commit();
    }
}
