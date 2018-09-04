package com.durga.balaji66.databaseproject;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = RegistrationActivity.this;
    private InputValidation inputValidation;
    private RegistrationDatabaseHelper registrationDatabaseHelper;
    private User user;

    private TextInputEditText mFirstName;
    private TextInputEditText mLastName;
    private TextInputEditText mEmailId;
    private TextInputEditText mPassword;
    private Button mRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        initListeners();
        initObjects();
    }

    public void initViews(){
        mFirstName = (TextInputEditText)findViewById(R.id.first_name_edit_text);
        mLastName = (TextInputEditText)findViewById(R.id.last_name_edit_text);
        mEmailId = (TextInputEditText)findViewById(R.id.email_id_edit_text);
        mPassword = (TextInputEditText)findViewById(R.id.password_edit_text);
        mRegistrationButton = (Button)findViewById(R.id.registration_button);

    }

    /*Initiate Listeners*/

    private void initListeners() {
        mRegistrationButton.setOnClickListener(this);

    }

    /*Initiate Objects*/
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        registrationDatabaseHelper = new RegistrationDatabaseHelper(activity);
        user = new User();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_button:
                postDataToSQLite();
                break;
//            case R.id.appCompatTextViewLoginLink:
//                Intent mLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(mLoginIntent);
//                finish();
//                break;
        }
    }

    private void postDataToSQLite() {

        /*Input Validation Happen Here*/
        if (!inputValidation.isInputEditTextFilled(mFirstName, getString(R.string.error_message_first_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(mLastName, getString(R.string.error_message_last_name))) {
            return;
        }

        if (!inputValidation.isInputEditTextEmail(mEmailId, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(mPassword, getString(R.string.error_message_password))) {
            return;
        }


        /*Here we are checking first Email already exist in the database or not then push all the user data to the database*/

        if (!registrationDatabaseHelper.checkUser(mEmailId.getText().toString().trim())) {

            user.setFirstname(mFirstName.getText().toString().trim());
            user.setLastname(mLastName.getText().toString().trim());
            user.setEmail(mEmailId.getText().toString().trim());
            user.setPassword(mPassword.getText().toString().trim());

            /*Here we add all the data to the database*/
            registrationDatabaseHelper.addUser(user);

            Toast.makeText(getApplicationContext(), getString(R.string.success_message), Toast.LENGTH_SHORT).show();
            Intent accountsIntent = new Intent(activity, SignInActivity.class);
            accountsIntent.putExtra("Email",mEmailId.getText().toString().trim());
            startActivity(accountsIntent);
            finish();
            emptyInputEditText();


        } else {
            // Toast Message to show error message that record already exists
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_exists), Toast.LENGTH_SHORT).show();

        }


    }

    private void emptyInputEditText() {
        mFirstName.setText(null);
        mLastName.setText(null);
        mEmailId.setText(null);
        mPassword.setText(null);
    }
}
