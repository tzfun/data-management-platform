package demo.sicau.datamanagementplatform.util;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 15:35 2018/11/20
 * @Description: 字符串操作类
 */
public class StrUtil {

    /**
     * 字符串符号转义，转义规则如下
     * <   &lt;
     * >   &gt;
     * '   &apos;
     * "   &quot;
     * @param str 传入字符串
     * @return String
     */
    public static String strEncode(String str){
        str = str.replace("<","&lt;");
        str = str.replace(">","&gt;");
        str = str.replace("\'","&apos;");
        str = str.replace("\"","&quot;");
        return str;
    }

    /**
     * 字符串解码
     * @param str 传入需要解码的字符串
     * @return String
     */
    public static String strDecode(String str){
        str = str.replace("&lt;","<");
        str = str.replace("&gt;",">");
        str = str.replace("&apos;","'");
        str = str.replace("&quot;","\"");
        return str;
    }

    /**
     * 自动编码多个字符串
     * @param strings 可传入多个需要编码的字符串
     */
    public static void autoEncodeStr(String... strings){
        for (int i = 0;i< strings.length;i++){
            strings[i] = strEncode(strings[i]);
        }
    }

    /**
     * 自动解码多个字符串
     * @param strings 可传入多个需要解码的字符串
     */
    public static void autoDecodeStr(String... strings){
        for (int i = 0;i< strings.length;i++){
            strings[i] = strDecode(strings[i]);
        }
    }
}
