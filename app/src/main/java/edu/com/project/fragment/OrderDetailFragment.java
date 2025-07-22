package edu.com.project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.com.project.R;
import edu.com.project.adapter.OrderDetailAdapter;
import edu.com.project.database.DatabaseHelper;
import edu.com.project.model.OrderDetail;
import java.util.List;

public class OrderDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle args = getArguments();
        if (args == null || !args.containsKey("order_id")) return;
        int orderId = args.getInt("order_id");

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        List<OrderDetail> orderDetails = dbHelper.getOrderDetails(orderId);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrderDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderDetailAdapter adapter = new OrderDetailAdapter(requireContext(), orderDetails, null);
        recyclerView.setAdapter(adapter);
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