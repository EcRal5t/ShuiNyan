package cc.ecisr.shuinyan;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import shuidict.R;

public class ResultFragment extends Fragment {
	private static final String TAG = "`ResultFragment";
	
	static private RecyclerView mRvMain;  // 不加static会显示两个View // a SHITTY method
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View selfView = inflater.inflate(R.layout.fragment_result, container, false);
		mRvMain = selfView.findViewById(R.id.result_list);
		
		ResultItemAdapter marketItemAdapter = new ResultItemAdapter(getActivity(), new ResultItemAdapter.iOnItemClickListener() {
			@Override
			public void onClick(@NonNull ResultItemAdapter.LinearViewHolder holder) {
				ArrayList<String> selectionList = new ArrayList<>();
				selectionList.add(getString(R.string.entry_menu_copy_chara));
				final String[] selections = selectionList.toArray(new String[0]);
				new AlertDialog.Builder(getContext())
						.setItems(selections, (dialogInterface, i) -> {
							if (i == 0) {
								View view = inflater.inflate(R.layout.layout_copy_alertdialog, null);
								TextView tv = view.findViewById(R.id.dialog_box_tv);
								tv.setText(holder.printContent());
								//tv.setTypeface(Typeface.createFromAsset(inflater.getContext().getAssets(), "HuanZhuangziSong.otf"));
								new AlertDialog.Builder(getContext())
										.setView(view).setPositiveButton(R.string.button_confirm, null).show();
							}
						}).create().show();
				
			}
			
			@Override
			public void onLongClick(@NonNull ResultItemAdapter.LinearViewHolder holder) { }
		});
		mRvMain.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mRvMain.setItemAnimator(new DefaultItemAnimator());
		mRvMain.setAdapter(marketItemAdapter);
		//mRvMain.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
		if (savedInstanceState!= null) {
			//rawReceivedData = savedInstanceState.getString("received_data");
			// TODO: refreshResult();
		}
		
		return selfView;
	}
	
	
	
	public void refreshResult() {
		if (mRvMain.getAdapter() == null) { return; }
		//mRvMain.getAdapter().notifyItemRangeChanged(0, ResultItemAdapter.ResultInfo.list.size());
		mRvMain.getAdapter().notifyDataSetChanged();
	}
	
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		Log.d(TAG, "onSaveInstanceState: " + System.identityHashCode(this));
		super.onSaveInstanceState(outState);
		//outState.putString("received_data", rawReceivedData);
	}
}