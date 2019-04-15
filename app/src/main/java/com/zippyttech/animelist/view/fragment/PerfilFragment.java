package com.zippyttech.animelist.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.Utils;
//import com.zippyttech.animelist.view.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imageuser;
    private TextView name,email,otherInfo;

    private OnFragmentInteractionListener mListener;
//    private GoogleApiClient googleApiClient;
    public static final String SHARED_KEY ="shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    public static String vcx;
    private String imag;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_perfil, container, false);
       initComponent(view);

        return view;
    }

    private void initComponent(View view) {
        settings = getContext().getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
        imageuser = (ImageView) view.findViewById(R.id.image_user);
        imag = settings.getString("ImageName", "null");

        Glide.with(this)
                .load(imag)
                .placeholder(R.drawable.ic_perfil)
                .error(R.drawable.ic_no_image)
                .into(imageuser);

//        Picasso.get().load(imag).placeholder(R.drawable.ic_perfil).error(R.drawable.ic_no_image)
//                .into(imageuser);
        name = (TextView) view.findViewById(R.id.user_name);
        email = (TextView) view.findViewById(R.id.email_user);
        otherInfo = (TextView) view.findViewById(R.id.other);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        /*if (googleApiClient ==null || googleApiClient.isConnected()) {
          try{
              googleApiClient = new GoogleApiClient
                      .Builder(getContext())
                      .enableAutoManage(getActivity(), 0, this)
                      .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                      .build();
          }catch (Exception e){
              e.printStackTrace();
          }

        }*/
        name.setText(settings.getString("UserName",getString(R.string.null_name_user)));
        email.setText(settings.getString("EmailName",getString(R.string.null_email_user)));

        otherInfo.setText(settings.getString("OtherInfo",getString(R.string.other_info_user)));
    }

    @Override
    public void onStart() {
        super.onStart();

    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()){
//          /*  GoogleSignInAccount account = result.getSignInAccount();
//                    editor.putString("UserName", account.getDisplayName());
//                    editor.putString("EmailName", account.getEmail());
//                    editor.putString("GoogleToken", account.getIdToken());
//          //  editor.putString("ImageUser", account.getPhotoUrl().toString());
//            editor.commit();*/
//
//        }else {
//                goLogInScreen();
//        }
//
//    }
//
//    private void goLogInScreen() {
//        Intent intent = new Intent(getContext(),LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
