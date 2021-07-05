package com.example.loginsamples.adapter;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.loginsamples.entity.ListItem;

import java.util.List;

public class ListItemEditTextListener implements TextWatcher {
    private int position;
    private final TrackedViewType type;
    private final List<ListItem> items;

    public ListItemEditTextListener(TrackedViewType type, List<ListItem> items) {
        this.type = type;
        this.items = items;
    }

    public void updatePosition(int position) {
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (type == TrackedViewType.ITEM_TEXT) {
            items.get(position).setItemText(charSequence.toString());
        } else if (type == TrackedViewType.FONT_SIZE) {
            try {
                items.get(position).setFontSize(Integer.parseInt(charSequence.toString()));
            } catch (Exception ignore) {

            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
