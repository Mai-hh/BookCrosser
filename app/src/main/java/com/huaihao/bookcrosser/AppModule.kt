package com.huaihao.bookcrosser

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.repo.DriftingRepo
import com.huaihao.bookcrosser.repo.ReviewRepo
import com.huaihao.bookcrosser.repo.impl.AuthRepoImpl
import com.huaihao.bookcrosser.repo.impl.DriftingRepoImpl
import com.huaihao.bookcrosser.repo.impl.ReviewRepoImpl
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.service.LocationService
import com.huaihao.bookcrosser.viewmodel.auth.ForgetPasswordViewModel
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
import com.huaihao.bookcrosser.viewmodel.auth.SignUpViewModel
import com.huaihao.bookcrosser.viewmodel.main.MapViewModel
import com.huaihao.bookcrosser.viewmodel.main.MyReviewViewModel
import com.huaihao.bookcrosser.viewmodel.main.ProfileViewModel
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingViewModel
import com.huaihao.bookcrosser.viewmodel.main.ReviewSquareViewModel
import com.huaihao.bookcrosser.viewmodel.main.ReviewsViewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepo> { AuthRepoImpl() }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<ILocationService> { LocationService(get(), get()) }
    single<DriftingRepo> { DriftingRepoImpl() }
    single<ReviewRepo> { ReviewRepoImpl() }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ReviewsViewModel() }
    viewModel { SearchViewModel() }
    viewModel { MapViewModel(get()) }
    viewModel { ShelfABookViewModel(get()) }
    viewModel { RequestDriftingViewModel(get()) }
    viewModel { ReviewSquareViewModel(get()) }
    viewModel { MyReviewViewModel(get()) }
}