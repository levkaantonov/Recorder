package levkaantonov.com.study.recorder.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BaseActivity {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var bottomNavItemClickListener: ((Int) -> Unit?)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHost.findNavController()
        NavigationUI.setupWithNavController(binding.bottomNv, navController)

        binding.bottomNv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.recorderFragment -> bottomNavItemClickListener?.invoke(0)
                R.id.listOfRecordsFragment -> bottomNavItemClickListener?.invoke(1)
            }
            true
        }
    }

    override fun registerBottomNavItemSelectedListener(listener: (position: Int) -> Unit) {
        bottomNavItemClickListener = listener
    }

    override fun setBottomNavItemChecked(position: Int) {
        binding.bottomNv.menu.getItem(position).isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavItemClickListener = null
    }
}