package com.example.covenantsermons.di

//import com.example.covenantsermons.modelDatabase.SermonDatabase
import com.example.covenantsermons.player.PodcastListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

/**
 * Created by Abdurahman Adilovic on 3/28/20.
 */

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

//    viewModel {
//        PlayerViewModel(get(), get(), get(), get())
//    }
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
}