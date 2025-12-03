package uk.ac.tees.mad.dailywords.ui.domain

import android.net.Uri

data class UploadResult(
    val localUri: Uri,
    val remoteUrl: String
)
