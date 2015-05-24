package net.typeblog.git.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.typeblog.git.R;
import net.typeblog.git.adapters.CommitAdapter;
import net.typeblog.git.dialogs.GitRevertDialog;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;
import static net.typeblog.git.BuildConfig.DEBUG;

public class CommitListFragment extends BaseListFragment<CommitAdapter, RevCommit>
{
	private static final String TAG = CommitListFragment.class.getSimpleName();
	
	private GitProvider mProvider;
	
	@Override
	protected CommitAdapter createAdapter() {
		return new CommitAdapter(mItemList);
	}

	@Override
	protected void onViewInflated() {
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (!(activity instanceof GitProvider)) {
			throw new IllegalStateException("no provider");
		}
		
		mProvider = (GitProvider) activity;
	}

	@Override
	protected boolean shouldEnterActionMode(int pos) {
		return true;
	}

	@Override
	protected boolean multiChoice() {
		return false;
	}

	@Override
	protected int getActionModeMenu() {
		return R.menu.action_mode_commits;
	}

	@Override
	protected boolean onActionModeItemSelected(int id) {
		RevCommit commit = mItemList.get(mList.getCheckedItemPosition());
		String commitId = ObjectId.toString(commit.getId());
		String shortMessage = commit.getShortMessage();
		switch (id) {
			case R.id.copy_commit_id:
				copyToClipboard(commitId);
				return true;
			case R.id.revert:
				new GitRevertDialog(getActivity(), mProvider, commit, shortMessage).show();
				return true;
			case R.id.reset_to:
				showConfirmDialog(
					getActivity(),
					String.format(getString(R.string.reset_to_confirm), commitId),
					new ResetToTask(),
					new String[]{commitId});
				return true;
			default:
				return super.onActionModeItemSelected(id);
		}
	}

	@Override
	protected void doLoad(List<RevCommit> list) {
		try {
			RevWalk walk = new RevWalk(mProvider.git().getRepository());
			String branch = "refs/heads/" + mProvider.git().getRepository().getBranch();
			for (RevCommit commit : mProvider.git().log().all().call()) {

				// Trick: Pick out commits from this branch
				// Origin: http://stackoverflow.com/questions/15822544/jgit-how-to-get-all-commits-of-a-branch-without-changes-to-the-working-direct
				RevCommit targetCommit = walk.parseCommit(mProvider.git().getRepository().resolve(commit.getName()));
				boolean foundInThisBranch = false;
				for (Map.Entry<String, Ref> m : mProvider.git().getRepository().getAllRefs().entrySet()) {
					if (m.getKey().startsWith("refs/heads")) {
						if (walk.isMergedInto(targetCommit, walk.parseCommit(m.getValue().getObjectId()))) {
							String foundInBranch = m.getValue().getName();
							
							if (foundInBranch.equals(branch)) {
								foundInThisBranch = true;
							}
						}
					}
				}
				
				if (!foundInThisBranch) continue;
				
				if (DEBUG) {
					Log.d(TAG, "adding commit " + commit.getShortMessage());
				}

				list.add(commit);
			}
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} catch (GitAPIException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}
	
	private class ResetToTask extends AsyncTask<String, Void, Void> {
		private ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progress = new ProgressDialog(getActivity());
			progress.setCancelable(false);
			progress.setMessage(getString(R.string.wait));
			progress.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				mProvider.git().reset()
					.setRef(params[0])
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
