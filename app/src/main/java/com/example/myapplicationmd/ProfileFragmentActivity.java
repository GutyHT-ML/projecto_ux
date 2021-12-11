package com.example.myapplicationmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProfileFragmentActivity extends Fragment {

    private ImageView btnAddPp;
    private ImageView ivProfile;

    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        TextInputLayout textInputLayout =  view.findViewById(R.id.TextInputLayoutPswd);
        TextInputEditText textInputLayoutPswd =  view.findViewById(R.id.TextInputEditTextPswd);

        TextInputLayout userInputLayout = view.findViewById(R.id.il_user);
        TextInputEditText userEditText = view.findViewById(R.id.et_user);

        btnAddPp = view.findViewById(R.id.iv_add_pp);
        ivProfile = view.findViewById(R.id.iv_profile);

        TextView tvProfile = view.findViewById(R.id.tv_profile);

        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);
        MaterialButton btnEdit = view.findViewById(R.id.btn_edit);

        String user = requireArguments().getString("user");

        sharedPreferences = requireActivity()
                .getSharedPreferences(user, Context.MODE_PRIVATE);

        tvProfile.setText(sharedPreferences.getString(getString(R.string.usuario),
                getString(R.string.off_app)));

        userEditText.setText(sharedPreferences.getString(getString(R.string.usuario), ""));
        textInputLayoutPswd.setText(sharedPreferences.getString(getString(R.string.contra), ""));
        Uri uri = Uri.parse(sharedPreferences.getString("uri", ""));

        Picasso.get()
                .load(uri)
                .error(R.drawable.ic_baseline_person_24)
                .transform(new CropCircleTransformation())
                .into(ivProfile);
        
        btnEdit.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.usuario),
                    Objects.requireNonNull(userEditText.getText()).toString());
            editor.putString(getString(R.string.contra),
                    Objects.requireNonNull(textInputLayoutPswd.getText()).toString());
            editor.apply();
            Toast.makeText(getContext(), "Cuenta actualizada", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(view1 -> {
            SharedPreferences tokenPref = requireActivity()
                    .getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = tokenPref.edit();
            editor.remove(getString(R.string.token));
            editor.remove(getString(R.string.usuario));
            editor.apply();

            LoginfragmentActivity nextFrag = new LoginfragmentActivity();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, nextFrag, "LoginFragment")
                    .addToBackStack(null)
                    .commit();
        });

        btnAddPp.setOnClickListener(view12 -> requestPermission());
        return view;
    }

    private void requestPermission () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            pickPhoto();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            pickPhoto();
                        } else {
                            Toast.makeText(getContext(), "You need to enable the permission",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
    private final ActivityResultLauncher<Intent> registerForActivityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
               if(result.getResultCode() == Activity.RESULT_OK) {
                   if (result.getData() != null) {
                       Uri data = result.getData().getData();
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("uri", data.toString());
                       editor.apply();
                       Picasso.get()
                               .load(data)
                               .transform(new CropCircleTransformation())
                               .into(ivProfile);
                   }
               }
            });

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        registerForActivityResult.launch(intent);
    }
}