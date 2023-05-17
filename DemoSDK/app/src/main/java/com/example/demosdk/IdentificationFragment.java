package com.example.demosdk;

import static android.animation.AnimatorInflater.loadAnimator;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;
import com.example.demosdk.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class IdentificationFragment extends Fragment {

    private final static String TAG = IdentificationActivity.class.getSimpleName();
    private static AnimatorSet mSetRightOut, mSetLeftIn;
    private static boolean mIsBackVisible = false;
    private static boolean isFlip = false;
    private static View mCardFrontLayout, mCardBackLayout;
    //back card
    private ImageView imgBack;
    public static ImageView imgQR;
    public static IdentificationActivity sIdentificationActivity;
    //front card
    public static ImageView imgFront;
    private static CircularImageView imgUser, imgAlpha;
    private TextView lblURL, lblIdentification, lblDate;
    public static Button btnFlip;
    // ImageView drawables to display spots.
    public static ImageView mSpotTop, mSpotBottom, mSpotLeft, mSpotRight;
    private static Resources resources;
    public static RelativeLayout layoutGui, layoutAlpha;
    private static final String IMAGE_GUI_TAG = "IMG GUI";
    private static final String IMG_ALPHA_TAG = "IMG_ALPHA";
    private static long timeTimmer;
    private static int gMiliseconsTimer1d;
    public static boolean timeOver = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.identification_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d(TAG, "vista creada");
            findViews();
            resources = getResources();
            initialSetUps();
            changeCameraDistance();
            btnFlip = getView().findViewById(R.id.btnFlip);
            loadDataUser(((IdentificationActivity) getActivity()).getResponseIV());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * instancia la vista frontal del carne para poder acceder a sus diferentes controles
     */
    protected void initialSetUps() {
        sIdentificationActivity = ((IdentificationActivity) getActivity());
        Log.d(TAG, "buscando elementos dento de los layouts");
        View mCardFrontLayoutElements = getView().findViewById(R.id.cardFront);
        View mCardBackLayoutElements = getView().findViewById(R.id.cardBack);
        //idtentificacion back
        Log.d(TAG, "cargando elementos dentro de la identificacion trasera");
        imgBack = mCardBackLayoutElements.findViewById(R.id.imgBack);
        imgQR = mCardBackLayoutElements.findViewById(R.id.imgQRCode);
        //idtentificacion front
        Log.d(TAG, "cargando elementos dentro de la identificacion delantera");
        imgFront = mCardFrontLayoutElements.findViewById(R.id.imgFront);
        imgUser = mCardFrontLayoutElements.findViewById(R.id.imgUser);
        lblURL = mCardFrontLayoutElements.findViewById(R.id.txtUrl);
        lblIdentification = mCardFrontLayoutElements.findViewById(R.id.txtIdentification);
        lblDate = mCardFrontLayoutElements.findViewById(R.id.txtDate);
        //containers front
        Log.d(TAG, "cargando containers front");
        layoutGui = mCardFrontLayoutElements.findViewById(R.id.carViewGui);
        layoutGui.setTag(IMAGE_GUI_TAG);
        layoutGui.setAlpha(.7f);
        layoutAlpha = mCardFrontLayoutElements.findViewById(R.id.carViewGuip);
        layoutAlpha.setTag(IMG_ALPHA_TAG);
        layoutAlpha.setAlpha(.7f);
//        //load controls
        Log.d(TAG, "cargando img trasera y delantera identidad");
        imgBack.setImageDrawable(resources.getDrawable(R.drawable.back_carnbe_flip));
        imgFront.setImageDrawable(resources.getDrawable(R.drawable.back_carnbe_flip));

        Log.d(TAG, "cargando imgenes");
        imgUser.setImageDrawable(resources.getDrawable(R.drawable.check_icon));
        lblURL.setText("- -");
        lblIdentification.setText("- -");
        lblDate.setText("- -");

        //img gui
        Log.d(TAG, "cargando imgenes gui");
        mSpotTop = getView().findViewById(R.id.spot_top);
        mSpotBottom = getView().findViewById(R.id.spot_bottom);
        mSpotLeft = getView().findViewById(R.id.spot_left);
        mSpotRight = getView().findViewById(R.id.spot_right);
        imgAlpha = getView().findViewById(R.id.imgUserAlpha);
//
        mSpotTop.setImageDrawable(resources.getDrawable(R.drawable.guilloquis1));
        mSpotLeft.setImageDrawable(resources.getDrawable(R.drawable.guilloquis2));
        mSpotBottom.setImageDrawable(resources.getDrawable(R.drawable.guilloquis3));
        mSpotRight.setImageDrawable(resources.getDrawable(R.drawable.guilloquis4));

        Log.d(TAG, "cargando imgenes alpha");
        imgAlpha.setImageDrawable(resources.getDrawable(R.drawable.user_default));
        imgAlpha.setAlpha(.7f);
        loadAnimations();
        loadQR();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((IdentificationActivity) getActivity()).setlistenersAndActivateSensors();
    }

    private void findViews() {
        mCardBackLayout = getView().findViewById(R.id.cardBack);
        mCardFrontLayout = getView().findViewById(R.id.cardFront);
    }

    public static void refreshControls(long time, String miliseconsTimer1d) {
        Log.d(TAG, "refrescando controles timer segundos: " + time);
        timeTimmer = time;
        gMiliseconsTimer1d = Integer.parseInt(miliseconsTimer1d);
        if (time >= Integer.parseInt(miliseconsTimer1d) && !timeOver) {
            loadQR();
            timeOver = true;
            ((IdentificationActivity) sIdentificationActivity).stopTimer();
            if (mIsBackVisible)
                ((IdentificationActivity) sIdentificationActivity).btnReloadQR.setVisibility(View.VISIBLE);
            imgQR.setImageBitmap(null);
            imgQR.setVisibility(View.GONE);
        }
    }

    public static void loadQR() {
        byte[] encodedBytes;
        String dataQRDecode;
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = sdf.format(currentTime);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int color = 0xFF000000;
        dataQRDecode = encriptar(((IdentificationActivity) sIdentificationActivity).getResponseIV().getUrlResult() + "|" + currentDate);
        encodedBytes = Base64.encode(dataQRDecode.getBytes(), dataQRDecode.getBytes().length);

        String dataQR = new String(encodedBytes);
        Bitmap bitmapQR;
        QRCodeGenerator obj_qr = new QRCodeGenerator();
        imgQR.setVisibility(View.VISIBLE);
        sIdentificationActivity.btnReloadQR.setVisibility(View.GONE);
        try {
            bitmapQR = obj_qr.getQRCodeImage(dataQR, 400, 400, color);
            imgQR.setImageBitmap(bitmapQR);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAlpha(float[] orientationValues) {
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        mSpotTop.setAlpha(0f);
        mSpotBottom.setAlpha(0f);
        mSpotLeft.setAlpha(0f);
        mSpotRight.setAlpha(0f);
        imgAlpha.setAlpha(0f);

        if (pitch > 0) {
            mSpotBottom.setAlpha(pitch);
        } else {
            mSpotTop.setAlpha(Math.abs(pitch));
        }
        if (roll > 0) {
            mSpotLeft.setAlpha(roll);

        } else {
            mSpotRight.setAlpha(Math.abs(roll));
            imgAlpha.setAlpha(Math.abs(roll));
        }
    }

    public static String encriptar(String texto) {
        String value = "";
        Cipher cipher;
        try {
            SecretKey key = new SecretKeySpec(Arrays.copyOf("IDEMIA".getBytes(), 16), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] textobytes = texto.getBytes();
            byte[] cipherbytes = cipher.doFinal(textobytes);
            value = new String(Base64.encode(cipherbytes, cipherbytes.length));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            System.err.println(ex.getMessage());
        }
        return value;
    }

    private void loadDataUser(ResponseIV responseIV) {
        try {
            Log.d(TAG, "cargando data del servidor");

            String fullURL = responseIV.getUrlResult();
//
//            String fullname = responseIV.getFullname();
//            String birth = responseIV.getBirth();
//            String document_number = responseIV.getDocument_number();
//
//            String pathToFileFront = responseIV.getSelfiImageUrlLocal();
//            File imgFileFront = new File(pathToFileFront);
//            if (imgFileFront.exists()) {
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFileFront.getAbsolutePath());
//                imgUser.setBorderWidth(0);
//                imgUser.setImageBitmap(myBitmap);
//                imgAlpha.setBorderWidth(0);
//                imgAlpha.setImageBitmap(myBitmap);
//            }
            lblURL.setText(fullURL);
//            lblIdentification.setText(document_number);
//            lblDate.setText(birth);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    //region gestures and events
    public static void onClickToFlip() { //check for what button is pressed
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;

        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
        if (timeTimmer >= gMiliseconsTimer1d) {
            sIdentificationActivity.btnReloadQR.setVisibility(mIsBackVisible ? View.VISIBLE : View.GONE);
        }
    }

    public static void shakeFlip() { //check for what button is pressed
        if (!isFlip) {
            imgFront.setImageDrawable(resources.getDrawable(R.drawable.back_carnbe_flip));
            isFlip = true;
        } else {
            imgFront.setImageDrawable(resources.getDrawable(R.drawable.back_carne));
            isFlip = false;
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) loadAnimator(this.getContext(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) loadAnimator(this.getContext(), R.animator.in_animation);
    }

    //endregion
}




