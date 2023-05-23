package com.example.myblog


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myblog.ui.theme.MyBlogTheme
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import androidx.core.content.ContextCompat

class MainActivity2 : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        loadArticles()
    }

    private lateinit var articleLayout: LinearLayout
    private lateinit var databaseHelper: SQLiteOpenHelper
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        articleLayout = findViewById(R.id.articleLayout0)

        databaseHelper = BlogDatabaseHelper(this)
        database = databaseHelper.readableDatabase


        val addButton: ImageButton = findViewById(R.id.addButton0)
        addButton.setOnClickListener {
            val intent = Intent(this, AddArticleActivity::class.java)
            startActivity(intent)
        }

        loadArticles()
    }


    private fun loadArticles() {
        val projection = arrayOf("id", "title", "content")
        val cursor: Cursor = database.query("articles", projection, null, null, null, null, null)

        articleLayout.removeAllViews()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))

            val articleButton = Button(this)
            articleButton.text = title
            articleButton.setOnClickListener {
                val intent = Intent(this, ArticleDetailsActivity::class.java)
                intent.putExtra("articleId", id)
                startActivity(intent)
            }


            val deleteButton = ImageButton(this)
            deleteButton.setImageResource(R.drawable.baseline_close_24)
            deleteButton.background = null
            deleteButton.setOnClickListener {
                deleteArticle(id)
                loadArticles()
            }

            val articleContainerLayout = LinearLayout(this)
            articleContainerLayout.orientation = LinearLayout.VERTICAL
            articleContainerLayout.addView(articleButton)
            articleContainerLayout.addView(deleteButton)


            val textViewTitle = TextView(this)
            textViewTitle.text = title
            textViewTitle.textSize = 20f

            textViewTitle.setTypeface(null, Typeface.BOLD)

            val textViewContent = TextView(this)
            textViewContent.text = TextUtils.ellipsize(content, textViewContent.paint, 50 * textViewContent.lineHeight.toFloat(), TextUtils.TruncateAt.END)
            textViewContent.maxLines = 2

            val articleLayoutItem = LinearLayout(this)
            articleLayoutItem.orientation = LinearLayout.VERTICAL
            articleLayoutItem.addView(textViewTitle)
            articleLayoutItem.addView(textViewContent)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val margin = resources.getDimensionPixelSize(R.dimen.articleLayout0)
            layoutParams.setMargins(margin, margin, margin, margin)
            articleLayoutItem.layoutParams = layoutParams

            articleLayout.addView(articleLayoutItem)
            articleLayout.addView(articleContainerLayout)
        }

        cursor.close()
    }

    private fun deleteArticle(articleId: Int) {
        val selection = "id = ?"
        val selectionArgs = arrayOf(articleId.toString())
        val deletedRows = database.delete("articles", selection, selectionArgs)
        if (deletedRows > 0) {
            Toast.makeText(this, "Article supprimé avec succès", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erreur lors de la suppression de l'article", Toast.LENGTH_SHORT).show()
        }
    }




}
