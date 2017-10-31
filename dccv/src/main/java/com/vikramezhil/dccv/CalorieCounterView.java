package com.vikramezhil.dccv;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Droid Calorie Counter View
 *
 * @author Vikram Ezhil
 */

public class CalorieCounterView extends View
{
    private Properties properties = new Properties();
    private RectF rectF;
    private PointF point;
    private Paint fillBackgroundPaint, backgroundPaint, foregroundPaint, dangerBackgroundPaint, dangerForegroundPaint;
    private Paint textHeaderPaint, textHeaderSubPaint, textFooterPaint, textFooterSubPaint;
    private ArrayList<Paint> textPaint = new ArrayList<>();
    private OnCalorieCounterListener onCalorieCounterListener;

    // MARK: View Constructors

    public CalorieCounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public CalorieCounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    // MARK: View Listeners

    /**
     * Sets the calorie counter listener
     *
     * @param onCalorieCounterListener The instance of the class which implements the listener
     */
    public void setOnCalorieCounterListener(OnCalorieCounterListener onCalorieCounterListener) {
        this.onCalorieCounterListener = onCalorieCounterListener;
    }

    // MAKE: View Super Class Methods

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean dangerMax = properties.dccvDangerMaxWarning && properties.dccvProgress > properties.dccvMax;

        // Background oval
        canvas.drawOval(rectF, dangerMax ? dangerBackgroundPaint : backgroundPaint);

        // Foreground oval
        float angle = 360 * properties.dccvProgress / properties.dccvMax;
        int startAngle = -90;
        canvas.drawArc(rectF, startAngle, angle, false, dangerMax ? dangerForegroundPaint : foregroundPaint);

        // Fill oval
        canvas.drawArc(rectF, 0, 360, false, fillBackgroundPaint);

        String drawText = getDrawText();
        if(!TextUtils.isEmpty(drawText)) {
            
            String[] splitDrawText = drawText.split("\n");
            if(textPaint.size() == splitDrawText.length && textPaint.size() == getYPositions().size()) {
                
                for (int catx=0;catx<splitDrawText.length;catx++)
                {
                    if(!splitDrawText[catx].equals("null")) {

                        float xPos = (getWidth() - textPaint.get(catx).measureText(splitDrawText[catx])) / 2.0f;
                        float yPos = getYPositions().get(catx);

                        canvas.drawText(splitDrawText[catx], xPos, yPos, textPaint.get(catx));
                    }
                }
            }
        }

        if(point != null && properties.dccvClickable) {
            // Drawn when the calorie counter is touched
            canvas.drawArc(rectF, 0, 360, false, backgroundPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        rectF.set(0 + properties.dccvThickness / 2, 0 + properties.dccvThickness / 2, min - properties.dccvThickness / 2, min - properties.dccvThickness / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(properties.dccvClickable)
        {
            float startX = event.getX();
            float startY = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:

                    point = new PointF(startX, startY);

                    invalidate();

                    break;

                case MotionEvent.ACTION_MOVE:

                    if(point != null) {
                        point.set(startX, startY);

                        invalidate();
                    }

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        float endX = event.getX();
                        float endY = event.getY();

                        if(isAValidClick(startX, endX, startY, endY) && onCalorieCounterListener != null) {
                            onCalorieCounterListener.onCalorieCounterClicked();
                        }
                    }

                    point = null;

                    invalidate();

                    break;
            }

            return true;
        }

        return false;
    }

    // MARK: View Private Methods

    /**
     * Initializes the circle progress view
     *
     * @param context The application context
     *
     * @param attrs The view attributes
     */
    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DroidCircleProgressView, 0, 0);

        try {
            properties.dccvMin = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvMin, properties.dccvMin);
            properties.dccvMax = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvMax, properties.dccvMax);
            properties.dccvProgress = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvProgress, properties.dccvProgress);
            properties.dccvBackgroundColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvBackgroundColor, properties.dccvBackgroundColor);
            properties.dccvProgressColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvProgressColor, properties.dccvProgressColor);
            properties.dccvHeaderTxtColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvHeaderTxtColor, properties.dccvHeaderTxtColor);
            properties.dccvHeaderSubTxtColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvHeaderSubTxtColor, properties.dccvHeaderSubTxtColor);
            properties.dccvFooterTxtColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvFooterTxtColor, properties.dccvFooterTxtColor);
            properties.dccvFooterSubTxtColor = typedArray.getInt(R.styleable.DroidCircleProgressView_dccvFooterSubTxtColor, properties.dccvFooterSubTxtColor);
            properties.dccvThickness = typedArray.getDimension(R.styleable.DroidCircleProgressView_dccvThickness, properties.dccvThickness);
            properties.dccvHeaderTxtSize= typedArray.getDimension(R.styleable.DroidCircleProgressView_dccvHeaderTxtSize, properties.dccvHeaderTxtSize);
            properties.dccvHeaderSubTxtSize = typedArray.getDimension(R.styleable.DroidCircleProgressView_dccvHeaderSubTxtSize, properties.dccvHeaderSubTxtSize);
            properties.dccvFooterTxtSize  = typedArray.getDimension(R.styleable.DroidCircleProgressView_dccvFooterTxtSize, properties.dccvFooterTxtSize);
            properties.dccvFooterSubTxtSize = typedArray.getDimension(R.styleable.DroidCircleProgressView_dccvFooterSubTxtSize, properties.dccvFooterSubTxtSize);

            String headerTxt = typedArray.getString(R.styleable.DroidCircleProgressView_dccvHeaderTxt);
            String headerSubTxt = typedArray.getString(R.styleable.DroidCircleProgressView_dccvHeaderSubTxt);
            String footerTxt = typedArray.getString(R.styleable.DroidCircleProgressView_dccvFooterTxt);
            String footerSubTxt = typedArray.getString(R.styleable.DroidCircleProgressView_dccvFooterSubTxt);

            properties.dccvHeaderTxt = (headerTxt == null || headerTxt.isEmpty()) ? null : headerTxt;
            properties.dccvHeaderSubTxt = (headerSubTxt == null || headerSubTxt.isEmpty()) ? null : headerSubTxt;
            properties.dccvFooterTxt = (footerTxt == null || footerTxt.isEmpty()) ? null : footerTxt;
            properties.dccvFooterSubTxt = (footerSubTxt == null || footerSubTxt.isEmpty()) ? null : footerSubTxt;

            properties.dccvClickable = typedArray.getBoolean(R.styleable.DroidCircleProgressView_dccvClickable, properties.dccvClickable);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            typedArray.recycle();
        }

        fillBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillBackgroundPaint.setColor(properties.dccvBackgroundColor);
        fillBackgroundPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(properties.dccvProgressColor, 0.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(properties.dccvThickness);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(properties.dccvProgressColor);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(properties.dccvThickness);

        int blendColor = blendColor(properties.dccvProgressColor, properties.dccvDangerProgressColor);
        dangerBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dangerBackgroundPaint.setColor(adjustAlpha(blendColor, 0.3f));
        dangerBackgroundPaint.setStyle(Paint.Style.STROKE);
        dangerBackgroundPaint.setStrokeWidth(properties.dccvThickness);

        dangerForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dangerForegroundPaint.setColor(blendColor);
        dangerForegroundPaint.setStyle(Paint.Style.STROKE);
        dangerForegroundPaint.setStrokeWidth(properties.dccvThickness);

        textHeaderPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textHeaderPaint.setColor(properties.dccvHeaderTxtColor);
        textHeaderPaint.setTextSize(properties.dccvHeaderTxtSize);

        textHeaderSubPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textHeaderSubPaint.setColor(properties.dccvHeaderSubTxtColor);
        textHeaderSubPaint.setTextSize(properties.dccvHeaderSubTxtSize);

        textFooterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textFooterPaint.setColor(properties.dccvFooterTxtColor);
        textFooterPaint.setTextSize(properties.dccvFooterTxtSize);

        textFooterSubPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textFooterSubPaint.setColor(properties.dccvFooterSubTxtColor);
        textFooterSubPaint.setTextSize(properties.dccvFooterSubTxtSize);

        // Adding the text paints to an array list
        textPaint = new ArrayList<>(Arrays.asList(textHeaderPaint, textHeaderSubPaint, textFooterPaint, textFooterSubPaint));
    }

    /**
     * Checks if the view touch is a valid click
     *
     * @param startX The start X position
     *
     * @param endX The end X position
     *
     * @param startY The start Y position
     *
     * @param endY The end Y position
     *
     * @return True - click was valid, False - if otherwise
     */
    private boolean isAValidClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);

        return !(differenceX > properties.clickActionThreshold || differenceY > properties.clickActionThreshold);
    }

    /**
     * Transparent the given color by the factor
     *
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     *
     * @param factor 1.0f to 0.0f
     *
     * @return A transplanted color
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Returns a blend color of 2 colors
     *
     * @param color1 The preferred color 1
     *
     * @param color2 The preferred color 2
     *
     * @return The blend color of color 1 & color 2
     */
    private int blendColor(int color1, int color2) {
        return ColorUtils.blendARGB(color1, color2, 0.8f);
    }

    /**
     * Gets the draw text
     *
     * @return The draw text
     */
    private String getDrawText() {
        return properties.dccvHeaderTxt + "\n" + properties.dccvHeaderSubTxt + "\n" + properties.dccvFooterTxt + "\n" + properties.dccvFooterSubTxt;
    }

    /**
     * Gets the y positions for the header, sub header, footer, sub footer
     * 
     * @return The calculated y position
     */
    private ArrayList<Float> getYPositions() {

        ArrayList<Float> yPositions = new ArrayList<>();

        float proposedYPosition = getHeight()/2;

        // Header height
        if(properties.dccvHeaderTxt == null) {
            yPositions.add(0f);
        }
        else if(properties.dccvHeaderSubTxt == null) {
            yPositions.add(proposedYPosition - ((textHeaderPaint.descent() + textHeaderPaint.ascent()) / 2));
        }
        else {
            // Decreasing the y position by 20%
            yPositions.add(Math.abs(proposedYPosition - ((proposedYPosition/100) * 20)));
        }

        // Sub Header height
        if(properties.dccvHeaderSubTxt == null) {
            yPositions.add(0f);
        }
        else if(properties.dccvHeaderTxt == null) {
            // Decreasing the y position by 20%
            yPositions.add(Math.abs(proposedYPosition - ((proposedYPosition/100) * 20)));
        }
        else {
            // Increasing the y position by 5%
            yPositions.add(Math.abs(proposedYPosition + ((proposedYPosition/100) * 5)) - ((textHeaderSubPaint.descent() + textHeaderSubPaint.ascent()) / 2));
        }

        // Footer height
        if(properties.dccvFooterTxt == null) {
            yPositions.add(0f);
        }
        else {
            // Increasing the y position by 35%
            yPositions.add(Math.abs(proposedYPosition + ((proposedYPosition/100) * 35))  - ((textFooterPaint.descent() + textFooterPaint.ascent()) / 2));
        }

        // Sub Footer height
        if(properties.dccvFooterSubTxt == null) {
            yPositions.add(0f);
        }
        else {
            // Increasing the y position by 50%
            yPositions.add(Math.abs(proposedYPosition + ((proposedYPosition/100) * 50)) - ((textFooterSubPaint.descent() + textFooterSubPaint.ascent()) / 2));
        }

        return yPositions;
    }

    // MARK: View Public Methods

    /**
     * Sets the minimum value for the calorie counter
     *
     * @param min The minimum value
     */
    public void setMinimum(int min) {
        properties.dccvMin = min;

        invalidate();
    }

    /**
     * Sets the maximum value for the calorie counter
     *
     * @param max The maximum value
     */
    public void setMaximum(int max) {
        properties.dccvMax = max;

        invalidate();
    }

    /**
     * Sets the progress for the calorie counter
     *
     * @param progress The progress
     */
    public void setProgress(int progress) {

        if(progress <= properties.dccvMax || properties.dccvIgnoreMax) {
            properties.dccvProgress = progress;

            if(onCalorieCounterListener != null) {
                onCalorieCounterListener.onProgressUpdated(progress,  progress <= properties.dccvMax ? properties.dccvMax - properties.dccvProgress : 0);
            }

            invalidate();
            requestLayout();
        }
    }

    /**
     * Set the progress with an animation for the calorie counter
     *
     * @param progress The progress it should animate to it
     */
    public void setProgressWithAnimation(int progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress", progress);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    /**
     * Sets the background color for the calorie counter
     *
     * @param bgColor The desired background color
     */
    public void setBackgroundColor(int bgColor) {
        properties.dccvBackgroundColor = bgColor;

        fillBackgroundPaint.setColor(bgColor);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the progress color for the calorie counter
     *
     * @param progressColor The desired progress color
     */
    public void setProgressColor(int progressColor) {
        properties.dccvProgressColor = progressColor;

        backgroundPaint.setColor(adjustAlpha(progressColor, 0.3f));
        foregroundPaint.setColor(progressColor);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the header text color for the calorie counter
     *
     * @param txtColor The desired color
     */
    public void setHeaderTxtColor(int txtColor) {
        properties.dccvHeaderTxtColor = txtColor;

        textHeaderPaint.setColor(txtColor);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the header sub text color for the calorie counter
     *
     * @param txtColor The desired color
     */
    public void setHeaderSubTxtColor(int txtColor) {
        properties.dccvHeaderSubTxtColor = txtColor;

        textHeaderSubPaint.setColor(txtColor);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the footer text color for the calorie counter
     *
     * @param txtColor The desired color
     */
    public void setFooterTxtColor(int txtColor) {
        properties.dccvFooterTxtColor = txtColor;

        textFooterPaint.setColor(txtColor);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the footer sub text color for the calorie counter
     *
     * @param txtColor The desired color
     */
    public void setFooterSubTxtColor(int txtColor) {
        properties.dccvFooterSubTxtColor = txtColor;

        textFooterSubPaint.setColor(txtColor);

        invalidate();
        requestLayout();
    }
    /**
     * Sets the thickness for the calorie counter
     *
     * @param thickness The desired thickness size
     */
    public void setProgressViewThickness(float thickness) {
        properties.dccvThickness = thickness;

        backgroundPaint.setStrokeWidth(thickness);
        foregroundPaint.setStrokeWidth(thickness);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the header text size for the calorie counter
     *
     * @param headerTxtSize The desired header text size
     */
    public void setHeaderTxtSize(float headerTxtSize) {
        properties.dccvHeaderTxtSize = headerTxtSize;

        textHeaderPaint.setTextSize(headerTxtSize);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the header sub text size for the calorie counter
     *
     * @param headerSubTxtSize The desired header sub text size
     */
    public void setHeaderSubTxtSize(float headerSubTxtSize) {
        properties.dccvHeaderSubTxtSize = headerSubTxtSize;

        textHeaderSubPaint.setTextSize(headerSubTxtSize);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the footer text size for the calorie counter
     *
     * @param footerTxtSize The desired footer text size
     */
    public void setFooterTxtSize(float footerTxtSize) {
        properties.dccvFooterTxtSize = footerTxtSize;

        textFooterPaint.setTextSize(footerTxtSize);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the footer sub text size for the calorie counter
     *
     * @param footerSubTxtSize The desired footer sub text size
     */
    public void setFooterSubTxtSize(float footerSubTxtSize) {
        properties.dccvFooterSubTxtSize = footerSubTxtSize;

        textFooterSubPaint.setTextSize(footerSubTxtSize);

        invalidate();
        requestLayout();
    }

    /**
     * Gets the header text of the calorie counter
     *
     * @return The header text
     */
    public String getHeaderTxt(){
        return properties.dccvHeaderTxt;
    }

    /**
     * Sets the header text for the calorie counter
     *
     * @param headerTxt The desired header text
     */
    public void setHeaderTxt(String headerTxt) {
        if(headerTxt.isEmpty()) {
            headerTxt = null;
        }

        properties.dccvHeaderTxt = headerTxt;

        invalidate();
        requestLayout();
    }

    /**
     * Gets the header sub text of the calorie counter
     *
     * @return The header sub text
     */
    public String getHeaderSubTxt(){
        return properties.dccvHeaderSubTxt;
    }

    /**
     * Sets the header sub text for the calorie counter
     *
     * @param headerSubTxt The desired header sub text
     */
    public void setHeaderSubTxt(String headerSubTxt) {
        if(headerSubTxt.isEmpty()) {
            headerSubTxt = null;
        }

        properties.dccvHeaderSubTxt = headerSubTxt;

        invalidate();
        requestLayout();
    }

    /**
     * Gets the footer text of the calorie counter
     *
     * @return The footer text
     */
    public String getFooterTxt(){
        return properties.dccvFooterTxt;
    }

    /**
     * Sets the footer text for the calorie counter
     *
     * @param footerTxt The desired footer text
     */
    public void setFooterTxt(String footerTxt) {
        if(footerTxt.isEmpty()) {
            footerTxt = null;
        }

        properties.dccvFooterTxt = footerTxt;

        invalidate();
        requestLayout();
    }

    /**
     * Gets the footer sub text of the calorie counter
     *
     * @return The footer sub text
     */
    public String getFooterSubTxt() {
        return properties.dccvFooterSubTxt;
    }

    /**
     * Sets the footer sub text for the calorie counter
     *
     * @param footerSubTxt The desired footer sub text
     */
    public void setFooterSubTxt(String footerSubTxt) {
        if(footerSubTxt.isEmpty()) {
            footerSubTxt = null;
        }

        properties.dccvFooterSubTxt = footerSubTxt;

        invalidate();
        requestLayout();
    }

    /**
     * Sets the click enabled status for the calorie counter
     *
     * @param clickable True - click will be enabled, False if otherwise
     */
    public void setClickable(boolean clickable) {
        properties.dccvClickable = clickable;
    }

    /**
     * Sets the allow greater than max value for the calorie counter
     *
     * @param ignoreMax True - greater than max value will be allowed, False if otherwise
     */
    public void setIgnoreMax(boolean ignoreMax) {
        properties.dccvIgnoreMax = ignoreMax;

        invalidate();
        requestLayout();
    }

    /**
     * Sets the danger max warning status for the calorie counter
     *
     * NOTE: Ignore max needs to be true to enable danger max warning
     *
     * @param dangerMaxWarning True - Danger max warning will be enabled, False if otherwise
     *
     * @param dangerProgressColor The desire danger progress color
     */
    public void setDangerMaxWarning(boolean dangerMaxWarning, int dangerProgressColor) {
        properties.dccvDangerMaxWarning = dangerMaxWarning;
        properties.dccvDangerProgressColor = dangerProgressColor;

        int blendColor = blendColor(properties.dccvProgressColor, dangerProgressColor);
        dangerForegroundPaint.setColor(blendColor);
        dangerBackgroundPaint.setColor(adjustAlpha(blendColor, 0.3f));

        invalidate();
        requestLayout();
    }
}
