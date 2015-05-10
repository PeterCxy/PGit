package net.typeblog.git.support;

import org.eclipse.jgit.api.Git;

public interface GitProvider
{
	public Git git();
	public String getLocation();
}
