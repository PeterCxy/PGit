package net.typeblog.git.dialogs;

import android.content.Context;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class RemoteCheckoutDialog extends BaseRefCreateDialog
{
	private String mName;
	
	public RemoteCheckoutDialog(Context context, GitProvider provider, String name) {
		super(context, provider);
		mName = name;
	}

	@Override
	protected void onInitView() {
		super.onInitView();
		
		mText.setText(mName.substring(mName.lastIndexOf("/") + 1, mName.length()));
	}
	
	@Override
	protected int getTitle() {
		return R.string.git_checkout_remote;
	}

	@Override
	protected void doCreate(String name) throws GitAPIException, RuntimeException {
		mProvider.git().checkout()
			.setCreateBranch(true)
			.setName(name)
			.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
			.setStartPoint(mName)
			.call();
	}

	
}
