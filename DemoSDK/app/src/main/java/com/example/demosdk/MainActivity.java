package com.example.demosdk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.demosdk.utils.OnboardingGenerator;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnStart, btnOmit;
    private PaperOnboardingFragment onBoardingFragment = null;
    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById (R.id.fragment_container_Act);
        btnStart = findViewById (R.id.btnStart_Act);
        btnOmit = findViewById (R.id.btnOmitAct);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TermsActivity.class);
            startActivity(intent);
        });
        btnOmit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TermsActivity.class);
            startActivity(intent);
        });
        setupOnboarding();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume ( );
        OnboardingGenerator.changeTextDimention (onBoardingFragment, MainActivity.this);
        OnboardingGenerator.changeColor (onBoardingFragment, MainActivity.this);
    }


    private void setupOnboarding() {

        fragmentManager = getSupportFragmentManager ( );
        btnOmit.setVisibility (View.VISIBLE);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<> ( );
        elements.add (new PaperOnboardingPage ("Become Digital SDK",
                "Nuestra API está diseñada para que pueda ser implementada según las necesidades de nuestros clientes debido a su sencillez.",
                Color.parseColor ("#CB4071C7"),
                R.drawable.becomewhite,
                R.drawable.become_icon));
        elements.add (new PaperOnboardingPage ("Validación de identidad",
                "Nuestro proceso de validación de identidad está conformado por 3 pasos: Autenticación, Verificación, Búsqueda de resultados.",
                Color.parseColor ("#5FA5A8"),
                R.drawable.onboard_image_2,
                R.drawable.become_icon));
        onBoardingFragment = OnboardingGenerator.setupOnboarding (fragmentManager, frameLayout, elements);
        onBoardingFragment.setOnRightOutListener (() -> {
//                hideOnboarding ( );
        });

        if(elements.size ( ) == 1){
            btnStart.setVisibility (View.VISIBLE);
        }

        onBoardingFragment.setOnChangeListener ((i, i1) -> {
            OnboardingGenerator.changeColor (onBoardingFragment, MainActivity.this);
            if (i1 >= elements.size ( ) - 1) {
                btnStart.animate ( )
                        .alpha (1f)
                        .setDuration (500)
                        .setListener (new AnimatorListenerAdapter( ) {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                btnStart.setVisibility (View.VISIBLE);
                            }
                        });

            } else {
                btnStart.animate ( )
                        .alpha (0f)
                        .setDuration (500)
                        .setListener (new AnimatorListenerAdapter ( ) {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                btnStart.setVisibility (View.GONE);
                            }
                        });
            }
        });
    }
}