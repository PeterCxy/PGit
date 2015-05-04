package net.typeblog.git.activities;

import net.typeblog.git.R;
import net.typeblog.git.fragments.CommitListFragment;
import net.typeblog.git.fragments.FileListFragment;

public class RepoActivity extends ToolbarActivity
{
	private CommitListFragment mCommits;
	private FileListFragment mFiles;

	@Override
	protected int getLayoutResource() {
		return R.layout.repo;
	}

	@Override
	protected void onInitView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mFiles = new FileListFragment();
		mFiles.setArguments(getIntent().getExtras());
		getFragmentManager().beginTransaction().replace(R.id.frame, mFiles).commit();
	}

}
