package com.eseo.getmyspot.domain.repository

import com.eseo.getmyspot.BuildConfig
import com.eseo.getmyspot.data.models.AddSpotBodyParam
import com.eseo.getmyspot.data.models.ChangeProfilePictureBodyParam
import com.eseo.getmyspot.data.models.ConnectionAccountBodyParam
import com.eseo.getmyspot.data.models.CreateAccountBodyParam
import com.eseo.getmyspot.data.remote.RemoteDataSource

interface RemoteRepository {

    suspend fun createAccount(pseudo: String, password: String): Boolean
    suspend fun connectionToAccount(pseudo: String, password: String): Boolean
    suspend fun changeProfilePicture(pseudo: String, image: String): Boolean
    suspend fun addSpot(addSpotBodyParam: AddSpotBodyParam): Boolean

}

class RemoteRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RemoteRepository {

    /**
     * Création d'un compte utilisateur
     *
     * @param pseudo pseudonyme de l'utilisateur
     * @param password mot de passe de l'utilisateur
     */
    override suspend fun createAccount(pseudo: String, password: String): Boolean {
        val result = remoteDataSource.createAccount(CreateAccountBodyParam(pseudo, password))
        return result.error == 0 && result.user_pseudo == pseudo
    }

    /**
     * Connection à un compte utilisateur
     *
     * @param pseudo pseudonyme de l'utilisateur
     * @param password mot de passe de l'utilisateur
     */
    override suspend fun connectionToAccount(pseudo: String, password: String): Boolean {
        val result = remoteDataSource.connectionToAccount(ConnectionAccountBodyParam(pseudo, password))
        return result.error == 0 && result.user_pseudo == pseudo
    }

    override suspend fun addSpot(addSpotBodyParam: AddSpotBodyParam): Boolean {
        val result = remoteDataSource.addSpot(addSpotBodyParam)
        return result.error == 0
    }

    /**
     * Changement de photo de profil
     *
     * @param pseudo pseudonyme de l'utilisateur
     * @param image image du profil
     */
    override suspend fun changeProfilePicture(pseudo: String, image: String): Boolean {
        val result = remoteDataSource.changeProfilePicture(ChangeProfilePictureBodyParam(pseudo, image))
        return result.error == 0 && result.user_pseudo == pseudo
    }
}