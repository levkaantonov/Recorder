package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import levkaantonov.com.study.recorder.databinding.FragmentListOfRecordsBinding
import levkaantonov.com.study.recorder.db.RecordsDb
import levkaantonov.com.study.recorder.ui.fragments.player.PlayerFragment
import levkaantonov.com.study.recorder.ui.fragments.remove_dialog.RemoveRecordFragment
import java.io.File

class ListOfRecordsFragment : Fragment(), OnClickListener {
    private var _binding: FragmentListOfRecordsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfRecordsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = RecordsDb.getInstance(requireContext().applicationContext).recordDao
        val viewModel =
            ListOfRecordsViewModelFactory(dao).create(ListOfRecordsViewModel::class.java)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}