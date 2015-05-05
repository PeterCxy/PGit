package net.typeblog.git.activities;

import android.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentStatePagerAdapter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import net.typeblog.git.R;
import net.typeblog.git.fragments.CommitListFragment;
import net.typeblog.git.fragments.FileListFragment;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class RepoActivity extends ToolbarActivity implements GitProvider
{
	private Repository mRepo;
	private Git mGit;
	
	private Fragment[] mFragments = {
		new FileListFragment(),
		new CommitListFragment(),
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

}
