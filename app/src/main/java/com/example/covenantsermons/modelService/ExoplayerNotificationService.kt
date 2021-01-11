package com.example.covenantsermons.modelService

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import com.example.covenantsermons.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory


//TODO only swiping left or right should stop podcast audio service
//TODO include previous, rewind, play, pause, fast forward and next
//TODO service will be started with startService and then binding therefore need to call stopSelf or
//stopService() from client. In this case when notification is swiped left or right one of the two
//will need to be called.

//Service sends notification and notification has controls media controls user can use to change
//podcast
class ExoplayerNotificationService : Service() {



}