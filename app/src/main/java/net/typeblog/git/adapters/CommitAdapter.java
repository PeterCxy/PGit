package net.typeblog.git.adapters;

import android.view.View;
import android.view.ViewGroup;
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

public class CommitAdapter extends BaseListAdapter<RevCommit>
{
	public CommitAdapter(List<RevCommit> list) {
		super(list);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.commit_item;
	}

	@Override
	protected void bindView(View v, RevCommit commit) {
		TextView hash = $(v, R.id.hash);
		TextView message = $(v, R.id.message);
		TextView author = $(v, R.id.author);
		TextView date = $(v, R.id.date);

		hash.setText(ObjectId.toString(commit.getId()));
		message.setText(commit.getShortMessage());
		author.setText(commit.getAuthorIdent().getName() + " <" + commit.getAuthorIdent().getEmailAddress() + ">");

		// Date
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ");
		format.setTimeZone(commit.getAuthorIdent().getTimeZone());
		Date commitDate = new Date();
		commitDate.setTime(commit.getCommitTime() * 1000l);
		date.setText(format.format(commitDate));
		
	}
}
