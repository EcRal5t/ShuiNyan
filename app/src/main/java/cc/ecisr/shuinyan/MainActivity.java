package cc.ecisr.shuinyan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import cc.ecisr.shuinyan.struct.ResultEntry;
import cc.ecisr.shuinyan.struct.ResultEntryManager;
import cc.ecisr.shuinyan.struct.SearchReplaceMap;
import cc.ecisr.shuinyan.utils.DbUtil;
import shuidict.R;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "`MainActivity";
	
	EditText inputEditText;
	Button btnQueryClear;
	ResultFragment resultFragment;
	
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inputEditText = findViewById(R.id.edit_text_input);
		btnQueryClear = findViewById(R.id.btn_clear);
		if (savedInstanceState == null) {
			resultFragment = new ResultFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.result_fragment, resultFragment).commit();
		} else {
			resultFragment = (ResultFragment) getSupportFragmentManager().getFragment(savedInstanceState, "result_fragment");
		}
		
		inputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if ("".equals(s.toString())) {
					btnQueryClear.setVisibility(View.GONE);
				} else {
					btnQueryClear.setVisibility(View.VISIBLE);
					if ("".equals(s.toString())) return;
					ResultItemAdapter.ResultInfo.clearItem();
					ResultEntry[] resultEntries =  ResultEntryManager.search(db, s.toString());
					for (ResultEntry entry : resultEntries) {
						if (ResultItemAdapter.ResultInfo.isAddedEntry(entry.id)) { continue; }
						ResultItemAdapter.ResultInfo.addItem(entry.id,
								entry.printLeft(getResources().getColor(R.color.blue_500)), entry.printUpper(), entry.printBottom());
					}
					resultFragment.refreshResult();
				}
			}
			@Override
			public void afterTextChanged(Editable s) { }
		});
		btnQueryClear.setVisibility(View.GONE);
		btnQueryClear.setOnClickListener(v -> inputEditText.setText(""));
		
		new DbUtil(this, Integer.parseInt(getString(R.string.app_version_database)));
		db = DbUtil.getDb(this);
		SearchReplaceMap.initialSearchReplaceMap();
		
	}
	
	
	@Override
	protected void onDestroy() {
		DbUtil.closeLink();
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (resultFragment != null) {
			getSupportFragmentManager().putFragment(outState, "result_fragment", resultFragment);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_action_notes) {
			startActivity(new Intent(MainActivity.this, InfoActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
}