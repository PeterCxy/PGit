package net.typeblog.git.activities;

import android.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentStatePagerAdapter;

import net.typeblog.git.R;
import net.typeblog.git.fragments.CommitListFragment;
import net.typeblog.git.fragments.FileListFragment;
import static net.typeblog.git.support.Utility.*;

public class RepoActivity extends ToolbarActivity
{
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
		
	}

}
