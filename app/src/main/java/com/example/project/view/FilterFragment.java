package com.example.project.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.ShoeDAO;
import com.example.project.viewmodel.FilterViewModel;

import java.util.Set;

public class FilterFragment extends Fragment {

    private RecyclerView brandRecyclerView, colorRecyclerView, styleRecyclerView;
    private FilterAdapter brandAdapter, colorAdapter, styleAdapter;
    private Button btnApply, btnReset;
    private ShoeDAO shoeDAO;
    private FilterViewModel filterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        shoeDAO = new ShoeDAO(requireContext());
        brandRecyclerView = view.findViewById(R.id.brandRecyclerView);
        colorRecyclerView = view.findViewById(R.id.colorRecyclerView);
        styleRecyclerView = view.findViewById(R.id.styleRecyclerView);

        brandAdapter = new FilterAdapter(shoeDAO.getDistinctBrands());
        colorAdapter = new FilterAdapter(shoeDAO.getDistinctColors());
        styleAdapter = new FilterAdapter(shoeDAO.getDistinctStyles());

        filterViewModel.getSelectedBrands().observe(getViewLifecycleOwner(), selectedBrands -> {
            brandAdapter.setSelectedItems(selectedBrands);
        });
        filterViewModel.getSelectedColors().observe(getViewLifecycleOwner(), selectedColors -> {
            colorAdapter.setSelectedItems(selectedColors);
        });
        filterViewModel.getSelectedStyles().observe(getViewLifecycleOwner(), selectedStyles -> {
            styleAdapter.setSelectedItems(selectedStyles);
        });

        setupRecyclerView(brandRecyclerView, brandAdapter);
        setupRecyclerView(colorRecyclerView, colorAdapter);
        setupRecyclerView(styleRecyclerView, styleAdapter);

        int verticalSpacing = (int) getResources().getDimension(R.dimen.vertical_spacing_filter);
        brandRecyclerView.addItemDecoration(new SpacingItemDecoration(verticalSpacing, 0));
        colorRecyclerView.addItemDecoration(new SpacingItemDecoration(verticalSpacing, 0));
        styleRecyclerView.addItemDecoration(new SpacingItemDecoration(verticalSpacing, 0));

        btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
                getParentFragmentManager().popBackStack();
            }
        });
        btnReset = view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilters();
            }
        });

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView, FilterAdapter adapter) {
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void applyFilter() {
        Set<String> selectedBrands = brandAdapter.getSelectedItems();
        Set<String> selectedColors = colorAdapter.getSelectedItems();
        Set<String> selectedStyles = styleAdapter.getSelectedItems();

        filterViewModel.setSelectedBrands(selectedBrands);
        filterViewModel.setSelectedColors(selectedColors);
        filterViewModel.setSelectedStyles(selectedStyles);

        FilterListener listener = (FilterListener) getActivity();
        if (listener != null) {
            listener.onFilterApplied(selectedBrands, selectedColors, selectedStyles);
        }
    }

    private void resetFilters() {
        brandAdapter.resetSelection();
        colorAdapter.resetSelection();
        styleAdapter.resetSelection();
    }

    public interface FilterListener {
        void onFilterApplied(Set<String> brands, Set<String> colors, Set<String> styles);
    }
}
