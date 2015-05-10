package net.typeblog.git.fragments;

import android.app.Activity;

import java.util.List;

import net.typeblog.git.adapters.StringArrayAdapter;
import net.typeblog.git.support.GitProvider;

public class RemoteListFragment extends BaseListFragment<StringArrayAdapter, String>
{
	private GitProvider mProvider;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mProvider = (GitProvider) activity;
	}

	@Override
	protected StringArrayAdapter createAdapter() {
		return new StringArrayAdapter(mItemList);
	}

	@Override
	protected void onViewInflated() {
		
	}

	@Override
	protected void doLoad(List<String> list) {
		list.addAll(mProvider.git().getRepository().getRemoteNames());
	}

}
