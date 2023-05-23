package com.example.myblog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ArticleDetailsActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var databaseHelper: SQLiteOpenHelper
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_detail)

        titleTextView = findViewById(R.id.articleTitleTextView)
        dateTextView = findViewById(R.id.articleDateTextView)
        contentTextView = findViewById(R.id.articleContentTextView)

        val articleId = intent.getIntExtra("articleId", -1)

        databaseHelper = BlogDatabaseHelper(this)
        database = databaseHelper.readableDatabase


        loadArticleDetails(articleId)
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Logique de retour ici
            onBackPressed()
        }
    }

    private fun loadArticleDetails(articleId: Int) {
        val projection = arrayOf("title", "date", "content")
        val selection = "id = ?"
        val selectionArgs = arrayOf(articleId.toString())
        val cursor: Cursor = database.query("articles", projection, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))

            titleTextView.text = title
            dateTextView.text = date
            contentTextView.text = content
        }
        val modifyButton: Button = findViewById(R.id.editButton)
        modifyButton.setOnClickListener {
            val EDIT_ACTIVITY_REQUEST_CODE = 1
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("articleId", articleId)
            intent.putExtra("articleTitle", titleTextView.text.toString())
            intent.putExtra("articleDate", dateTextView.text.toString())
            intent.putExtra("articleContent", contentTextView.text.toString())
            startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE)
        }


        cursor.close()
    }
}

