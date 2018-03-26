package com.nfet.icare.pojo;

import java.util.List;

public class ErrorInfo {
	// 错误信息
	private String errorType;
	// 错误行数
	private List<String> errorLine;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public List<String> getErrorLine() {
		return errorLine;
	}

	public void setErrorLine(List<String> errorLine) {
		this.errorLine = errorLine;
	}

}
