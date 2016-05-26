package com.jade.customervisit.bean;

public class QueryServiceStatusResult extends RequestResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3998183177416085950L;
	
	int codeSignFlag;
	
	int codeExitFlag;
	
	int contentFlag;
	
	int takePhotoFlag;
	
	int praiseFlag;

	public QueryServiceStatusResult() {
		super();
	}

	public QueryServiceStatusResult(String retcode, String retinfo) {
		super(retcode, retinfo);
	}

	public int getCodeSignFlag() {
		return codeSignFlag;
	}

	public void setCodeSignFlag(int codeSignFlag) {
		this.codeSignFlag = codeSignFlag;
	}

	public int getContentFlag() {
		return contentFlag;
	}

	public void setContentFlag(int contentFlag) {
		this.contentFlag = contentFlag;
	}

	public int getPraiseFlag() {
		return praiseFlag;
	}

	public void setPraiseFlag(int praiseFlag) {
		this.praiseFlag = praiseFlag;
	}

	public int getCodeExitFlag() {
		return codeExitFlag;
	}

	public void setCodeExitFlag(int codeExitFlag) {
		this.codeExitFlag = codeExitFlag;
	}

	public int getTakePhotoFlag() {
		return takePhotoFlag;
	}

	public void setTakePhotoFlag(int takePhotoFlag) {
		this.takePhotoFlag = takePhotoFlag;
	}

}
