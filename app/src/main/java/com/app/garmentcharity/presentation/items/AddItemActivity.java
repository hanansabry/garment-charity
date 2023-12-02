package com.app.garmentcharity.presentation.items;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.garmentcharity.R;
import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.databinding.ActivityAddItemBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.categories.CategoriesViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;

public class AddItemActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ItemsViewModel itemsViewModel;
    private ActivityAddItemBinding binding;
    private Category selectedCategory;

    @Override
    public View getDataBindingView() {
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoriesViewModel categoriesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(CategoriesViewModel.class);
        itemsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(ItemsViewModel.class);

        categoriesViewModel.retrieveCategories(sessionManager.getFirebaseId());
        categoriesViewModel.getCategoriesLiveData().observe(this, this::initiateCategoriesSpinner);

        itemsViewModel.getAddItemStateLiveData().observe(this, success -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
            if (success) {
                Toast.makeText(this, "Item is added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Can't add item, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        itemsViewModel.getErrorState().observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        categoriesViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    private void initiateCategoriesSpinner(List<Category> categories) {
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        categoriesAdapter.add(getString(R.string.select_category));
        for (Category category : categories) {
            categoriesAdapter.add(category.getName());
        }
        binding.categoriesSpinner.setAdapter(categoriesAdapter);
        binding.categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategory = categories.get(position - 1);
                    binding.categoryEditText.setText(selectedCategory.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onSelectCategoryClicked(View view) {
        binding.categoriesSpinner.performClick();
    }

    public void onSaveClicked(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.GONE);

        String itemName = binding.itemEditText.getText().toString();
        double points = binding.pointsEditText.getText().toString().isEmpty() ? 0 :
                Double.parseDouble(binding.pointsEditText.getText().toString());
        String selectedSize = null;
        int selectedSizeId = binding.sizeRadioGroup.getCheckedRadioButtonId();
        if (selectedSizeId == R.id.large_radio) {
            selectedSize = Item.ItemSize.Large.name();
        } else if (selectedSizeId == R.id.medium_radio) {
            selectedSize = Item.ItemSize.Medium.name();
        } else if (selectedSizeId == R.id.small_radio) {
            selectedSize = Item.ItemSize.Small.name();
        }

        if (itemName.isEmpty() || points == 0 || selectedSize == null || selectedCategory == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
        } else {
            Item item = new Item();
            item.setName(itemName);
            item.setCategory(selectedCategory.getName());
            item.setPoints(points);
            item.setSize(selectedSize);

            itemsViewModel.addItem(sessionManager.getFirebaseId(), selectedCategory.getId(), item);
        }

    }

}