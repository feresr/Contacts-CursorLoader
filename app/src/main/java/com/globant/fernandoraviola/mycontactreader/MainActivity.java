package com.globant.fernandoraviola.mycontactreader;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

        SimpleCursorAdapter mAdapter;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No contacts yet");


            //Create an adapter for the listview
            mAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_2, // A layout for each item on the list
                    null, // Cursor, null for now.
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.LAST_TIME_CONTACTED}, // What to retrieve from the cursor
                    new int[]{android.R.id.text1, android.R.id.text2}, // Where to put the retrieved information
                    0
            );

            setListAdapter(mAdapter);

            //Start the loader (or bind, if already started)
            getLoaderManager().initLoader(0, //ID
                    null, // (SEARCH) ARGUMENTS
                    this // An implementation of LoaderCallbacks
            );
        }

        /**
         * A list of which columns to return. Passing null will return all columns, which is inefficient.
         * (Used below)
         */
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.LAST_TIME_CONTACTED,
                ContactsContract.Contacts.LOOKUP_KEY,
        };


        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

            return new CursorLoader(getActivity(),
                    ContactsContract.Contacts.CONTENT_URI, // URI of the contacts content provider
                    CONTACTS_SUMMARY_PROJECTION, // What columns do we want.
                    select, // Query
                    null,
                    /* You may include ?s in selection, which will be replaced by the values from
                     selectionArgs, in the order that they appear in the selection. The values will
                     be bound as Strings */

                    null // Sorting order
            );

        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            // Discard old cursor by new one without closing them
            mAdapter.swapCursor(cursor);

        }

        /**
         * This is called when the Loader needs to be reset. A loader will automatically
         * return the last result when called twice or more.
         * If we want it to re do the query (perpahps with new parameters) it is mandatory to
         * reset it.
         */
        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            // Release cursor
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            // Also called when destroying the activity/fragment.
            mAdapter.swapCursor(null);
        }


    }
}
