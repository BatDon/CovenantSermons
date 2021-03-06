# CovenantSermons

<img src="app/src/main/res/drawable/github_portrait_list_play.jpg" width=220> |
<img src="app/src/main/res/drawable/github_portrait_list_delete.jpg" width=220> |
<img src="app/src/main/res/drawable/github_portrait_detail_menu.jpg" width=220>
<img src="app/src/main/res/drawable/github_landscape_detail.jpg" width=220> | |
| ---      | ---       |
|<img src="app/src/main/res/drawable/github_landscape_list.jpg" width=220> |

## Summary

This is a podcast app that allows you to play podcasts. To listen to a podcast just click the download button and once it is done downloading just click on it. You can close the app and listen to it in the background. You can then change the track in by swiping down and displaying the notification menu. This player allows for all standard controls play, pause, fast forward, rewind, skip and previous. If you are done listening to a podcast and want to save some space just swipe it to the side and it will be removed from the database.

## Libraries

- Exoplayer
- Lifecycle
- Koin
- Timber
- Constraint Layout
- Espresso
- Rxjava
- Firebase
- Multidex
- Glide
- Coroutines
- WorkManager
- Navigation
- AndroidX AppCompat
- RecyclerView

## Bugs

When app is closed and notification is clicked on. The app launches twice. It seems that the problem comes from the PlayerNotificationManager's onPlayerStateChanged being called twice. Please explore the testing_memory_leaks branch for help debugging. 

## Features to Add

App currently only allows users to download one podcast at a time. To implement this feature podcasts job needs to be queued in the WorkManager.
