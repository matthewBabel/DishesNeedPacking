package garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import garlicquasar.babel.matt.dishesneedpacking.R;

public class BitmapHolder {
    public Bitmap rotateImg;
    public Bitmap nextImg;
    public Bitmap spoonImg;
    public Bitmap knifeImg;
    public Bitmap forkImg;
    public Bitmap cupImg;
    public Bitmap bowlImg;
    public Bitmap backImg;

    public BitmapHolder(Context context) {
        rotateImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate);
        nextImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.right_arrow);
        spoonImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.spoon);
        knifeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.knife);
        forkImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.fork);
        cupImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cup);
        bowlImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.mixingbowl);
        backImg = getBitmapFromVectorDrawable(context, R.drawable.ic_arrow_back_black_24dp);

    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
