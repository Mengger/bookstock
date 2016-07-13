package com.bookrecovery.until;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRegexUntil {

	public static String[] regexString(String info,String regex){
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		List<String> regexRtn=new ArrayList<String>();
		while(m.find()) {
			regexRtn.add(m.group());
	     }
		return  regexRtn.toArray(new String[]{});
	}
}
