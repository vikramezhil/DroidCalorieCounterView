package com.vikramezhil.dccv;

/**
 * Droid Calorie Counter View Listener
 *
 * @author Vikram Ezhil
 */

public interface OnCalorieCounterListener
{
    /**
     * Sends an update when the calorie counter view is clicked
     */
    void onCalorieCounterClicked();

    /**
     * Sends an update when the progress value is updates
     *
     * @param progress The updated progress value
     *
     * @param remaining The remaining value
     */
    void onProgressUpdated(int progress, int remaining);
}
