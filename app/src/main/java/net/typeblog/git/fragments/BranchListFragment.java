package net.typeblog.git.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.RefAdapter;
import net.typeblog.git.dialogs.GitPushDialog;
import net.typeblog.git.dialogs.RemoteTrackDialog;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

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
			case R.id.checkout:
				showConfirmDialog(
					getActivity(),
					String.format(getString(R.string.checkout_confirm), selected),
					new CheckoutTask(),
					new String[]{selected});
				return true;
			case R.id.delete:
				showConfirmDialog(
					getActivity(),
					String.format(getString(R.string.delete_confirm), selected),
					new DeleteTask(),
					new String[]{selected});
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
	
	private class CheckoutTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progress = new ProgressDialog(getActivity());
			progress.setMessage(getString(R.string.wait));
			progress.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				mProvider.git().checkout()
					.setName(params[0])
					.call();
			} catch (GitAPIException e) {
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progress.dismiss();
		}

	}
	
	private class DeleteTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progress = new ProgressDialog(getActivity());
			progress.setMessage(getString(R.string.wait));
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				mProvider.git().branchDelete()
					.setBranchNames(params[0])
					.call();
			} catch (GitAPIException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			progress.dismiss();
		}
	}

}
