package com.hazel.neunotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteDatabase database;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = NoteDatabase.getInstance(this);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        loadNotes();

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult(intent, 1);
        });

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            startActivityForResult(intent, 2);
        });
    }

    private void loadNotes() {
        new Thread(() -> {
            List<Note> notes = database.noteDao().getAllNotes();
            runOnUiThread(() -> adapter.setNotes(notes));
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");

            if (requestCode == 1) {
                // Add new note
                Note note = new Note(title, content);
                new Thread(() -> {
                    database.noteDao().insert(note);
                    loadNotes();
                }).start();
            } else if (requestCode == 2) {
                // Edit note
                int id = data.getIntExtra("id", -1);
                if (id != -1) {
                    Note note = new Note(id, title, content);
                    new Thread(() -> {
                        database.noteDao().update(note);
                        loadNotes();
                    }).start();
                }
            }
        }
    }
}
