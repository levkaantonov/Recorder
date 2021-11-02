package levkaantonov.com.study.recorder.di

import android.app.Application
import dagger.*
import levkaantonov.com.study.recorder.data.db.RecordDao
import levkaantonov.com.study.recorder.data.db.RecordsDb
import levkaantonov.com.study.recorder.data.db.RecordsRepository
import levkaantonov.com.study.recorder.data.db.RecordsRepositoryImpl
import levkaantonov.com.study.recorder.data.preferences.PreferencesDataStore
import levkaantonov.com.study.recorder.data.preferences.PreferencesRepository
import levkaantonov.com.study.recorder.data.preferences.PreferencesRepositoryImpl
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

    @Singleton
    @Binds
    fun bindPreferencesRepositoryImplToPreferencesRepository(
        repository: PreferencesRepositoryImpl
    ): PreferencesRepository
}

@Module
class SharedPrefsModule {

    @Singleton
    @Provides
    fun providePrefsDataStore(application: Application): PreferencesDataStore {
        return PreferencesDataStore(application)
    }
}