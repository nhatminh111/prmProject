package edu.com.project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.bumptech.glide.Glide;
import edu.com.project.R;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Book;

public class BookDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        int bookId = getArguments() != null ? getArguments().getInt("bookId", -1) : -1;
        if (bookId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Book book = dbHelper.getBookById(bookId);

            if (book != null) {
                TextView title = view.findViewById(R.id.textTitle);
                TextView author = view.findViewById(R.id.textAuthor);
                TextView price = view.findViewById(R.id.textPrice);
                TextView description = view.findViewById(R.id.textDescription);
                ImageView image = view.findViewById(R.id.imageBook);

                title.setText(book.getTitle());
                author.setText(book.getAuthor());
                price.setText("$" + book.getPrice());
                description.setText(book.getDescription());
                Glide.with(this).load(book.getImageUrl()).into(image);

                Button buttonAddToCart = view.findViewById(R.id.buttonAddToCart);
                buttonAddToCart.setOnClickListener(v -> {
                    // Get logged-in user
                    SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE);
                    String username = prefs.getString("username", null);
                    if (username == null) {
                        Toast.makeText(getContext(), "Please log in first.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int userId = dbHelper.getUserIdByUsername(username);
                    if (userId == -1) {
                        Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dbHelper.addOrUpdateCartItem(userId, bookId);
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    // Update cart badge in MainActivity
                    if (getActivity() instanceof edu.com.project.MainActivity) {
                        int cartCount = dbHelper.getCartItemsForUser(userId).size();
                        ((edu.com.project.MainActivity) getActivity()).setBadge(cartCount);
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}