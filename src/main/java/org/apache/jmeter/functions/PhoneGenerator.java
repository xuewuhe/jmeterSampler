package org.apache.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneGenerator extends AbstractFunction {
    private static final String KEY = "__PhoneNum";
    private static List<String> desc = new LinkedList<>();
    private CompoundVariable varName;

    static {
        desc.add(JMeterUtils.getResString("function_name_paropt"));
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String value = getMobile();
        if (null != this.varName) {
            String name = varName.execute().trim();
            JMeterVariables variables = getVariables();
            if (null != variables && name.length() > 0) {
                variables.put(name, value);
            }
        }
        return value;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 0, 1);
        Object[] arr = collection.toArray();
        if (arr.length > 0) {
            this.varName = (CompoundVariable) arr[0];
        }
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }

    public static boolean isMobileNO(String mobiles) {

        Pattern pattern = Pattern.compile("^((13[0-9])|(14[^4,\\D])|(19[0,5-9]))\\d{8}$");

        Matcher matcher = pattern.matcher(mobiles);

        return matcher.matches();
    }


    // 返回随机电话号码
    public String getMobile() {

        while (true) {
            String randomPhone = randomPhone();
            if (isMobileNO(randomPhone)) {
                return randomPhone;
            }
        }
    }


    // 产生随机电话号码格式数字
    public String randomPhone() {
        String phone = "1";

        java.util.Random random = new java.util.Random();
        int nextInt = random.nextInt(3);

        if (nextInt == 0) {
            phone = phone + "3" + randomNumber();
        } else if (nextInt == 1) {
            phone = phone + "4" + randomNumber();
        } else {
            phone = phone + "9" + randomNumber();
        }
        return phone;
    }

    // 生成长度为9的随机数
    public String randomNumber() {

        java.util.Random random = new Random();
        int nextInt = random.nextInt(900000000) + 100000000;
        int abs = Math.abs(nextInt);
        String valueOf = String.valueOf(abs);
        return valueOf;
    }
}
