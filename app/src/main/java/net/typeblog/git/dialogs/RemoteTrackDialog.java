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
	private String mBranch;
	
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

		mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_with_radio, R.id.item_name, mRemotes));
		mList.setItemChecked(0, true);
	}

	@Override
	protected void onConfirm() {
		String remote = mRemotes.get(mList.getCheckedItemPosition());
		String shortName = mBranch.replace("refs/heads/", "");
		
		StoredConfig conf = mProvider.git().getRepository().getConfig();
		conf.setString("branch", shortName, "remote", remote);
		conf.setString("branch", shortName, "merge", mBranch);
		
		try {
			conf.save();
		} catch (IOException e) {
			
		}
		
		dismiss();
	}
	
}
