// AddressBook.java
// Main activity for the Address Book app.
package com.deitel.addressbook;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;

public class AddressBook extends AppCompatActivity  implements ObservableScrollViewCallbacks
{
   public static final String ROW_ID = "row_id"; // Intent extra key
   private ListView movieListView; // the ListActivity's ListView
   private CursorAdapter movieAdapter; // adapter for ListView


   @Override
   public void onScrollChanged(int scrollY, boolean firstScroll,
                               boolean dragging) {
   }

   @Override
   public void onDownMotionEvent() {
   }

   @Override
   public void onUpOrCancelMotionEvent(ScrollState scrollState) {

      ActionBar ab = getSupportActionBar();
      if (scrollState == ScrollState.UP) {
         if (ab.isShowing()) {
            ab.hide();
         }
      } else if (scrollState == ScrollState.DOWN) {
         if (!ab.isShowing()) {
            ab.show();
         }
      }

   }
   
   // called when the activity is first created
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      setContentView(R.layout.movie_list_item);

      ObservableListView listView = (ObservableListView) findViewById(R.id.list);
      listView.setScrollViewCallbacks(AddressBook.this);


      super.onCreate(savedInstanceState); // call super's onCreate
      //movieListView = getListView(); // get the built-in ListView
//      movieListView.setOnItemClickListener(viewmovieListener);

      // map each movie's name to a TextView in the ListView layout
      // Add these codes after ListView initialization
//      ArrayList<String> items = new ArrayList<String>();
//      for (int i = 1; i <= 100; i++) {
//         items.add("Item " + i);
//      }
//      listView.setAdapter(new ArrayAdapter<String>(
//              this, android.R.layout.simple_list_item_1, items));

      String[] from = new String[] { "name" };
      int[] to = new int[] { android.R.id.text1 };
      movieAdapter = new SimpleCursorAdapter(
         AddressBook.this, android.R.layout.simple_list_item_1, null, from, to);
      listView.setAdapter(movieAdapter);
      listView.setOnItemClickListener(viewmovieListener);
      listView.setBackgroundColor(Color.BLUE);




      //   setListAdapter(movieAdapter); // set movieView's adapter
   } // end method onCreate

   @Override
   protected void onResume() 
   {
      super.onResume(); // call super's onResume method
      
       // create new GetmoviesTask and execute it
       new GetmoviesTask().execute((Object[]) null);
    } // end method onResume

   @Override
   protected void onStop() 
   {
      Cursor cursor = movieAdapter.getCursor(); // get current Cursor
      
      if (cursor != null) 
         cursor.deactivate(); // deactivate it
      
      movieAdapter.changeCursor(null); // adapted now has no Cursor
      super.onStop();
   } // end method onStop

   // performs database query outside GUI thread
   private class GetmoviesTask extends AsyncTask<Object, Object, Cursor>
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(AddressBook.this);

      // perform the database access
      @Override
      protected Cursor doInBackground(Object... params)
      {
         databaseConnector.open();

         // get a cursor containing call movies
         return databaseConnector.getAllmovies();
      } // end method doInBackground

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         movieAdapter.changeCursor(result); // set the adapter's Cursor
         databaseConnector.close();
      } // end method onPostExecute
   } // end class GetmoviesTask
      
   // create the Activity's menu from a menu resource XML file
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.addressbook_menu, menu);
      return true;
   } // end method onCreateOptionsMenu
   
   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // create a new Intent to launch the AddEditmovie Activity
      Intent addNewmovie =
         new Intent(AddressBook.this, AddEditmovie.class);
      startActivity(addNewmovie); // start the AddEditmovie Activity
      return super.onOptionsItemSelected(item); // call super's method
   } // end method onOptionsItemSelected

   // event listener that responds to the user touching a movie's name
   // in the ListView
   OnItemClickListener viewmovieListener = new OnItemClickListener()
   {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
         long arg3) 
      {
         // create an Intent to launch the Viewmovie Activity
         Intent viewmovie =
            new Intent(AddressBook.this, Viewmovie.class);
         
         // pass the selected movie's row ID as an extra with the Intent
         viewmovie.putExtra(ROW_ID, arg3);
         startActivity(viewmovie); // start the Viewmovie Activity
      } // end method onItemClick
   }; // end viewmovieListener
} // end class AddressBook


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
