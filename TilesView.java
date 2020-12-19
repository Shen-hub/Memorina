package com.example.memorina;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class TilesView extends View {

    final int PAUSE_LENGTH = 2;
    boolean isOnPauseNow = false;

    int openedCard = 0;

    ArrayList<ArrayList<Card>> cards = new ArrayList<>();
    ArrayList<Card> openedCardList = new ArrayList<>();

    Integer width, height;
    Context context;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        invalidate();
        newGame();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        for (Integer i = 0; i < cards.size(); i++){
            for (Integer j = 0; j < cards.get(i).size(); j++){
                cards.get(i).get(j).draw(canvas, j * width / 4, i * height / 4,  width / 4,height / 4);
            }
        }

        if(checkWin()) {
            Toast.makeText(context, "Вы победили!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {

            for (int i = 0; i < cards.size(); i++){
                for (int j = 0; j < cards.get(i).size(); j++){
                    Card c = cards.get(i).get(j);
                    if (openedCard == 0 && !c.isDeleted) {
                        if (c.flip(x, y)) {
                            openedCard ++;
                            openedCardList.add(c);
                            invalidate();
                            return true;
                        }
                    }

                    if (openedCard == 1 && c != openedCardList.get(0) && !c.isDeleted) {

                        if (c.flip(x, y)) {
                            openedCard ++;
                            openedCardList.add(c);
                            if (openedCardList.get(0).color == openedCardList.get(1).color) {
                                openedCardList.get(0).isDeleted = true;
                                openedCardList.get(1).isDeleted = true;
                            }
                            else {
                                PauseTask task = new PauseTask();
                                task.execute(PAUSE_LENGTH);
                                isOnPauseNow = true;
                            }

                            openedCardList.clear();
                            openedCard = 0;

                            invalidate();
                            return true;
                        }
                    }

                }
            }
        }

        return true;
    }

    private boolean checkWin() {
        for (int i = 0; i < cards.size(); i++) {
            for(int j = 0; j < cards.get(i).size(); j++) {
                if (!cards.get(i).get(j).isDeleted) {
                    return false;
                }
            }
        }
        return true;
    }

    public void newGame() {
        int[] colors = {Color.parseColor("#FFFF5600"), Color.parseColor("#FFFF7F00"), Color.parseColor("#FF04819E"), Color.parseColor("#FF00AA72")};
        int n = 4;

        Random random = new Random();
        Integer randomColor = colors[0];
        LinkedList<Integer> randomColors = new LinkedList<>();

        for (int i = 0; i < n * n; i++) {
            if (i % 2 == 0) {
                Integer pos = random.nextInt(colors.length);
                randomColor = colors[pos];
            }
            randomColors.add(randomColor);
        }
        Collections.shuffle(randomColors);

        for (Integer i = 0; i < n; i++) {
            cards.add(new ArrayList<Card>());
            for (Integer j = 0 ; j < n; j++) {
                Card card = new Card(randomColors.getFirst());
                randomColors.removeFirst();
                cards.get(i).add(card);
            }
        }
        invalidate();
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                Thread.sleep(integers[0] * 1000);
            } catch (InterruptedException e) {}
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < cards.size(); i++) {
                for (int j = 0; j < cards.get(i).size(); j++) {
                    Card c = cards.get(i).get(j);
                    if (c.isOpen) {
                        c.isOpen = false;
                    }
                }
            }
            openedCard = 0;
            isOnPauseNow = false;
            invalidate();
        }
    }
}
