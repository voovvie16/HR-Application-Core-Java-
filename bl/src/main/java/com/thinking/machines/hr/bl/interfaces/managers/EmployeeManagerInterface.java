package com.thinking.machines.hr.bl.interfaces.managers;

import java.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;

public interface EmployeeManagerInterface
{
    public void addEmployee(EmployeeInterface employee) throws BLException;

    public void updateEmployee(EmployeeInterface employee) throws BLException;

    public void removeEmployee(String employeeID) throws BLException;

    public Set<EmployeeInterface> getEmployees() throws BLException;
    public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException;

    public EmployeeInterface getEmployeeByEmployeeID(String employeeID) throws BLException;
    public EmployeeInterface getEmployeeByAadharNumber(String aadharNumber) throws BLException;
    public EmployeeInterface getEmployeeByPanNumber(String panNumber) throws BLException;

    public boolean employeeIDExists(String employeeID) throws BLException;
    public boolean employeeAadharNumberExists(String aadharNumber) throws BLException;
    public boolean employeePanNumberExists(String panNumber) throws BLException;
    public boolean isDesignationAlloted(int designationCode) throws BLException;

    public int getEmployeeCount() throws BLException;
    public int getEmployeeCountByDesignation(int designationCode) throws BLException;
}