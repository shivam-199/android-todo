package com.example.todolists;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ToDo> todoItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<ToDo> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        ToDo item = todoItems.get(position);

        holder.todoItemName.setText(item.getName());
        holder.dateAdded.setText(item.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView todoItemName, dateAdded;
        private Button noButton, yesButton, saveButton;
        public int id;
        FirebaseAuth mAuth;


        private ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            mAuth = FirebaseAuth.getInstance();

            todoItemName = (TextView) view.findViewById(R.id.name);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);

            final Button editButton = (Button) view.findViewById(R.id.editButton);
            final Button deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen/ DetailsActivity
                    int position = getAdapterPosition();

                    ToDo item = todoItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", item.getName());
                    intent.putExtra("id", item.getId());
                    intent.putExtra("date", item.getDateItemAdded());
                    context.startActivity(intent);
                }
            });
        }


        @Override
        public void onClick(View v) {
            ToDo todo;
            switch (v.getId()) {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    todo = todoItems.get(position);
                    editItem(todo);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    todo = todoItems.get(position);
                    deleteItem(todo);
                    break;
            }
        }


        private void editItem(final ToDo todo) {
            alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.myDialog));
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText todosItem = (EditText) view.findViewById(R.id.todoItem);

            saveButton = (Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            alertDialogBuilder.setTitle("Editing Item " + todo.getName());
            dialog = alertDialogBuilder.create();
            dialog.show();


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = todosItem.getText().toString().trim();

                    //Toast.makeText(context,, Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(name)) {
                        todosItem.setError("Item Required");
                        todosItem.requestFocus();
                        return;
                    }

                    final FirebaseUser user = mAuth.getCurrentUser();
                    final String Uid = user.getUid();
                    DatabaseReference databaseItems = FirebaseDatabase.getInstance().getReference("ToDo").child(Uid).child(todo.getId());
                    ToDo newtodo = new ToDo(name, getTime(), todo.getId());
                    //Toast.makeText(context,todo.getId(), Toast.LENGTH_SHORT).show();
                    databaseItems.setValue(newtodo);

                    dialog.dismiss();

                }
            });

        }


        private void deleteItem(final ToDo todo) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.myDialog));

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            noButton = (Button) view.findViewById(R.id.noButton);
            yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    final String Uid = user.getUid();
                    DatabaseReference dItem = FirebaseDatabase.getInstance().getReference("ToDo").child(Uid).child(todo.getId());
                    dItem.removeValue();
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });

        }


        public  String getTime() {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = timeFormat.format(date);

            return (formattedTime);

        }


    }
}