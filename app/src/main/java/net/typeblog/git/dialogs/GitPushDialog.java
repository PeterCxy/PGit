package net.typeblog.git.dialogs;

import android.content.Context;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import static net.typeblog.git.support.Utility.*;

public class GitPushDialog extends BasePullPushDialog
{
	public GitPushDialog(Context context, GitProvider provider, String ref) {
		super(context, provider, ref);
	}
	
	@Override
	protected void onInitView() {
		setTitle(R.string.git_push);
		super.onInitView();
	}

	@Override
	protected void doTask(ProgressMonitor monitor, String remote, String ref, CredentialsProvider authorization, boolean force) {
		try {
			mProvider.git().push()
				.setRemote(remote)
				.add(ref)
				.setCredentialsProvider(authorization)
				.setPushTags()
				.setForce(force)
				.setProgressMonitor(monitor)
				.call();
		} catch (GitAPIException e) {
			
		}
	}
	
}
