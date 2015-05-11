package net.typeblog.git.fragments;

import org.eclipse.jgit.api.ListBranchCommand;

import java.util.List;

public class RemoteBranchListFragment extends BranchListFragment
{

	@Override
	protected boolean shouldEnterActionMode(int pos) {
		// TODO: Do not return false. "Checkout remote branch" needs implementation.
		return false;
	}
	
	@Override
	protected int getActionModeMenu() {
		return 0;
	}

	@Override
	protected ListBranchCommand.ListMode getListMode() {
		return ListBranchCommand.ListMode.REMOTE;
	}
}
