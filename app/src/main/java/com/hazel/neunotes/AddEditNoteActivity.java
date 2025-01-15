package com.hazel.neunotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        buttonSave = findViewById(R.id.button_save);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextContent.setText(intent.getStringExtra("content"));
        } else {
            setTitle("Add Note");
        }

        buttonSave.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();

            Intent data = new Intent();
            data.putExtra("title", title);
            data.putExtra("content", content);

            if (intent.hasExtra("id")) {
                data.putExtra("id", intent.getIntExtra("id", -1));
            }

            setResult(RESULT_OK, data);
            finish();
        });
    }
}
