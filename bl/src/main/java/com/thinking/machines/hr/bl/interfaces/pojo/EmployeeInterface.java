package com.thinking.machines.hr.bl.interfaces.pojo;

import java.math.*;
import java.util.*;

import com.thinking.machines.enums.*;

public interface EmployeeInterface extends Comparable<EmployeeInterface>, java.io.Serializable
{
    public void setEmployeeID(String employeeID);
    public String getEmployeeID();
    
    public void setName(String name);
    public String getName();
    
    public void setDesignation(DesignationInterface designation);
    public DesignationInterface getDesignation();
    
    public void setDateOfBirth(Date DateOfBirth);
    public Date getDateOfBirth();

    public void setGender(GENDER gender);
    public char getGender();

    public void setIsIndian(boolean IsIndian);
    public boolean getIsIndian();

    public void setBasicSalary(BigDecimal basicSalary);
    public BigDecimal getBasicSalary();

    public void setPanNumber(String panNumber);
    public String getPanNumber();

    public void setAadharNumber(String aadharNumber); 
    public String getAadharNumber();
}