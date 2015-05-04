package net.typeblog.git.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.adapters.CommitAdapter;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.BuildConfig.DEBUG;

public class CommitListFragment extends BaseListFragment<CommitAdapter>
{
	private static final String TAG = CommitListFragment.class.getSimpleName();
	
	private List<RevCommit> mList = new ArrayList<>();
	private GitProvider mProvider;
	
	@Override
	protected CommitAdapter createAdapter() {
		return new CommitAdapter(mList);
	}

	@Override
	protected void onViewInflated() {
		
	}

	@Override
	protected void reload() {
		
		if (DEBUG) {
			Log.d(TAG, getArguments().getString("location"));
		}
		
		new LoadTask().execute();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (!(activity instanceof GitProvider)) {
			throw new IllegalStateException("no provider");
		}
		
		mProvider = (GitProvider) activity;
	}
	
	private class LoadTask extends AsyncTask<Void, Void, List<RevCommit>> {

		@Override
		protected List<RevCommit> doInBackground(Void... params) {
			try {
				List<RevCommit> ret = new ArrayList<>();
				for (RevCommit commit : mProvider.git().log().all().call()) {
					
					if (DEBUG) {
						Log.d(TAG, "adding commit " + commit.getShortMessage());
					}
					
					ret.add(commit);
				}
				return ret;
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			} catch (GitAPIException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<RevCommit> result) {
			super.onPostExecute(result);
			
			if (result == null) return;
			
			mList.clear();
			mList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
