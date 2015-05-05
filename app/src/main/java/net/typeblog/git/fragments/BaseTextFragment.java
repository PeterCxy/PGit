package net.typeblog.git.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public abstract class BaseTextFragment extends Fragment
{
	protected TextView mText;
	
	protected abstract String doLoad();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.text, container, false);
		
		mText = $(v, R.id.text);
		
		reload();
		
		return v;
	}
	
	public void reload() {
		new LoaderTask().execute();
	}
	
	private class LoaderTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			return doLoad();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			mText.setText(result);
		}
	}
}
