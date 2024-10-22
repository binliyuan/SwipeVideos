package com.deepscience.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
//    @BindView(R.id.login_btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complex_activity_main);
        button = findViewById(R.id.login_btn);
        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NavigationActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: login");
            }
        });
//        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: " + "1111111111");
        switch (v.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: login");
                break;
            default:
                Log.d(TAG, "onClick: ");
                break;
        }
    }
}
