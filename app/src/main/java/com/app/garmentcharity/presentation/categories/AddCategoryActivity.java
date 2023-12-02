package com.app.garmentcharity.presentation.categories;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.databinding.ActivityAddCategoryBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;

import javax.inject.Inject;

public class AddCategoryActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 1;
    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityAddCategoryBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri selectedImageUri;

    @Override
    public View getDataBindingView() {
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPickImageResultLauncher();
        categoriesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(CategoriesViewModel.class);
        categoriesViewModel.getAddCategoryStateLiveData().observe(this, success -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
            if (success != null) {
                Toast.makeText(this, "Category is added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Can't add category, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        categoriesViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
        });
    }

    private void registerPickImageResultLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    binding.categoryImage.setImageURI(selectedImageUri);
                                } catch (Exception e) {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }

    public void onSelectImageClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                    , Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        } else {
            launchImagePicker();
        }
    }

    private void launchImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(photoPickerIntent);
    }

    public void onSaveClicked(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.GONE);
        String categoryName = binding.categoryEditText.getText().toString();
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "You must add category name", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
        } else {
            Category category = new Category();
            category.setName(categoryName);
            if (selectedImageUri != null) {
                category.setImage(selectedImageUri.toString());
            }
            categoriesViewModel.addCategory(sessionManager.getFirebaseId(), category);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            }
        }
    }
}