package com.example.myapplicationmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Mostrar el fragment
        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginfragmentActivity(), "LoginFragment")
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new LoginfragmentActivity(), "LoginFragment")
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new ProfileFragmentActivity(), "ProfileFragment")
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}