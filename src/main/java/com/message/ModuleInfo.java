package com.message;

public class ModuleInfo  {
	
	private String module;
	
	private String moduleValue;
	
	private String desc;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModuleValue() {
		return moduleValue;
	}

	public void setModuleValue(String moduleValue) {
		this.moduleValue = moduleValue;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

	@Override
	public int hashCode() {
		return this.module.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 if(obj != null && obj instanceof ModuleInfo){
			 ModuleInfo moduleInfo = (ModuleInfo)obj;
			return  moduleInfo.getModule().equals(this.module);
		 }
		 return false;
	}
}
