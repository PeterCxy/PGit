package net.typeblog.git.activities;

import net.typeblog.git.R;
import net.typeblog.git.fragments.CommitListFragment;

public class RepoActivity extends ToolbarActivity
{
	private CommitListFragment mCommits;

	@Override
	protected int getLayoutResource() {
		return R.layout.repo;
	}

	@Override
	protected void onInitView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mCommits = new CommitListFragment();
		mCommits.setArguments(getIntent().getExtras());
		getFragmentManager().beginTransaction().replace(R.id.frame, mCommits).commit();
	}

}
