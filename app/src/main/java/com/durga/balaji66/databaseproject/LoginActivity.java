package com.durga.balaji66.databaseproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView mScrollView;

    private TextInputEditText mEmailIdTextInputEditText;
    private TextInputEditText mPasswordTextInputEditText;
    private CheckBox mRememberMeCheckBox;
    private Button mLoginButton;
    private Button mRegistrationButton;
    private Button mCandidateListButton;

    private InputValidation inputValidation;

    private RegistrationDatabaseHelper registrationDatabaseHelper;

    private final AppCompatActivity activity = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
        initObjects();
        if (!new PrefManager(this).isUserLogedOut()) {
            //user's email and password both are saved in preferences
            startHomeActivity();
        }
    }

    public void initViews(){

        mScrollView = (ScrollView)findViewById(R.id.scrollView);
        mEmailIdTextInputEditText = (TextInputEditText) findViewById(R.id.email_id_edit_text);
        mPasswordTextInputEditText = (TextInputEditText)findViewById(R.id.password_edit_text);
        mRememberMeCheckBox = (CheckBox)findViewById(R.id.check_box_remember_me);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mRegistrationButton = (Button)findViewById(R.id.registration_button);
        mCandidateListButton = (Button)findViewById(R.id.candidate_list_button);

    }

    public void initListeners(){
        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);
        mCandidateListButton.setOnClickListener(this);
    }

    public void initObjects(){
        registrationDatabaseHelper = new RegistrationDatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                verifyFromSqlite();
                break;
            case R.id.registration_button:
                Intent mRegistrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(mRegistrationIntent);
                finish();
                break;

            case R.id.candidate_list_button:
                Intent mCandidateListIntent = new Intent(LoginActivity.this, CandidateListActivity.class);
                startActivity(mCandidateListIntent);
                finish();
                break;
        }

    }

    private void verifyFromSqlite() {

        if (!inputValidation.isInputEditTextEmail(mEmailIdTextInputEditText, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(mPasswordTextInputEditText,  getString(R.string.error_message_password))) {
            return;
        }

        if (registrationDatabaseHelper.checkUser(mEmailIdTextInputEditText.getText().toString().trim(),
                mPasswordTextInputEditText.getText().toString().trim())) {

            attemptLogin();

        } else {
            Snackbar.make(mScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailIdTextInputEditText.getText().toString();
        String password = mPasswordTextInputEditText.getText().toString();

        // save data in local shared preferences
        if (mRememberMeCheckBox.isChecked()) {
            saveLoginDetails(email, password);
            startHomeActivity();
        }
        else
        {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        Intent accountsIntent = new Intent(activity, SignInActivity.class);
        accountsIntent.putExtra("Email",mEmailIdTextInputEditText.getText().toString().trim());
        startActivity(accountsIntent);
        finish();
    }

    private void saveLoginDetails(String email, String password) {
        new PrefManager(this).saveLoginDetails(email, password);
    }
}
