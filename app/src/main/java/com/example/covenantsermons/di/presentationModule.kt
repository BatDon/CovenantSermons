package com.example.covenantsermons.di

//import com.example.covenantsermons.modelDatabase.SermonDatabase
import com.example.covenantsermons.ImageRepository
import com.example.covenantsermons.ImageViewModel
import com.example.covenantsermons.ImageViewModelFactory
import com.example.covenantsermons.MasterFragmentViewModel
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext


val presentationModule = module {
    factory<CoroutineContext> {
        SupervisorJob()
    }
    factory {
        Dispatchers.IO
    }
//    single<SermonDatabase> {
//        SermonDatabase()
//    }

    viewModel {
        PlayerViewModel(get(), get(), get(), get())
    }

//
//    viewModel {
//        PodcastListViewModel(get(), get(), get())
//    }
    viewModel {
        PodcastListViewModel()
    }
//
//    viewModel {
//        PodcastDetailsViewModel(get(), get(), get())
//    }
    viewModel{
        MasterFragmentViewModel()
    }

//    single {
//        ImageRepository()
//    }

    single {
        ImageRepository(repository: Repository) -> ImageRepository(repository)}
        factory<SplashContract.Presenter> { (view: SplashContract.View) -> SplashPresenter(view) }
    }

    single{
        ImageViewModelFactory(get())
    }

    viewModel{
        ImageViewModel(get())
    }
}