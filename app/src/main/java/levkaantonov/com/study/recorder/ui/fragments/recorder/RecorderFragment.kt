package levkaantonov.com.study.recorder.ui.fragments.recorder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.Lazy
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.appComponent
import levkaantonov.com.study.recorder.databinding.FragmentRecorderBinding
import levkaantonov.com.study.recorder.services.RecordService
import levkaantonov.com.study.recorder.ui.utils.isServiceRunning
import levkaantonov.com.study.recorder.ui.utils.viewBinding
import java.io.File
import javax.inject.Inject


class RecorderFragment : Fragment() {
    @Inject
    lateinit var recorderViewModelFactory: Lazy<RecorderViewModel.RecorderViewModelFactory>
    private val viewModel: RecorderViewModel by viewModels { recorderViewModelFactory.get() }
    private val binding: FragmentRecorderBinding by viewBinding(FragmentRecorderBinding::inflate)

    private var count: Int? = null

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { requestPermissionCallback(it) }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
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

        subscribeUi()

        if (!requireActivity().isServiceRunning()) {
            viewModel.resetTimer()
        } else {
            binding.recordingFab.setImageResource(R.drawable.ic_media_stop)
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
    }

    private fun subscribeUi() {
        viewModel.countOfRecords.observe(viewLifecycleOwner) { count ->
            this.count = count
        }
        viewModel.elapsedTime.observe(viewLifecycleOwner) { elapsedTime ->
            binding.tvTimer.text = elapsedTime
        }
        binding.recordingFab.setOnClickListener {
            val permissionRecordAudio = android.Manifest.permission.RECORD_AUDIO
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permissionRecordAudio
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(permissionRecordAudio)
            } else {
                if (requireActivity().isServiceRunning()) {
                    onRecord(false)
                    viewModel.stopTimer()
                } else {
                    onRecord(true)
                    viewModel.startTimer()
                }
            }
        }
    }

    private fun onRecord(start: Boolean) {
        val serviceIntent = Intent(
            requireActivity(),
            RecordService::class.java
        )

        if (!start) {
            binding.recordingFab.setImageResource(R.drawable.ic_mic)
            requireActivity().apply {
                stopService(serviceIntent)
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            return
        }
        binding.recordingFab.setImageResource(R.drawable.ic_media_stop)
        showToast(getString(R.string.recording_started))
        val folder = File(
            requireActivity()
                .getExternalFilesDir(null)
                ?.absolutePath
                .toString() +
                    getString(R.string.app_folder_name)
        )
        if (!folder.exists()) {
            folder.mkdir()
        }
        requireActivity().apply {
            startService(serviceIntent)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

    }

    private fun requestPermissionCallback(granted: Boolean) {
        if (granted) {
            onRecord(true)
            viewModel.startTimer()
        } else {
            showToast(getString(R.string.toast_Permissions_not_granted))
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.apply {
                setShowBadge(false)
                setSound(null, null)
            }

            val manager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }
}