package com.example.covenantsermons.di

//import com.example.covenantsermons.modelDatabase.SermonDatabase
//import com.example.covenantsermons.viewmodel.DownloadViewModelFactory
import com.example.covenantsermons.ImageViewModel
import com.example.covenantsermons.ImageViewModelFactory
import com.example.covenantsermons.MasterFragmentViewModel
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.repository.AudioRepository
import com.example.covenantsermons.repository.ImageRepository
import com.example.covenantsermons.repository.SermonRepository
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.example.covenantsermons.viewmodel.SermonViewModel
import com.example.covenantsermons.viewmodel.SermonViewModelFactory
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