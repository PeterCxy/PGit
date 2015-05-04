package net.typeblog.git.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public abstract class BaseListFragment<T extends BaseAdapter> extends Fragment implements AdapterView.OnItemClickListener
{
	protected ListView mList;
	protected T mAdapter;
	
	protected abstract T createAdapter();
	protected abstract void onViewInflated();
	protected abstract void reload();
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
}
