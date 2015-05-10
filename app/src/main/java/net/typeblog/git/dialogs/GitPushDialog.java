package net.typeblog.git.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import static net.typeblog.git.support.Utility.*;

public class GitPushDialog extends ToolbarDialog
{
	private GitProvider mProvider;
	private String mRef;
	
	private List<String> mRemotes = new ArrayList<>();
	
	private CheckBox mForce;
	private ListView mList;
	private TextView mUserName, mPassword;
	
	public GitPushDialog(Context context, GitProvider provider, String ref) {
		super(context);
		mProvider = provider;
		mRef = ref;
	}
	
	@Override
	protected int getLayoutResource() {
		return R.layout.push;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.git_push);
		mForce = $(this, R.id.force);
		mList = $(this, R.id.remotes);
		mUserName = $(this, R.id.username);
		mPassword = $(this, R.id.password);
		
		String[] auth = RepoManager.getInstance().getAuthPass(mProvider.getLocation());
		if (auth != null && auth.length > 1) {
			mUserName.setText(auth[0]);
			mPassword.setText(auth[1]);
		}
		
		mRemotes.addAll(mProvider.git().getRepository().getRemoteNames());
		
		if (mRemotes.size() == 0) {
			Toast.makeText(getContext(), R.string.no_remotes, Toast.LENGTH_SHORT).show();
			mForce.post(new Runnable() {
				@Override
				public void run() {
					dismiss();
				}
			});
			return;
		}
		
		mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_with_radio, R.id.item_name, mRemotes));
		mList.setItemChecked(0, true);
	}

	@Override
	protected void onConfirm() {
		new PushTask().execute();
	}
	
	private class PushTask extends AsyncTask<Void, String, Void> {
		private ProgressDialog mProgress;
		private String mRemote;
		private String mMessage;
		private String mName, mPass;
		private int mWorks = 0;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRemote = mRemotes.get(mList.getCheckedItemPosition());
			mName = mUserName.getText().toString();
			mPass = mPassword.getText().toString();
			mProgress = new ProgressDialog(getContext());
			mProgress.setCancelable(false);
			mMessage = getContext().getString(R.string.wait);
			mProgress.setMessage(mMessage);
			mProgress.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				mProvider.git().push()
						.setRemote(mRemote)
						.add(mRef)
						.setPushTags()
						.setForce(mForce.isChecked())
						.setProgressMonitor(new ProgressMonitor() {
							@Override
							public void start(int p1) {
								
							}

							@Override
							public void beginTask(String name, int works) {
								mMessage = name;
								mWorks = works;
								publishProgress(name);
							}

							@Override
							public void update(int completed) {
								publishProgress(mMessage + " (" + completed + "/" + mWorks + ")");
							}

							@Override
							public void endTask() {
								
							}

							@Override
							public boolean isCancelled() {
								return false;
							}	
						})
						.call();
			} catch (GitAPIException e) {
				
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			mProgress.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			// Give the user enough time to read the last message
			mList.postDelayed(new Runnable() {
				@Override
				public void run() {
					mProgress.dismiss();
					dismiss();
				}
			}, 1000);
		}
	}
	
}
