package com.becomedigital.sdk.identity.becomedigitalsdk;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.SharedParameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewImageFragment extends Fragment {

    private SharedParameters.typeDocument typeDocument;
    private BDIVConfig config;
    private String selectedCountry,
            selectedCountyCo2 = "",
            urlVideoFile = "",
            urlDocFront = "",
            urlDocBack = "";
    private boolean isFront;
    private boolean isSelfie;
    public PreviewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_preview_image, container, false);
    }

    Button btnIsOkImage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        ((MainBDIV) requireActivity ( )).changeColorToolbar (true);
        ImageView imgToPreviewFront = requireActivity ( ).findViewById (R.id.imgToPreviewFront);
        ImageView imgToPreviewBack = requireActivity ( ).findViewById (R.id.imgToPreviewBack);
        ImageView imgProgresssbar = requireActivity().findViewById(R.id.imgProgressBarDoc);
        Bundle arguments = getArguments();
        if (arguments != null) {
            config = (BDIVConfig) arguments.getSerializable("config");
            if (arguments.containsKey("typeDocument"))
                typeDocument = (SharedParameters.typeDocument) arguments.getSerializable("typeDocument");
            if (arguments.containsKey("urlVideoFile"))
                urlVideoFile = arguments.getString("urlVideoFile");
            if (arguments.containsKey("selectedCountyCo2"))
                selectedCountyCo2 = arguments.getString("selectedCountyCo2");
            if (arguments.containsKey("selectedCountry"))
                selectedCountry = arguments.getString("selectedCountry");
            if (arguments.containsKey("urlDocBack"))
                urlDocBack = arguments.getString("urlDocBack");
            if (arguments.containsKey("urlDocFront"))
                urlDocFront = arguments.getString("urlDocFront");
            if (arguments.containsKey("isFront"))
                isFront = getArguments().getBoolean("isFront");
            if (arguments.containsKey("isSelfie"))
                isSelfie = getArguments().getBoolean("isSelfie");

        }
        File imgFileF = new File (urlDocFront);
        File imgFileB = new File (urlDocBack);
        if (imgFileF.exists ( )) {
            Bitmap myBitmap = BitmapFactory.decodeFile (imgFileF.getAbsolutePath ( ));
            imgToPreviewFront.setImageBitmap (myBitmap);
        }
        if(isSelfie){
            imgProgresssbar.setVisibility(View.INVISIBLE);
        }
        if (typeDocument == SharedParameters.typeDocument.PASSPORT) {
            imgToPreviewBack.setVisibility(View.GONE);
        }else{
            if (imgFileB.exists ( )) {
                Bitmap myBitmap = BitmapFactory.decodeFile (imgFileB.getAbsolutePath ( ));
                imgToPreviewBack.setImageBitmap (myBitmap);
            }
        }

        btnIsOkImage = requireActivity ( ).findViewById (R.id.btnOkImage);
        Button btnRetry = requireActivity ( ).findViewById (R.id.btnRetry);

        btnRetry.setOnClickListener (view1 -> {
            if (imgFileF.exists ( )) {
                imgFileF.delete ( );
            }
            if (imgFileB.exists ( )) {
                imgFileB.delete ( );
            }
            findNavController (view).popBackStack ( );

        });
        btnIsOkImage.setOnClickListener (view12 -> {
            if(isSelfie){
                ((MainBDIV) requireActivity ( )).displayLoader (true );
                ((MainBDIV) requireActivity ( )).facialAuth(config, urlDocFront);
            }else{
                navigate ( );
            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle
        outState.clear();
    }


    private void navigate() {
        Bundle bundle = new Bundle ( );
        bundle.putString ("urlDocFront", urlDocFront);
        bundle.putString ("urlDocBack", urlDocBack);
        bundle.putString("selectedCountry",selectedCountry);
        bundle.putString("selectedCountyCo2",selectedCountyCo2);
        bundle.putSerializable("typeDocument",typeDocument);
        bundle.putSerializable("config",config);
        bundle.putString ("urlVideoFile", urlVideoFile);
        findNavController (requireActivity ( ), R.id.nav_host_fragment).navigate (R.id.actionFinish, bundle);
//        if (typeDocument == SharedParameters.typeDocument.PASSPORT) {
//            findNavController (requireActivity ( ), R.id.nav_host_fragment).navigate (R.id.actionFinish, bundle);
//        } else {
//
//            if (isFront) {
//                bundle.putBoolean ("isFront", false);
//                findNavController (requireActivity ( ), R.id.nav_host_fragment).navigate (R.id.actionCaptureBackDocument, bundle);
//            } else {
//                findNavController (requireActivity ( ), R.id.nav_host_fragment).navigate (R.id.actionFinish, bundle);
//            }
//        }
    }

}
