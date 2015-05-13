package net.typeblog.git.support;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
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
}
