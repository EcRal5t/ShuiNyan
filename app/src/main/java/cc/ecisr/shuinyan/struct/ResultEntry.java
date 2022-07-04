package cc.ecisr.shuinyan.struct;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;


public class ResultEntry {
	public int id;
	public String term, class_, attach, note, mean;
	
	public SpannableString printLeft(int highlightColor) {
		int beginPosition=0, endPosition;
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		
		if (!"".equals(term)) {
			String modifiedTerm = term;
			for (int index=0; index<SearchReplaceMap.displayMapKey.size(); index++) {
				modifiedTerm = modifiedTerm.replaceAll(SearchReplaceMap.displayMapKey.get(index), SearchReplaceMap.displayMapVal.get(index));
			}
			ssb.append(modifiedTerm);
			endPosition = ssb.length();
			ssb.setSpan(new ForegroundColorSpan(highlightColor),
					beginPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return new SpannableString(ssb);
	}
	
	public SpannableString printUpper() {
		int beginPosition, endPosition;
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(class_);
		if (!"".equals(attach)) {
			ssb.append(" | ").append(attach);
		}
		if (!"".equals(note)) {
			beginPosition = ssb.length();
			ssb.append(" | ").append(note);
			endPosition = ssb.length();
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#6C6C6C")),
					beginPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return new SpannableString(ssb);
	}
	
	public SpannableString printBottom() {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		String modifiedMean = mean;
		for (int index=0; index<SearchReplaceMap.displayMapKey.size(); index++) {
			modifiedMean = modifiedMean.replaceAll(SearchReplaceMap.displayMapKey.get(index), SearchReplaceMap.displayMapVal.get(index));
		}
		ssb.append(modifiedMean);
		return new SpannableString(ssb);
	}
}
