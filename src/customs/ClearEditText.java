package customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ClearEditText extends EditText {
	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int edittextstyle) {
		super(context, attrs, edittextstyle);
	}
}
