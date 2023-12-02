package com.app.garmentcharity.presentation.requesets;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.databinding.ItemRequestLayoutBinding;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestItemsAdapter extends RecyclerView.Adapter<RequestItemsAdapter.RequestItemViewHolder> {

    private final List<Item> itemList;
    private final HashMap<Integer, String> quantitiesEditTextValues = new HashMap<>();


    public RequestItemsAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestLayoutBinding binding = ItemRequestLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RequestItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item, quantitiesEditTextValues, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public HashMap<Integer, String> getQuantitiesEditTextValues() {
        return quantitiesEditTextValues;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    static class RequestItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemRequestLayoutBinding binding;

        public RequestItemViewHolder(@NonNull ItemRequestLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Item item, HashMap<Integer, String> quantitiesEditTextValues, int position) {
            // Check if there's a saved value for this position
            // Restore the previously entered value in the EditText
            // Clear the EditText if there's no saved value
            binding.quantityEdittext.setText(quantitiesEditTextValues.getOrDefault(position, ""));
            binding.quantityEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    quantitiesEditTextValues.put(position, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }
}
