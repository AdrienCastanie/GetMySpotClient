package com.eseo.getmyspot.domain.repository

import com.eseo.getmyspot.BuildConfig
import com.eseo.getmyspot.data.models.CreateAccountBodyParam
import com.eseo.getmyspot.data.remote.RemoteDataSource
import java.lang.Exception

interface RemoteRepository {

    suspend fun createAccount(pseudo: String, password: String): Boolean

}

class RemoteRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RemoteRepository {

    /**
     * Création d'un compte utilisateur
     *
     * @param pseudo pseudonyme de l'utilisateur
     * @param password mot de passe de l'utilisateur
     */
    override suspend fun createAccount(pseudo: String, password: String): Boolean {
        System.out.println("QUENTIN 1 : " + BuildConfig.REMOTE_URI)
        val result = remoteDataSource.createAccount(CreateAccountBodyParam(pseudo, password))
        System.out.println("QUENTIN 2 : " + BuildConfig.REMOTE_URI)
        return result.error == 0 && result.user_pseudo == pseudo
    }

}