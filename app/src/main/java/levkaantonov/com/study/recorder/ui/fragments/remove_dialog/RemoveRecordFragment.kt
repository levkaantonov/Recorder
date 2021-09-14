package levkaantonov.com.study.recorder.ui.fragments.remove_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.databinding.FragmentRemoveRecordBinding
import levkaantonov.com.study.recorder.db.RecordsDb
import levkaantonov.com.study.recorder.ui.fragments.player.PlayerFragment

class RemoveRecordFragment : DialogFragment() {
//    private var _binding: FragmentRemoveRecordBinding? = null
//    private val binding get() = checkNotNull(_binding)
    private var viewModel: RemoveRecordViewModel? = null

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentRemoveRecordBinding.inflate(layoutInflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireActivity().application
        val dao = RecordsDb.getInstance(application).recordDao

        viewModel = ViewModelProvider(
            this,
            RemoveRecordViewModelFactory(dao)
        ).get(RemoveRecordViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemPath = arguments?.getString(ARG_ITEM_PATH)
        val itemId = arguments?.getInt(ARG_ITEM_ID)
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.alert_dialog_delete_record_question))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                try {
                    itemPath?.let { viewModel?.removeFile(it) }
                    itemId?.let { viewModel?.removeItem(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()
    }


    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
        private const val ARG_ITEM_ID = "recording_item_id"

        fun newInstance(itemId: Int, itemPath: String?): RemoveRecordFragment {
            val f = RemoveRecordFragment()
            f.arguments = bundleOf(
                Pair(ARG_ITEM_PATH, itemPath),
                Pair(ARG_ITEM_ID, itemId)
            )
            return f
        }
    }
}