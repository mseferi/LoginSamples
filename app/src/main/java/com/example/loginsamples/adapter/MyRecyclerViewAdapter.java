package com.example.loginsamples.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsamples.R;
import com.example.loginsamples.entity.ListItem;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ListItem> items;
    private final LayoutInflater mInflater;
    public RecyclerView rvData;
    private RvAdapterCallbacks callbacks;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, RvAdapterCallbacks callbacks) {
        this.mInflater = LayoutInflater.from(context);
        this.callbacks = callbacks;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_row, parent, false);
        return new ViewHolder(view, new ListItemEditTextListener(TrackedViewType.ITEM_TEXT, items),
                new ListItemEditTextListener(TrackedViewType.FONT_SIZE, items),
                new ListItemCheckBoxChangeListener(TrackedViewType.IS_BOLD, items),
                new ListItemCheckBoxChangeListener(TrackedViewType.IS_LINED, items));

    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.etItemTextTextListener.updatePosition(holder.getAdapterPosition());
        holder.etFontSizeEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.isBoldChangedListener.updatePosition(holder.getAdapterPosition());
        holder.isLinedChangedListener.updatePosition(holder.getAdapterPosition());
        holder.etItemText.setText(item.getItemText());
        holder.etFontSize.setText(String.valueOf(item.getFontSize()));
        holder.cbIsPrintable.setChecked(item.isPrintable());
        holder.cbIsBold.setChecked(item.isBold());
        holder.cbIsLined.setChecked(item.isLined());

        holder.ibDelete.setOnClickListener(v -> {
            callbacks.onDeleteSelected(item);
            items.remove(item);
        });


        if (holder.cbIsBold.isChecked()) {
            holder.etItemText.setTypeface(holder.etItemText.getTypeface(), Typeface.BOLD);
        } else {
            holder.etItemText.setTypeface(null, Typeface.NORMAL);
        }


        holder.cbIsBold.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (holder.cbIsBold.isChecked()) {
                holder.etItemText.setTypeface(holder.etItemText.getTypeface(), Typeface.BOLD);

            } else {
                holder.etItemText.setTypeface(null, Typeface.NORMAL);
            }
            if (!rvData.isComputingLayout()) {
                items.get(position).setBold(holder.cbIsBold.isChecked());
                notifyDataSetChanged();
            }

        });


        holder.cbIsPrintable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!rvData.isComputingLayout()) {
                items.get(position).setPrintable(holder.cbIsPrintable.isChecked());
                notifyDataSetChanged();
            }
        });


        holder.cbIsLined.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!rvData.isComputingLayout()) {
                items.get(position).setLined(holder.cbIsLined.isChecked());
                notifyDataSetChanged();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etItemText;
        EditText etFontSize;
        CheckBox cbIsPrintable;
        CheckBox cbIsBold;
        CheckBox cbIsLined;
        ImageButton ibDelete;
        ListItemEditTextListener etItemTextTextListener;
        ListItemEditTextListener etFontSizeEditTextListener;
        ListItemCheckBoxChangeListener isBoldChangedListener;
        ListItemCheckBoxChangeListener isLinedChangedListener;


        ViewHolder(View itemView, ListItemEditTextListener etItemTextTextListener, ListItemEditTextListener etFontSizeEditTextListener, ListItemCheckBoxChangeListener isBoldChangedListener, ListItemCheckBoxChangeListener isLinedChangedListener) {
            super(itemView);
            etItemText = itemView.findViewById(R.id.etItemText);
            etFontSize = itemView.findViewById(R.id.etFontSize);
            cbIsPrintable = itemView.findViewById(R.id.cbIsPrintable);
            cbIsBold = itemView.findViewById(R.id.cbIsBold);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            cbIsLined = itemView.findViewById(R.id.cbIsLined);


            this.etItemTextTextListener = etItemTextTextListener;
            etItemText.addTextChangedListener(this.etItemTextTextListener);

            this.etFontSizeEditTextListener = etFontSizeEditTextListener;
            etFontSize.addTextChangedListener(this.etFontSizeEditTextListener);

            this.isBoldChangedListener = isBoldChangedListener;
            cbIsBold.setOnCheckedChangeListener(this.isBoldChangedListener);

            this.isLinedChangedListener = isLinedChangedListener;
            cbIsLined.setOnCheckedChangeListener(this.isBoldChangedListener);

        }
    }
}

