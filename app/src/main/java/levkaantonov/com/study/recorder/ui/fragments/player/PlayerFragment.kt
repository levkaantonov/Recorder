package levkaantonov.com.study.recorder.ui.fragments.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import levkaantonov.com.study.recorder.appComponent
import levkaantonov.com.study.recorder.databinding.FragmentPlayerBinding
import levkaantonov.com.study.recorder.ui.utils.args
import levkaantonov.com.study.recorder.ui.utils.viewBinding
import javax.inject.Inject

class PlayerFragment : DialogFragment() {
    @Inject
    lateinit var factory: PlayerViewModel.PlayerViewModelFactory.Factory
    private val viewModel: PlayerViewModel by viewModels { factory.create(itemPath) }
    private val binding by viewBinding(FragmentPlayerBinding::inflate)
    private val itemPath: String by args()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.player.observe(viewLifecycleOwner) { it ->
            binding.playerView.player = it
        }
    }

    companion object {
        private const val ARG_ITEM_PATH = "itemPath"

        fun newInstance(itemPath: String?): PlayerFragment {
            val f = PlayerFragment()
            f.arguments = bundleOf(ARG_ITEM_PATH to itemPath)
            return f
        }
    }
}