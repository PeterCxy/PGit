package net.typeblog.git.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public abstract class BaseListFragment<A extends BaseAdapter, I> extends Fragment implements AdapterView.OnItemClickListener
{
	protected ListView mList;
	protected List<I> mItemList = new ArrayList<>();
	protected A mAdapter;
	
	protected abstract A createAdapter();
	protected abstract void onViewInflated();
	protected abstract void doLoad(List<I> list);
	protected void onItemClick(int position) {};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list, container, false);
		
		mList = $(v, R.id.list);
		mList.setOnItemClickListener(this);
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
