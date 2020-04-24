package org.apache.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PIDGenerator extends AbstractFunction {
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__PidNum";

    public PIDGenerator(){}

    private CompoundVariable varName;
    private CompoundVariable date;
    private CompoundVariable gender;

    static {
        desc.add("date:yyyy-MM-dd");//添加参数名称
        desc.add("gender:M/F");//添加参数名称
        desc.add(JMeterUtils.getResString("function_name_paropt"));//获取配置文件中function_name_paropt值
    }

    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String date = this.date.execute().trim().replace("-","");
        String gender = this.gender.execute().trim();
        String var = generate(date);
        if (null != this.varName){
            JMeterVariables variables = getVariables();
            String varName = this.varName.execute().trim();
            if (null != variables && varName.length() > 0){
                variables.put(varName, var);
            }
        }
        return var;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 3);
        Object[] objects = collection.toArray();
        this.date = (CompoundVariable)objects[0];
        this.gender = (CompoundVariable)objects[1];
        if (collection.size() > 2){
            this.varName = (CompoundVariable)objects[2];
        }else {
            this.varName = null;
        }
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }
    private String generate(String date) {
        StringBuilder generater = new StringBuilder();
        generater.append("110101");
        generater.append(date);
        generater.append(this.randomCode());
        generater.append(this.calcTrailingNumber(generater.toString().toCharArray()));
        return generater.toString();
    }
    private char calcTrailingNumber(char[] chars) {
        if (chars.length < 17) {
            return ' ';
        }
        int[] c = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        char[] r = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        int[] n = new int[17];
        int result = 0;
        for (int i = 0; i < n.length; i++) {
            n[i] = Integer.parseInt(chars[i] + "");
        }
        for (int i = 0; i < n.length; i++) {
            result += c[i] * n[i];
        }
        return r[result % 11];
    }
    private String randomCode() {
        int code = (int) (Math.random() * 1000);
        if (code < 10) {
            return "00" + code;
        } else if (code < 100) {
            return "0" + code;
        } else {
            return "" + code;
        }
    }
}
