package uk.ac.tees.mad.dailywords.ui.data.repository

import android.net.Uri
import uk.ac.tees.mad.dailywords.ui.domain.UploadResult

interface SupabaseStorageRepository {
    suspend fun uploadProfilePicture(imageUri: Uri): UploadResult
}