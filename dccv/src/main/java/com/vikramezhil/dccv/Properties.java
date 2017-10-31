package com.vikramezhil.dccv;

import android.graphics.Color;

/**
 * Droid Calorie Counter View Properties
 *
 * @author Vikram Ezhil
 */

class Properties 
{
    final int clickActionThreshold = 200;

    int dccvMin = 0;

    int dccvMax = 100;

    int dccvProgress = 0;

    int dccvBackgroundColor = Color.TRANSPARENT;

    int dccvProgressColor = Color.parseColor("#4CAF50");

    int dccvDangerProgressColor = Color.RED;

    int dccvHeaderTxtColor = Color.BLACK;

    int dccvHeaderSubTxtColor = Color.BLACK;

    int dccvFooterTxtColor = Color.parseColor("#757575");

    int dccvFooterSubTxtColor = Color.parseColor("#757575");

    float dccvThickness = 35;

    float dccvHeaderTxtSize = 150;

    float dccvHeaderSubTxtSize = 75;

    float dccvFooterTxtSize = 55;

    float dccvFooterSubTxtSize = 55;

    String dccvHeaderTxt = null;

    String dccvHeaderSubTxt = null;

    String dccvFooterTxt = null;

    String dccvFooterSubTxt = null;

    boolean dccvClickable = false;

    boolean dccvIgnoreMax = false;

    boolean dccvDangerMaxWarning = false;
}
