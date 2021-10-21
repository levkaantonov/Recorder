package levkaantonov.com.study.recorder.ui.fragments.remove_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.Lazy
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.appComponent
import levkaantonov.com.study.recorder.ui.utils.args
import javax.inject.Inject

class RemoveRecordFragment : DialogFragment() {
    @Inject
    lateinit var factory: Lazy<RemoveRecordViewModel.RemoveRecordViewModelFactory>
    private val viewModel: RemoveRecordViewModel by viewModels { factory.get() }
    private val itemPath: String by args()
    private val itemId: Int by args()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requireContext().appComponent.inject(viewModel)
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.alert_dialog_delete_record_question))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                try {
                    viewModel.removeFile(itemPath)
                    viewModel.removeItem(itemId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    companion object {
        private const val ARG_ITEM_PATH = "itemPath"
        private const val ARG_ITEM_ID = "itemId"

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