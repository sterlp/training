package org.sterl.training.android.curvedarrow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class CurvedArrowView extends View {

    /** The factor we curve the line based on the length */
    private float curveFactor = 8.0f;
    /** The size of the error head of the line */
    private float arrowHeadSize = 50;

    private int arrowTo = 0; // 0 == top see attrs.xml CurvedArrowView

    private final Paint paint = new Paint();
    private final Path arrowHeadPath = new Path();
    private final Path linePath = new Path();


    public CurvedArrowView(Context context) {
        super(context);
        init(context, null);
    }

    public CurvedArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurvedArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(convertDpToPixel(3f));
        paint.setStyle(Paint.Style.STROKE);
        arrowHeadSize = convertDpToPixel(12.5f);

        // https://developer.android.com/training/custom-views/create-view.html
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CurvedArrowView, 0, 0);

            try {
                paint.setColor(a.getColor(R.styleable.CurvedArrowView_arrowColor, Color.BLACK));
                arrowTo = a.getInt(R.styleable.CurvedArrowView_arrowTo, 0);
            } finally {
                a.recycle();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(arrowHeadPath, paint);
        canvas.drawPath(linePath, paint);

    }

    /*
     *       +
     *
     * + P2       + P1
     */
    private final PointF arrowHead = new PointF();
    private final PointF arrowP1 = new PointF();
    private final PointF arrowP2 = new PointF();

    private final PointF lineStart = new PointF();
    private final PointF lineMid = new PointF();
    private final PointF lineEnd = new PointF();

    private final Matrix rotate = new Matrix();

    // calculate the path only on a layout change ...
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed && getHeight() > arrowHeadSize && getWidth() > arrowHeadSize) {
            linePath.reset();
            arrowHeadPath.reset();

            final int length = getHeight();
            float curveX = 0;
            float curveY = 0;

            final float halfX = getWidth() / 2.0f;
            final float halfY = getHeight() / 2.0f;
            final float halfArrow = arrowHeadSize / 2.0f;
            final float factor = (float)getWidth() / (float)getHeight();

            float degrees = 0;

            switch (arrowTo) {
                case 0: // arrow to TOP
                    arrowHead.set(halfX, halfArrow / 2);
                    arrowP1.set(arrowHead.x + halfArrow, arrowHead.y + arrowHeadSize);
                    arrowP2.set(arrowP1.x - arrowHeadSize, arrowP1.y);

                    lineStart.set(halfX, getHeight());
                    lineMid.set(halfX, halfY);

                    curveX = -length / curveFactor;
                    curveY = 0;

                    degrees = 15;

                    break;
                case 1: // TOP left
                    arrowHead.set(arrowHeadSize, halfArrow / 2);
                    arrowP1.set(arrowHead.x + halfArrow, arrowHead.y + arrowHeadSize);
                    arrowP2.set(arrowP1.x - arrowHeadSize, arrowP1.y);

                    lineStart.set(halfX, getHeight());
                    lineMid.set(lineStart.x / 2, lineStart.y / 2);


                    curveX = -length / curveFactor;
                    curveY = -curveX;

                    if (factor >= 2) {
                        degrees = -30;
                    } else if (factor >= 1) {
                        degrees = -15;
                    } else if (getHeight() > getWidth()) {
                        degrees = 10;
                    }

                    break;
                case 2: // TOP right
                    //TODO
                    break;
                case 3: // bottom
                    // TODO
                    break;
                case 4: // bottom left
                    arrowHead.set(arrowHeadSize, getHeight() - halfArrow);
                    arrowP1.set(arrowHead.x - halfArrow, arrowHead.y - arrowHeadSize);
                    arrowP2.set(arrowP1.x + arrowHeadSize, arrowP1.y);

                    lineStart.set(halfX, 0);
                    lineMid.set(halfX / 2, arrowHead.y / 2);


                    curveX = -length / curveFactor;
                    curveY = curveX;

                    if (factor >= 2) {
                        degrees = 25;
                    } else if (factor >= 1) {
                        degrees = 10;
                    } else if (getHeight() > getWidth()) {
                        degrees = -5;
                    }
                    //degrees = 0;
                    break;
                case 5: // bottom right
                    break;
            }
            lineEnd.set( (arrowP2.x + arrowP1.x) / 2 , (arrowP2.y + arrowP1.y) / 2);

            linePath.moveTo(lineStart.x, lineStart.y);
            linePath.quadTo(Math.max(lineMid.x + curveX, 0), Math.max(lineMid.y + curveY, 0), lineEnd.x, lineEnd.y);


            arrowHeadPath.moveTo(arrowHead.x, arrowHead.y);
            arrowHeadPath.lineTo(arrowP1.x, arrowP1.y);
            arrowHeadPath.lineTo(arrowP2.x, arrowP2.y);
            arrowHeadPath.close();


            Log.d("Rotate", "Degrees: " + degrees + " factor: " + (float)getWidth() / (float)getHeight());
            if (degrees != 0) {
                rotate.reset();
                rotate.postRotate(degrees, lineEnd.x, lineEnd.y);
                arrowHeadPath.transform(rotate);
            }

        } else if (getHeight() < arrowHeadSize || getWidth() < arrowHeadSize) {
            linePath.reset();
            arrowHeadPath.reset();
        }
    }

    private float convertDpToPixel(float dp){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}