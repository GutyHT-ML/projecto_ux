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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class LoginfragmentActivity extends Fragment {

    private Boolean validUser = false;
    private Boolean validPswd = false;
    private String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_loginfragment,container,false);
        //Guardar las propiedades de los views en variables de java
        TextInputLayout textInputLayout =  view.findViewById(R.id.TextInputLayoutPswd);
        TextInputEditText textInputLayoutPswd =  view.findViewById(R.id.TextInputEditTextPswd);

        TextInputLayout userInputLayout = view.findViewById(R.id.il_user);
        TextInputEditText userEditText = view.findViewById(R.id.et_user);

        ImageView ivLogo = view.findViewById(R.id.iv_logo);

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
            if (validPswd && validUser) {
                SharedPreferences sharedPref = getActivity()
                        .getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(
                        getString(R.string.usuario), userEditText.getText().toString()
                );
                editor.apply();

                ProfileFragmentActivity nextFrag = new ProfileFragmentActivity();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, nextFrag, "ProfileFragment")
                            .addToBackStack(null)
                            .commit();
            } else {
                Toast.makeText(getContext(), "User: " + validUser + " Pswd: " + validPswd,
                        Toast.LENGTH_SHORT).show();
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