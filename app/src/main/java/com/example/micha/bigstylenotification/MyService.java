package com.example.micha.bigstylenotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


public class MyService extends Service implements AudioManager.OnAudioFocusChangeListener {

	private ComponentName remoteComponentName;
	private RemoteControlClient remoteControlClient;

	private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;

	AudioManager audioManager;

	// RemoteControlClient enables exposing information meant to be consumed by remote controls capable
	// of displaying metadata, artwork and media transport control buttons.
	// A remote control client object is associated with a media button event receiver.
	// This event receiver must have been previously registered with registerMediaButtonEventReceiver(ComponentName)
	// before the RemoteControlClient can be registered through registerRemoteControlClient(RemoteControlClient).

	// Here is an example of creating a RemoteControlClient instance after registering a media button event receiver:

	// ComponentName myEventReceiver = new ComponentName(getPackageName(), MyRemoteControlEventReceiver.class.getName());
	// AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	// myAudioManager.registerMediaButtonEventReceiver(myEventReceiver);

	// build the PendingIntent for the remote control client

	// Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
	// mediaButtonIntent.setComponent(myEventReceiver);
	// PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
	// create and register the remote control client
	// RemoteControlClient myRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
	// myAudioManager.registerRemoteControlClient(myRemoteControlClient);

	// https://developer.android.com/reference/android/media/RemoteControlClient.html

	private void RegisterRemoteClient(){
		remoteComponentName = new ComponentName(getApplicationContext(), new MyReceiver().ComponentName());
		try {
			if(remoteControlClient == null) {

				audioManager.registerMediaButtonEventReceiver(remoteComponentName);
				Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
				mediaButtonIntent.setComponent(remoteComponentName);
				PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
				remoteControlClient = new RemoteControlClient(mediaPendingIntent);
				audioManager.registerRemoteControlClient(remoteControlClient);

			}


		} catch (Exception ex){
		}
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {


		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        currentVersionSupportBigNotification = Util.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = Util.currentVersionSupportLockScreenControls();

		if(currentVersionSupportLockScreenControls){
			RegisterRemoteClient();
		}

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