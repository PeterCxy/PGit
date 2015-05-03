package net.typeblog.git;

import android.app.Application;

import net.typeblog.git.support.GlobalContext;

public class PGitApplication extends Application
{

	@Override
	public void onCreate() {
		super.onCreate();
		GlobalContext.set(this);
	}
}
