package org.kexing.management.infrastruction.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * sql server special character escape
 * @author lh
 */
public class SqlServerSpecialCharacterEscapeUtil {
  public static final char ESCAPE_CHAR = '\\';

  private static final Set<Character> specialCharacterSet =
      Arrays.stream(new String[] {"%", "_", "[]", "[^]"})
          .flatMap(s -> Arrays.stream(ArrayUtils.toObject(s.toCharArray())))
          .collect(Collectors.toSet());

  public static String escape(String original, char escapeChar) {
      		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < original.length(); i++) {
			char item =  original.charAt(i);
			if(specialCharacterSet.contains(item)|| item==escapeChar){
				temp.append(escapeChar).append(item);
			}else{
				temp.append(original.charAt(i));
			}
		}
		return temp.toString();
  }
}
