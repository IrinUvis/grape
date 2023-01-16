package uvis.irin.grape.core.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    private const val RETRY_TIME_MILLIS = 5000L

    @Provides
    fun provideFirebaseStorage() = Firebase.storage.apply {
        maxDownloadRetryTimeMillis = RETRY_TIME_MILLIS
        maxUploadRetryTimeMillis = RETRY_TIME_MILLIS
        maxOperationRetryTimeMillis = RETRY_TIME_MILLIS
        maxChunkUploadRetry = RETRY_TIME_MILLIS
    }
}
