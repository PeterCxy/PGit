package net.typeblog.git.support;

import android.app.Activity;
import android.view.View;

import android.support.v7.app.AppCompatDialog;

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
}
