package net.typeblog.git.support;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import android.support.v7.app.AppCompatDialog;

import net.typeblog.git.R;

public class Utility
{
	public static <T extends View> T $(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}
	
	public static <T extends View> T $(View v, int id) {
		return (T) v.findViewById(id);
	}
	
	public static <T extends View> T $(AppCompatDialog dialog, int id) {
		return (T) dialog.findViewById(id);
	}
	
	public static void copyToClipboard(String text) {
		ClipboardManager cm = (ClipboardManager) GlobalContext.get().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData cd = ClipData.newPlainText("primary", text);
		cm.setPrimaryClip(cd);
		Toast.makeText(GlobalContext.get(), R.string.copied, Toast.LENGTH_SHORT).show();
	}
	
	public static <T> void showConfirmDialog(Context context, String alert, final AsyncTask<T, ?, ?> task, final T[] params) {
		new AlertDialog.Builder(context)
			.setMessage(alert)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					task.execute(params);
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.show();
	}
}
