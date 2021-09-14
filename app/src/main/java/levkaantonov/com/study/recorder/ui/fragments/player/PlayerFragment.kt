package levkaantonov.com.study.recorder.ui.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import levkaantonov.com.study.recorder.databinding.FragmentPlayerBinding

class PlayerFragment : DialogFragment() {
    private var viewModel: PlayerViewModel? = null
    private var itemPath: String? = null

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPath = arguments?.getString(PlayerFragment.ARG_ITEM_PATH)
        itemPath?.let {
            viewModel = ViewModelProvider(
                this,
                PlayerViewModelFactory(it, requireActivity().application)
            ).get(PlayerViewModel::class.java)
        }
        viewModel?.apply {
            player.observe(viewLifecycleOwner) { player ->
                player?.let {
                    binding.playerView.player = player
                }

            }
        }

    }

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"

        fun newInstance(itemPath: String?): PlayerFragment {
            val f: PlayerFragment = PlayerFragment()
            f.arguments = bundleOf(Pair(ARG_ITEM_PATH, itemPath))
            return f
        }
    }
}