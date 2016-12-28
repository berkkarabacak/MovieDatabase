// Viewmovie.java
// Activity for viewing a single movie.
package com.deitel.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Viewmovie extends Activity
{
   private long rowID; // selected movie's name
   private TextView nameTextView; // displays movie's name
   private TextView phoneTextView; // displays movie's phone
   private TextView producerTextView; // displays movie's producer
   private TextView streetTextView; // displays movie's street
   private TextView cityTextView; // displays movie's city/state/zip

   // called when the activity is first created
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.view_movie);

      // get the EditTexts
      nameTextView = (TextView) findViewById(R.id.nameTextView);
      phoneTextView = (TextView) findViewById(R.id.phoneTextView);
      producerTextView = (TextView) findViewById(R.id.producerTextView);
      streetTextView = (TextView) findViewById(R.id.streetTextView);
      cityTextView = (TextView) findViewById(R.id.cityTextView);
      
      // get the selected movie's row ID
      Bundle extras = getIntent().getExtras();
      rowID = extras.getLong(AddressBook.ROW_ID); 
   } // end method onCreate

   // called when the activity is first created
   @Override
   protected void onResume()
   {
      super.onResume();
      
      // create new LoadmovieTask and execute it
      new LoadmovieTask().execute(rowID);
   } // end method onResume
   
   // performs database query outside GUI thread
   private class LoadmovieTask extends AsyncTask<Long, Object, Cursor>
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(Viewmovie.this);

      // perform the database access
      @Override
      protected Cursor doInBackground(Long... params)
      {
         databaseConnector.open();
         
         // get a cursor containing all data on given entry
         return databaseConnector.getOnemovie(params[0]);
      } // end method doInBackground

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         super.onPostExecute(result);
   
         result.moveToFirst(); // move to the first item 
   
         // get the column index for each data item
         int nameIndex = result.getColumnIndex("name");
         int phoneIndex = result.getColumnIndex("phone");
         int producerIndex = result.getColumnIndex("producer");
         int streetIndex = result.getColumnIndex("street");
         int cityIndex = result.getColumnIndex("city");
   
         // fill TextViews with the retrieved data
         nameTextView.setText(result.getString(nameIndex));
         phoneTextView.setText(result.getString(phoneIndex));
         producerTextView.setText(result.getString(producerIndex));
         streetTextView.setText(result.getString(streetIndex));
         cityTextView.setText(result.getString(cityIndex));
   
         result.close(); // close the result cursor
         databaseConnector.close(); // close database connection
      } // end method onPostExecute
   } // end class LoadmovieTask
      
   // create the Activity's menu from a menu resource XML file
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.view_movie_menu, menu);
      return true;
   } // end method onCreateOptionsMenu
   
   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId()) // switch based on selected MenuItem's ID
      {
         case R.id.editItem:
            // create an Intent to launch the AddEditmovie Activity
            Intent addEditmovie =
               new Intent(this, AddEditmovie.class);
            
            // pass the selected movie's data as extras with the Intent
            addEditmovie.putExtra(AddressBook.ROW_ID, rowID);
            addEditmovie.putExtra("name", nameTextView.getText());
            addEditmovie.putExtra("phone", phoneTextView.getText());
            addEditmovie.putExtra("producer", producerTextView.getText());
            addEditmovie.putExtra("street", streetTextView.getText());
            addEditmovie.putExtra("city", cityTextView.getText());
            startActivity(addEditmovie); // start the Activity
            return true;
         case R.id.deleteItem:
            deletemovie(); // delete the displayed movie
            return true;
         default:
            return super.onOptionsItemSelected(item);
      } // end switch
   } // end method onOptionsItemSelected
   
   // delete a movie
   private void deletemovie()
   {
      // create a new AlertDialog Builder
      AlertDialog.Builder builder = 
         new AlertDialog.Builder(Viewmovie.this);

      builder.setTitle(R.string.confirmTitle); // title bar string
      builder.setMessage(R.string.confirmMessage); // message to display

      // provide an OK button that simply dismisses the dialog
      builder.setPositiveButton(R.string.button_delete,
         new DialogInterface.OnClickListener()
         {
            @Override
            public void onClick(DialogInterface dialog, int button)
            {
               final DatabaseConnector databaseConnector = 
                  new DatabaseConnector(Viewmovie.this);

               // create an AsyncTask that deletes the movie in another
               // thread, then calls finish after the deletion
               AsyncTask<Long, Object, Object> deleteTask =
                  new AsyncTask<Long, Object, Object>()
                  {
                     @Override
                     protected Object doInBackground(Long... params)
                     {
                        databaseConnector.deletemovie(params[0]);
                        return null;
                     } // end method doInBackground

                     @Override
                     protected void onPostExecute(Object result)
                     {
                        finish(); // return to the AddressBook Activity
                     } // end method onPostExecute
                  }; // end new AsyncTask

               // execute the AsyncTask to delete movie at rowID
               deleteTask.execute(new Long[] { rowID });               
            } // end method onClick
         } // end anonymous inner class
      ); // end call to method setPositiveButton
      
      builder.setNegativeButton(R.string.button_cancel, null);
      builder.show(); // display the Dialog
   } // end method deletemovie
} // end class Viewmovie
