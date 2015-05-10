package net.typeblog.git.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public class CheckableWrapperRelativeLayout extends RelativeLayout implements Checkable
{
	private int mCheckableId;
	private Checkable mCheckable;
	
	public CheckableWrapperRelativeLayout(Context context) {
		this(context, null);
	}
	
	public CheckableWrapperRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CheckableWrapperRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableWrapper, defStyle, 0);
		mCheckableId = a.getResourceId(R.styleable.CheckableWrapper_target, 0);
		a.recycle();
		
		if (mCheckableId == 0) {
			throw new IllegalArgumentException("checkable not provided");
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCheckable = $(this, mCheckableId);
		
		if (mCheckable == null) {
			throw new IllegalStateException("No checkable child is found");
		}
	}

	@Override
	public boolean isChecked() {
		return mCheckable.isChecked();
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckable.setChecked(checked);
	}

	@Override
	public void toggle() {
		mCheckable.toggle();
	}

}
