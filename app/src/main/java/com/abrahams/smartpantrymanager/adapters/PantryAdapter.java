package com.abrahams.smartpantrymanager.adapters;

// new imports.
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.abrahams.smartpantrymanager.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter {

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
    // class create for row holder.
    static class PantryViewHolder extends RecyclerView.ViewHolder {

        final TextView textName;
        final TextView textQuantity;
        final TextView textExpiry;
        final ImageButton buttonDelete;

        PantryViewHolder(View itemView) {
            super(itemView);
        }
}

