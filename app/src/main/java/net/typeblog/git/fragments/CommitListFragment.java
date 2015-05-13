package net.typeblog.git.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.CommitAdapter;
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
		switch (id) {
			case R.id.copy_commit_id:
				copyToClipboard(ObjectId.toString(mItemList.get(mList.getCheckedItemPosition()).getId()));
				return true;
			default:
				return super.onActionModeItemSelected(id);
		}
	}

	@Override
	protected void doLoad(List<RevCommit> list) {
		try {
			for (RevCommit commit : mProvider.git().log().all().call()) {

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
}
