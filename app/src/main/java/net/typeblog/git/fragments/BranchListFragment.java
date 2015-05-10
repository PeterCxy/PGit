package net.typeblog.git.fragments;

import android.app.Activity;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.RefAdapter;
import net.typeblog.git.dialogs.GitPushDialog;
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
		if (id == R.id.push) {
			new GitPushDialog(getActivity(), mProvider, "refs/heads/master").show();
			return true;
		} else {
			return super.onActionModeItemSelected(id);
		}
	}

	@Override
	protected void doLoad(List<Ref> list) {
		try {
			list.addAll(mProvider.git().branchList().call());
		} catch (GitAPIException e) {
			
		}
	}

}
