package cc.ecisr.shuinyan.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DbUtil extends SQLiteOpenHelper {
	private static final String TAG = "`DbUtil";
	
	//声明数据库的实例
	private static SQLiteDatabase db = null;
	private static SQLiteDatabase db_in = null;
	
	public static final String DB_NAME = "dict.db";
	
	public static final String LIKE = "LIKE";
	public static final String REG = "REGEXP";
	
	static String dbPath = "";
	
	static boolean updateNeeded = false;
	
	public DbUtil(@Nullable Context context, int version) {
		super(context, DB_NAME, null, version);
		db_in = this.getWritableDatabase();
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate, " + db.getVersion());
		db.execSQL("CREATE TABLE IF NOT EXISTS _info_ ( version VARCHAR );");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "onUpgrade: " + oldVersion + " -> " + newVersion);
		updateNeeded = true;
	}
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "onUpgrade: " + oldVersion + " -> " + newVersion);
		updateNeeded = true;
	}
	
	
	static public SQLiteDatabase getDb(Context context) {
		Log.i(TAG, "getDb");
		if ("".equals(dbPath)) {
			dbPath = context.getFilesDir().getPath() + File.separator;
		}
		checkDataBase(context, updateNeeded);
		Log.i(TAG, "dbPath: " + dbPath);
		
		if (db == null) {
			Log.i(TAG, "Null db");
			db = SQLiteDatabase.openDatabase(dbPath + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
		}
		return db;
	}
	
	
	//关闭数据库的读连接
	public static void closeLink() {
		if (db != null && db.isOpen()) {
			db.close();
			db = null;
		}
	}
	
	
	private static void checkDataBase(Context context, boolean forceUpdate) {
		Log.i(TAG, "forced update = " + forceUpdate);
		if (updateNeeded || forceUpdate || !(new File(dbPath + DB_NAME)).exists()) {
			try {
				File f = new File(dbPath);
				if (!f.exists()) {
					f.mkdir();
				}
				Log.i(TAG, "orig length: " + (new File(dbPath + DB_NAME)).length());
				InputStream is = context.getAssets().open("db"+File.separator+DB_NAME);
				OutputStream os = new FileOutputStream(dbPath + DB_NAME);
				byte[] buffer = new byte[1024];
				int length, t=0;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
					t += length;
				}
				
				// 关闭文件流
				os.flush();
				os.close();
				is.close();
				Log.i(TAG, "Checked db non-exist: moved length: " + t);
				
				updateNeeded = false;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}
	
	private static boolean checkDataBase_() {
		SQLiteDatabase checkDB = null;
		
		try {
			checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			Log.i(TAG, "database does not exist");
		}
		boolean exist = (checkDB != null);
		if (checkDB != null) {
			checkDB.close();
		}
		return exist;
	}
}
