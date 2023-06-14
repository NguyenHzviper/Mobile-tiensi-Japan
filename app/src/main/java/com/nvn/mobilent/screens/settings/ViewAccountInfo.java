package com.nvn.mobilent.screens.settings;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nvn.mobilent.R;

public class ViewAccountInfo extends AppCompatActivity {

    TextView tv_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_account_info);

        setControl();
        setEvent();
    }

    private void setEvent() {
        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(){
        tv_password = findViewById(R.id.tv_password);
    }
}