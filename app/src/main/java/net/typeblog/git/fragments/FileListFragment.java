package net.typeblog.git.fragments;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.typeblog.git.adapters.FileAdapter;

public class FileListFragment extends BaseListFragment<FileAdapter>
{
	private List<File> mList = new ArrayList<>();
	private String mRepo, mCurrent;

	@Override
	protected FileAdapter createAdapter() {
		return new FileAdapter(mList);
	}

	@Override
	protected void onViewInflated() {
		
	}

	@Override
	protected void reload() {
		new FetchTask().execute();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mRepo = getArguments().getString("location");
		mCurrent = mRepo;
	}

	@Override
	protected void onItemClick(int position) {
		super.onItemClick(position);
		File f = mList.get(position);
		
		if (!f.isDirectory()) return;
		
		if (f.getAbsolutePath().endsWith("..")) {
			mCurrent = new File(mCurrent).getParentFile().getAbsolutePath();
		} else {
			mCurrent = f.getAbsolutePath();
		}
		reload();
	}
	
	private class FetchTask extends AsyncTask<Void, Void, List<File>> {

		@Override
		protected List<File> doInBackground(Void... params) {
			List<File> ret = new ArrayList<>();
			File[] files = new File(mCurrent).listFiles();
			ret.addAll(Arrays.asList(files));
			Collections.sort(ret, new FileComparator());
			
			if (!new File(mCurrent).equals(new File(mRepo))) {
				ret.add(0, new File(mCurrent + "/.."));
			}
			
			return ret;
		}

		@Override
		protected void onPostExecute(List<File> result) {
			super.onPostExecute(result);
			mList.clear();
			mList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private class FileComparator implements Comparator<File> {

		@Override
		public int compare(File p1, File p2) {
			if (p1.isDirectory() && !p2.isDirectory()) {
				return -1;
			} else if (!p1.isDirectory() && p2.isDirectory()) {
				return 0;
			} else {
				return Collator.getInstance().compare(p1.getName(), p2.getName());
			}
		}
		
	}

}
