package net.typeblog.git.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import net.typeblog.git.support.GlobalContext;

public abstract class BaseListAdapter<T> extends BaseAdapter
{
	protected abstract int getLayoutResource();
	protected abstract void bindView(View v, T item);
	
	protected List<T> mList;
	private LayoutInflater mInflater;
	
	public BaseListAdapter(List<T> list) {
		mList = list;
		mInflater = (LayoutInflater) GlobalContext.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if (position >= getCount()) return convertView;
		
		View v = convertView;
		if (v == null) {
			v = mInflater.inflate(getLayoutResource(), container, false);
		}
		
		bindView(v, mList.get(position));
		
		return v;
	}
}
