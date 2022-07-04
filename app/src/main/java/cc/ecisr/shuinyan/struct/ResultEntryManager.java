package cc.ecisr.shuinyan.struct;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cc.ecisr.shuinyan.utils.DbUtil;
import cc.ecisr.shuinyan.utils.StringUtil;

public class ResultEntryManager {
	private static final String TAG = "`ResultEntryManager";
	public static String table_name = "Okinawa";
	
	static public ResultEntry[] search(SQLiteDatabase db, String s) {
		if ("".equals(s)) return new ResultEntry[0];
		boolean isAsciiString = StringUtil.isAsciiString(s);
		if (isAsciiString) {
			try { Pattern.compile(s); } catch (PatternSyntaxException ignored) { return new ResultEntry[0]; }
			String replacedS = s;
			for (int index = 0; index< SearchReplaceMap.replaceMapKey.size(); index++) {
				replacedS = replacedS.replaceAll(SearchReplaceMap.replaceMapKey.get(index), SearchReplaceMap.replaceMapVal.get(index));
			}
			ResultEntry[] resultEntries1 = retrieveResultBySql(db,"REPLACE(REPLACE(term,'=',''),'¬','')",  DbUtil.REG, replacedS + ".*");
			
			String ellipsisS = replacedS;
			for (int index=0; index<SearchReplaceMap.ellipsisMapKey.size(); index++) {
				ellipsisS = ellipsisS.replaceAll(SearchReplaceMap.ellipsisMapKey.get(index), SearchReplaceMap.ellipsisMapVal.get(index));
			}
			if (ellipsisS.equals(replacedS)) return resultEntries1;
			ResultEntry[] resultEntries2 = retrieveResultBySql(db, "REPLACE(REPLACE(term,'=',''),'¬','')", DbUtil.REG, ellipsisS + ".*");
			
			return StringUtil.concat(resultEntries1, resultEntries2);
		} else {
			return retrieveResultBySql(db, "meaning", DbUtil.LIKE, "%" + s + "%");
		}
	}
	
	
	static ResultEntry[] retrieveResultBySql(SQLiteDatabase db, String col, String method, String desc) {
		String sql = String.format("SELECT * FROM `%s` WHERE `id` IN (SELECT `id` FROM `%s` WHERE %s %s ? LIMIT 200)", table_name, table_name, col, method);
		Log.i(TAG, sql + ", [ DESC = " + desc);
		try {
			Cursor cursor = db.rawQuery(sql, new String[]{desc});
			ArrayList<ResultEntry> entries = new ArrayList<>(cursor.getCount());
			ResultEntry entry;
			while (cursor.moveToNext()) {
				entry = new ResultEntry();
				entry.id = cursor.getInt(0);
				entry.term = cursor.getString(1);
				entry.class_ = cursor.getString(2);
				entry.attach = cursor.getString(3);
				entry.note = cursor.getString(4);
				entry.mean = cursor.getString(5);
				entries.add(entry);
			}
			cursor.close();
			ResultEntry[] entries_ = entries.toArray(new ResultEntry[0]);
			Arrays.sort(entries_, (o1, o2) -> {
				int o1ml = o1.mean.length(), o2ml = o2.mean.length();
				int o1tl = o1.term.length(), o2tl = o2.term.length();
				return o1tl!=o2tl ? Integer.compare(o1tl, o2tl) : Integer.compare(o1ml, o2ml);
			});
			return entries_;
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
		}
		return new ResultEntry[0];
	}
	
}
