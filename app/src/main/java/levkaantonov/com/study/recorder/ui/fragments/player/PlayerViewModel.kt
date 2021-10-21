package levkaantonov.com.study.recorder.ui.fragments.player

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PlayerViewModel @AssistedInject constructor(
    itemPath: String,
    application: Application
) :
    AndroidViewModel(application),
    LifecycleObserver {
    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> = _player

    private var contentPosition = 0L
    private var playWhenReady = true
    var itemPath: String? = itemPath

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded() {
        setUpPlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        releasePlayer()
    }

    private fun setUpPlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(
            getApplication(),
            Util.getUserAgent(getApplication(), "recorder")
        )

        val uri = Uri.parse(itemPath)
        val mediaItem = MediaItem.fromUri(uri)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        val player = SimpleExoPlayer.Builder(getApplication()).build()
        player.addMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)

        _player.value = player
    }

    private fun releasePlayer() {
        val player = _player.value ?: return
        _player.value = null
        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    class PlayerViewModelFactory @AssistedInject constructor(
        @Assisted("itemPath") private val itemPath: String,
        private val application: Application
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == PlayerViewModel::class.java)
            return PlayerViewModel(itemPath, application) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted("itemPath") itemPath: String): PlayerViewModelFactory
        }
    }
}
