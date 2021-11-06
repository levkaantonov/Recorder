package levkaantonov.com.study.recorder.ui.activities

interface BaseActivity {
    fun setBottomNavItemChecked(position: Int)
    fun registerBottomNavItemSelectedListener(listener: (position: Int) -> Unit)
}
