package net.typeblog.git.fragments;

import android.app.Activity;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

import net.typeblog.git.adapters.RefAdapter;
import net.typeblog.git.support.GitProvider;

public class TagListFragment extends BaseListFragment<RefAdapter, Ref>
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
	protected void doLoad(List<Ref> list) {
		try {
			list.addAll(mProvider.git().tagList().call());
		} catch (GitAPIException e) {

		}
	}

}
