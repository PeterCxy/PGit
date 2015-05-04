package net.typeblog.git.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import net.typeblog.git.R;
import net.typeblog.git.support.GlobalContext;
import static net.typeblog.git.support.Utility.*;

public class CommitAdapter extends BaseAdapter
{
	private List<RevCommit> mList;
	private LayoutInflater mInflater;
	
	public CommitAdapter(List<RevCommit> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= getCount()) return convertView;
		
		View v = convertView;
		
		if (v == null) {
			v = mInflater.inflate(R.layout.commit_item, parent, false);
		}
		
		TextView hash = $(v, R.id.hash);
		TextView message = $(v, R.id.message);
		TextView author = $(v, R.id.author);
		TextView date = $(v, R.id.date);
		
		RevCommit commit = mList.get(position);
		
		hash.setText(ObjectId.toString(commit.getId()));
		message.setText(commit.getShortMessage());
		author.setText(commit.getAuthorIdent().getName() + " <" + commit.getAuthorIdent().getEmailAddress() + ">");
		
		// Date
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ");
		format.setTimeZone(commit.getAuthorIdent().getTimeZone());
		Date commitDate = new Date();
		commitDate.setTime(commit.getCommitTime() * 1000l);
		date.setText(format.format(commitDate));
		
		return v;
	}
}
