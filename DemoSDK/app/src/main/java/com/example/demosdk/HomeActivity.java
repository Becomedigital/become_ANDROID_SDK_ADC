package com.example.demosdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.becomedigital.sdk.identity.becomedigitalsdk.StartActivity;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeInterfaseCallback;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeResponseManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.LoginError;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

public class HomeActivity extends AppCompatActivity {
    private final BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew ( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(v -> HomeActivity.super.onBackPressed());
        EditText textUser = findViewById(R.id.textIdUSer);

        String validatiopnTypes =  "VIDEO/PASSPORT/DNI/LICENSE" ;
        String clientSecret =  "" ;
        String clientId = "";
        String contractId = "";
        TextView textError = findViewById(R.id.textResponse);

        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BecomeResponseManager.getInstance().startAutentication(HomeActivity.this,
                        new BDIVConfig(clientId,
                                clientSecret,
                                contractId,
                                validatiopnTypes,
                                true,
                                null,
                                textUser.getText().toString()
                        ));
            }
        });

        BecomeResponseManager.getInstance().registerCallback(mCallbackManager, new BecomeInterfaseCallback() {
            @Override
            public void onSuccess(final ResponseIV responseIV) {
                Intent intent = new Intent(HomeActivity.this, IdentificationActivity.class);
                intent.putExtra("responseIV", (Parcelable) responseIV);
                intent.putExtra("idUser",  textUser.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.d("cancel", "cancel by user");
                textError.setText(R.string.text_cancelk_by_user);
            }

            @Override
            public void onError(LoginError pLoginError) {
                Log.d("Error", pLoginError.getMessage());
                textError.setText(pLoginError.getMessage());
            }
        });
//
//        BecomeResponseManager.getInstance().registerCallback(mCallbackManager, new BecomeInterfaseCallback() {
//            @Override
//            public void onSuccess(final ResponseIV responseIV) {
//                Intent intent = new Intent(HomeActivity.this, IdentificationActivity.class);
//                intent.putExtra("responseIV", (Parcelable) responseIV);
//                intent.putExtra("idUser",  textUser.getText().toString());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("cancel", "cancel by user");
//                textError.setText(R.string.text_cancelk_by_user);
//            }
//
//            @Override
//            public void onError(LoginError pLoginError) {
//                Log.d("Error", pLoginError.getMessage());
//                textError.setText(pLoginError.getMessage());
//            }
//        });

    }
}