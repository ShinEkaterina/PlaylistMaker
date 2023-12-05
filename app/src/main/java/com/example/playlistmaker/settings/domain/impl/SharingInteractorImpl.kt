package com.example.playlistmaker.settings.domain.impl


import com.example.playlistmaker.settings.data.sharing.ExternalNavigator
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.example.playlistmaker.settings.domain.model.EmailData

const val LINK_TO_SHARE = "https://practicum.yandex.ru/profile/android-developer/"
const val SENDER_EMAIL = "123@gmail.com"
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

    override fun openSupport(subject:String,message:String) {
        externalNavigator.openEmail(
            EmailData(
                SENDER_EMAIL,
                subject,
                message
            )
        )
    }

    private fun getShareAppLink(): String {
        return (LINK_TO_SHARE)
    }


    private fun getTermsLink(): String {
        return LINK_TERMS
    }
}

