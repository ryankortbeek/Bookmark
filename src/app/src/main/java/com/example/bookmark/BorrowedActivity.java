package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Owner;
import com.example.bookmark.models.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of books that they are currently
 * borrowing. They can select a book which takes them to the
 * BorrowedBookDetailsActivity where they can see the books details and
 * return the book.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class BorrowedActivity extends AppCompatActivity implements SearchDialogFragment.OnFragmentInteractionListener {

    List<Book> borrowedBooks = new ArrayList<>();
    BookList borrowedBooksAdapter;
    ListView borrowedBooksListView;

    ActionBar borrowedActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        getSupportActionBar().setTitle("Borrowed");

        borrowedBooksListView = findViewById(R.id.borrowed_books_listview);

        getBorrowedBooks();
        borrowedBooksAdapter = new BookList(this, borrowedBooks, true, false);
        borrowedBooksListView.setAdapter(borrowedBooksAdapter);
        borrowedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BorrowedActivity.this,
                    BorrowedBookDetailsActivity.class);
                // TODO decide how the book data is to be sent to the
                //  BorrowedBookDetailsActivity
                intent.putExtra("selected-book",
                    (Parcelable) borrowedBooks.get(i));
                startActivity(intent);
            }
        });
    }

    private void getBorrowedBooks() {
        // TODO get books currently borrowed by current user - need access to
        //  current user and firebase

        // Proof of concept
        Owner owner = new Owner("u", "fn", "ln",
            "email", "pn");

        Book b1 = new Book("Title 1", "Author 1", "1111111", owner);
        b1.setDescription("Book 1 description");

        borrowedBooks.add(b1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the search icon
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter_search_search_btn) {
            // Opens search fragment
            new SearchDialogFragment().show(getSupportFragmentManager(),
                "SEARCH_AVAILABLE_BOOKS");
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void executeSearch(String searchString) {
        // TODO call search method from singleton that interacts with firebase

        Intent intent = new Intent(BorrowedActivity.this, ExploreActivity.class);
        // TODO put books that match the searched keyword(s) into intent that
        //  is sent to the ExploreActivity which will display the search
        //  results

        // Proof of concept
        intent.putExtra("proof", "Intent has been received!");

        startActivity(intent);
    }
}