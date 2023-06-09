package com.nvn.homely.screens.forgot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nvn.homely.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setControl();
        setEvent();
        actionToolBar();
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setEvent() {
        EmailFragment emailFragment = new EmailFragment();
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(emailFragment);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_forgot);
        btnEmail = findViewById(R.id.btnrecemail);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container1, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
    }
}