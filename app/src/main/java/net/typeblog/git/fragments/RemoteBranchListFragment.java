package net.typeblog.git.fragments;

import org.eclipse.jgit.api.ListBranchCommand;

import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.dialogs.RemoteCheckoutDialog;

public class RemoteBranchListFragment extends BranchListFragment
{

	@Override
	protected boolean shouldEnterActionMode(int pos) {
		return true;
	}
	
	@Override
	protected int getActionModeMenu() {
		return R.menu.action_mode_remote_branch;
	}

	@Override
	protected boolean onActionModeItemSelected(int id) {
		String name = mItemList.get(mList.getCheckedItemPosition()).getName();
		
		switch (id) {
			case R.id.checkout:
				new RemoteCheckoutDialog(getActivity(), mProvider, name).show();
				return true;
			default:
				return false;
		}
	}

	@Override
	protected boolean multiChoice() {
		return false;
	}

	@Override
	protected ListBranchCommand.ListMode getListMode() {
		return ListBranchCommand.ListMode.REMOTE;
	}
}
