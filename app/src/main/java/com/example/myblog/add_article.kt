package com.example.myblog


import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddArticleActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseHelper: SQLiteOpenHelper
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        titleEditText = findViewById(R.id.titleEditText)
        dateEditText = findViewById(R.id.dateEditText)
        contentEditText = findViewById(R.id.contentEditText)
        saveButton = findViewById(R.id.saveButton)

        databaseHelper = BlogDatabaseHelper(this)
        database = databaseHelper.writableDatabase


        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date = dateEditText.text.toString()
            val content = contentEditText.text.toString()
            if (title.isNotEmpty() && date.isNotEmpty() && content.isNotEmpty()) {
                val values = ContentValues().apply {
                    put("title", title)
                    put("date", date)
                    put("content", content)
                }


                val newRowId = database.insert("articles", null, values)

                finish()
            } else{
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Logique de retour ici
            onBackPressed()
        }
    }
}
