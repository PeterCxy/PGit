package net.typeblog.git.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ListView;

import android.support.v4.view.ViewCompat;
import android.support.v7.view.ActionMode;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.activities.ToolbarActivity;
import static net.typeblog.git.support.Utility.*;

public abstract class BaseListFragment<A extends BaseAdapter, I> extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
	protected ListView mList;
	protected List<I> mItemList = new ArrayList<>();
	protected A mAdapter;
	
	protected abstract A createAdapter();
	protected abstract void onViewInflated();
	protected abstract void doLoad(List<I> list);
	protected void onItemClick(int position) {};
	protected boolean shouldEnterActionMode(int pos) {
		return false;
	}
	protected int getActionModeMenu() {
		return 0;
	}
	protected boolean onActionModeItemSelected(int id) {
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list, container, false);
		
		mList = $(v, R.id.list);
		mList.setOnItemClickListener(this);
		mList.setOnItemLongClickListener(this);
		mAdapter = createAdapter();
		mList.setAdapter(mAdapter);
		
		onViewInflated();
		reload();
		
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> list, View v, int position, long id) {
		onItemClick(position);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> list, View v, final int position, long id) {
		
		if (!shouldEnterActionMode(position)) return false;
		
		((ToolbarActivity) getActivity()).startMyActionMode(new ActionMode.Callback() {

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					getActivity().getMenuInflater().inflate(getActionModeMenu(), menu);
					mList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
					mList.setItemChecked(position, true);
					mList.setOnItemClickListener(null);
					mList.setOnItemLongClickListener(null);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode p1, Menu p2) {
					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem menu) {
					if (onActionModeItemSelected(menu.getItemId())) {
						mode.finish();
						return true;
					} else {
						return false;
					}
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					mList.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
					mList.clearChoices();
					mList.setOnItemClickListener(BaseListFragment.this);
					mList.setOnItemLongClickListener(BaseListFragment.this);
					
					for (int i = 0; i < mList.getChildCount(); i++) {
						((Checkable) mList.getChildAt(i)).setChecked(false);
					}
				}
		});
		return true;
	}
	
	protected void reload() {
		new LoaderTask().execute();
	}
	
	private class LoaderTask extends AsyncTask<Void, Void, List<I>> {

		@Override
		protected List<I> doInBackground(Void... params) {
			List<I> ret = new ArrayList<>();
			doLoad(ret);
			return ret;
		}

		@Override
		protected void onPostExecute(List<I> result) {
			super.onPostExecute(result);
			mItemList.clear();
			mItemList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
