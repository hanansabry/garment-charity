package com.app.garmentcharity.presentation.requesets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.databinding.RequestDetailsItemLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private final List<Request> requestList;
    private final RequestsCallback requestsCallback;

    public RequestsAdapter(List<Request> requestList, RequestsCallback requestsCallback) {
        this.requestList = requestList;
        this.requestsCallback = requestsCallback;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestDetailsItemLayoutBinding binding = RequestDetailsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.bind(request, requestsCallback);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {

        private final RequestDetailsItemLayoutBinding binding;

        public RequestViewHolder(@NonNull RequestDetailsItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Request request, RequestsCallback requestsCallback) {
            binding.setRequest(request);
            binding.setCallback(requestsCallback);
            binding.executePendingBindings();
        }
    }

    public interface RequestsCallback {
        void onRequestClick(Request request);
    }
}
