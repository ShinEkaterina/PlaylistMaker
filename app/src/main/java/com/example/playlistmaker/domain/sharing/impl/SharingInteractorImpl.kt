
package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

const val LINK_TO_SHARE = "https://practicum.yandex.ru/profile/android-developer/"
const val SENDER_EMAIL = "yep4yep@gmail.com"
const val LINK_TERMS = "https://yandex.ru/legal/practicum_offer/"

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return (LINK_TO_SHARE)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = SENDER_EMAIL,
            subject = "VERY OMPORTANT EMAIL",
            text = "dughsuuguighiuhiu"
        )
    }

    private fun getTermsLink(): String {
        return LINK_TERMS
    }
}

