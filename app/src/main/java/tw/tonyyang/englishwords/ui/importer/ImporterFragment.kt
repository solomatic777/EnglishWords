package tw.tonyyang.englishwords.ui.importer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import tw.tonyyang.englishwords.util.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.databinding.FragmentDropboxchooserBinding
import tw.tonyyang.englishwords.state.Result
import tw.tonyyang.englishwords.util.UiUtils

class ImporterFragment : Fragment() {

    private lateinit var binding: FragmentDropboxchooserBinding

    private lateinit var layout: View

    private val viewModel: ImporterViewModel by sharedViewModel()

    private var fileUrl: String? = null

    private var progress: AlertDialog? = null

    private val startChooseFileLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Logger.d(TAG, "choose file result")
                val uri = result?.data?.data
                if (uri != null) {
                    fileUrl = uri.toString()
                    binding.tvFilename.text = fileUrl
                    binding.tvFileSize.text = ""
                    binding.btnSubmit.isEnabled = true
                }
            }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    activity?.let {
                        MaterialDialog.Builder(it)
                            .setMessage(it.getString(R.string.importer_external_storage_permission_granted))
                            .setCancelable(false)
                            .setPositiveButton(it.getString(android.R.string.ok)) { dialogInterface, _ ->
                                chooseFileFromLocal()
                                dialogInterface.dismiss()
                            }
                            .build()
                            .show()
                    } ?: chooseFileFromLocal()
                } else {
                    activity?.let {
                        MaterialDialog.Builder(it)
                            .setMessage(it.getString(R.string.importer_external_storage_permission_denied))
                            .setCancelable(false)
                            .setPositiveButton(it.getString(android.R.string.ok)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .build()
                            .show()
                    }
                }
            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        activity?.let {
            progress = UiUtils.getProgressDialog(it, it.getString(R.string.loading_message))
        }
        binding.btnChooser.setOnClickListener {
            chooseFile()
        }
        binding.btnSubmit.setOnClickListener {
            viewModel.importWords(fileUrl)
        }
        binding.btnSubmit.isEnabled = false
    }

    private fun initObservers() {
        viewModel.showResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.InProgress -> progress?.show()
                is Result.Success -> {
                    progress?.hide()
                    Toast.makeText(activity, it.data, Toast.LENGTH_LONG).show()
                }
                is Result.Error -> {
                    progress?.hide()
                    Toast.makeText(activity, it.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun chooseFile() {
        val activity = activity ?: return
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            chooseFileFromLocal()
        } else requestExternalStoragePermission()
    }

    private fun requestExternalStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            activity?.let {
                MaterialDialog.Builder(it)
                    .setMessage(it.getString(R.string.importer_external_storage_access_required))
                    .setCancelable(false)
                    .setPositiveButton(it.getString(android.R.string.ok)) { dialogInterface, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        dialogInterface.dismiss()
                    }
                    .build()
                    .show()
            } ?: requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            activity?.let {
                MaterialDialog.Builder(it)
                    .setMessage(it.getString(R.string.importer_external_storage_permission_not_available))
                    .setCancelable(false)
                    .setPositiveButton(it.getString(android.R.string.ok)) { dialogInterface, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        dialogInterface.dismiss()
                    }
                    .build()
                    .show()
            } ?: requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
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