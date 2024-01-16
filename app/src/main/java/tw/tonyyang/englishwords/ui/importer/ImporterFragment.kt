package tw.tonyyang.englishwords.ui.importer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.databinding.FragmentDropboxchooserBinding
import tw.tonyyang.englishwords.state.Result
import tw.tonyyang.englishwords.util.UiUtils

class ImporterFragment : Fragment() {

    private lateinit var binding: FragmentDropboxchooserBinding

    private lateinit var layout: View

    private lateinit var progress: AlertDialog

    private val viewModel: ImporterViewModel by sharedViewModel()

    private var fileUrl: String? = null

    private val startChooseFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result?.data?.data
            if (uri != null) {
                fileUrl = uri.toString()
                binding.tvFilename.text = fileUrl
                binding.tvFileSize.text = ""
                binding.btnSubmit.isEnabled = true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDropboxchooserBinding.inflate(inflater, container, false)
        layout = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        progress = UiUtils.getProgressDialog(requireContext(), getString(R.string.loading_message))
        binding.btnChooser.setOnClickListener {
            chooseFile()
        }
        binding.btnSubmit.setOnClickListener {
            viewModel.importWords(fileUrl)
        }
        binding.btnSubmit.isEnabled = false
    }

    private fun initObservers() {
        viewModel.showResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.InProgress -> progress.show()
                is Result.Success -> {
                    progress.hide()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_LONG).show()
                }

                is Result.Error -> {
                    progress.hide()
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun chooseFile() {
        chooseFileFromLocal()
    }

    private fun chooseFileFromLocal() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/vnd.ms-excel"
        }
        val destIntent = Intent.createChooser(intent, "檔案目錄選擇")
        startChooseFileLauncher.launch(destIntent)
    }

    companion object {
        private const val TAG = "ImporterFragment"

        fun newInstance() = ImporterFragment()
    }
}