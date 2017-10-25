package genformula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
 
class FunctionEntity {
	/**
	 * formula1
	 */
	private String functionName;
	/**
	 *  String data1 = "Math.pow(p0+10,2.4)*math.pow(p1+5,0.6)/18.5";
	 */
	private String formulaInfo;
	/**
	 * 参数名字
	 */
	private Set<String> paramNames = new HashSet<>();
	
	/**
	 * 套用的公式名称
	 */
	private Set<String> paramFunctions = new HashSet<>();
	
	/**
	 * 方法描述
	 */
	private String remarks;
	
	/**
	 * 参数描述
	 */
	private String parameter; 	
	 
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Set<String> getParamFunctions() {
		return paramFunctions;
	}
	public void setParamFunctions(Set<String> paramFunctions) {
		this.paramFunctions = paramFunctions;
	}
	//参数=描述
	private TreeMap<String,String> params = new TreeMap<>();
	
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFormulaInfo() {
		return formulaInfo;
	}
	public void setFormulaInfo(String formulaInfo) {
		this.formulaInfo = formulaInfo;
	}
	 
	public Set<String> getParamNames() {
		return paramNames;
	}
	public void setParamNames(Set<String> paramNames) {
		this.paramNames = paramNames;
	}
	public TreeMap<String, String> getParams() {
		return params;
	}
	public void setParams(TreeMap<String, String> params) {
		this.params = params;
	}
}

class FunctionName {
	/**
	 * formula1(p1,p2.......)
	 */
	private String name;
	/**
	 * 参数个数
	 */
	private Set<String> paramName = new HashSet<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getParamName() {
		return paramName;
	}
	public void setParamName(Set<String> paramName) {
		this.paramName = paramName;
	}
}
 

public class FormulaUtil {
	
	public static int formula1(int p1,int p0){return (int)(Math.pow(p0+10,2.4)*Math.pow(p1+5,0.6)/18.5);}
	public static int formula2(int p2,int p1,int p0){return (int)(Math.round(p2*formula1(p1,p0))/100);}
	
	public static final Pattern p = Pattern.compile("p[0-9]+");
	public static final Pattern f = Pattern.compile("f[0-9]+");
	
	public static Set<String> getParamName(String data) {
		Set<String> result  = new HashSet<>();  
		Matcher m = p.matcher(data); 
		while(m.find()){
			result.add(m.group());
		} 
		return result;
	}
	
	public static Set<String> getParamFunction(String data) {
		Set<String> result  = new HashSet<>();  
		Matcher m = f.matcher(data); 
		while(m.find()){
			result.add(m.group());
		} 
		return result;
	}
	 
	public static FunctionName getFunctionName(String data,int i){
		FunctionName functionName = new FunctionName(); 
		Set<String> paramNames = getParamName(data);
		String parmaNameStr = "";
		for (String  paramName : paramNames) {
			parmaNameStr += ","+paramName;
		}
		if(parmaNameStr.length()>0){
			parmaNameStr = parmaNameStr.substring(1);
		}
		
		functionName.setName("formula"+i+"("+parmaNameStr+")");
		functionName.setParamName(paramNames);
		return functionName;
	}
	
	public static FunctionEntity getFunctionEntity(String data,int i,String parameter,String remarks){
		FunctionEntity functionEntity = new FunctionEntity();
		
		functionEntity.setFormulaInfo(data);
		functionEntity.setFunctionName("formula"+i);
		Set<String> paramNames = getParamName(data);
		functionEntity.setParamNames(paramNames); 
		functionEntity.setParamFunctions(getParamFunction(data));
		functionEntity.setRemarks(remarks);
		functionEntity.setParameter(parameter);
		
		return functionEntity;
	}
	
	 
	
	public static List<Data> getDatas(List<Map<String,String>> source) {
		
		List<Data> result = new ArrayList<>();
		  
		
		Map<String,FunctionName> fucntionNames = new HashMap<>(); 
		List<FunctionEntity> data = new ArrayList<>();
		Map<String,String> paramsDesc = new HashMap<>(); 
		
		for (Map<String,String> entry : source) {
			if(entry == null || entry.size() ==0) continue;
			try {
				String ID = entry.get("ID");
				if(ID == null || "".equals(ID)) {
					continue;
				} 
				int id = Integer.valueOf(ID); 
				String dataStr = entry.get("calculation");
				fucntionNames.put("f"+id, getFunctionName(dataStr,id)); 
				String remarks = entry.get("remarks");	//方法描述
				
				
				String parameter = entry.get("parameter");	//参数描述
				if(parameter!=null && !"".equals(parameter)){
					String[] parameters = parameter.split(",");
					for (String string : parameters) {
						String[] datas = string.split(":");
						paramsDesc.put(datas[0], datas[1]);
					}
				}
				
				data.add(getFunctionEntity(dataStr,id,parameter,remarks));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
		
		
		
		
		for (FunctionEntity functionEntity : data) {
			String fInfo = functionEntity.getFormulaInfo();
			Set<String> paramFunctions = functionEntity.getParamFunctions();
			for (Entry<String,FunctionName> fEntity : fucntionNames.entrySet()) {
				String fNameStr = fEntity.getKey();
				FunctionName fName =  fEntity.getValue();
				
				if(paramFunctions.contains(fNameStr)){
					fInfo = fInfo.replace(fNameStr,fName.getName());
					functionEntity.setFormulaInfo(fInfo);
					
					Set<String>  parmaNames = functionEntity.getParamNames(); 
					Set<String> paramOther = fName.getParamName();
					parmaNames.addAll(paramOther);
				}
			}
		}
		
		
		
		for (FunctionEntity functionEntity  : data) {
			String fName = functionEntity.getFunctionName();
			Set<String> paramNamesTemp = functionEntity.getParamNames();
			String paramStr = "";
			List<String> parameterDescs = new ArrayList<>();
			for(String paramName : paramNamesTemp) {
				paramStr+=",int "+paramName;
				parameterDescs.add(paramName+" "+paramsDesc.get(paramName));
			}
			if(paramStr.length()>0) {
				paramStr = paramStr.substring(1);
			}
			
			Data dataSource = new Data();
			dataSource.setFormulaInfo(functionEntity.getFormulaInfo());
			dataSource.setName(fName);
			dataSource.setParamStr(paramStr);
			dataSource.setParameterDescs(parameterDescs);
			dataSource.setRemarks(functionEntity.getRemarks());
			
//			String parameter = functionEntity.getParameter();
//			if(parameter != null && !"".equals(parameter)){
//				String[] datas = parameter.split(",");
//				List<String> parameterDescs = new ArrayList<>();
//				for (String string : datas) {
//					parameterDescs.add(string);
//				}
//				dataSource.setParameterDescs(parameterDescs);
//			}
			result.add(dataSource); 
		}
		
		return result;
	}
	 
}


