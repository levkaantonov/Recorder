package levkaantonov.com.study.recorder.ui.activities

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationUI.setupWithNavController(
            binding.bottomNv,
            Navigation.findNavController(this, binding.navHostFragment.id)
        )
    }

    fun isServiceRunning(): Boolean {
        val manager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Int.MAX_VALUE).forEach {
            if (getString(R.string.recordServiceName) == it.service.className)
                return true
        }
        return false
    }
}