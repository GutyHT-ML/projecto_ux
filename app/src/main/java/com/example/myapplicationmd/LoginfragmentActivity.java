package com.example.myapplicationmd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okio.HashingSink;

public class LoginfragmentActivity extends Fragment {

    private Boolean validUser = false;
    private Boolean validPswd = false;
    private Boolean validEmail = false;
    private Boolean isRegistry = false;
    private String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_loginfragment, container,false);
        //Guardar las propiedades de los views en variables de java
        TextInputLayout textInputLayout =  view.findViewById(R.id.TextInputLayoutPswd);
        TextInputEditText textInputLayoutPswd =  view.findViewById(R.id.TextInputEditTextPswd);

        TextInputLayout userInputLayout = view.findViewById(R.id.il_user);
        TextInputEditText userEditText = view.findViewById(R.id.et_user);

        ImageView ivLogo = view.findViewById(R.id.iv_logo);

        TextView tvOption = view.findViewById(R.id.tv_option);
        TextView tvAction = view.findViewById(R.id.tv_action);
        tvOption.setOnClickListener(view12 -> {
            isRegistry = !isRegistry;
            if (isRegistry) {
                tvOption.setText(R.string.login);
                tvAction.setVisibility(View.VISIBLE);
                tvAction.startAnimation(AnimationUtils.makeInAnimation(getContext(), true));
//                emailInputLayout.setVisibility(View.GONE);
            } else {
                tvOption.setText(R.string.signin);
                tvAction.setVisibility(View.INVISIBLE);
                tvAction.startAnimation(AnimationUtils.makeOutAnimation(getContext(), false));
//                emailInputLayout.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences tokenPref = requireActivity()
                .getPreferences(Context.MODE_PRIVATE);
        String token = tokenPref.getString(getString(R.string.token), null);
        if (token != null) {
            Toast.makeText(getContext(), token + "Welcome", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("user", tokenPref.getString(getString(R.string.usuario), null));
            ProfileFragmentActivity nextFrag = new ProfileFragmentActivity();
            nextFrag.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, nextFrag, "ProfileFragment")
                    .addToBackStack(null)
                    .commit();
        }

        MaterialButton materialButton = view.findViewById(R.id.next_button);
        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);

        Picasso.get()
                .load(R.drawable.off)
                .transform(new CropCircleTransformation())
                .into(ivLogo);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEditText.setText("");
                textInputLayoutPswd.setText("");
            }
        });


        materialButton.setOnClickListener(view1 -> {
            Log.d(TAG, "onClick: " + validPswd);
            if (isRegistry) {
                if (validUser && validPswd) {
                    SharedPreferences sharedPref = requireActivity()
                            .getSharedPreferences(String.valueOf(userEditText.getText()),
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(
                            getString(R.string.usuario), Objects.requireNonNull(userEditText.getText()).toString()
                    );
                    editor.putString(
                            getString(R.string.contra),
                            Objects.requireNonNull(textInputLayoutPswd.getText()).toString());
                    editor.apply();
                    Toast.makeText(getContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                    tvOption.callOnClick();
                    textInputLayoutPswd.setText("");
                } else {
                    Toast.makeText(getContext(), "User: " + validUser + " Pswd: " + validPswd,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (validPswd && validUser) {
                    SharedPreferences sharedPref = requireActivity()
                            .getSharedPreferences(String.valueOf(userEditText.getText()),
                                    Context.MODE_PRIVATE);
                    String user = sharedPref.getString(getString(R.string.usuario), null);
                    if (user == null) {
                        Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    } else{

                        Bundle bundle = new Bundle();
                        bundle.putString("user", user);
                        SharedPreferences.Editor editor = tokenPref.edit();
                        editor.putString(
                                getString(R.string.token), String.valueOf(user.hashCode())
                        );
                        editor.putString(
                                getString(R.string.usuario), user
                        );
                        editor.apply();

                        ProfileFragmentActivity nextFrag = new ProfileFragmentActivity();
                        nextFrag.setArguments(bundle);
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, nextFrag, "ProfileFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    Toast.makeText(getContext(), "User: " + validUser + " Pswd: " + validPswd,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        textInputLayoutPswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validate(textInputLayoutPswd.getText())) {
                    validPswd = true;
                    textInputLayout.setErrorEnabled(false);
                } else {
                    validPswd = false;
                    textInputLayout.setError("Error");
                }
            }
        });

        userEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validate(userEditText.getText())) {
                    validUser = true;
                    userInputLayout.setErrorEnabled(false);
                } else {
                    validUser = false;
                    userInputLayout.setError("Error");
                }
            }
        });
        return view;
    }

    public boolean validate(Editable entrada){
        return entrada != null && entrada.length() >= 6;
    }
}