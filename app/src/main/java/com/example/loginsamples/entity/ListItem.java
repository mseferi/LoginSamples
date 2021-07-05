package com.example.loginsamples.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ListItem implements Serializable {
    @PrimaryKey
    @NonNull
    private String itemText;
    private boolean isPrintable;
    private boolean isBold;
    boolean isLined;
    private int fontSize;
    private int position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem item = (ListItem) o;
        return isPrintable == item.isPrintable &&
                isBold == item.isBold &&
                isLined == item.isLined &&
                fontSize == item.fontSize &&
                position == item.position &&
                itemText.equals(item.itemText);
    }

    @Override
    public int hashCode() {
        return itemText.hashCode();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private static int positionDelegate;

    public static ListItem getGenereicItem() {
        return new ListItem("", true, false, false, 20);
    }

    public ListItem(String itemText, boolean isPrintable, boolean isBold, boolean isLined, int fontSize) {
        this.itemText = itemText;
        this.isPrintable = isPrintable;
        this.isBold = isBold;
        this.isLined = isLined;
        this.fontSize = fontSize;
        position = positionDelegate;
        positionDelegate++;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public boolean isPrintable() {
        return isPrintable;
    }

    public void setPrintable(boolean printable) {
        isPrintable = printable;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isLined() {
        return isLined;
    }

    public void setLined(boolean lined) {
        isLined = lined;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }


}
