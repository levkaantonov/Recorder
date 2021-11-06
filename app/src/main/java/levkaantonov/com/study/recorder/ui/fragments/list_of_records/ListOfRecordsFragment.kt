package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.Lazy
import levkaantonov.com.study.recorder.appComponent
import levkaantonov.com.study.recorder.databinding.FragmentListOfRecordsBinding
import levkaantonov.com.study.recorder.ui.fragments.list_of_records.ListOfRecordsViewModel.*
import levkaantonov.com.study.recorder.ui.fragments.player.PlayerFragment
import levkaantonov.com.study.recorder.ui.fragments.remove_dialog.RemoveRecordFragment
import levkaantonov.com.study.recorder.ui.utils.viewBinding
import java.io.File
import javax.inject.Inject

class ListOfRecordsFragment : Fragment(), OnClickListener {

    private lateinit var factory: Lazy<ListOfRecordsViewModelFactory>
    private val viewModel: ListOfRecordsViewModel by viewModels { factory.get() }
    private val binding by viewBinding(FragmentListOfRecordsBinding::inflate)

    @Inject
    fun injectFactory(factory: Lazy<ListOfRecordsViewModelFactory>) {
        this.factory = factory
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        val adapter = ListOfRecordsAdapter(this)
        binding.rvRecords.adapter = adapter

        viewModel.records.observe(viewLifecycleOwner) {
            it?.let {
                adapter.data = it
            }
        }
        binding.lifecycleOwner = this
    }

    override fun playRecord(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(requireContext(), "file not found", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val playerFragment = PlayerFragment.newInstance(filePath)
            val transaction = requireActivity()
                .supportFragmentManager
                .beginTransaction()
            playerFragment.show(transaction, "dialog_playback")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun removeItem(itemPath: String, recordId: Int) {
        try {
            val removeRecordFragment = RemoveRecordFragment.newInstance(recordId, itemPath)
            val transaction = requireActivity()
                .supportFragmentManager
                .beginTransaction()
            removeRecordFragment.show(transaction, "dialog_remove")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}