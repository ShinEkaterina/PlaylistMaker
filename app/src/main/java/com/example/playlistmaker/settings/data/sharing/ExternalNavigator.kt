package com.example.playlistmaker.settings.data.sharing

import com.example.playlistmaker.settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)

    fun openEmail(emailData: EmailData)
}