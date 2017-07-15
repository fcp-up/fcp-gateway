package com.dfkj.fcp.core.util;

import java.util.ArrayList;
import java.util.HashSet;

public final class ArrayListUtil {
	
	public static void removeDuplicate(ArrayList arList){
		HashSet h = new HashSet(arList);
		arList.clear();
		arList.addAll(h);
	}

}
