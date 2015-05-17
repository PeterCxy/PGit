package net.typeblog.git.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import net.typeblog.git.tasks.GitTask;
import static net.typeblog.git.support.Utility.*;

public class GitRevertDialog extends ToolbarDialog
{
	private static final String REVERT_MSG = "Revert '%s'\n\nThis reverts commit %s";
	
	private GitProvider mProvider;
	private AnyObjectId mCommit;
	private String mShortMsg;
	
	private EditText mMessage;
	
	public GitRevertDialog(Context context, GitProvider provider, AnyObjectId commit, String shortMsg) {
		super(context);
		mProvider = provider;
		mCommit = commit;
		mShortMsg = shortMsg;
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.revert;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.git_revert);
		
		mMessage = $(this, R.id.commit_message);
		mMessage.setText(String.format(REVERT_MSG, mShortMsg, ObjectId.toString(mCommit.toObjectId())));
	}

	@Override
	protected void onConfirm() {
		new RevertTask().execute();
	}
	
	private class RevertTask extends GitTask<Void> {
		
		public RevertTask() {
			super(getContext(), mProvider);
		}
		
		@Override
		protected void doGitTask(GitProvider provider, Void... params) throws GitAPIException, RuntimeException {
			RepoManager m = RepoManager.getInstance();
			String message = mMessage.getText().toString();
			String username = m.getCommitterName();
			String email = m.getCommitterEmail();
			
			provider.git().revert()
				.include(mCommit)
				.setOurCommitName(message)
				.call();
			
			// Dirty hack: Do not know how to set committer identity 
			provider.git().commit()
				.setAmend(true)
				.setMessage(message)
				.setCommitter(username, email)
				.setAuthor(username, email)
				.call();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismiss();
		}

	}

}
