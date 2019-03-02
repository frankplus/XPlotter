package com.technologies.francesco.xplotter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import formulaCalc.Calculator;


/**
 * TODO: document your custom view class.
 */
public class CanvasView extends View {
    private String formula;
    private double xMagnific;
    private double yMagnific;
    private double magnificFactor = 1.04;
    private Paint axesPaint;
    private Paint graphPaint;
    private Paint gridPaint;
    //private Path path;
    private ScaleGestureDetector scaleGestureDetector;
    private Calculator calc;

    public void setFormula(String formula){this.formula=formula;}
    public void setMagnification(int xMagn, int yMagn){xMagnific=xMagn;yMagnific=yMagn;}
    public void xZoomIn(){xMagnific*=magnificFactor;}
    public void xZoomOut(){xMagnific/=magnificFactor;}
    public void yZoomIn(){yMagnific*=magnificFactor;}
    public void yZoomOut(){yMagnific/=magnificFactor;}

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float dpToPixel(float dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public void init(Context context){
        xMagnific=10;
        yMagnific=10;
        axesPaint = new Paint();
        //axesPaint.setTextSize(dpToPixel(20));
        axesPaint.setColor(Color.WHITE);
        axesPaint.setStrokeWidth(3);

        graphPaint = new Paint();
        graphPaint.setStyle(Paint.Style.STROKE);
        graphPaint.setColor(Color.WHITE);
        graphPaint.setStrokeWidth(3);

        gridPaint = new Paint();
        //gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setColor(Color.GRAY);
        //DashPathEffect dashEff = new DashPathEffect(new float[]{10,10},0);
        //gridPaint.setPathEffect(dashEff);
        //path = new Path();

        formula="sin(x)";

        calc = new Calculator(formula);
        scaleGestureDetector = new ScaleGestureDetector(context,new ScaleListener());
    }

    public CanvasView(Context context) {
        super(context);
        init(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = (float)(getWidth() - getPaddingLeft() - getPaddingRight());
        float h = (float)(getHeight() - getPaddingTop() - getPaddingBottom());

        //draw axes
        canvas.drawLine(0, h / 2, w, h / 2, axesPaint);//horizontal
        canvas.drawLine(w / 2, 0, w / 2, h, axesPaint);//vertical

        //draw grid and numbers
        int nTicks = 8;
        float xTick = w/nTicks;
        float yTick = h/nTicks;
        axesPaint.setTextSize(xTick * 0.4F);
        float tickSize=dpToPixel(3);
        int x=0,y=0;
        for(int i=1; i<nTicks; i++){
            x+=xTick;
            y+=yTick;

            if((nTicks%2==0 && i!=nTicks/2) || nTicks%2==1){
                //vertical lines
//                path.moveTo(x,0);
//                path.lineTo(x,h);
//                canvas.drawPath(path,gridPaint);
                canvas.drawLine(x,0,x,h,gridPaint);
                String s = String.format("%.2g", (double) (x - w / 2) / xMagnific);
                axesPaint.setTextSize(20*tickSize/s.length());
                canvas.drawText(s, x, h/2-tickSize, axesPaint);

                //horizontal lines
//                path.moveTo(0,y);
//                path.lineTo(w,y);
//                canvas.drawPath(path,gridPaint);
                canvas.drawLine(0,y,w,y,gridPaint);
                s = String.format("%.2g", (double) -(y - h / 2) / yMagnific);
                axesPaint.setTextSize(50);
                canvas.drawText(s , w/2+tickSize, y, axesPaint);
            }
        }

        if(formula!=null){
            try{
                //plot the function
                calc.init(formula);
                float prex=0,prey=0;
                boolean startDraw=true;
                for(int i=0; i<w; i+=3){
                    double xOnGraph = (double)(i-w/2)/xMagnific;
                    float ypoint = Math.round(-(calc.calculation(xOnGraph))*yMagnific + (double)h/2);
                    if(ypoint>0 && ypoint<h){
                        if(!startDraw)
                            canvas.drawLine(prex,prey,(float)i,ypoint,graphPaint);
                        else
                            startDraw=false;
                        prex=i;
                        prey=ypoint;
                    }
                    else{
                        startDraw=true;
                    }
                }
            }catch(RuntimeException e){
//                Window parentWindow = SwingUtilities.windowForComponent(this);
//                Frame parentFrame = null;
//                if (parentWindow instanceof Frame)
//                    parentFrame = (Frame)parentWindow;
//                JOptionPane.showMessageDialog(parentFrame, e.getMessage());
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        //let scaleGestureDetector inspect all events
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detect) {
            xMagnific *= detect.getCurrentSpanX()/detect.getPreviousSpanX();
            yMagnific *= detect.getCurrentSpanY()/detect.getPreviousSpanY();
            invalidate();
            return true;
        }
    }
}
