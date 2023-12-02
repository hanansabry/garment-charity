package com.app.garmentcharity.presentation.regions;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.garmentcharity.R;
import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.databinding.RegionItemLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RegionsAdapter extends RecyclerView.Adapter<RegionsAdapter.RegionViewHolder> {

    private final List<Region> regionList;
    private SparseBooleanArray selectedItems; // to store selected items


    public RegionsAdapter(List<Region> regionList) {
        this.regionList = regionList;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RegionItemLayoutBinding binding = RegionItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RegionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = regionList.get(position);
        holder.bind(region);

        holder.itemView.setActivated(selectedItems.get(position, false));
        holder.itemView.setOnClickListener(view -> {
            // Toggle item selection
            toggleItemSelection(position);
        });
    }

    private void toggleItemSelection(int position) {
        // Toggle the selection state of the item
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        // Notify item change to refresh the view
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(SparseBooleanArray selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Region getItem(int position) {
        return regionList.get(position);
    }

    class RegionViewHolder extends RecyclerView.ViewHolder {

        private final RegionItemLayoutBinding binding;

        public RegionViewHolder(@NonNull RegionItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Region region) {
            binding.setRegion(region);
            // Change background color based on selection state
            if (selectedItems.get(getAdapterPosition(), false)) {
                // Item is selected, change background color
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.transparent_gray));
            } else {
                // Item is not selected, set default background color
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }
        }
    }


}
