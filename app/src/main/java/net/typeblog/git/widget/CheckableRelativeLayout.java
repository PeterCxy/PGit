package net.typeblog.git.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import net.typeblog.git.R;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable
{
	private boolean mIsChecked = false;
	
	public CheckableRelativeLayout(Context context) {
		this(context, null);
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setChecked(boolean checked) {
		setBackgroundDrawable(checked ? new ColorDrawable(getResources().getColor(R.color.selector_white)) : null);
	}

	@Override
	public boolean isChecked() {
		return mIsChecked;
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
	}
}
