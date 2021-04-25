package com.batdon.covenantsermons.di

import com.batdon.covenantsermons.ImageViewModel
import com.batdon.covenantsermons.ImageViewModelFactory
import com.batdon.covenantsermons.MasterFragmentViewModel
import com.batdon.covenantsermons.player.PlayerViewModel
import com.batdon.covenantsermons.player.PodcastListViewModel
import com.batdon.covenantsermons.repository.AudioRepository
import com.batdon.covenantsermons.repository.ImageRepository
import com.batdon.covenantsermons.repository.SermonRepository
import com.batdon.covenantsermons.viewmodel.DownloadViewModel
import com.batdon.covenantsermons.viewmodel.SermonViewModel
import com.batdon.covenantsermons.viewmodel.SermonViewModelFactory
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
    single {
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
        ImageRepository(get())
    }

    single{
        AudioRepository(get())
    }

//    single {
//        ImageRepository(repository: Repository) -> ImageRepository(repository)}
//        factory<SplashContract.Presenter> { (view: SplashContract.View) -> SplashPresenter(view) }
//    }

    single{
        ImageViewModelFactory(get())
    }

    viewModel{
        ImageViewModel(get())
    }

    single{
        SermonViewModelFactory(get())
    }

    single{
        SermonViewModel(get<SermonRepository>())
    }

//    single{
//        DownloadViewModelFactory(get<WorkManager>())
//    }

//    viewModel{
//        DownloadViewModel(get())
//    }
//    viewModel{
//        DownloadViewModel(get())
//    }
    single { DownloadViewModel(get()) }

//    viewModel{
//        DownloadViewModel(get<WorkManager>())
//    }
}