package com.rn5.lists.enums;

import com.google.gson.annotations.SerializedName;
import com.rn5.lists.R;

import lombok.Getter;

@Getter
public enum GroupColor {

    @SerializedName("RED")
    RED(0, R.color.red, R.color.bk_red),
    @SerializedName("ORANGE")
    ORANGE(1, R.color.orange, R.color.bk_orange),
    @SerializedName("YELLOW")
    YELLOW(2, R.color.yellow, R.color.bk_yellow),
    @SerializedName("GREEN")
    GREEN(3, R.color.green, R.color.bk_green),
    @SerializedName("BLUE")
    BLUE(4, R.color.blue, R.color.bk_blue),
    @SerializedName("PURPLE")
    PURPLE(5, R.color.purple, R.color.bk_purple),
    @SerializedName("GRAY")
    GRAY(7, R.color.gray, R.color.off_white);

    private final int intValue;
    private final int color;
    private final int groupColor;

    GroupColor(int intValue, int color, int groupColor) {
        this.intValue = intValue;
        this.color = color;
        this.groupColor = groupColor;
    }

    public static GroupColor getFromIntValue(int intValue) {
        for (GroupColor c : values()) {
            if (c.getIntValue() == intValue)
                return c;
        }
        return GRAY;
    }
}
