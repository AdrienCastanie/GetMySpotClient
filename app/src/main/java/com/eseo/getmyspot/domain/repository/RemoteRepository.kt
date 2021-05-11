package com.eseo.getmyspot.domain.repository

import com.eseo.getmyspot.data.remote.RemoteDataSource

interface RemoteRepository {

}

class RemoteRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RemoteRepository {

}