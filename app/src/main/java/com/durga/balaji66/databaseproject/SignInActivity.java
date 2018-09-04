package com.durga.balaji66.databaseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText mFirstNameEditText;
    private TextInputEditText mLastNameEditText;
    private TextInputEditText mEmailIdEditText;
    private TextInputEditText mPasswordEditText;

    private Button mEditButton;
    private Button mSaveButton;
    private Button mDeleteButton;

    private InputValidation inputValidation;
    RegistrationDatabaseHelper registrationDatabaseHelper;
    private User user;

    String email, emailFromIntent, emailFromPreferences;

    String firstName, lastName, emailId, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        gettingMail();

        emailFromIntent = getIntent().getStringExtra("Email");

        initViews();
        initListeners();
        initObjects();

        editableFalse();

        if(emailFromPreferences.isEmpty()){
            email = emailFromIntent;
        }else{
            email = emailFromPreferences;
        }

        getData();
    }

    private void gettingMail() {
        emailFromPreferences = new PrefManager(this).getEmail();
    }

    private void getData() {
        SQLiteDatabase db = registrationDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where user_email='"+email+"'",null);
        if (cursor.moveToFirst())
        {
            do
            {
                firstName = cursor.getString(1);
                lastName = cursor.getString(2);
                emailId = cursor.getString(3);
                password = cursor.getString(4);

            }while (cursor.moveToNext());
        }

        mFirstNameEditText.setText(firstName);
        mLastNameEditText.setText(lastName);
        mEmailIdEditText.setText(emailId);
        mPasswordEditText.setText(password);
    }


    private void initViews() {
        mFirstNameEditText = (TextInputEditText) findViewById(R.id.first_name_editable_text);
        mLastNameEditText = (TextInputEditText) findViewById(R.id.last_name_editable_text);
        mEmailIdEditText = (TextInputEditText) findViewById(R.id.email_id_editable_text);
        mPasswordEditText = (TextInputEditText) findViewById(R.id.password_editable_text);

        mEditButton = (Button) findViewById(R.id.edit_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mDeleteButton = (Button)findViewById(R.id.delete_button);
    }

    private void initListeners() {
        mEditButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(this);
        registrationDatabaseHelper = new RegistrationDatabaseHelper(this);
        user = new User();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.edit_button:
                editableTrue();
//                edit.setEnabled(false);
//                save.setEnabled(true);
                break;
            case R.id.save_button:
                if(postDataToSQLite() == false) {

                    String fname = mFirstNameEditText.getText().toString().trim();
                    String lname = mLastNameEditText.getText().toString().trim();
                    String email = mEmailIdEditText.getText().toString().trim();

                    String password = mPasswordEditText.getText().toString().trim();

                    if (registrationDatabaseHelper.updateUserData(fname, lname, email, password)) {
                        Toast.makeText(getApplicationContext(), "User Data Updated Succesfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Data Not Updated", Toast.LENGTH_SHORT).show();
                    }

//                    edit.setEnabled(true);
//                    save.setEnabled(false);
                    editableFalse();
                }
                break;
            case R.id.delete_button:
                String email = mEmailIdEditText.getText().toString().trim();

//                if (registrationDatabaseHelper.delete(email)) {
//                    Toast.makeText(getApplicationContext(), "User Data Updated Succesfully", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Data Not Updated", Toast.LENGTH_SHORT).show();
//                }

                registrationDatabaseHelper.delete(email);

                Toast.makeText(getApplicationContext(), "Delete Succesfully", Toast.LENGTH_SHORT).show();

//                    edit.setEnabled(true);
//                    save.setEnabled(false);
                editableFalse();
                break;
        }

    }

    private boolean postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(mFirstNameEditText, getString(R.string.error_message_first_name))) {
            return true;
        }
        if (!inputValidation.isInputEditTextFilled(mLastNameEditText, getString(R.string.error_message_last_name))) {
            return true;
        }
        if (!inputValidation.isInputEditTextEmail(mEmailIdEditText, getString(R.string.error_message_email))) {
            return true;
        }
        if (!inputValidation.isInputEditTextFilled(mPasswordEditText, getString(R.string.error_message_password))) {
            return true;
        }
        return false;
    }

    private void editableFalse() {
        mFirstNameEditText.setEnabled(false);
        mLastNameEditText.setEnabled(false);
        mEmailIdEditText.setEnabled(false);
        mPasswordEditText.setEnabled(false);
    }

    private void editableTrue() {
        mFirstNameEditText.setEnabled(true);
        mLastNameEditText.setEnabled(true);
        mPasswordEditText.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to Logout")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new PrefManager(getApplicationContext()).clear();
                                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignInActivity.super.onBackPressed();
                        // super.onBackPressed();
                        finish();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
