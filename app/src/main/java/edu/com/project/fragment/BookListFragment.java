// app/src/main/java/edu/com/project/fragment/BookListFragment.java
package edu.com.project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;
import edu.com.project.R;
import edu.com.project.adapter.BookSliderAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Book;

public class BookListFragment extends Fragment {
    private ViewPager2 viewPager;
    private BookSliderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        viewPager = view.findViewById(R.id.viewPagerBooks);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Book> books = dbHelper.getAllBooks();

        adapter = new BookSliderAdapter(books, book -> {
            Bundle args = new Bundle();
            args.putInt("bookId", book.getId());
            Fragment detailFragment = new BookDetailFragment();
            detailFragment.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        viewPager.setAdapter(adapter);

        return view;
    }
}