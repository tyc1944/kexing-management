package org.kexing.management.domin.util;

/**
 * no 生成器 工具类
 *
 * @author lh
 */
public class NoGeneratorUtils {

  /**
   * 编号生成器
   *
   * @param prefix
   * @return
   */
  public static String noGenerator(NoGeneratorPrefix prefix) {
    return prefix.toString() + System.currentTimeMillis();
  }

  /**
   * No generator string.
   *
   * @param prefix the prefix
   * @param suffix the suffix
   * @return the string
   */
  public static String noGenerator(NoGeneratorPrefix prefix, String suffix) {
    return noGenerator(prefix) + suffix;
  }

  public enum NoGeneratorPrefix {
    /** 工单编号前缀 */
    WO,
    XJ,
    ;
  }
}
