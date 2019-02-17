package com.gohool.mygrocerylist.mygrocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import com.gohool.mygrocerylist.mygrocerylist.Model.ToDo;
import com.gohool.mygrocerylist.mygrocerylist.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_GROCERY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    /**
     *  CRUD OPERATIONS: Create, Read, Update, Delete Methods
     */

    //Add Grocery
    public void addGrocery(ToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, todo.getName());
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");

    }


    //Get a Grocery
    public ToDo getToDo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
             Constants.KEY_GROCERY_ITEM, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


            ToDo todo = new ToDo();
            todo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            todo.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));

            //convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
            .getTime());

            todo.setDateItemAdded(formatedDate);



        return todo;
    }


    //Get all Groceries
    public List<ToDo> getAllGroceries() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<ToDo> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM,
                Constants.KEY_DATE_NAME}, null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                ToDo grocery = new ToDo();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime());

                grocery.setDateItemAdded(formatedDate);

                // Add to the groceryList
                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }

        return groceryList;
    }


    //Updated Grocery
    public int updateToDo(ToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, todo.getName());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//get system time


        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] { String.valueOf(todo.getId())} );
    }


    //Delete Grocery
    public void deleteToDo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});

        db.close();

    }


    //Get count
    public int getToDoCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
