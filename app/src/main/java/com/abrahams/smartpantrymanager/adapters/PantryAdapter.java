package com.abrahams.smartpantrymanager.adapters;

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
}

