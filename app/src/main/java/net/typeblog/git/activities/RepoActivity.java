package net.typeblog.git.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentStatePagerAdapter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import net.typeblog.git.R;
import net.typeblog.git.dialogs.BranchCreateDialog;
import net.typeblog.git.dialogs.GitCommitDialog;
import net.typeblog.git.dialogs.GitPullDialog;
import net.typeblog.git.dialogs.RemoteAddDialog;
import net.typeblog.git.fragments.BranchListFragment;
import net.typeblog.git.fragments.CommitListFragment;
import net.typeblog.git.fragments.FileListFragment;
import net.typeblog.git.fragments.GitStatusFragment;
import net.typeblog.git.fragments.RemoteBranchListFragment;
import net.typeblog.git.fragments.RemoteListFragment;
import net.typeblog.git.fragments.TagListFragment;
import net.typeblog.git.tasks.GitTask;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class RepoActivity extends ToolbarActivity implements GitProvider
{
	private Repository mRepo;
	private Git mGit;
	
	private Fragment[] mFragments = {
		new FileListFragment(),
		new CommitListFragment(),
		new GitStatusFragment(),
		new BranchListFragment(),
		new TagListFragment(),
		new RemoteListFragment(),
		new RemoteBranchListFragment()
	};
	
	private ViewPager mPager;

	@Override
	protected int getLayoutResource() {
		return R.layout.repo;
	}

	@Override
	protected void onInitView() {
		
		try {
			mRepo = new FileRepositoryBuilder()
				.setGitDir(new File(getIntent().getStringExtra("location") + "/.git"))
				.readEnvironment()
				.findGitDir()
				.build();
		} catch (IOException e) {
			finish();
		}
		mGit = new Git(mRepo);
		
		// Pager
		mPager = $(this, R.id.pager);
		mPager.setOffscreenPageLimit(Integer.MAX_VALUE);
		
		for (Fragment f : mFragments) {
			f.setArguments(getIntent().getExtras());
		}
		
		final String[] tabs = getResources().getStringArray(R.array.repo_tabs);
		mPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
			@Override
			public int getCount() {
				return mFragments.length;
			}

			@Override
			public Fragment getItem(int pos) {
				return mFragments[pos];
			}
			
			@Override
			public String getPageTitle(int pos) {
				return tabs[pos];
			}
		});
		setupTabs(mPager);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
		
	}

	@Override
	public Git git() {
		return mGit;
	}

	@Override
	public String getLocation() {
		return getIntent().getStringExtra("location");
	}

	@Override
	public void onBackPressed() {
		int cur = mPager.getCurrentItem();
		if (mFragments[cur] instanceof FileListFragment) {
			FileListFragment f = (FileListFragment) mFragments[cur];
			
			if (f.canGoBack()) {
				f.goBack();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.repo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.commit:
				new GitCommitDialog(this, this).show();
				return true;
			case R.id.pull:
				new GitPullDialog(this, this).show();
				return true;
			case R.id.add_remote:
				new RemoteAddDialog(this, this).show();
				return true;
			case R.id.clean_all:
				// TODO Add a confirm dialog
				new CleanAllTask().execute();
				return true;
			case R.id.branch_create:
				new BranchCreateDialog(this, this).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private class CleanAllTask extends GitTask<Void> {
		
		public CleanAllTask() {
			super(RepoActivity.this, RepoActivity.this);
		}

		@Override
		protected void doGitTask(GitProvider provider, Void... params) throws GitAPIException, RuntimeException {
			mGit.reset().call();
			mGit.stashCreate().call();
			mGit.clean().call();
		}
	}

}
