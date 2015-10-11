package com.example.student.friends_ms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void generateDB(View view)
    {
        // Inspired from http://stackoverflow.com/questions/1974489/how-to-get-text-from-edittext
        EditText editText = (EditText) findViewById(R.id.editText);
        String DB_NAME = editText.getText().toString();
        if(!DB_NAME.equals("Database_MS.db"))
        {
            Toast.makeText(getBaseContext(), R.string.database_not_found_error, Toast.LENGTH_SHORT).show();
            return;
        }
        final String TABLE_NAME = "friends_MS";

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this, DB_NAME);
        try
        {
            database = dbOpenHelper.openDataBase();
        }
        catch (SQLException e)
        {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ArrayList friends = new ArrayList<String>();
        Cursor friendCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        friendCursor.moveToFirst();
        if(!friendCursor.isAfterLast())
        {
            do { friends.add(friendCursor.getString(1)); } while(friendCursor.moveToNext());
        }

        friendCursor.close();

        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, friends);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor emailPhoneCursor = database.rawQuery("SELECT email, mobilenum FROM " + TABLE_NAME + " WHERE name='" + ((TextView) view).getText().toString() + "'", null);
                emailPhoneCursor.moveToFirst();
                String information = "Email: " + emailPhoneCursor.getString(0) + "\n\nMobile Number: " + emailPhoneCursor.getString(1);

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(R.string.contact_alert_title);
                adb.setMessage(information);
                adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle(R.string.settings_alert_title);
            adb.setMessage(R.string.about_info);
            adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog ad = adb.create();
            ad.show();
            return true;
        }

        else if(id == R.id.action_help)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle(R.string.settings_alert_title);
            adb.setMessage(R.string.help_info);
            adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog ad = adb.create();
            ad.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
