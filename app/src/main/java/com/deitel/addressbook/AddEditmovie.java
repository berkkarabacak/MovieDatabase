// AddEditmovie.java
// Activity for adding a new entry to or  
// editing an existing entry in the address book.
package com.deitel.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditmovie extends Activity
{
   private long rowID; // id of movie being edited, if any
   
   // EditTexts for movie information
   private EditText nameEditText;
   private EditText phoneEditText;
   private EditText producerEditText;
   private EditText streetEditText;
   private EditText cityEditText;
   
   // called when the Activity is first started
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState); // call super's onCreate
      setContentView(R.layout.add_movie); // inflate the UI

      nameEditText = (EditText) findViewById(R.id.nameEditText);
      producerEditText = (EditText) findViewById(R.id.producerEditText);
      phoneEditText = (EditText) findViewById(R.id.phoneEditText);
      streetEditText = (EditText) findViewById(R.id.streetEditText);
      cityEditText = (EditText) findViewById(R.id.cityEditText);
      
      Bundle extras = getIntent().getExtras(); // get Bundle of extras

      // if there are extras, use them to populate the EditTexts
      if (extras != null)
      {
         rowID = extras.getLong("row_id");
         nameEditText.setText(extras.getString("name"));  
         producerEditText.setText(extras.getString("producer"));
         phoneEditText.setText(extras.getString("phone"));  
         streetEditText.setText(extras.getString("street"));  
         cityEditText.setText(extras.getString("city"));  
      } // end if
      
      // set event listener for the Save movie Button
      Button savemovieButton =
         (Button) findViewById(R.id.savemovieButton);
      savemovieButton.setOnClickListener(savemovieButtonClicked);
   } // end method onCreate

   // responds to event generated when user clicks the Done Button
   OnClickListener savemovieButtonClicked = new OnClickListener()
   {
      @Override
      public void onClick(View v) 
      {
         if (nameEditText.getText().length() != 0)
         {
            AsyncTask<Object, Object, Object> savemovieTask =
               new AsyncTask<Object, Object, Object>() 
               {
                  @Override
                  protected Object doInBackground(Object... params) 
                  {
                     savemovie(); // save movie to the database
                     return null;
                  } // end method doInBackground
      
                  @Override
                  protected void onPostExecute(Object result) 
                  {
                     finish(); // return to the previous Activity
                  } // end method onPostExecute
               }; // end AsyncTask
               
            // save the movie to the database using a separate thread
            savemovieTask.execute((Object[]) null);
         } // end if
         else
         {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder = 
               new AlertDialog.Builder(AddEditmovie.this);
      
            // set dialog title & message, and provide Button to dismiss
            builder.setTitle(R.string.errorTitle); 
            builder.setMessage(R.string.errorMessage);
            builder.setPositiveButton(R.string.errorButton, null); 
            builder.show(); // display the Dialog
         } // end else
      } // end method onClick
   }; // end OnClickListener savemovieButtonClicked

   // saves movie information to the database
   private void savemovie()
   {
      // get DatabaseConnector to interact with the SQLite database
      DatabaseConnector databaseConnector = new DatabaseConnector(this);

      if (getIntent().getExtras() == null)
      {
         // insert the movie information into the database
         databaseConnector.insertmovie(
            nameEditText.getText().toString(),
            producerEditText.getText().toString(),
            phoneEditText.getText().toString(), 
            streetEditText.getText().toString(),
            cityEditText.getText().toString());
      } // end if
      else
      {
         databaseConnector.updatemovie(rowID,
            nameEditText.getText().toString(),
            producerEditText.getText().toString(),
            phoneEditText.getText().toString(), 
            streetEditText.getText().toString(),
            cityEditText.getText().toString());
      } // end else
   } // end class savemovie
} // end class AddEditmovie


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
