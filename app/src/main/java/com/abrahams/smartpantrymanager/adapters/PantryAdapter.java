package com.abrahams.smartpantrymanager.adapters;


import com.abrahams.smartpantrymanager.util.PantryPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.abrahams.smartpantrymanager.models.PantryItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemActionListener {

        void onItemClick(PantryItem item);

        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> items;
    private final OnItemActionListener listener;

    public PantryAdapter(
            List<PantryItem> items,
            OnItemActionListener listener) {

        this.items = new ArrayList<>(items);
        this.listener = listener;
    }

    @Override
    public PantryViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            PantryViewHolder holder, int position) {

        PantryItem item = items.get(position);

        holder.textName.setText(item.getName());

        String quantity = BigDecimal.valueOf(item.getQuantity())
                .stripTrailingZeros()
                .toPlainString();

        holder.textQuantity.setText(quantity + " " + item.getUnit());

        String expiry = item.getExpiryDate();

        boolean showExpiry = PantryPreferences.shouldShowExpiryDates(
                holder.itemView.getContext());

        if (!showExpiry || expiry == null || expiry.trim().isEmpty()) {
            holder.textExpiry.setVisibility(View.GONE);
        } else {
            holder.textExpiry.setVisibility(View.VISIBLE);
            holder.textExpiry.setText("Expires: " + expiry);
        }

        holder.itemView.setOnClickListener(
                view -> listener.onItemClick(item));

        holder.buttonDelete.setOnClickListener(
                view -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    // Existing row holder stays below.
    static class PantryViewHolder extends RecyclerView.ViewHolder {

        final TextView textName;
        final TextView textQuantity;
        final TextView textExpiry;
        final ImageButton buttonDelete;

        PantryViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textItemName);
            textQuantity = itemView.findViewById(R.id.textItemQuantity);
            textExpiry = itemView.findViewById(R.id.textItemExpiry);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteItem);
        }
    }
}



