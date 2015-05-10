package net.typeblog.git.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseBooleanArray;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.FileAdapter;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.BuildConfig.DEBUG;

public class FileListFragment extends BaseListFragment<FileAdapter, File>
{
	private static final String TAG = FileListFragment.class.getSimpleName();
	
	private String mRepo, mCurrent;
	private GitProvider mProvider;

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
		mProvider = (GitProvider) activity;
	}

	@Override
	protected void onItemClick(int position) {
		super.onItemClick(position);
		File f = mItemList.get(position);
		
		if (!f.isDirectory() || f.getName().equals(".git")) return;
		
		if (f.getAbsolutePath().endsWith("..")) {
			goBack();
		} else {
			mCurrent = f.getAbsolutePath();
			reload();
		}
	}

	@Override
	protected boolean shouldEnterActionMode(int pos) {
		return true;
	}

	@Override
	protected int getActionModeMenu() {
		return R.menu.action_mode_file;
	}

	@Override
	protected boolean onActionModeItemSelected(int id) {
		if (id == R.id.git_add) {
			AddCommand add = mProvider.git().add();
			SparseBooleanArray items = mList.getCheckedItemPositions();
			for (int i = 0; i < items.size(); i++) {
				int item = items.keyAt(i);
				boolean checked = items.valueAt(i);
				if (checked) {
					String pattern = mItemList.get(item).getPath().replace(mRepo, "");
					
					if (pattern.startsWith("/"))
						pattern = pattern.replaceFirst("/", "");
					
					add.addFilepattern(pattern);
					
					if (DEBUG) {
						Log.d(TAG, pattern);
					}
				}
			}
			
			try {
				add.call();
			} catch (GitAPIException e) {
				
			}
			
			return true;
		} else {
			return super.onActionModeItemSelected(id);
		}
	}

	@Override
	protected void doLoad(List<File> list) {
		File[] files = new File(mCurrent).listFiles();
		list.addAll(Arrays.asList(files));
		Collections.sort(list, new FileComparator());

		if (canGoBack()) {
			list.add(0, new File(mCurrent + "/.."));
		}
	}
	
	public void goBack() {
		mCurrent = new File(mCurrent).getParentFile().getAbsolutePath();
		reload();
	}
	
	public boolean canGoBack() {
		return !new File(mCurrent).equals(new File(mRepo));
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
