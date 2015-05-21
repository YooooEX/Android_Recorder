package activities;

import com.schutzstaffel.recorder.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

public class ChooseActivity extends Activity {

	EditText field_search;
	Button btn_clear;
	TextWatcher watcher;
	ScrollView contactsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		initViews();
		loadData();
	}

	private void initViews() {
		field_search = (EditText) findViewById(R.id.searchEditText);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		contactsView = (ScrollView) findViewById(R.id.contactsView);
		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				field_search.setText("");
				btn_clear.setVisibility(View.INVISIBLE);
			}
		});
		watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (field_search.getText().toString() != ""
						|| field_search.getText().toString() != null) {
					btn_clear.setVisibility(View.VISIBLE);
				}
			}
		};
		field_search.addTextChangedListener(watcher);
	}

	private void loadData() {
	}

}
