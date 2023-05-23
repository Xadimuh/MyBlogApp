package com.example.myblog

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageButton

class EditActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseHelper: BlogDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        databaseHelper = BlogDatabaseHelper(this)
        titleEditText = findViewById(R.id.titleEditText)
        dateEditText = findViewById(R.id.dateEditText)
        contentEditText = findViewById(R.id.contentEditText)
        saveButton = findViewById(R.id.saveButton)

        // Récupérer les données de l'article à modifier depuis l'intent
        val articleId = intent.getIntExtra("articleId", -1)
        var articleTitle = intent.getStringExtra("articleTitle")
        var articleDate = intent.getStringExtra("articleDate")
        var articleContent = intent.getStringExtra("articleContent")

        // Pré-remplir les EditText avec les données de l'article existant
        titleEditText.setText(articleTitle)
        dateEditText.setText(articleDate)
        contentEditText.setText(articleContent)

        saveButton.setOnClickListener {
            // Récupérer les nouvelles données de l'utilisateur depuis les EditText
            val newTitle = titleEditText.text.toString()
            val newDate = dateEditText.text.toString()
            val newContent = contentEditText.text.toString()

            if (newTitle.isEmpty() || newDate.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val database = databaseHelper.writableDatabase

            // Créer un objet ContentValues avec les nouvelles valeurs
            val updatedValues = ContentValues().apply {
                put("title", newTitle)
                put("date", newDate)
                put("content", newContent)
            }

            val backButton: ImageButton = findViewById(R.id.backButton)
            backButton.setOnClickListener {
                // Logique de retour ici
                onBackPressed()
            }
            // Définir la clause WHERE pour mettre à jour l'article avec l'ID correspondant
            val selection = "id = ?"
            val selectionArgs = arrayOf(articleId.toString())

            // Mettre à jour l'article dans la base de données
            val rowsAffected = database.update("articles", updatedValues, selection, selectionArgs)

            if (rowsAffected > 0) {
                val resultIntent = Intent()
                resultIntent.putExtra("articleId", articleId)
                resultIntent.putExtra("articleTitle", articleTitle)
                resultIntent.putExtra("articleDate", articleDate)
                resultIntent.putExtra("articleContent", articleContent)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                Toast.makeText(this, "Article modifié avec succès", Toast.LENGTH_SHORT).show()


                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Erreur lors de la modification de l'article", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
