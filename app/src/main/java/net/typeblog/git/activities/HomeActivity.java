package net.typeblog.git.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.support.v7.widget.AppCompatEditText;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.adapters.RepoAdapter;
import net.typeblog.git.dialogs.ToolbarDialog;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import net.typeblog.git.tasks.GitTask;
import static net.typeblog.git.BuildConfig.DEBUG;
import static net.typeblog.git.support.Utility.*;

public class HomeActivity extends ToolbarActivity implements AdapterView.OnItemClickListener
{
	private static final String TAG = HomeActivity.class.getSimpleName();
	
	private List<String> mRepos = new ArrayList<>();
	private List<String> mRepoNames = new ArrayList<>();
	private List<String> mRepoUrls = new ArrayList<>();
	private RepoAdapter mAdapter;
	private ListView mList;
	private View mAdd;

	@Override
	protected int getLayoutResource() {
		return R.layout.main;
	}

	@Override
	protected void onInitView() {
		//new AddRepoDialog().show();
		mList = $(this, R.id.repo_list);
		mAdd = $(this, R.id.fab);
		mAdapter = new RepoAdapter(mRepoNames, mRepoUrls);
		mList.setOnItemClickListener(this);
		mList.setAdapter(mAdapter);
		reload();
		
		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AddRepoDialog().show();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
		String path = mRepos.get(pos);
		if (new File(path).isDirectory() && new File(path + "/.git").isDirectory()) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setClass(this, RepoActivity.class);
			i.putExtra("location", mRepos.get(pos));
			i.putExtra("name", mRepoNames.get(pos));
			startActivity(i);
		} else {
			showConfirmDialog(
				this,
				String.format(getString(R.string.clone_confirm), mRepoUrls.get(pos)),
				new CloneTask(path, mRepoUrls.get(pos)),
				RepoManager.getInstance().getAuthPass(path));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.committer:
				new CommitterDialog().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void reload() {
		mRepos.clear();
		mRepoNames.clear();
		mRepoUrls.clear();
		
		RepoManager manager = RepoManager.getInstance();
		
		String[] repos = manager.getRepoLocationList();
		mRepos.addAll(Arrays.asList(repos));
		
		for (String repo : mRepos) {
			
			repo = repo.trim();
			
			if (repo.equals("")) continue;
			
			if (DEBUG) {
				Log.d(TAG, "processing repo " + repo);
			}
			
			if (repo.lastIndexOf("/") == repo.length() - 1) {
				repo = repo.substring(0, repo.length() - 1);
			}
			
			mRepoNames.add(repo.substring(repo.lastIndexOf("/") + 1, repo.length()));
			mRepoUrls.add(manager.getUrl(repo));
		}
		
		mAdapter.notifyDataSetChanged();
	}
	
	private GitProvider buildGitProvider(final String repo) {
		Git g = null;

		try {
			g = new Git(new FileRepositoryBuilder()
						.setGitDir(new File(repo + "/.git"))
						.readEnvironment()
						.findGitDir()
						.build());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		final Git git = g;
		
		return new GitProvider() {
			@Override
			public Git git() {
				return git;
			}
			
			@Override
			public String getLocation() {
				return repo;
			}
		};
	}
	
	private class AddRepoDialog extends ToolbarDialog {
		private EditText location, url, username, password;
		
		AddRepoDialog() {
			super(HomeActivity.this);
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.add_repo;
		}

		@Override
		protected void onInitView() {
			setTitle(R.string.add_repo);
			
			location = $(this, R.id.local_path);
			url = $(this, R.id.clone_url);
			username = $(this, R.id.username);
			password = $(this, R.id.password);
		}

		@Override
		protected void onConfirm() {
			String location = this.location.getText().toString();
			
			String msg = RepoManager.checkRepo(location);
			if (msg != null) {
				Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
			} else {
				String url = this.url.getText().toString();
				String username = this.username.getText().toString().trim();
				String password = this.password.getText().toString().trim();
				
				RepoManager.getInstance().addRepo(location, url, username, password);
				
				dismiss();
				reload();
			}
		}
	}
	
	private class CommitterDialog extends ToolbarDialog {
		EditText name, email;
		
		CommitterDialog() {
			super(HomeActivity.this);
		}
		
		@Override
		protected int getLayoutResource() {
			return R.layout.set_committer;
		}

		@Override
		protected void onInitView() {
			setTitle(R.string.committer);
			
			name = $(this, R.id.committer_name);
			email = $(this, R.id.committer_email);
			
			name.setText(RepoManager.getInstance().getCommitterName());
			email.setText(RepoManager.getInstance().getCommitterEmail());
		}

		@Override
		protected void onConfirm() {
			RepoManager.getInstance().setCommitterIdentity(name.getText().toString().trim(), email.getText().toString().trim());
			dismiss();
		}
		
	}
	
	private class CloneTask extends GitTask<String> {
		String currentMsg = "";
		int currentWork = 0;
		int currentCompleted = 0;
		String url = "";
		
		CloneTask(String repo, String url) {
			super(HomeActivity.this, buildGitProvider(repo));
			this.url = url;
		}

		@Override
		protected void doGitTask(GitProvider provider, String... params) throws GitAPIException, RuntimeException {
			provider.git().cloneRepository()
				.setDirectory(new File(provider.getLocation()))
				.setURI(url)
				.setBare(false)
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider(params[0], params[1]))
				.setCloneSubmodules(true)
				.setCloneAllBranches(true)
				.setProgressMonitor(new ProgressMonitor() {
					@Override
					public void start(int p1) {
					}

					@Override
					public void beginTask(String msg, int work) {
						currentMsg = msg;
						currentWork = work;
						currentCompleted = 0;
						
						publishProgress(currentMsg + "(" + "0/" + work + ")");
					}

					@Override
					public void update(int completed) {
						currentCompleted += completed;
						publishProgress(currentMsg + "(" + currentCompleted + "/" + currentWork + ")");
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
		}
		
	}
}
