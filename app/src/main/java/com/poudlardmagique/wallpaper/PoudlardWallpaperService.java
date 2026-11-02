package com.poudlardmagique.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

public class PoudlardWallpaperService
        extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new PoudlardEngine();
    }

    private class PoudlardEngine extends Engine {

        private final Handler handler =
                new Handler(Looper.getMainLooper());

        private final Paint paint = new Paint();
        private boolean visible = false;

        private final Runnable animation = new Runnable() {
            @Override
            public void run() {
                dessiner();
            }
        };

        private void dessiner() {
            handler.removeCallbacks(animation);

            if (!visible) return;

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();

                if (canvas != null) {
                    canvas.drawColor(
                            Color.rgb(5, 18, 35));

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(45);
                    paint.setTextAlign(
                            Paint.Align.CENTER);

                    canvas.drawText(
                            "Poudlard Magique",
                            canvas.getWidth() / 2f,
                            canvas.getHeight() / 2f,
                            paint);
                }
            } catch (Exception e) {
                android.util.Log.e(
                        "PoudlardMagique",
                        "Erreur de dessin", e);
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.postDelayed(animation, 33);
        }

        @Override
        public void onVisibilityChanged(
                boolean estVisible) {
            visible = estVisible;

            if (visible) {
                dessiner();
            } else {
                handler.removeCallbacks(animation);
            }
        }

        @Override
        public void onSurfaceDestroyed(
                SurfaceHolder holder) {
            visible = false;
            handler.removeCallbacks(animation);
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onDestroy() {
            visible = false;
            handler.removeCallbacks(animation);
            super.onDestroy();
        }
    }
}
