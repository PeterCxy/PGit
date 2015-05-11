package net.typeblog.git.fragments;

import android.app.Activity;

import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.RefAdapter;
import net.typeblog.git.dialogs.GitPushDialog;
import net.typeblog.git.dialogs.RemoteTrackDialog;
import net.typeblog.git.support.GitProvider;

public class BranchListFragment extends BaseListFragment<RefAdapter, Ref>
{
	private GitProvider mProvider;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mProvider = (GitProvider) activity;
	}

	@Override
	protected RefAdapter createAdapter() {
		return new RefAdapter(mItemList);
	}

	@Override
	protected void onViewInflated() {
		
	}

	@Override
	protected boolean shouldEnterActionMode(int pos) {
		return true;
	}

	@Override
	protected int getActionModeMenu() {
		return R.menu.action_mode_branch;
	}

	@Override
	protected boolean onActionModeItemSelected(int id) {
		String selected = mItemList.get(mList.getCheckedItemPosition()).getName();
		switch (id) {
			case R.id.push:
				new GitPushDialog(getActivity(), mProvider, selected).show();
				return true;
			case R.id.track_remote:
				new RemoteTrackDialog(getActivity(), mProvider, selected).show();
				return true;
			default:
				return super.onActionModeItemSelected(id);
		}
	}

	@Override
	protected boolean multiChoice() {
		return false;
	}

	@Override
	protected void doLoad(List<Ref> list) {
		try {
			list.addAll(mProvider.git().branchList().setListMode(getListMode()).call());
		} catch (GitAPIException e) {
			
		}
	}
	
	protected ListBranchCommand.ListMode getListMode() {
		return null;
	}

}
