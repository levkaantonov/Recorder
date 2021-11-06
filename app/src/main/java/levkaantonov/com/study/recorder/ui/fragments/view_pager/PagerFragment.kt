package levkaantonov.com.study.recorder.ui.fragments.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import levkaantonov.com.study.recorder.databinding.FragmentPagerBinding
import levkaantonov.com.study.recorder.ui.activities.BaseActivity
import levkaantonov.com.study.recorder.ui.fragments.list_of_records.ListOfRecordsFragment
import levkaantonov.com.study.recorder.ui.fragments.recorder.RecorderFragment
import levkaantonov.com.study.recorder.ui.utils.viewBinding

class PagerFragment : Fragment() {
    private val binding by viewBinding(FragmentPagerBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        with(binding) {
            val adapter = FragmentPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            adapter.set { RecorderFragment::class.java.newInstance() }
            adapter.set { ListOfRecordsFragment::class.java.newInstance() }
            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        (requireActivity() as BaseActivity)
                            .setBottomNavItemChecked(position)
                    }
                })
            (requireActivity() as BaseActivity).registerBottomNavItemSelectedListener { position ->
                viewPager.currentItem = position
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null
    }

    class FragmentPagerAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        private val builders: ArrayList<() -> Fragment> = ArrayList()

        override fun getItemCount(): Int = builders.size

        override fun createFragment(position: Int): Fragment {
            return builders[position].invoke()
        }

        fun set(builder: () -> Fragment) {
            builders.add(builder)
        }
    }
}