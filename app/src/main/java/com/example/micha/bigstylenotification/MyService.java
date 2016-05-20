package com.example.micha.bigstylenotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


public class MyService extends Service implements AudioManager.OnAudioFocusChangeListener{


    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {


        currentVersionSupportBigNotification = Util.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = Util.currentVersionSupportLockScreenControls();

		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

			newNotification();

		return START_STICKY;
	}

	@SuppressLint("NewApi")
	private void newNotification() {

		Log.v("TAG","biulding notificatino");

		String songName = "Song Name";
		String albumName = "Album Name";
		RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.custom_notification);
		RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);

		Notification notification = new NotificationCompat.Builder(getApplicationContext())
        .setSmallIcon(R.drawable.ic_music)
        .setContentTitle(songName).build();

		//setListeners(simpleContentView);
		//setListeners(expandedView);

		notification.contentView = simpleContentView;
		if(currentVersionSupportBigNotification){
			notification.bigContentView = expandedView;
		}

		try{
			//long albumId = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumId();
			//Bitmap albumArt = UtilFunctions.getAlbumart(getApplicationContext(), albumId);
            Bitmap albumArt = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.default_album_art);
			if(albumArt != null){
				notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
				if(currentVersionSupportBigNotification){
					notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
				}
			}else{
				notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
				if(currentVersionSupportBigNotification){
					notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

        /**
		if(PlayerConstants.SONG_PAUSED){
			notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
			notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

			if(currentVersionSupportBigNotification){
				notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
				notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
			}
		}else{
			notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
			notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

			if(currentVersionSupportBigNotification){
				notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
				notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
			}
		}**/

		notification.contentView.setTextViewText(R.id.textSongName, songName);
		notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
		if(currentVersionSupportBigNotification){
			notification.bigContentView.setTextViewText(R.id.textSongName, songName);
			notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
		}
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		startForeground(1111, notification);
	}

	/**
	 * Notification click listeners
	 * @param view
	 */

    /**
	public void setListeners(RemoteViews view) {
		Intent previous = new Intent(NOTIFY_PREVIOUS);
		Intent delete = new Intent(NOTIFY_DELETE);
		Intent pause = new Intent(NOTIFY_PAUSE);
		Intent next = new Intent(NOTIFY_NEXT);
		Intent play = new Intent(NOTIFY_PLAY);

		PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

		PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

		PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPause, pPause);

		PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnNext, pNext);

		PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

	}**/

	@Override
	public void onDestroy() {


		super.onDestroy();
	}



	@Override
	public void onAudioFocusChange(int focusChange) {}
}