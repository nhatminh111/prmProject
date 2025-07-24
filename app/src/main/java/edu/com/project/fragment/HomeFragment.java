package edu.com.project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import edu.com.project.R;
import edu.com.project.adapter.BookSliderAdapter;
import edu.com.project.adapter.BrandingCarouselAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.Book;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Branding carousel
        ViewPager2 carouselViewPager = view.findViewById(R.id.carouselViewPager);
        List<String> brandingImages = Arrays.asList(
                "https://tse3.mm.bing.net/th/id/OIP.h6M_wDjfSAj4F4c1MeOwYgHaEK?pid=Api&P=0&h=220",
                "https://tse1.mm.bing.net/th/id/OIP.4WF0bbwUN4t8thjExiJXfQHaDq?pid=Api&P=0&h=220",
                "https://tse3.mm.bing.net/th/id/OIP.WzqX8UxKKM0dI95LYm0JfgHaDe?pid=Api&P=0&h=220",
                "https://tse2.mm.bing.net/th/id/OIP.onzVGdQVNX1d5h3CTcc7PwHaDO?pid=Api&P=0&h=220",
                "https://tse1.mm.bing.net/th/id/OIP.2ZjEEB6TGRkKdVYheBw8HQHaEo?pid=Api&P=0&h=220"
        );
        carouselViewPager.setAdapter(new BrandingCarouselAdapter(brandingImages));

        // 2. Book sliders
        RecyclerView rvFeatures = view.findViewById(R.id.rvFeatures);
        RecyclerView rvNewArrivals = view.findViewById(R.id.rvNewArrivals);
        RecyclerView rvBestSellers = view.findViewById(R.id.rvBestSellers);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Book> allBooks = dbHelper.getAllBooks();

        // Split books into three sections of 10 each
        List<Book> features = new ArrayList<>(allBooks.subList(0, Math.min(10, allBooks.size())));
        List<Book> newArrivals = allBooks.size() > 10
                ? new ArrayList<>(allBooks.subList(10, Math.min(20, allBooks.size())))
                : new ArrayList<>();
        List<Book> bestSellers = allBooks.size() > 20
                ? new ArrayList<>(allBooks.subList(20, Math.min(30, allBooks.size())))
                : new ArrayList<>();

        // Store original lists for search reset
        final List<Book> originalFeatures = new ArrayList<>(features);
        final List<Book> originalNewArrivals = new ArrayList<>(newArrivals);
        final List<Book> originalBestSellers = new ArrayList<>(bestSellers);

        BookSliderAdapter.OnBookClickListener bookClickListener = book -> {
            Bundle args = new Bundle();
            args.putInt("bookId", book.getId());
            Fragment detailFragment = new BookDetailFragment();
            detailFragment.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        };

        BookSliderAdapter featuresAdapter = new BookSliderAdapter(features, bookClickListener);
        BookSliderAdapter newArrivalsAdapter = new BookSliderAdapter(newArrivals, bookClickListener);
        BookSliderAdapter bestSellersAdapter = new BookSliderAdapter(bestSellers, bookClickListener);

        rvFeatures.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFeatures.setAdapter(featuresAdapter);

        rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNewArrivals.setAdapter(newArrivalsAdapter);

        rvBestSellers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvBestSellers.setAdapter(bestSellersAdapter);

        // 3. Search functionality
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("Search sneakers, brands, or styles...");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String lower = newText.toLowerCase();

                features.clear();
                newArrivals.clear();
                bestSellers.clear();

                for (Book b : originalFeatures) {
                    if (b.getTitle().toLowerCase().contains(lower)) features.add(b);
                }
                for (Book b : originalNewArrivals) {
                    if (b.getTitle().toLowerCase().contains(lower)) newArrivals.add(b);
                }
                for (Book b : originalBestSellers) {
                    if (b.getTitle().toLowerCase().contains(lower)) bestSellers.add(b);
                }

                featuresAdapter.notifyDataSetChanged();
                newArrivalsAdapter.notifyDataSetChanged();
                bestSellersAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return view;
    }
}