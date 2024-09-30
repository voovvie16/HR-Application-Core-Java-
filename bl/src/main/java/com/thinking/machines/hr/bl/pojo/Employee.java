package com.thinking.machines.hr.bl.pojo;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.enums.*;

import java.math.*;
import java.util.*;


public class Employee implements EmployeeInterface
{
    private String EmployeeID;
    private String name;
    private DesignationInterface designation;
    private Date dateOfBirth;
    private char gender;
    private boolean isIndian;
    private BigDecimal basicSalary;
    private String panNumber;
    private String aadharNumber;

    public Employee()
    {
        this.EmployeeID = "";
        this.name = "";
        this.designation = null;
        this.dateOfBirth = null;
        this.gender = ' ';
        this.isIndian = false;
        this.basicSalary = null;
        this.panNumber = "";
        this.aadharNumber = "";
    }
    public void setEmployeeID(java.lang.String EmployeeID)
    {
        this.EmployeeID = EmployeeID;
    }
    public java.lang.String getEmployeeID()
    {
        return this.EmployeeID;
    }
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    public java.lang.String getName()
    {
        return this.name;
    }
    public void setDesignation(DesignationInterface designation)
    {
        this.designation = designation;
    }
    public DesignationInterface getDesignation()
    {
        return this.designation;
    }
    public void setDateOfBirth(java.util.Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
        }
    public java.util.Date getDateOfBirth()
    {
        return this.dateOfBirth;
    }
    public void setGender(GENDER gender)
    {
        if (gender == GENDER.MALE)
            this.gender = 'M';
        else
            this.gender = 'F';
    }
    public char getGender()
    {
        return this.gender;
    }
    public void setIsIndian(boolean isIndian)
    {
        this.isIndian = isIndian;
    }
    public boolean getIsIndian()
    {
        return this.isIndian;
    }
    public void setBasicSalary(java.math.BigDecimal basicSalary)
    {
        this.basicSalary = basicSalary;
    }
    
    public java.math.BigDecimal getBasicSalary()
    {
        return this.basicSalary;
    }
    public void setPanNumber(java.lang.String panNumber)
    {
        this.panNumber = panNumber;
    }
    public java.lang.String getPanNumber()
    {
        return this.panNumber;
    }
    public void setAadharNumber(java.lang.String aadharNumber)
    {
        this.aadharNumber = aadharNumber;
    }
    public java.lang.String getAadharNumber()
    {
        return this.aadharNumber;
    }

    
    public boolean equals(Object other)
    {
        if (!(other instanceof EmployeeInterface)) return false;

        EmployeeInterface employee = (EmployeeInterface) other;
        return this.EmployeeID.equalsIgnoreCase(employee.getEmployeeID());
    }

    public int compareTo(EmployeeInterface other)
    {
        return this.EmployeeID.compareToIgnoreCase(other.getEmployeeID());
    }

    public int hashCode()
    {
        return this.EmployeeID.toUpperCase().hashCode();
    }
}

