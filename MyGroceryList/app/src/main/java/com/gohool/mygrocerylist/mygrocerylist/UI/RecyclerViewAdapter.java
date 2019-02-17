package com.gohool.mygrocerylist.mygrocerylist.UI;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gohool.mygrocerylist.mygrocerylist.Activities.DetailsActivity;
import com.gohool.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.gohool.mygrocerylist.mygrocerylist.Model.ToDo;
import com.gohool.mygrocerylist.mygrocerylist.R;

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

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        ToDo grocery = todoItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groceryItemName;
        private TextView dateAdded;
        private Button editButton;
        private Button deleteButton;
        public int id;


        private ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen/ DetailsActivity
                    int position = getAdapterPosition();

                    ToDo grocery = todoItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    context.startActivity(intent);


                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    ToDo todo = todoItems.get(position);
                    editItem(todo);

                    break;
                case R.id.deleteButton:
                     position = getAdapterPosition();
                     todo = todoItems.get(position);
                     deleteItem(todo.getId());

                    break;


            }
        }

        private void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

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
                    //delete the item.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteToDo(id);
                    todoItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });

        }


        public void editItem(final ToDo grocery) {

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.todoItem);
            final TextView title = (TextView) view.findViewById(R.id.tile);

            title.setText("Edit Item");
            Button saveButton = (Button) view.findViewById(R.id.saveButton);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update item
                    grocery.setName(groceryItem.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty()) {
                        db.updateToDo(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else {
                        Snackbar.make(view, "Added Item", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }
    }

}
