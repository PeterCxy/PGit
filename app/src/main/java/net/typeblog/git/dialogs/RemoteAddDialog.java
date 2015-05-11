package net.typeblog.git.dialogs;

import android.content.Context;
import android.widget.TextView;

import org.eclipse.jgit.lib.StoredConfig;

import java.io.IOException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class RemoteAddDialog extends ToolbarDialog
{
	private GitProvider mProvider;
	private TextView mName, mUrl;
	
	public RemoteAddDialog(Context context, GitProvider provider) {
		super(context);
		mProvider = provider;
	}
	
	@Override
	protected int getLayoutResource() {
		return R.layout.add_remote;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.add_remote);
		
		mName = $(this, R.id.remote_name);
		mUrl = $(this, R.id.remote_url);
	}

	@Override
	protected void onConfirm() {
		StoredConfig conf = mProvider.git().getRepository().getConfig();
		String name = mName.getText().toString().trim();
		
		if (name.equals("")) return;
		
		conf.setString("remote", name, "url", mUrl.getText().toString().trim());
		conf.setString("remote", name, "fetch", "+refs/heads/*:refs/remotes/" + name + "/*");
		try {
			conf.save();
		} catch (IOException e) {
			
		}
		dismiss();
	}
}
