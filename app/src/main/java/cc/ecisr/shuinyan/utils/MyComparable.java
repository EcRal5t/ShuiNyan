package cc.ecisr.shuinyan.utils;

import androidx.annotation.NonNull;

public class MyComparable implements Comparable {
	int count;
	public MyComparable(int count) {
		this.count = count;
	}
	
	@NonNull
	public String toString() {
		return "R[count:" + count + "]";
	}
	
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj != null && obj.getClass() == MyComparable.class){
			MyComparable r = (MyComparable)obj;
			return r.count == this.count;
		}
		return false;
	}
	
	public int compareTo(Object obj){
		MyComparable r = (MyComparable)obj;
		return Integer.compare(count, r.count);
	}
}