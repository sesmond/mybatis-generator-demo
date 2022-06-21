package generator.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;

public class StringUtils {
    private static Pattern maskEmailPattern = Pattern.compile("(?<=^.{3})(.*?)(?=@)");
    private static Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

    private StringUtils() {
    }

    public static String getLockKey(String loginName) {
        return loginName + "_LOCK";
    }

    public static String formatEmailShow(String email) {
        if (isEmpty(email)) {
            return "";
        } else {
            if (email.indexOf("@") > 0) {
                Matcher m = maskEmailPattern.matcher(email);
                email = m.replaceAll("***");
            }

            return email;
        }
    }

    public static String toIntRateUnit(BigDecimal decimal) {
        if (isEmpty((Object) decimal)) {
            return "";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = (new BigDecimal("100")).multiply(decimal);
            String temp = (new DecimalFormat("#0.00")).format(multiplicand.doubleValue());
            return temp + multifarious;
        }
    }

    public static String fmtMicrometer(String text) {
        String str = checkAmountNumber(text);
        DecimalFormat df = null;
        if (str.indexOf(".") > 0) {
            if (str.length() - str.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0.00");
        }

        double number = 0.0D;

        try {
            number = Double.parseDouble(text);
        } catch (Exception var6) {
            number = 0.0D;
        }

        return df.format(number);
    }

    public static BigDecimal str2BigDecimal(String value) {
        return new BigDecimal(value);
    }

    public static String bigDecimal2String(BigDecimal d) {
        return d == null ? "0.00" : fmtMicrometer(d.toPlainString());
    }

    public static BigDecimal string2BigDecimal(String d) {
        return isBlank(d) ? new BigDecimal("0") : new BigDecimal(d);
    }

    public static String bigDecimal2StringForIOS(BigDecimal amount) {
        if (amount == null) {
            return "0万";
        } else {
            MathContext mc = new MathContext(6, RoundingMode.HALF_UP);
            BigDecimal result = amount.divide(new BigDecimal("10000"), mc);
            return result.toPlainString() + "万";
        }
    }

    public static String subStr2(BigDecimal value) {
        String v = checkPercentNumber(value);
        return v.length() > 2 ? v.substring(0, v.length() - 2) : v;
    }

    public static String subStr2(BigDecimal value, String unit) {
        String v = checkPercentNumber(value);
        return v.length() > 2 ? v.substring(0, v.length() - 2) + unit : v + unit;
    }

    public static String checkAmountNumber(BigDecimal value) {
        return null != value && !"".equals(value.toPlainString()) ? value.toPlainString() : "0.00";
    }

    public static String checkAmountNumber(String value) {
        return isEmpty(value) ? "0.00" : value;
    }

    public static String checkPercentNumber(BigDecimal value) {
        return null != value && !"".equals(value.toPlainString()) ? value.toPlainString() : "0.0000";
    }

    public static boolean isEmpty(String s) {
        return org.apache.commons.lang3.StringUtils.isEmpty(s);
    }

    public static boolean isNotEmpty(String s) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(s);
    }

    public static boolean isEmpty(Object obj) {
        return "".equals(killNull(obj));
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String toProgress(BigDecimal decimal) {
        if (null == decimal) {
            return "0.00%";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = new BigDecimal("100");
            String result = decimal.multiply(multiplicand).toPlainString();
            int index = result.indexOf(".");
            String rate = "";
            if (index != -1) {
                rate = result.substring(0, index + 3);
            }

            return rate + multifarious;
        }
    }

    public static String toProgressByBit(BigDecimal decimal) {
        if (null == decimal) {
            return "0.00%";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = new BigDecimal("100");
            String result = decimal.multiply(multiplicand).toPlainString();
            int index = result.indexOf(".");
            String rate = "";
            if (index != -1) {
                rate = result.substring(0, index);
            }

            return rate + multifarious;
        }
    }

    public static String toCreditRate(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "万分之";
            BigDecimal multiplicand = (new BigDecimal("10000")).multiply(decimal);
            String temp = (new DecimalFormat("#0.00")).format(multiplicand.doubleValue());
            return multifarious + temp;
        }
    }

    public static String toCreditRate(String interestRate) {
        if (isEmpty(interestRate)) {
            return "";
        } else {
            String multifarious = "万分之";
            BigDecimal multiplicand = (new BigDecimal("10000")).multiply(new BigDecimal(interestRate));
            String temp = (new DecimalFormat("#0.##")).format(multiplicand.doubleValue());
            return multifarious + temp;
        }
    }

    public static String toCreditInterestRate(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "万分之";
            BigDecimal multiplicand = (new BigDecimal("10000")).multiply(decimal);
            String temp = (new DecimalFormat("#0.#")).format(multiplicand.doubleValue());
            return multifarious + temp;
        }
    }

    public static String toCreditRateNofomat(BigDecimal amount) {
        if (amount == null) {
            return "0";
        } else {
            MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
            BigDecimal result = amount.multiply(new BigDecimal("10000"), mc);
            return subZeroAndDot(result.toPlainString());
        }
    }

    public static String toRate(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            BigDecimal multiplicand = (new BigDecimal("100")).multiply(decimal);
            String temp = (new DecimalFormat("#0.00")).format(multiplicand.doubleValue());
            return temp;
        }
    }

    public static String toRateWithUnit(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = (new BigDecimal("100")).multiply(decimal);
            String temp = (new DecimalFormat("#0.00")).format(multiplicand.doubleValue());
            return temp + multifarious;
        }
    }

    public static String toGuaranteeRateWithUnit(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = (new BigDecimal("100")).multiply(decimal);
            String temp = (new DecimalFormat("#")).format(multiplicand.doubleValue());
            return temp + multifarious;
        }
    }

    public static String toPercentageWithUnit(BigDecimal decimal) {
        if (null == decimal) {
            return "0%";
        } else {
            String multifarious = "%";
            String temp = (new DecimalFormat("#")).format(decimal);
            return temp + multifarious;
        }
    }

    public static String toAllPercentageWithUnit(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "%";
            String temp = (new DecimalFormat("#0.00")).format(decimal);
            return temp + multifarious;
        }
    }

    public static String toPeriodWithDays(Integer period) {
        if (null == period) {
            return "";
        } else {
            String multifarious = "天";
            return period.toString() + multifarious;
        }
    }

    public static String toAmountWithUnit(BigDecimal amount) {
        return null == amount ? "" : bigDecimal2String(amount) + "元";
    }

    public static String toRateAlias(BigDecimal decimal, String unit) {
        if (null == decimal) {
            return "";
        } else {
            String multifarious = "%";
            BigDecimal multiplicand = new BigDecimal("100");
            String result = decimal.multiply(multiplicand).toPlainString();
            int index = result.indexOf(".");
            String rate = "";
            if (index != -1) {
                rate = result.substring(0, index + 3);
            }

            return rate + multifarious + "/" + unit;
        }
    }

    public static String toFlag(String flag) {
        if ("Y".equals(flag)) {
            return "是";
        } else {
            return "N".equals(flag) ? "否" : "";
        }
    }

    public static String killNull(String text) {
        return text == null ? "" : text;
    }

    public static String killNull(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

    public static String killNullByAmount(String text) {
        return !isEmpty(text) && !"null".equals(text) ? text : "0";
    }

    public static String killNullForDouble(String text) {
        return "".equals(text) ? "0" : text;
    }

    public static String killNull(Long longVal) {
        return longVal == null ? "" : String.valueOf(longVal);
    }

    public static String killNull(Integer longVal) {
        return longVal == null ? "" : String.valueOf(longVal);
    }

    public static String maskString(String msg) {
        if (msg == null) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer(msg.length());
            if (msg.length() < 3) {
                return msg;
            } else {
                sb.append(msg.substring(0, 1));

                for (int i = 0; i < msg.length() - 2; ++i) {
                    sb.append("*");
                }

                sb.append(msg.substring(msg.length() - 1, msg.length()));
                return sb.toString();
            }
        }
    }

    public static String maskIdNo(String idNo) {
        if (idNo != null && idNo.length() >= 10) {
            String prefix = idNo.substring(0, 3);
            String postfix = idNo.substring(idNo.length() - 3);
            return prefix + "************" + postfix;
        } else {
            throw new RuntimeException("Invaid id no.");
        }
    }

    public static String maskName(String name) {
        String subStr = "";
        if (name != null && name.length() != 0) {
            if (name.length() == 2) {
                subStr = name.substring(0, 1) + "*";
            } else if (name.length() == 3) {
                subStr = name.substring(0, 1) + "**";
            } else {
                subStr = name.substring(0, 1) + "***";
            }

            return subStr;
        } else {
            return subStr;
        }
    }

    public static String maskBankAccountNo(String bankAccountNo) throws Exception {
        if (bankAccountNo != null && bankAccountNo.length() >= 9) {
            String prefix = bankAccountNo.substring(0, 3);
            String postfix = bankAccountNo.substring(bankAccountNo.length() - 3);
            return prefix + "************" + postfix;
        } else {
            throw new Exception("银行账号格式错误！");
        }
    }

    public static boolean regexLoginName(String loginName) {
        loginName = loginName.trim();
        boolean flag = loginName.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_一-龥]+$");
        return flag;
    }

    public static boolean regexMobile(String mobile) {
        if (null != mobile && !"".equals(mobile) && mobile.length() >= 11) {
            boolean flag = mobile.matches("^[0-9]*$");
            return flag;
        } else {
            return false;
        }
    }

    public static boolean regexIdNo(String idNo) {
        Matcher idNumMatcher = idNumPattern.matcher(idNo);
        boolean flag = idNumMatcher.matches();
        return flag;
    }

    public static boolean regexEmail(String email) {
        boolean flag = email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        return flag;
    }

    public static String maskLoginName(String loginName) {
        String subStr = "";
        if (loginName != null && loginName.length() != 0) {
            if (loginName.length() == 3) {
                subStr = loginName.substring(0, 1) + "**" + loginName.substring(2, loginName.length());
            } else {
                subStr = loginName.substring(0, 1) + "***" + loginName.substring(loginName.length() - 2, loginName.length());
            }

            return subStr;
        } else {
            return subStr;
        }
    }

    public static String maskMobile(String mobile) {
        String subMobile = "";
        if (null != mobile && !"".equals(mobile) && mobile.length() >= 11) {
            subMobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
            return subMobile;
        } else {
            return "";
        }
    }

    public static String maskEmail(String email) {
        String subEmail = "";
        if (null != email && (!"".equals(email) || email.contains("@"))) {
            int index = email.lastIndexOf("@");
            subEmail = "****" + email.substring(index, email.length());
            return subEmail;
        } else {
            return "";
        }
    }

    public static String maskEmailT(String email) {
        String subEmail = "";
        if (null != email && (!"".equals(email) || email.contains("@"))) {
            int index = email.lastIndexOf("@");
            if (email.substring(0, index).length() <= 3) {
                return email;
            } else {
                subEmail = email.substring(0, 3) + "**" + email.substring(index, email.length());
                return subEmail;
            }
        } else {
            return "";
        }
    }

    public static String maskProjectName(String projectName) {
        if (null != projectName && !"".equals(projectName)) {
            if (projectName.length() <= 15) {
                return projectName;
            } else {
                String maskName = projectName.substring(0, 15);
                return maskName + "...";
            }
        } else {
            return "";
        }
    }


    public static String getTagValue(String name, String xml) {
        String start = "<" + name + ">";
        String end = "</" + name + ">";
        String value = xml.substring(xml.indexOf(start) + start.length(), xml.indexOf(end));
        return value;
    }

    public static String getTagValueWithTag(String name, String xml) {
        String start = "<" + name + ">";
        String end = "</" + name + ">";
        String value = xml.substring(xml.indexOf(start), xml.indexOf(end) + end.length());
        return value;
    }


    public static String removeIt(String str) {
        return str != null ? str.replaceAll(" ", "") : str;
    }

    public static String paddingPrefix(long oldValue, int length) {
        String f = "%0" + length + "d";
        return String.format(f, oldValue);
    }

    public static String getSexByIdNo(String idNo) {
        String sex = "";
        if (idNo != null && idNo.length() != 0) {
            String sexFlag;
            if (idNo.length() == 15) {
                sexFlag = idNo.substring(14, idNo.length());
                sex = (Integer.parseInt(sexFlag) & 1) == 1 ? "M" : "F";
                return sex;
            } else if (idNo.length() == 18) {
                sexFlag = idNo.substring(16, idNo.length() - 1);
                sex = (Integer.parseInt(sexFlag) & 1) == 1 ? "M" : "F";
                return sex;
            } else {
                return sex;
            }
        } else {
            return sex;
        }
    }

    public static BigDecimal strturnToBigDec(double str) {
        BigDecimal dig = new BigDecimal(str);
        return dig;
    }

    public static boolean isNumeric(String str) {
        boolean flag = true;

        try {
            Double.valueOf(str);
            return flag;
        } catch (Exception var3) {
            flag = false;
            return flag;
        }
    }

    public static boolean isIntegeric(String str) {
        boolean flag = true;

        try {
            Integer.parseInt(str);
            return flag;
        } catch (Exception var3) {
            flag = false;
            return flag;
        }
    }

    public static boolean isValidDate(String str, String format) {
        boolean convertSuccess = true;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);

        try {
            sdf.parse(str);
            return convertSuccess;
        } catch (ParseException var5) {
            convertSuccess = false;
            return convertSuccess;
        }
    }

    public static String bigDecimalToThousand(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        } else {
            MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
            BigDecimal result = amount.divide(new BigDecimal("10000"), mc);
            return bigdecimalToStringWithoutZero(result);
        }
    }

    public static boolean checkDoubleDec(double str) {
        String str_1 = String.valueOf(str);
        String str_2 = str_1.substring(str_1.indexOf(".") + 1, str_1.length());
        return str_2.length() <= 4;
    }

    public static List<Long> stringArrToLongArr(String strValue, String regex) {
        List<Long> longList = new ArrayList();
        if (strValue != null && !"".equals(strValue)) {
            String[] strs = strValue.split(regex);
            String[] var4 = strs;
            int var5 = strs.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String str = var4[var6];
                longList.add(Long.valueOf(str));
            }
        }

        return longList;
    }

    public static BigDecimal decimalFormatFor2DecimalPlaces(BigDecimal amount) {
        return new BigDecimal((new DecimalFormat("#0.00")).format(amount));
    }

    public static int getPageCount(int total, int pageSize) {
        return total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    public static String getDurationUnit(String unit) {
        if ("Y".equals(unit)) {
            return "年";
        } else if ("M".equals(unit)) {
            return "个月";
        } else {
            return "D".equals(unit) ? "天" : "";
        }
    }

    public static String subZeroAndDot(String s) {
        if (isEmpty(s)) {
            return "0";
        } else {
            if (s.indexOf(".") > 0) {
                s = s.replaceAll("0+?$", "");
                s = s.replaceAll("[.]$", "");
            }

            return s;
        }
    }

    public static String bigdecimalToStringWithoutZero(BigDecimal d) {
        return bigdecimalToStringScale(d);
    }

    public static String bigdecimalKillNull(String bigDecimal) {
        return isEmpty(bigDecimal) ? "0" : bigDecimal;
    }

    public static String bigdecimalToStringScale(BigDecimal bigDecimal) {
        return bigdecimalToStringWithoutZero(bigDecimal, "0.00");
    }

    public static String bigdecimalToStringWithoutZero(BigDecimal bigDecimal, String valueIfNull) {
        return bigdecimalToString(bigDecimal, valueIfNull, "###,##0.00");
    }

    public static String bigdecimalToStringWithoutZeroAndPo(BigDecimal bigDecimal, String valueIfNull) {
        return bigdecimalToString(bigDecimal, valueIfNull, "#####0.00");
    }

    public static String bigdecimalToString(BigDecimal bigDecimal, String valueIfNull, String format) {
        if (bigDecimal == null) {
            return valueIfNull;
        } else {
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
            DecimalFormat df = new DecimalFormat(format);
            return df.format(bigDecimal);
        }
    }

    public static String bigdecimalToStringWithoutZero(String decimalString) {
        if (isEmpty(decimalString)) {
            return "0";
        } else if (decimalString.contains(",")) {
            return decimalString;
        } else {
            DecimalFormat df = new DecimalFormat("###,###.##");
            String amount = df.format(new BigDecimal(decimalString));
            return amount;
        }
    }

    public static String cutDecimal(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            int value = decimal.intValue();
            return killNull(value);
        }
    }

    public static String fomatAmount(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            int value = decimal.divide(BigDecimal.valueOf(10000L)).intValue();
            return killNull(value);
        }
    }

    public static String getMinValue(String first, String second) {
        return (new BigDecimal(first)).compareTo(new BigDecimal(second)) > -1 ? bigdecimalToStringWithoutZero(second) : bigdecimalToStringWithoutZero(first);
    }

    public static String formatEmail(String email) {
        String subEmail = "";
        if (!isEmpty(email) && email.contains("@")) {
            if (email.split("@")[0].length() > 3) {
                int index = email.lastIndexOf("@");
                subEmail = email.substring(0, 3) + "***" + email.substring(index, email.length());
                return subEmail;
            } else {
                return email;
            }
        } else {
            return "";
        }
    }

    public static BigDecimal toProgress(String decimal) {
        if (null != decimal && !"".equals(decimal)) {
            String rate = decimal.substring("万分之".length(), decimal.length());
            return (new BigDecimal(rate)).divide(new BigDecimal("10000"));
        } else {
            return new BigDecimal("0");
        }
    }

    public static String toIntRateByKeepTwoDec(BigDecimal decimal) {
        if (null == decimal) {
            return "";
        } else {
            BigDecimal multiplicand = (new BigDecimal("10000")).multiply(decimal);
            String temp = (new DecimalFormat("#0.00")).format(multiplicand.doubleValue());
            return temp;
        }
    }

    public static List<String> stringToStringList(String strValue, String regex) {
        List<String> strList = new ArrayList();
        if (strValue != null && !"".equals(strValue)) {
            String[] strs = strValue.split(regex);
            String[] var4 = strs;
            int var5 = strs.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String str = var4[var6];
                strList.add(str);
            }
        }

        return strList;
    }

    public static String accountBalanceFormat(String balance) {
        if (!isEmpty(balance)) {
            balance = balance.replaceAll(",", "");
            BigDecimal bd = new BigDecimal(balance);
            return bigdecimalToStringWithoutZero(bd);
        } else {
            return "0";
        }
    }

    public static String encodeString(String string, Charset originCharset, Charset targetCharset) {
        return new String(string.getBytes(originCharset), targetCharset);
    }

    public static String encodeUrl(String string, String charset) {
        if (null != charset && !charset.isEmpty()) {
            try {
                return URLEncoder.encode(string, charset);
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }
        }

        return URLEncoder.encode(string);
    }

    public static String decodeUrl(String string, String charset) {
        if (null != charset && !charset.isEmpty()) {
            try {
                return URLDecoder.decode(string, charset);
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return URLDecoder.decode(string);
        }
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0 && !"null".equals(str)) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static BigDecimal nullToDefault(BigDecimal bigDecimal) {
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal;
    }

    public static String subStringByIndex(String str, int beginIndex, int endIndex) {
        if (isEmpty(str)) {
            return "";
        } else {
            int index = str.length();
            return 0 <= beginIndex && 0 <= endIndex && beginIndex <= endIndex && beginIndex <= index && endIndex <= index ? str.substring(beginIndex, endIndex) : str;
        }
    }

    public static String generateTokenValue(String tokenKey) {
        return tokenKey + System.currentTimeMillis();
    }

    public static String getStringByMap(Map map, String keyName) {
        return getStringDefByMap(map, keyName, "");
    }

    public static String getStringDefByMap(Map map, String keyName, String defStr) {
        if (null == map) {
            return defStr;
        } else {
            Object obj = map.get(keyName);
            return null == obj ? defStr : map.get(keyName).toString();
        }
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                String result = ObjectUtils.toString(first);
                return result;
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while (iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }

    @SafeVarargs
    public static <T> String join(T... elements) {
        return join((Object[]) elements, (String) null);
    }

    public static String join(Object[] array, char separator) {
        return array == null ? null : join((Object[]) array, separator, 0, array.length);
    }

    public static String join(long[] array, char separator) {
        return array == null ? null : join((long[]) array, separator, 0, array.length);
    }

    public static String join(int[] array, char separator) {
        return array == null ? null : join((int[]) array, separator, 0, array.length);
    }

    public static String join(short[] array, char separator) {
        return array == null ? null : join((short[]) array, separator, 0, array.length);
    }

    public static String join(byte[] array, char separator) {
        return array == null ? null : join((byte[]) array, separator, 0, array.length);
    }

    public static String join(char[] array, char separator) {
        return array == null ? null : join((char[]) array, separator, 0, array.length);
    }

    public static String join(float[] array, char separator) {
        return array == null ? null : join((float[]) array, separator, 0, array.length);
    }

    public static String join(double[] array, char separator) {
        return array == null ? null : join((double[]) array, separator, 0, array.length);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(long[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(int[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(byte[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(short[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(char[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(double[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(float[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    buf.append(array[i]);
                }

                return buf.toString();
            }
        }
    }

    public static String join(Object[] array, String separator) {
        return array == null ? null : join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }

            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                String result = ObjectUtils.toString(first);
                return result;
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while (iterator.hasNext()) {
                    buf.append(separator);
                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(Iterable<?> iterable, char separator) {
        return iterable == null ? null : join(iterable.iterator(), separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        return iterable == null ? null : join(iterable.iterator(), separator);
    }

    public static String formatBinaryNumber(long number) {
        StringBuilder show = new StringBuilder();
        String[] units = new String[]{"B", "K", "M", "G", "T"};

        for (int i = 0; i < units.length; ++i) {
            String unit = units[i];
            if (number <= 0L) {
                break;
            }

            if (i < units.length - 1) {
                long subNumber = number % 1024L;
                if (subNumber > 0L) {
                    show.insert(0, unit);
                    show.insert(0, subNumber);
                    show.insert(0, " ");
                }

                number /= 1024L;
            } else {
                show.insert(0, unit);
                show.insert(0, number);
                show.insert(0, " ");
            }
        }

        return show.toString();
    }
}
