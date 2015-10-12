package com.example.student.friends_ms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

// Inspired from http://blog.softeq.com/2012/12/using-pre-populated-sqlite-database-in.html

public class DBOpenHelper extends SQLiteOpenHelper
{
    private static String DB_PATH; // Path to the folder with databases
    private static String DB_NAME;
    private SQLiteDatabase database;
    private final Context context;

    public DBOpenHelper(Context _context, String _databaseName)
    {
        super(_context, _databaseName, null, 1);
        context = _context;

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        /*File DB_PATH_FILE = new File(DB_PATH);
        if(!DB_PATH_FILE.exists())
            DB_PATH_FILE.mkdirs();*/

        DB_NAME = _databaseName;
        try
        {
            database = openDataBase();
        }
        catch (SQLException e)
        {
            Log.e(this.getClass().toString(), "Error while opening database.");
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException
    {
        String path = DB_PATH + DB_NAME;
        if(database == null)
        {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }

        return database;
    }

    public void createDataBase()
    {
        this.getReadableDatabase();
        try
        {
            copyDataBase();
        }
        catch (IOException e)
        {
            Log.e(this.getClass().toString(), "Copying Error.");
            System.out.println(e);
            throw new Error("Error copying database.");
        }
    }

    private void copyDataBase() throws IOException
    {
        // Open Stream for reading from the pre built database
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        // Path to create empty database on Android device
        String outFileName = DB_PATH + DB_NAME;
        // Create a stream to write to the database byte by bte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        // Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead = externalDbStream.read(buffer)) > 0)
        {
            localDbStream.write(buffer, 0, bytesRead);
        }

        // Closing the streams
        externalDbStream.close();
        localDbStream.close();
    }

    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            String path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(Exception e)
        {
            Log.e(this.getClass().toString(), "Error while checking database.");
        }

        if(checkDB != null)
            checkDB.close();

        return (checkDB != null);
    }

    @Override
    public synchronized void close()
    {
        if(database != null)
            database.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  }
}

