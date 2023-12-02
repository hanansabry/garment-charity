package com.app.garmentcharity.presentation.organization;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.garmentcharity.data.models.RequestItem;
import com.app.garmentcharity.databinding.ItemRequestDetailsLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizationRequestItemsAdapter extends RecyclerView.Adapter<OrganizationRequestItemsAdapter.OrganizationRequestItemViewHolder> {

    private final List<RequestItem> requestItemList;

    public OrganizationRequestItemsAdapter(List<RequestItem> requestItemList) {
        this.requestItemList = requestItemList;
    }

    @NonNull
    @Override
    public OrganizationRequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestDetailsLayoutBinding binding = ItemRequestDetailsLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new OrganizationRequestItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationRequestItemViewHolder holder, int position) {
        RequestItem requestItem = requestItemList.get(position);
        holder.bind(requestItem);
    }

    @Override
    public int getItemCount() {
        return requestItemList.size();
    }

    static class OrganizationRequestItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemRequestDetailsLayoutBinding binding;

        public OrganizationRequestItemViewHolder(@NonNull ItemRequestDetailsLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(RequestItem requestItem) {
            binding.setRequestItem(requestItem);
            binding.executePendingBindings();
        }
    }
}
