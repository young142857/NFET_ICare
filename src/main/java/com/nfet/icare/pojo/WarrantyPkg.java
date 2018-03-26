package com.nfet.icare.pojo;

//延保方案表
public class WarrantyPkg extends BaseBean {
	//方案编号
	private String warrantyPkgNo;
	//方案时限
	private int warrantyPkgPeriod;
	//方案内容
	private String warrantyPkgContent;
	//方案价格
	private String warrantyPkgPrice;
	//方案图号地址
	private String warrantyPkgImage;
	//延保类型(1:延保 2:上门)
	private int warrantyType;
	//延保截止
	private String warrantTo;
	//上门截止
	private String visitTo;
	
	public String getWarrantyPkgNo() {
		return warrantyPkgNo;
	}
	public void setWarrantyPkgNo(String warrantyPkgNo) {
		this.warrantyPkgNo = warrantyPkgNo;
	}
	public int getWarrantyPkgPeriod() {
		return warrantyPkgPeriod;
	}
	public void setWarrantyPkgPeriod(int warrantyPkgPeriod) {
		this.warrantyPkgPeriod = warrantyPkgPeriod;
	}
	public String getWarrantyPkgContent() {
		return warrantyPkgContent;
	}
	public void setWarrantyPkgContent(String warrantyPkgContent) {
		this.warrantyPkgContent = warrantyPkgContent;
	}
	public String getWarrantyPkgPrice() {
		return warrantyPkgPrice;
	}
	public void setWarrantyPkgPrice(String warrantyPkgPrice) {
		this.warrantyPkgPrice = warrantyPkgPrice;
	}
	public String getWarrantyPkgImage() {
		return warrantyPkgImage;
	}
	public void setWarrantyPkgImage(String warrantyPkgImage) {
		this.warrantyPkgImage = warrantyPkgImage;
	}
	public int getWarrantyType() {
		return warrantyType;
	}
	public void setWarrantyType(int warrantyType) {
		this.warrantyType = warrantyType;
	}
	public String getWarrantTo() {
		return warrantTo;
	}
	public void setWarrantTo(String warrantTo) {
		this.warrantTo = warrantTo;
	}
	public String getVisitTo() {
		return visitTo;
	}
	public void setVisitTo(String visitTo) {
		this.visitTo = visitTo;
	}
	@Override
	public String toString() {
		return "WarrantyPkg [warrantyPkgNo=" + warrantyPkgNo + ", warrantyPkgPeriod=" + warrantyPkgPeriod
				+ ", warrantyPkgContent=" + warrantyPkgContent + ", warrantyPkgPrice=" + warrantyPkgPrice
				+ ", warrantyPkgImage=" + warrantyPkgImage + ", warrantyType=" + warrantyType + ", warrantTo="
				+ warrantTo + ", visitTo=" + visitTo + "]";
	}
}
