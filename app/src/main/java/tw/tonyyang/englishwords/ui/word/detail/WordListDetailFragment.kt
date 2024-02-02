package tw.tonyyang.englishwords.ui.word.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import timber.log.Timber
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.FragmentWordListDetailBinding
import tw.tonyyang.englishwords.ui.base.BaseFragment

class WordListDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentWordListDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = arguments?.getParcelable(WordListDetailActivity.EXTRA_SELECTED_WORD) as? Word
        if (word != null) {
            binding.tvWord.text = word.word.replace("*", "")
            binding.tvWordmean.text = word.wordMean
            binding.tvWordSentence.text = word.wordSentence
            binding.tvCategory.text = word.category
            binding.ratingbar.setStar(word.wordStar.toFloat())
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
        fun newInstance(selectedWords: Word?) = WordListDetailFragment().apply {
            val bundle = Bundle().apply {
                putParcelable(WordListDetailActivity.EXTRA_SELECTED_WORD, selectedWords)
            }
            arguments = bundle
        }
    }
}