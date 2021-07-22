package tw.tonyyang.englishwords.ui.word.list

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.ui.base.BaseActivity

class WordListActivity : BaseActivity() {

    private val category: String by lazy {
        intent.extras?.getString(EXTRA_CATEGORY) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.ic_launcher)
            setDisplayUseLogoEnabled(true)
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WordListFragment.newInstance(category))
                    .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }
}