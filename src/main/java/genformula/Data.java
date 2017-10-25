package genformula;

import java.util.ArrayList;
import java.util.List;

public class Data{
	
	private String name;
	private String paramStr;
	private String formulaInfo;
	private List<String> parameterDescs = new ArrayList<>(); 	//描述参数
	private String remarks;			//描述方法
	 
	public List<String> getParameterDescs() {
		return parameterDescs;
	}
	public void setParameterDescs(List<String> parameterDescs) {
		this.parameterDescs = parameterDescs;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParamStr() {
		return paramStr;
	}
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	public String getFormulaInfo() {
		return formulaInfo;
	}
	public void setFormulaInfo(String formulaInfo) {
		this.formulaInfo = formulaInfo;
	}
}