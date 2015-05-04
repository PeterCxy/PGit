package net.typeblog.git.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GlobalContext;
import static net.typeblog.git.support.Utility.*;

public class FileAdapter extends BaseAdapter
{
	private List<File> mList;
	private LayoutInflater mInflater;
	
	public FileAdapter(List<File> list) {
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
	public View getView(int pos, View convertView, ViewGroup container) {
		if (pos >= getCount()) return convertView;
		
		View v = convertView;
		
		if (v == null) {
			v = mInflater.inflate(R.layout.file_item, container, false);
		}
		
		TextView name = $(v, R.id.file_name);
		
		File f = mList.get(pos);
		
		name.setText(f.getName());
		name.setTextColor(GlobalContext.get().getResources().getColor(f.isDirectory() ? R.color.color_primary_dark : R.color.white));
		
		return v;
	}
}
