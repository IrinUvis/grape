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

    @Provides
    fun provideFirebaseStorage() = Firebase.storage.apply {
        maxDownloadRetryTimeMillis = 5000
        maxUploadRetryTimeMillis = 5000
        maxOperationRetryTimeMillis = 5000
        maxChunkUploadRetry = 5000
    }
}