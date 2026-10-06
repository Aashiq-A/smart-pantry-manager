package com.abrahams.smartpantrymanager.adapters;

import com.abrahams.smartpantrymanager.models.PantryItem;

public class PantryAdapter {

    public interface OnItemActionListener {

       void onItemClick(PantryItem item);

       void onDeleteClick(PantryItem item);
    }
}

