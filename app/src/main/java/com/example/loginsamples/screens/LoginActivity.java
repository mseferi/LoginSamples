package com.example.loginsamples.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsamples.R;
import com.example.loginsamples.utils.Keys;
import com.example.loginsamples.utils.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;


    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    /*
    Starting Register activity on tvRegister click
     */
    @OnClick(R.id.tvRegister)
    public void setTvRegister() {
        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(myIntent);
    }


    @OnClick(R.id.btnLogin)
    public void setBtnLogin() {
        if (etUsername.getText().toString().equals(Keys.ADMIN_USERNAME) && etPassword.getText().toString().equals(Keys.ADMIN_PASSWORD)) {
            Intent intent = new Intent(LoginActivity.this, CustomPrintActivity.class);
            startActivity(intent);
        } else {
            // Username or password false, display and an error
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage(R.string.please_try_again);
            dlgAlert.setTitle(R.string.wrong_username);
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
    }


    @OnClick(R.id.ivCroatian)
    public void getCroatian() {
        context = LocaleHelper.setLocale(LoginActivity.this, "hr");
        getLanguageResources();
    }


    @OnClick(R.id.ivGerman)
    public void getGerman() {
        context = LocaleHelper.setLocale(LoginActivity.this, "de");
        getLanguageResources();
    }


    @OnClick(R.id.ivEnglish)
    public void getEnglish() {
        context = LocaleHelper.setLocale(LoginActivity.this, "en");
        getLanguageResources();
    }

    public void getLanguageResources() {
        resources = context.getResources();
        tvWelcome.setText(resources.getString(R.string.welcome));
        tvRegister.setText(resources.getString(R.string.don_t_have_an_account_yet_register_here));
        tvForgotPassword.setText(resources.getString(R.string.forgot_password));
        tvLogin.setText(resources.getString(R.string.login));
        btnLogin.setText(resources.getString(R.string.login));
        etUsername.setHint(resources.getString(R.string.username));
        etPassword.setHint(resources.getString(R.string.login_password_hint));

    }
}

