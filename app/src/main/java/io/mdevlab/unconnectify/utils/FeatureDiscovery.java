package io.mdevlab.unconnectify.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.mdevlab.unconnectify.R;
import io.mdevlab.unconnectify.adapter.AlarmViewHolder;
import jonathanfinerty.once.Once;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by mdevlab on 2/28/17.
 */

public class FeatureDiscovery {

    private static FeatureDiscovery instance = null;
    private static final int ONCE_TRIGGER_MODE = Once.THIS_APP_INSTALL;
    private AlarmViewHolder holder;
    public static final long DEFAULT_HOLDER_ANIMATION_DURATION = 1500L;

    private FeatureDiscovery() {
    }

    public static FeatureDiscovery getInstance() {
        if (instance == null)
            instance = new FeatureDiscovery();
        return instance;
    }

    /**
     * @param activity: Activity calling this method
     * @param target:   View on which the focus will be done in the feature discovery
     * @param icon:     Icon on top of the target to be displayed
     * @param title:    Title of the description text
     * @param body:     body of the description text
     */
    private void discoverFeature(final Activity activity, View target, int icon, String title, String body) {
        try {
            MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(target)
                    .setPrimaryText(title)
                    .setSecondaryText(body)
                    .setBackgroundColourFromRes(R.color.onboarding_color)
                    .setAnimationInterpolator(new FastOutSlowInInterpolator());

            if (icon > 0)
                tapTargetPromptBuilder.setIcon(icon);

            tapTargetPromptBuilder.create()
                    .show();

        } catch (Exception e) {
            Log.e("MaterialTapTargetPrompt", "MaterialTapTargetPrompt exception");
            e.printStackTrace();
        }
    }

    private void setTarget(final Activity activity, final View target, final int icon, final String title, final String body, final String targetAction) {
        if (!Once.beenDone(ONCE_TRIGGER_MODE, targetAction)) {
            discoverFeature(activity, target, icon, title, body);
            Once.markDone(targetAction);
        }
    }

    public void createAlarmFeatureDiscovery(Activity activity, View target) {
        setTarget(activity,
                target,
                R.drawable.ic_add_alarm,
                activity.getString(R.string.feature_discovery_create_alarm_title),
                activity.getString(R.string.feature_discovery_create_alarm_body),
                Constants.FEATURE_DISCOVERY_CREATE_ALARM);
    }

    public void onFirstAlarmCreatedFeatureDiscovery(final Activity activity, final View startTimeView, final View endTimeView, final View wifiView, final View sundayView,final AlarmViewHolder holder) {
       this.holder =holder;
        try {
            if (!Once.beenDone(ONCE_TRIGGER_MODE, Constants.FEATURE_DISCOVERY_START_TIME)) {
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(startTimeView)
                        .setPrimaryText(activity.getString(R.string.feature_discovery_start_time_title))
                        .setSecondaryText(activity.getString(R.string.feature_discovery_start_time_title))
                        .setBackgroundColourFromRes(R.color.onboarding_color)
                        .setFocalColourFromRes(R.color.onboarding_color)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            }

                            @Override
                            public void onHidePromptComplete() {
                                endTimeFeatureDiscovery(activity, endTimeView, wifiView, sundayView);
                            }
                        })
                        .create()
                        .show();
                Once.markDone(Constants.FEATURE_DISCOVERY_START_TIME);
            }
        } catch (Exception e) {
            Log.e("MaterialTapTargetPrompt", "MaterialTapTargetPrompt exception");
            e.printStackTrace();
        }
    }

    private void endTimeFeatureDiscovery(final Activity activity, final View endTimeView, final View wifiView, final View sundayView) {
        try {
            if (!Once.beenDone(ONCE_TRIGGER_MODE, Constants.FEATURE_DISCOVERY_END_TIME)) {
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(endTimeView)
                        .setPrimaryText(activity.getString(R.string.feature_discovery_end_time_title))
                        .setSecondaryText(activity.getString(R.string.feature_discovery_end_time_body))
                        .setBackgroundColourFromRes(R.color.onboarding_color)
                        .setFocalColourFromRes(R.color.onboarding_color)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            }

                            @Override
                            public void onHidePromptComplete() {
                                connectionFeatureDiscovery(activity, wifiView, sundayView);
                            }
                        })
                        .create()
                        .show();
                Once.markDone(Constants.FEATURE_DISCOVERY_END_TIME);
            }
        } catch (Exception e) {
            Log.e("MaterialTapTargetPrompt", "MaterialTapTargetPrompt exception");
            e.printStackTrace();
        }
    }

    private void connectionFeatureDiscovery(final Activity activity, final View wifiView, final View sundayView) {
        try {
            if (!Once.beenDone(ONCE_TRIGGER_MODE, Constants.FEATURE_DISCOVERY_CONNECTION)) {
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(wifiView)
                        .setPrimaryText(activity.getString(R.string.feature_discovery_connection_title))
                        .setSecondaryText(activity.getString(R.string.feature_discovery_connection_body))
                        .setBackgroundColourFromRes(R.color.onboarding_color)
                        .setFocalColourFromRes(R.color.onboarding_color)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            }

                            @Override
                            public void onHidePromptComplete() {
                                dayFeatureDiscovery(activity, sundayView);
                            }
                        })
                        .create()
                        .show();
                Once.markDone(Constants.FEATURE_DISCOVERY_CONNECTION);
            }
        } catch (Exception e) {
            Log.e("MaterialTapTargetPrompt", "MaterialTapTargetPrompt exception");
            e.printStackTrace();
        }
    }

    private void dayFeatureDiscovery(final Activity activity, final View sundayView) {
        try {
            if (!Once.beenDone(ONCE_TRIGGER_MODE, Constants.FEATURE_DISCOVERY_DAY)) {
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(sundayView)
                        .setPrimaryText(activity.getString(R.string.feature_discovery_day_title))
                        .setSecondaryText(activity.getString(R.string.feature_discovery_day_body))
                        .setBackgroundColourFromRes(R.color.onboarding_color)
                        .setFocalColourFromRes(R.color.onboarding_color)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                            }

                            @Override
                            public void onHidePromptComplete() {
                                onStartAnimation(holder);
                            }
                        })
                        .create()
                        .show();
                Once.markDone(Constants.FEATURE_DISCOVERY_DAY);
            }
        } catch (Exception e) {
            Log.e("MaterialTapTargetPrompt", "MaterialTapTargetPrompt exception");
            e.printStackTrace();
        }
    }

    /**
     * This animation is part of the Discovery   process to show the user the turn on/off alarm  and the hidden container
     * for deleting the alarm
     *
     * the alarm is translated using the value animator API
     *
     * @param holder Holder to animate
     */
    public void onStartAnimation(final AlarmViewHolder holder) {
        //Get an instance of value animator
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,-400);
        //Add a listner from where the translation will happen
        //after we start the animation
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //translated the target views
                holder.mAlarmContainer.setTranslationX(value);
                holder.mSwipeRevealLayoutContainer.setTranslationX(value);


            }
        });
        //Set the repetition and the mode
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        valueAnimator.setDuration(1000L);
        //Start the animation
        valueAnimator.start();

    }
}
