package net.typeblog.git.fragments;

import android.app.Activity;

import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.Set;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;

public class GitStatusFragment extends BaseTextFragment
{
	private GitProvider mProvider;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mProvider = (GitProvider) activity;
	}

	@Override
	protected String doLoad() {
		StringBuilder sb = new StringBuilder();
		Status stat = null;
		
		try {
			stat = mProvider.git().status().call();
		} catch (GitAPIException e) {
			return null;
		}
		
		if (stat.isClean()) {
			sb.append(getString(R.string.status_nothing));
		} else {
			Set<String> toCommit = stat.getUncommittedChanges();
			if (toCommit.size() > 0) {
				sb.append(getString(R.string.status_changes_to_commit)).append("\n");
				addSetToBuilder(toCommit, sb);
			}
			
			Set<String> untracked = stat.getUntracked();
			if (untracked.size() > 0) {
				sb.append(getString(R.string.status_untracked)).append("\n");
				addSetToBuilder(untracked, sb);
			}
			
			Set<String> modified = stat.getModified();
			if (modified.size() > 0) {
				sb.append(getString(R.string.status_modified)).append("\n");
				addSetToBuilder(modified, sb);
			}
			
			Set<String> removed = stat.getRemoved();
			if (removed.size() > 0) {
				sb.append(getString(R.string.status_deleted)).append("\n");
				addSetToBuilder(removed, sb);
			}
			
			Set<String> conflicting = stat.getConflicting();
			if (conflicting.size() > 0) {
				sb.append(getString(R.string.status_conflicting)).append("\n");
				addSetToBuilder(conflicting, sb);
			}
			
		}
		
		return sb.toString();
	}
	
	private void addSetToBuilder(Set<String> set, StringBuilder builder) {
		for (String str : set) {
			builder.append("        ").append(str).append("\n");
		}
	}
}
