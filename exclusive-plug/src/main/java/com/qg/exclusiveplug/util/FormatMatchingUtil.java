package com.qg.exclusiveplug.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class FormatMatchingUtil {

    /**
     * 匹配格式
     * @param message 传入信息
     * @param format 规定格式
     * @return 符合返回true，否则为false
     */
    public static boolean isFormatMatching(String message, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }

    /**
     * 判断嵌入式传来的设备信息是否符合格式
     *
     * @param serviceInfo 设备信息
     * @return 符合返回true，否则为false
     */
    public static boolean isServiceInfo(String serviceInfo) {
        String rexp = "(\\w*:V_\\d:[0-9]+[.][0-9]*,I_\\d:[0-9]+[.][0-9]*,P_\\d:[0-9]+[.][0-9]*,PF_\\d:[-]?[0-9]+[.][0-9]*,F_\\d:[0-9]+[.][0-9]*,W_\\d:[0-9]+[.][0-9]*,S_\\d:\\d,\\dend)*";
        Pattern pat = Pattern.compile(rexp);
        Matcher matcher = pat.matcher(serviceInfo);
        return matcher.matches();
    }

    /**
     * 判断嵌入式传来的设备信息是否为注册验证信息
     */
    public static boolean isDeviceIndexs(String DeviceIndexs) {
        String rexp = "(\\d[:]?).*";
        Pattern pat = Pattern.compile(rexp);
        Matcher matcher = pat.matcher(DeviceIndexs);
        return matcher.matches();
    }

//    /**
//     * 切割字符串
//     *
//     * @param serviceInfo 设备信息
//     * @return 设备信息实体类
//     */
//    @Deprecated
    /*public static Device getDevice(String serviceInfo) {
        String rexp = "([0-9]+[.][0-9]*)";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(serviceInfo);
        int i = 0;
        double[] doubles = new double[6];
        while (matcher.find()) {
            doubles[i] = Double.parseDouble(matcher.group());
            i++;
        }
        return new Device((int) serviceInfo.charAt(serviceInfo.length() - 1) - 48,
                serviceInfo.split(":")[0], doubles[1], doubles[0], doubles[2], doubles[3], doubles[4],
                DateUtil.currentTime(), doubles[5]);
    }*/

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    private static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    private static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断验证码是否符合格式
     * @param str 验证码
     * @return 是否符合格式
     */
    public static boolean isCheckCode(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "[0-9]{4}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断密码是否符合格式
     * @param str 密码
     * @return 是否符合格式
     */
    public static boolean isPassword(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "[0-9]{6,16}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T)object;
    }

    /**
     * 验证WebSocket通信协议
     */
    public static boolean isDeviceIndex(String message) {
        if (null != message) {
            String regExp = "\\{\"index\":[0-9]*}";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(message);
            return m.matches();
        }
        return false;
    }
}
