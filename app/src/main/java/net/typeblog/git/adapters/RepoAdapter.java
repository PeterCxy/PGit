package net.typeblog.git.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GlobalContext;
import static net.typeblog.git.support.Utility.*;

public class RepoAdapter extends BaseListAdapter<String>
{
	private List<String> mUrlList;
	
	public RepoAdapter(List<String> repoList, List<String> urlList) {
		super(repoList);
		mUrlList = urlList;
	}

	@Override
	public void notifyDataSetChanged() {
		
		if (mUrlList.size() != mList.size()) {
			throw new IllegalStateException("Size not match");
		}
		
		super.notifyDataSetChanged();
	}

	@Override
	protected void bindView(View v, String item) {
		TextView name = $(v, R.id.name);
		TextView url = $(v, R.id.url);

		name.setText(item);
		url.setText(mUrlList.get(mList.indexOf(item)));

		if (url.getText().toString().trim().equals("")) {
			url.setText(GlobalContext.get().getString(R.string.nothing));
		}
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.repo_item;
	}

}
