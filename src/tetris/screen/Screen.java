package tetris.screen;

import tetris.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public abstract class Screen extends JPanel {

    private final Color background;
    private final int maxAlpha;
    private final int delay;
    private final int delta;

    private int alpha;

    private boolean isFading = false;

    public boolean isFading() {
        return isFading;
    }

    public Screen(Color background, int maxAlpha) {
        this(background, maxAlpha, 15, 7);
    }

    public Screen(Color background, int maxAlpha, int delay, int delta) {
        this.background = background;
        this.maxAlpha = maxAlpha;
        this.delay = delay;
        this.delta = delta;
        setBackground(background);
    }


    public CompletableFuture<Void> fadeIn() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        isFading = true;

        setVisible(true);

        alpha = 0;

        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {


            @Override
            public void run() {
                if (alpha + delta >= maxAlpha) {
                    future.complete(null);
                    timer.cancel();
                    isFading = false;
                    alpha = maxAlpha;
                    MainFrame.getInstance().repaint();
                    return;
                }

                setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), alpha));


                alpha += delta;
                MainFrame.getInstance().repaint();
            }
        }, 1, delay);


        return future;
    }


    public CompletableFuture<Void> fadeOut() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        isFading = true;


        java.util.Timer timer = new Timer();

        alpha = maxAlpha;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (alpha - delta <= 1) {
                    future.complete(null);
                    timer.cancel();
                    isFading = false;
                    setVisible(false);
                    alpha = 1;
                    MainFrame.getInstance().repaint();
                    return;
                }

                setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), alpha));


                alpha -= delta;
                MainFrame.getInstance().repaint();
            }
        }, 1, delay);


        return future;
    }

    public abstract void onResize();

}
