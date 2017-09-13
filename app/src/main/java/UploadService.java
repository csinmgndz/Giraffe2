import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.sinem.giraffe.BaseTaskService;
import com.example.sinem.giraffe.R;
import com.example.sinem.giraffe.SignUpRegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by sinem on 12.9.2017.
 */

public class UploadService extends BaseTaskService {

    private static final String TAG = "UploadService";

    private static final int NOTIF_ID_DOWNLOAD = 165;

    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_uri";

    private StorageReference storageReference;

    @Override
    public void onCreate() {
        super.onCreate();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return super.onBind(ıntent);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        if(ACTION_UPLOAD.equals(intent.getAction()))
        {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            uploadFromUri(fileUri);
        }

        return START_REDELIVER_INTENT;
    }

    private void uploadFromUri(final Uri fileUri)
    {
        taskStarted();
        showUploadProgressNotification();

        final StorageReference photoRef = storageReference.child("photos")
                .child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                        broadcastUploadFinished(downloadUrl,fileUri);
                        showUploadFinishedNotification(downloadUrl,fileUri);
                        taskCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {

                        broadcastUploadFinished(null,fileUri);
                        showUploadFinishedNotification(null,fileUri);
                        taskCompleted();
                    }
                });
    }


    private boolean broadcastUploadFinished(@Nullable Uri downloadURL,@Nullable Uri fileUri)
    {
        boolean success = downloadURL != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL,downloadURL)
                .putExtra(EXTRA_FILE_URI,fileUri);

        return LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

    }


    private void showUploadProgressNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("Firebase Database")
                .setContentText("Uploading...")
                .setProgress(0,0,true)
                .setOngoing(true)
                .setAutoCancel(false);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD,builder.build());
    }

    private void showUploadFinishedNotification(@Nullable Uri downloadUri,@Nullable Uri fileUri)
    {
        Intent intent = new Intent(this, SignUpRegisterActivity.class);
        intent.putExtra(EXTRA_DOWNLOAD_URL,downloadUri);
        intent.putExtra(EXTRA_FILE_URI,fileUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        boolean success = downloadUri != null;

        String message = success ? "Upload finished" : "Upload failed";


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);



        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD,builder.build());

    }

    public static IntentFilter getIntentFilter()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);
        return filter;
    }

}
