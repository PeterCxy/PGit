package net.typeblog.git.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;

public abstract class GitTask<P> extends AsyncTask<P, String, String>
{
	private Context mContext;
	private GitProvider mProvider;
	private ProgressDialog mProgress;
	
	public GitTask(Context context, GitProvider provider) {
		mContext = context;
		mProvider = provider;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		mProgress = new ProgressDialog(mContext);
		mProgress.setCancelable(false);
		mProgress.setMessage(mContext.getString(R.string.wait));
		mProgress.show();
	}

	@Override
	protected String doInBackground(P... params) {
		try {
			doGitTask(mProvider, params);
		} catch (GitAPIException e) {
			return mContext.getString(R.string.error_git) + e.getMessage();
		} catch (RuntimeException e) {
			return mContext.getString(R.string.error_internal) + e.getMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgress.dismiss();
		
		if (result != null) {
			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		mProgress.setMessage(values[0]);
	}
	
	protected abstract void doGitTask(GitProvider provider, P... params) throws GitAPIException, RuntimeException;
}
