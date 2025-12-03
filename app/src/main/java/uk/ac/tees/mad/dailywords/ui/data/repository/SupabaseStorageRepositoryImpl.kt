package uk.ac.tees.mad.dailywords.ui.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.dailywords.ui.domain.UploadResult
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class SupabaseStorageRepositoryImpl(
    private val storage: Storage,
    private val context: Context,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : SupabaseStorageRepository {

    override suspend fun uploadProfilePicture(imageUri: Uri): UploadResult {
        val fileName = "${auth.currentUser?.uid}_${UUID.randomUUID()}"
        val localFile = saveImageToCache(imageUri, fileName)

        storage.from("satyam").upload(fileName, localFile.readBytes())
        val remoteUrl = storage.from("satyam").publicUrl(fileName)

        firestore.collection("users").document(auth.currentUser?.uid!!).update("profile_image_url", remoteUrl).await()

        return UploadResult(localUri = localFile.toUri(), remoteUrl = remoteUrl)
    }

    private fun saveImageToCache(uri: Uri, fileName: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}