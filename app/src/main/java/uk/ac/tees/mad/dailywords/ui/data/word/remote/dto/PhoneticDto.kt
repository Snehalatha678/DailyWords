package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

data class PhoneticDto(
    val audio: String?,
    val license: LicenseDto?,
    val sourceUrl: String?,
    val text: String?
)