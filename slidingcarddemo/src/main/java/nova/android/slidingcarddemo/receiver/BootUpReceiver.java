
package nova.android.slidingcarddemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import nova.android.slidingcarddemo.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * ClassName: BootUpReceiver
 * Description: the boot up receiver is responsible for starting up the MainService.
 *
 * @author lipeng
 */
public class BootUpReceiver extends BroadcastReceiver {
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("boot up ", "onReceive: " + intent.getAction());
        if (intent.getAction().equals(action_boot)) {
            Intent i = new Intent(context, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}