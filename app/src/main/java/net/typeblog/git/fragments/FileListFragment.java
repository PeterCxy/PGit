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

public class FileListFragment extends BaseListFragment<FileAdapter, File>
{
	private String mRepo, mCurrent;

	@Override
	protected FileAdapter createAdapter() {
		return new FileAdapter(mItemList);
	}

	@Override
	protected void onViewInflated() {
		
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
		File f = mItemList.get(position);
		
		if (!f.isDirectory() || f.getName().equals(".git")) return;
		
		if (f.getAbsolutePath().endsWith("..")) {
			mCurrent = new File(mCurrent).getParentFile().getAbsolutePath();
		} else {
			mCurrent = f.getAbsolutePath();
		}
		reload();
	}

	@Override
	protected void doLoad(List<File> list) {
		File[] files = new File(mCurrent).listFiles();
		list.addAll(Arrays.asList(files));
		Collections.sort(list, new FileComparator());

		if (!new File(mCurrent).equals(new File(mRepo))) {
			list.add(0, new File(mCurrent + "/.."));
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
