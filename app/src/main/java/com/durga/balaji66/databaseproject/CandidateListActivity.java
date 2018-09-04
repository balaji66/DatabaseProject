package com.durga.balaji66.databaseproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CandidateListActivity extends AppCompatActivity {

    private AppCompatActivity activity = CandidateListActivity.this;

    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private RegistrationDatabaseHelper registrationDatabaseHelper;
    private Button mDeleteAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list);

        initViews();
        initObjects();

        mDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationDatabaseHelper.deleteAll();
                getDataFromSQLite();

            }
        });
    }

    private void initViews() {
        recyclerViewUsers = (RecyclerView) findViewById(android.R.id.list);
        mDeleteAllButton = (Button)findViewById(R.id.delete_all_button);
    }

    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(getApplicationContext(),listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        registrationDatabaseHelper = new RegistrationDatabaseHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {


        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {

            private ProgressDialog pDailog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDailog = new ProgressDialog(activity);
                pDailog.setMessage("Please wait Login......");
                pDailog.show();
                pDailog.setCancelable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(registrationDatabaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (pDailog.isShowing()) {
                    pDailog.dismiss();
                }
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mLoginIntent = new Intent(CandidateListActivity.this, LoginActivity.class);
        startActivity(mLoginIntent);
        finish();
    }
}
