package com.example.student.friends_ms;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import java.sql.SQLException;

/*
 * Created by Ma'ad on 11/10/15.
 */
public class ContentProviderMS extends ContentProvider
{
    private DBOpenHelper dbOpenHelper;
    private static final String DB_NAME = "Database_MS.db";
    private SQLiteDatabase db;


    @Override
    public boolean onCreate()
    {
          return true;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return -1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return -1;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        System.out.println("In function query of content provider: " + DB_NAME);

        dbOpenHelper = new DBOpenHelper(getContext(), DB_NAME);
        try
        {
            db = dbOpenHelper.openDataBase();
        }
        catch (SQLException e)
        {
            System.out.println("Error in opening databse: " + e);
        }

        Cursor friendCursor = db.rawQuery("SELECT * FROM friends_MS", null);

        return friendCursor;
    }

}