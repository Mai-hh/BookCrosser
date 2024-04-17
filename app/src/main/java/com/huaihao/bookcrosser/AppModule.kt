package com.huaihao.bookcrosser

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.repo.impl.AuthRepoImpl
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.service.LocationService
import com.huaihao.bookcrosser.viewmodel.auth.ForgetPasswordViewModel
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
import com.huaihao.bookcrosser.viewmodel.auth.SignUpViewModel
import com.huaihao.bookcrosser.viewmodel.main.MapViewModel
import com.huaihao.bookcrosser.viewmodel.main.ProfileViewModel
import com.huaihao.bookcrosser.viewmodel.main.RequestsViewModel
import com.huaihao.bookcrosser.viewmodel.main.ReviewsViewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepo> { AuthRepoImpl() }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<ILocationService> { LocationService(get(), get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { RequestsViewModel() }
    viewModel { ReviewsViewModel() }
    viewModel { SearchViewModel() }
    viewModel { MapViewModel(get()) }
}