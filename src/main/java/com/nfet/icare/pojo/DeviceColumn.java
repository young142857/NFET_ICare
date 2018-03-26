package com.nfet.icare.pojo;

public class DeviceColumn
  extends BaseBean
{
  private String deviceType;
  private int warrantyType;
  private String firstId;
  private String secondId;
  private String thirdId;
  
  public String getDeviceType()
  {
    return this.deviceType;
  }
  
  public void setDeviceType(String deviceType)
  {
    this.deviceType = deviceType;
  }
  
  public int getWarrantyType()
  {
    return this.warrantyType;
  }
  
  public void setWarrantyType(int warrantyType)
  {
    this.warrantyType = warrantyType;
  }
  
  public String getFirstId()
  {
    return this.firstId;
  }
  
  public void setFirstId(String firstId)
  {
    this.firstId = firstId;
  }
  
  public String getSecondId()
  {
    return this.secondId;
  }
  
  public void setSecondId(String secondId)
  {
    this.secondId = secondId;
  }
  
  public String getThirdId()
  {
    return this.thirdId;
  }
  
  public void setThirdId(String thirdId)
  {
    this.thirdId = thirdId;
  }
}
