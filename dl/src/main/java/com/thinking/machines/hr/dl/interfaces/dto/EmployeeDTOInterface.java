package com.thinking.machines.hr.dl.interfaces.dto;

import java.math.*;
import java.util.*;
import com.thinking.machines.enums.*;

public interface EmployeeDTOInterface extends Comparable<EmployeeDTOInterface>, java.io.Serializable
{
    public void setEmployeeID(String employeeID);
    public String getEmployeeID();
    
    public void setName(String name);
    public String getName();
    
    public void setDesignationCode(int designationCode);
    public int getDesignationCode();
    
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