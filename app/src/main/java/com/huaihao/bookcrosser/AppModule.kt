package com.huaihao.bookcrosser

import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.repo.impl.AuthRepoImpl
import com.huaihao.bookcrosser.viewmodel.auth.ForgetPasswordViewModel
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
import com.huaihao.bookcrosser.viewmodel.auth.SignUpViewModel
import com.huaihao.bookcrosser.viewmodel.main.ProfileViewModel
import com.huaihao.bookcrosser.viewmodel.main.RequestsViewModel
import com.huaihao.bookcrosser.viewmodel.main.ReviewsViewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepo> { AuthRepoImpl() }
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { RequestsViewModel() }
    viewModel { ReviewsViewModel() }
    viewModel { SearchViewModel() }
}