package com.eseo.getmyspot.di

import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.domain.repository.RemoteRepositoryImpl
import com.eseo.getmyspot.view.account.signin.SignInViewModel
import com.eseo.getmyspot.view.account.signup.CreateAccountViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    // Inject dependencies for the MainViewModel (the only UI in this boilerplate)
    viewModel { CreateAccountViewModel(get()) }
    viewModel { SignInViewModel(get()) }

    // Sample Remote Data Repository
    single<RemoteRepository>(createdAtStart = true) { RemoteRepositoryImpl(get()) }
}

val moduleApp = listOf(appModule, remoteDataSourceModule)