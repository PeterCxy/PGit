package net.typeblog.git.support;

import android.content.Context;

public class GlobalContext
{
	private static Context mContext;
	
	public static void set(Context context) {
		mContext = context.getApplicationContext();
	}
	
	public static Context get() {
		return mContext;
	}
}
