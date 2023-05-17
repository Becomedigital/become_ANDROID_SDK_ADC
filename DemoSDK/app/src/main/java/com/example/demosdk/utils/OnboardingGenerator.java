package com.example.demosdk.utils;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.demosdk.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnboardingGenerator {

    public static PaperOnboardingFragment setupOnboarding(FragmentManager fragmentManager, FrameLayout frameLayout, ArrayList<PaperOnboardingPage> dataForOnboarding) {
        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance (dataForOnboarding);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ( );
        fragmentTransaction.add (frameLayout.getId ( ), onBoardingFragment);
        fragmentTransaction.commit ( );
        return onBoardingFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  void changeTextDimention(final PaperOnboardingFragment onBoardingFragment, Activity activity) {
        try {
            View rootView = onBoardingFragment.getView ( );
            if (rootView != null) {
                ViewGroup container = (ViewGroup) rootView;
                int count = container.getChildCount ( );
                for (int i = 0; i < count; i++) {
                    View v = container.getChildAt (i);
                    ViewGroup containerChild = (ViewGroup) v;
                    int countChild = containerChild.getChildCount ( );
                    for (int cframe = 0; cframe < countChild; cframe++) {
                        View vframe = containerChild.getChildAt (cframe);
                        if (vframe instanceof LinearLayout) {
                            ViewGroup containerChildFrame = (ViewGroup) vframe;
                            int countChildFrame = containerChildFrame.getChildCount ( );
                            for (int cLinear = 0; cLinear < countChildFrame; cLinear++) {
                                View vLinear = containerChildFrame.getChildAt (cLinear);
                                if (vLinear instanceof FrameLayout) {
                                    ViewGroup containerChildLienar = (ViewGroup) vLinear;
                                    int countChilLinear = containerChildLienar.getChildCount ( );
                                    for (int iLinearTxt = 0; iLinearTxt < countChilLinear; iLinearTxt++) {
                                        View vLinearTxt = containerChildLienar.getChildAt (iLinearTxt);
                                        if (vLinearTxt instanceof LinearLayout) {
                                            ViewGroup containerChildLinearTxt = (ViewGroup) vLinearTxt;
                                            int countChildLinearTxt = containerChildLinearTxt.getChildCount ( );
                                            TextView txt;
                                            for (int iTxt = 0; iTxt < countChildLinearTxt; iTxt++) {
                                                View vTxt = containerChildLinearTxt.getChildAt (iTxt);
                                                if (vTxt instanceof TextView) {
                                                    txt = (TextView) vTxt;
                                                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(txt, 7, 21, 2, TypedValue.COMPLEX_UNIT_SP);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d (activity.getClass ().getSimpleName (),e.getMessage ());
        }
    }


    public static  void changeColor(final PaperOnboardingFragment onBoardingFragment, Activity activity) {
        try {
            View rootView = onBoardingFragment.getView ( );
            if (rootView != null) {

                ViewGroup container = (ViewGroup) rootView;
                int count = container.getChildCount ( );

                for (int i = 0; i < count; i++) {
                    View v = container.getChildAt (i);
                    View rootViewChild = v;
                    ViewGroup containerChild = (ViewGroup) rootViewChild;
                    int countChild = containerChild.getChildCount ( );

                    for (int cframe = 0; cframe < countChild; cframe++) {
                        View vframe = containerChild.getChildAt (cframe);
                        View rootViewChildFrame = vframe;
                        if (vframe instanceof LinearLayout) {
                            ViewGroup containerChildFrame = (ViewGroup) rootViewChildFrame;
                            int countChildFrame = containerChildFrame.getChildCount ( );


                            for (int cLinear = 0; cLinear < countChildFrame; cLinear++) {
                                View vLinear = containerChildFrame.getChildAt (cLinear);
                                View rootViewChillinear = vLinear;
                                if (vLinear instanceof FrameLayout) {
                                    ViewGroup containerChildLienar = (ViewGroup) rootViewChillinear;
                                    int countChilLinear = containerChildLienar.getChildCount ( );


                                    for (int iLinearTxt = 0; iLinearTxt < countChilLinear; iLinearTxt++) {
                                        View vLinearTxt = containerChildLienar.getChildAt (iLinearTxt);

                                        View rootViewChildLinearTxt = vLinearTxt;

                                        if (vLinearTxt instanceof LinearLayout) {
                                            ViewGroup containerChildLinearTxt = (ViewGroup) rootViewChildLinearTxt;
                                            int countChildLinearTxt = containerChildLinearTxt.getChildCount ( );
                                            TextView txt;
                                            for (int iTxt = 0; iTxt < countChildLinearTxt; iTxt++) {
                                                View vTxt = containerChildLinearTxt.getChildAt (iTxt);
                                                if (vTxt instanceof TextView) {
                                                    txt = (TextView) vTxt;
                                                    txt.setTextColor (activity.getResources ( ).getColor (R.color.white));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d (activity.getClass ().getSimpleName (),e.getMessage ());
        }
    }
}
