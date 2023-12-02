package com.app.garmentcharity.presentation.organization;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.garmentcharity.R;
import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.databinding.OrganizationMainItemLayoutBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizationMainItemsAdapter extends RecyclerView.Adapter<OrganizationMainItemsAdapter.MainItemHolder> {

    private final List<Region> regions;
    private final List<Category> categories;
    private final List<Item> items;
    private final String className;
    private int selectedItem = -1;

    public OrganizationMainItemsAdapter(@Nullable List<Region> regions,
                                        @Nullable List<Category> categories,
                                        @Nullable List<Item> items,
                                        String className) {
        this.regions = regions;
        this.categories = categories;
        this.items = items;
        this.className = className;
    }

    @NonNull
    @Override
    public MainItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrganizationMainItemLayoutBinding binding = OrganizationMainItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MainItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainItemHolder holder, int position) {
        if (className.equals(Region.class.getName())) {
            Region region = regions.get(position);
            holder.bindRegion(region);
        } else if (className.equals(Category.class.getName())) {
            Category category = categories.get(position);
            holder.bindCategory(category, position);
        } else if (className.equals(Item.class.getName())) {
            Item item = items.get(position);
            holder.bindItem(item);
        }
    }

    @Override
    public int getItemCount() {
        if (regions != null) {
            return regions.size();
        } else if (categories != null) {
            return categories.size();
        } else if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public void addRegions(ArrayList<Region> selectedRegions) {
        if (regions != null) {
            int newRegionsStartPosition = regions.size();
            regions.addAll(selectedRegions);
            notifyItemRangeInserted(newRegionsStartPosition, selectedRegions.size());
//            notifyDataSetChanged();
        }
    }

    public void addCategory(Category category) {
        if (categories != null) {
            int insertPosition = categories.size();
            categories.add(category);
            notifyItemInserted(insertPosition);
        }
    }

    class MainItemHolder extends RecyclerView.ViewHolder {

        private final OrganizationMainItemLayoutBinding binding;

        public MainItemHolder(@NonNull OrganizationMainItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindRegion(Region region) {
            binding.setName(region.getName());
            binding.imageView.setPadding(64, 64, 64, 64);
            binding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        private void bindCategory(Category category, int position) {
            itemView.setSelected(selectedItem == position);
            if (itemView.isSelected()) {
                binding.parent.setBackground(AppCompatResources.getDrawable(itemView.getContext(),R.drawable.gray_dashed_stroke_cornered));
                binding.parent.setPadding(16,16,16,16);
            } else {
                binding.parent.setBackground(null);
                binding.parent.setPadding(0,0,0,0);
            }
            binding.setName(category.getName());
            if (category.getImage() != null) {
                Glide.with(itemView.getContext())
                        .load(category.getImage())
                        .placeholder(R.drawable.place_holder)
                        .into(binding.imageView);
            }
        }

        private void bindItem(Item item) {
            binding.setName(item.getName());
            binding.imageView.setPadding(32, 32, 32, 32);
            binding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(itemView.getContext())
                    .load(R.drawable.item2)
                    .placeholder(R.drawable.place_holder)
                    .into(binding.imageView);
        }
    }

    public interface CategoriesClickListener {
        void onCategoryClicked(Category category);
    }
}
