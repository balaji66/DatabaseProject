package com.durga.balaji66.databaseproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> listUsers;

    private Context context;



    public UsersRecyclerAdapter(Context context, List<User> listUsers ) {

        this.context = context;
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        holder.textViewFirstName.setText(listUsers.get(position).getFirstname());
        holder.textViewLastName.setText(listUsers.get(position).getLastname());
        holder.textViewEmail.setText(listUsers.get(position).getEmail());
        holder.textViewPassword.setText(listUsers.get(position).getPassword());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();

                RegistrationDatabaseHelper registrationDatabaseHelper =  new RegistrationDatabaseHelper(context);
                registrationDatabaseHelper.delete(holder.textViewEmail.getText().toString().trim());
//                holder.textViewFirstName.setText(null);
//                holder.textViewLastName.setText(null);
//                holder.textViewEmail.setText(null);
//                holder.textViewPassword.setText(null);
                Intent mCandidateListIntent = new Intent(context, CandidateListActivity.class);
                mCandidateListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mCandidateListIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFirstName;
        public TextView textViewLastName;
        public TextView textViewEmail;
        public TextView textViewPassword;
        public Button deleteButton;



        public UserViewHolder(View view) {
            super(view);
            textViewFirstName = (AppCompatTextView) view.findViewById(R.id.textViewFirstName);
            textViewLastName = (AppCompatTextView) view.findViewById(R.id.textViewLastName);
            textViewEmail = (AppCompatTextView) view.findViewById(R.id.textViewEmail);
            textViewPassword = (AppCompatTextView) view.findViewById(R.id.textViewPassword);
            deleteButton = (Button)view.findViewById(R.id.delete_button);

        }
    }


}

