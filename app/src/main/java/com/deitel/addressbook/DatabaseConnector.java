// DatabaseConnector.java
// Provides easy connection and creation of Usermovies database.
package com.deitel.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "Movies";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper; // database helper

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } // end DatabaseConnector constructor

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } // end method open

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } // end method close

   // inserts a new movie in the database
   public void insertmovie(String name, String producer, String phone,
      String state, String city) 
   {
      ContentValues newmovie = new ContentValues();
      newmovie.put("name", name);
      newmovie.put("producer", producer);
      newmovie.put("phone", phone);
      newmovie.put("street", state);
      newmovie.put("city", city);

      open(); // open the database
      database.insert("movies", null, newmovie);
      close(); // close the database
   } // end method insertmovie

   // inserts a new movie in the database
   public void updatemovie(long id, String name, String producer,
      String phone, String state, String city) 
   {
      ContentValues editmovie = new ContentValues();
      editmovie.put("name", name);
      editmovie.put("producer", producer);
      editmovie.put("phone", phone);
      editmovie.put("street", state);
      editmovie.put("city", city);

      open(); // open the database
      database.update("movies", editmovie, "_id=" + id, null);
      close(); // close the database
   } // end method updatemovie

   // return a Cursor with all movie information in the database
   public Cursor getAllmovies()
   {
      return database.query("movies", new String[] {"_id", "name"},
         null, null, null, null, "name");
   } // end method getAllmovies

   // get a Cursor containing all information about the movie specified
   // by the given id
   public Cursor getOnemovie(long id)
   {
      return database.query(
         "movies", null, "_id=" + id, null, null, null, null);
   } // end method getOnmovie

   // delete the movie specified by the given String name
   public void deletemovie(long id)
   {
      open(); // open the database
      database.delete("movies", "_id=" + id, null);
      close(); // close the database
   } // end method deletemovie
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } // end DatabaseOpenHelper constructor

      // creates the movies table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named movies
         String createQuery = "CREATE TABLE movies" +
            "(_id integer primary key autoincrement," +
            "name TEXT, producer TEXT, phone TEXT," +
            "street TEXT, city TEXT);";
                  
         db.execSQL(createQuery); // execute the query
      } // end method onCreate

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } // end method onUpgrade
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector


/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/
