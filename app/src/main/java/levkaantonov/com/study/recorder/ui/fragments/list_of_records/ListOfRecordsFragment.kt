package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import levkaantonov.com.study.recorder.databinding.FragmentListOfRecordsBinding
import levkaantonov.com.study.recorder.db.RecordsDb

class ListOfRecordsFragment : Fragment() {
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
        val adapter = ListOfRecordsAdapter()
        binding.rvRecords.adapter = adapter

        viewModel.records.observe(viewLifecycleOwner) {
            it?.let {
                adapter.data = it
            }
        }

        binding.lifecycleOwner = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}