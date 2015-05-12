package net.typeblog.git.dialogs;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.jgit.lib.StoredConfig;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class RemoteTrackDialog extends ToolbarDialog
{
	private List<String> mRemotes = new ArrayList<>();
	private GitProvider mProvider;
	private ListView mList;
	private String mBranch, mShortBranch;
	
	public RemoteTrackDialog(Context context, GitProvider provider, String branch) {
		super(context);
		mProvider = provider;
		mBranch = branch;
	}
	
	@Override
	protected int getLayoutResource() {
		return R.layout.track_remote;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.track_remote);
		
		mList = $(this, R.id.remotes);

		mRemotes.addAll(mProvider.git().getRepository().getRemoteNames());
		mRemotes.add(0, getContext().getString(R.string.none));

		if (mRemotes.size() == 0) {
			Toast.makeText(getContext(), R.string.no_remotes, Toast.LENGTH_SHORT).show();
			mList.post(new Runnable() {
				@Override
				public void run() {
					dismiss();
				}
			});
			return;
		}
		
		// Load tracking branch
		mShortBranch = mBranch.replace("refs/heads/", "");
		StoredConfig conf = mProvider.git().getRepository().getConfig();
		String trakcing = conf.getString("branch", mShortBranch, "remote");
		int index = mRemotes.indexOf(trakcing);
		if (index < 0) index = 0;

		mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_with_radio, R.id.item_name, mRemotes));
		mList.setItemChecked(index, true);
	}

	@Override
	protected void onConfirm() {
		int pos = mList.getCheckedItemPosition();
		
		StoredConfig conf = mProvider.git().getRepository().getConfig();
		
		if (pos == 0) {
			conf.unset("branch", mShortBranch, "remote");
			conf.unset("branch", mShortBranch, "merge");
		} else {
			String remote = mRemotes.get(pos);
			
			conf.setString("branch", mShortBranch, "remote", remote);
			conf.setString("branch", mShortBranch, "merge", mBranch);
		}
		
		try {
			conf.save();
		} catch (IOException e) {

		}
		
		dismiss();
	}
	
}
