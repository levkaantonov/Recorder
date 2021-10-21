package levkaantonov.com.study.recorder.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.*
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.data.RecordsRepository
import levkaantonov.com.study.recorder.data.RecordsRepositoryImpl
import levkaantonov.com.study.recorder.data.db.RecordDao
import levkaantonov.com.study.recorder.data.db.RecordsDb
import levkaantonov.com.study.recorder.services.RecordService
import levkaantonov.com.study.recorder.ui.fragments.list_of_records.ListOfRecordsFragment
import levkaantonov.com.study.recorder.ui.fragments.player.PlayerFragment
import levkaantonov.com.study.recorder.ui.fragments.recorder.RecorderFragment
import levkaantonov.com.study.recorder.ui.fragments.remove_dialog.RemoveRecordFragment
import levkaantonov.com.study.recorder.ui.fragments.remove_dialog.RemoveRecordViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(removeRecordViewModel: RemoveRecordViewModel)
    fun inject(playerFragment: PlayerFragment)
    fun inject(recordService: RecordService)
    fun inject(recorderFragment: RecorderFragment)
    fun inject(removeRecordFragment: RemoveRecordFragment)
    fun inject(listOfRecordsFragment: ListOfRecordsFragment)
}

@Module(includes = [DbModule::class, AppBindModule::class, SharedPrefsModule::class])
class AppModule

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideRecordDao(
        db: RecordsDb
    ): RecordDao {
        return db.recordDao
    }

    @Singleton
    @Provides
    fun provideRecordDb(
        application: Application
    ): RecordsDb {
        return RecordsDb.getInstance(application)
    }
}

@Module
interface AppBindModule {

    @Singleton
    @Binds
    fun bindRecordsRepositoryImplToRecordsRepository(
        recordsRepository: RecordsRepositoryImpl
    ): RecordsRepository
}

@Module
class SharedPrefsModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            application.getString(R.string.shared_prefs_name),
            Context.MODE_PRIVATE
        )
    }
}