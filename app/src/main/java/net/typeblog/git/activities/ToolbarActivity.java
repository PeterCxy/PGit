package net.typeblog.git.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.typeblog.git.R;
import net.typeblog.git.widget.SlidingTabLayout;
import net.typeblog.git.widget.SlidingTabStrip;
import static net.typeblog.git.support.Utility.*;

public abstract class ToolbarActivity extends AppCompatActivity
{
	private Toolbar mToolbar;
	private SlidingTabLayout mTabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());
		
		// Find toolbar
		mToolbar = $(this, R.id.toolbar);
		mTabs = $(this, R.id.tabs);
		
		if (mToolbar == null)
			throw new IllegalStateException("No Toolbar");
		
		setSupportActionBar(mToolbar);
		
		if (Build.VERSION.SDK_INT >= 21) {
			findViewById(R.id.toolbar_wrapper).setElevation(15.6f);
		}
		
		onInitView();
	}
	
	protected void setupTabs(ViewPager pager) {
		mTabs.setVisibility(View.VISIBLE);
		mTabs.setViewPager(pager);
		
		final int color = getResources().getColor(R.color.white);
		mTabs.setCustomTabColorizer(new SlidingTabStrip.SimpleTabColorizer() {
			@Override
			public int getSelectedTitleColor(int position) {
				return color;
			}
			
			@Override
			public int getIndicatorColor(int position) {
				return color;
			}
		});
	}
	
	protected Toolbar getToolbar() {
		return mToolbar;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected abstract int getLayoutResource();
	protected abstract void onInitView();
}
