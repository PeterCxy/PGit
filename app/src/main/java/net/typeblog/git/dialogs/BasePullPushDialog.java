package net.typeblog.git.dialogs;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import net.typeblog.git.tasks.GitTask;
import static net.typeblog.git.support.Utility.*;

public abstract class BasePullPushDialog extends ToolbarDialog
{
	protected GitProvider mProvider;
	private String mRef;

	private List<String> mRemotes = new ArrayList<>();

	protected CheckBox mForce;
	private ListView mList;
	private TextView mUserName, mPassword;
	
	protected abstract void doTask(ProgressMonitor monitor, String remote, String ref, CredentialsProvider authorization, boolean force) throws GitAPIException, RuntimeException;

	public BasePullPushDialog(Context context, GitProvider provider, String ref) {
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
		new Task().execute();
	}

	private class Task extends GitTask<Void> {
		private String mRemote;
		private String mMessage;
		private String mName, mPass;
		private int mWorks = 0;
		
		public Task() {
			super(getContext(), mProvider);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRemote = mRemotes.get(mList.getCheckedItemPosition());
			mName = mUserName.getText().toString();
			mPass = mPassword.getText().toString();
			mMessage = getContext().getString(R.string.wait);
		}

		@Override
		protected void doGitTask(GitProvider provider, Void... params) throws GitAPIException, RuntimeException {
			doTask(new ProgressMonitor() {
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
			}, mRemote, mRef, new UsernamePasswordCredentialsProvider(mName, mPass), mForce.isChecked());
			/*try {
				
			} catch (GitAPIException e) {

			}*/
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismiss();
		}
	}

}
