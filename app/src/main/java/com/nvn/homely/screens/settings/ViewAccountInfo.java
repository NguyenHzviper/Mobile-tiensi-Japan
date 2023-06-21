package com.nvn.homely.screens.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.nvn.homely.R;
import com.nvn.homely.data.datalocal.DataLocalManager;
import com.nvn.homely.data.model.user.User;

import java.util.Objects;

public class ViewAccountInfo extends AppCompatActivity {

    TextView tv_password, tv_useremail;
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_account_info);
        user = DataLocalManager.getUser();

        setControl();
        setActionBar();
        setEvent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    private void setEvent() {
        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        tv_useremail.setText(user.getEmail());
    }

    private void setControl(){
        tv_password = findViewById(R.id.tv_password);
        tv_useremail = findViewById(R.id.tv_useremail);

        toolbar = findViewById(R.id.toolbar2);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar); // hỗ trợ toolbar như actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //set nút backhome toolbar
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }
}