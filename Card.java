package com.example.memorina;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Card {
    Paint p = new Paint();

    public Card( int color) {
        this.color = color;
    }

    int color, backColor = Color.LTGRAY;
    boolean isOpen = false;
    Boolean isDeleted = false;
    float x, y, width, height;
    public void draw(Canvas c, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        p.setStrokeWidth(4);
        p.setStyle(Paint.Style.FILL);
        if (isOpen) {
            p.setColor(color);
        } else p.setColor(backColor);
        if (!isDeleted) {
            Rect r = new Rect((int)x, (int)y, (int)(x + width), (int)(y + height));
            c.drawRect(r, p);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.WHITE);
            c.drawRect(r, p);
        }
    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        }
        else return false;
    }
}
