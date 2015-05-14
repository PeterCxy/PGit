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
	
	private class RevertTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progress = new ProgressDialog(getContext());
			progress.setCancelable(false);
			progress.setMessage(getContext().getString(R.string.wait));
			progress.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mProvider.git().revert()
					.include(mCommit)
					.setOurCommitName(mMessage.getText().toString())
					.call();
			} catch (GitAPIException e) {
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progress.dismiss();
			dismiss();
		}

	}

}
