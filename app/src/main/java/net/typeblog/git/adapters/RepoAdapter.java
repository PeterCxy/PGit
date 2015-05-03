package net.typeblog.git.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GlobalContext;
import static net.typeblog.git.support.Utility.*;

public class RepoAdapter extends BaseAdapter
{
	private List<String> mRepoList;
	private List<String> mUrlList;
	private LayoutInflater mInflater;
	
	public RepoAdapter(List<String> repoList, List<String> urlList) {
		mRepoList = repoList;
		mUrlList = urlList;
		mInflater = (LayoutInflater) GlobalContext.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void notifyDataSetChanged() {
		
		if (mUrlList.size() != mRepoList.size()) {
			throw new IllegalStateException("Size not match");
		}
		
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mRepoList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mRepoList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= getCount()) return convertView;
		
		View v = convertView;
		
		if (v == null) {
			v = mInflater.inflate(R.layout.repo_item, parent, false);
		}
		
		TextView name = $(v, R.id.name);
		TextView url = $(v, R.id.url);
		
		name.setText(mRepoList.get(position));
		url.setText(mUrlList.get(position));
		
		if (url.getText().toString().trim().equals("")) {
			url.setText(GlobalContext.get().getString(R.string.nothing));
		}
		
		return v;
	}

}
