package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

import java.util.*;
import java.math.*;

public class EmployeeManager implements EmployeeManagerInterface
{
    private Map<String, EmployeeInterface> employeeIDWiseEmployeeMap;
    private Map<String, EmployeeInterface> aadharNumberWiseEmployeeMap;
    private Map<String, EmployeeInterface> panNumberWiseEmployeeMap;
    private Map<Integer, Set<EmployeeInterface>> designationCodeWiseEmployeeMap;
    private Set<EmployeeInterface> employeesSet;

    private static EmployeeManager employeeManager = null;

    private EmployeeManager() throws BLException
    {
        populateDataStructures();
    }

    private void populateDataStructures() throws BLException
    {
        this.employeeIDWiseEmployeeMap = new HashMap<>();
        this.aadharNumberWiseEmployeeMap = new HashMap<>();
        this.panNumberWiseEmployeeMap = new HashMap<>();
        this.designationCodeWiseEmployeeMap = new HashMap<>();
        this.employeesSet = new TreeSet<>();

        try
        {
            Set<EmployeeDTOInterface> dlEmployees = new EmployeeDAO().getAll();

            DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();

            EmployeeInterface employee;
            DesignationInterface designation;
            Set<EmployeeInterface> tempSet;

            for (EmployeeDTOInterface dlEmployee: dlEmployees)
            {
                employee = new Employee();
                employee.setEmployeeID(dlEmployee.getEmployeeID());
                employee.setName(dlEmployee.getName());

                designation = designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
                employee.setDesignation(designation);

                employee.setDateOfBirth(dlEmployee.getDateOfBirth());
                employee.setAadharNumber(dlEmployee.getAadharNumber());
                employee.setPanNumber(dlEmployee.getPanNumber());

                if (dlEmployee.getGender() == 'M')
                    employee.setGender(GENDER.MALE);
                else
                    employee.setGender(GENDER.FEMALE);

                employee.setIsIndian(dlEmployee.getIsIndian());
                employee.setBasicSalary(dlEmployee.getBasicSalary());

                this.employeeIDWiseEmployeeMap.put(employee.getEmployeeID().toUpperCase(), employee);
                this.panNumberWiseEmployeeMap.put(employee.getPanNumber().toUpperCase(), employee);
                this.aadharNumberWiseEmployeeMap.put(employee.getAadharNumber().toUpperCase(), employee);
                this.employeesSet.add(employee);

                tempSet = this.designationCodeWiseEmployeeMap.get(designation.getCode());
                if (tempSet == null)
                {
                    tempSet = new TreeSet<>();
                    tempSet.add(employee);
                    this.designationCodeWiseEmployeeMap.put(designation.getCode(), tempSet);
                }
                else
                {
                    tempSet.add(employee);
                }

            }
        }
        catch (DAOException daoException)
        {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    public static EmployeeManagerInterface getEmployeeManager() throws BLException
    {
        if (employeeManager == null) employeeManager = new EmployeeManager();
        return employeeManager;
    }

    public void addEmployee(EmployeeInterface employee) throws BLException
    {
        BLException blException = new BLException();

        if (employee == null)
        {
            blException.setGenericException("Employee Required");
            throw blException;
        }

        String employeeID = employee.getEmployeeID();
        String name = employee.getName();
        DesignationInterface designation = employee.getDesignation();
        int designationCode = 0;
        char gender = employee.getGender();
        Date DOB = employee.getDateOfBirth();
        boolean isIndian = employee.getIsIndian();
        String aadharNumber = employee.getAadharNumber();
        String panNumber = employee.getPanNumber();
        BigDecimal basicSalary = employee.getBasicSalary();

        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();

        if (employeeID != null)
        {
            employeeID = employeeID.trim();
            if (employeeID.length() > 0)
                blException.addException("employeeID", "EmployeeID should be nil/empty");
        }

        if (name == null)
            blException.addException("name", "name Required");
        else
        {
            name = name.trim();
            if (name.length() == 0)
                blException.addException("name", "Name Required");
        }

        if (designation == null)
            blException.addException("designation", "Designation Required");
        else
        {
            designationCode = designation.getCode();
            if (!designationManager.designationCodeExists(designationCode))
                blException.addException("designation", "Invalid Designation");
        }

        if (DOB == null)
            blException.addException("DOB", "Date of Birth Required");

        if (gender == ' ')
            blException.addException("gender", "Gender Required");

        if (basicSalary == null)
            blException.addException("basicSalary", "Basic Salary Required");
        else
            if (basicSalary.signum() == -1)
                blException.addException("basicSalary", "Basic Salary cannot be negative");

        if (panNumber == null)
            blException.addException("panNumber", "Pan Number Required");
        else
        {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0)
                blException.addException("panNumber", "Pan Number Required");
        }

        if (aadharNumber == null)
            blException.addException("aadharNumber", "aadhar Number Required");
        else
        {
            aadharNumber = aadharNumber.trim();
            if (aadharNumber.length() == 0)
                blException.addException("aadharNumber", "aadhar Number Required");
        }

        if (blException.hasExceptions())
            throw blException;
        try
        {
            EmployeeDTOInterface dlEmployee;
            dlEmployee = new EmployeeDTO();

            dlEmployee.setName(name);
            dlEmployee.setDesignationCode(designationCode);
            dlEmployee.setGender((gender == 'M')?GENDER.MALE:GENDER.FEMALE);
            dlEmployee.setDateOfBirth(DOB);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setAadharNumber(aadharNumber);
            dlEmployee.setPanNumber(panNumber);
            dlEmployee.setIsIndian(isIndian);

            new EmployeeDAO().add(dlEmployee);
            employee.setEmployeeID(dlEmployee.getEmployeeID());

            EmployeeInterface dsEmployee = new Employee();
            dsEmployee.setEmployeeID(dlEmployee.getEmployeeID());
            dsEmployee.setName(name);
            designation = ((DesignationManager)designationManager).getDSDesignationByCode(designationCode);
            dsEmployee.setDesignation(designation);
            dsEmployee.setGender((gender == 'M')?GENDER.MALE:GENDER.FEMALE);
            dsEmployee.setDateOfBirth(DOB);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setAadharNumber(aadharNumber);
            dsEmployee.setPanNumber(panNumber);
            dsEmployee.setBasicSalary(basicSalary);

            this.employeeIDWiseEmployeeMap.put(employeeID.toUpperCase(), dsEmployee);
            this.aadharNumberWiseEmployeeMap.put(aadharNumber.toUpperCase(), dsEmployee);
            this.panNumberWiseEmployeeMap.put(panNumber.toUpperCase(), dsEmployee);
            this.employeesSet.add(dsEmployee);

            Set<EmployeeInterface> tempSet;
            tempSet = this.designationCodeWiseEmployeeMap.get(dsEmployee.getDesignation().getCode());
            if (tempSet == null)
            {
                tempSet = new TreeSet<>();
                tempSet.add(dsEmployee);
                this.designationCodeWiseEmployeeMap.put(dsEmployee.getDesignation().getCode(), tempSet);
            }
            else
            {
                tempSet.add(employee);
            }
        }
        catch (DAOException daoe)
        {
            BLException bl = new BLException();
            bl.setGenericException(daoe.getMessage());
            throw bl;
        }
    }

    public void updateEmployee(EmployeeInterface employee) throws BLException
    {
        BLException blException = new BLException();

        if (employee == null)
        {
            blException.setGenericException("Employee Required");
            throw blException;
        }

        String employeeID = employee.getEmployeeID();
        String name = employee.getName();
        DesignationInterface designation = employee.getDesignation();
        int designationCode = 0;
        char gender = employee.getGender();
        Date DOB = employee.getDateOfBirth();
        boolean isIndian = employee.getIsIndian();
        String aadharNumber = employee.getAadharNumber();
        String panNumber = employee.getPanNumber();
        BigDecimal basicSalary = employee.getBasicSalary();

        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();

        if (employeeID == null)
            blException.addException("employeeID", "employeeID Required");
        else
        {
            employeeID = employeeID.trim();
            if (employeeID.length() == 0)
                blException.addException("employeeID", "EmployeeID Required");
            else
                if (this.employeeIDWiseEmployeeMap.get(employeeID) == null)
                    blException.addException("employeeID", "EmployeeID does not exist");
        }

        if (name == null)
            blException.addException("name", "name Required");
        else
        {
            name = name.trim();
            if (name.length() == 0)
                blException.addException("name", "Name Required");
        }

        if (designation == null)
            blException.addException("designation", "Designation Required");
        else
        {
            designationCode = designation.getCode();
            if (!designationManager.designationCodeExists(designationCode))
                blException.addException("designation", "Invalid Designation");
        }

        if (DOB == null)
            blException.addException("DOB", "Date of Birth Required");

        if (gender == ' ')
            blException.addException("gender", "Gender Required");

        if (basicSalary == null)
            blException.addException("basicSalary", "Basic Salary Required");
        else
            if (basicSalary.signum() == -1)
                blException.addException("basicSalary", "Basic Salary cannot be negative");

        if (panNumber == null)
            blException.addException("panNumber", "Pan Number Required");
        else
        {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0)
                blException.addException("panNumber", "Pan Number Required");
            else
            {
                EmployeeInterface ee = panNumberWiseEmployeeMap.get(panNumber.toUpperCase());
                if (ee != null && ee.getEmployeeID().equalsIgnoreCase(employeeID) == false)
                    blException.addException("panNumber", "Pan Number : " + panNumber + " exists");
            }
        }

        if (aadharNumber == null)
            blException.addException("aadharNumber", "Aadhar Number Required");
        else
        {
            aadharNumber = aadharNumber.trim();
            if (aadharNumber.length() == 0)
                blException.addException("aadharNumber", "Aadhar Number Required");
            else
            {
                EmployeeInterface ee = aadharNumberWiseEmployeeMap.get(aadharNumber.toUpperCase());
                if (ee != null && ee.getEmployeeID().equalsIgnoreCase(employeeID) == false)
                    blException.addException("aadharNumber", "Aadhar Number : " + aadharNumber + " exists");
            }
        }

        if (aadharNumber == null)
            blException.addException("aadharNumber", "aadhar Number Required");
        else
        {
            aadharNumber = aadharNumber.trim();
            if (aadharNumber.length() == 0)
                blException.addException("aadharNumber", "aadhar Number Required");
        }

        if (blException.hasExceptions())
            throw blException;
        try
        {
            EmployeeInterface dsEmployee;
            dsEmployee = this.employeeIDWiseEmployeeMap.get(employeeID.toLowerCase());
            String oldPanNumber = dsEmployee.getPanNumber();
            String oldAadharNumber = dsEmployee.getAadharNumber();
            int oldDesignationCode = dsEmployee.getDesignation().getCode();

            EmployeeDTOInterface dlEmployee;
            dlEmployee = new EmployeeDTO();

            dlEmployee.setEmployeeID(dsEmployee.getEmployeeID());
            dlEmployee.setName(name);
            dlEmployee.setDesignationCode(designationCode);
            dlEmployee.setGender((gender == 'M')?GENDER.MALE:GENDER.FEMALE);
            dlEmployee.setDateOfBirth(DOB);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setAadharNumber(aadharNumber);
            dlEmployee.setPanNumber(panNumber);
            dlEmployee.setIsIndian(isIndian);

            new EmployeeDAO().update(dlEmployee);
            employee.setEmployeeID(dlEmployee.getEmployeeID());

            dsEmployee.setEmployeeID(dlEmployee.getEmployeeID());
            dsEmployee.setName(name);
            designation = ((DesignationManager)designationManager).getDSDesignationByCode(designationCode);
            dsEmployee.setDesignation(designation);
            dsEmployee.setGender((gender == 'M')?GENDER.MALE:GENDER.FEMALE);
            dsEmployee.setDateOfBirth(DOB);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setAadharNumber(aadharNumber);
            dsEmployee.setPanNumber(panNumber);
            dsEmployee.setBasicSalary(basicSalary);

            this.employeeIDWiseEmployeeMap.remove(employeeID.toUpperCase());
            this.aadharNumberWiseEmployeeMap.remove(oldAadharNumber.toUpperCase());
            this.panNumberWiseEmployeeMap.remove(oldPanNumber.toUpperCase());
            this.employeesSet.remove(dsEmployee);

            if (oldDesignationCode != dsEmployee.getDesignation().getCode())
            {
                Set<EmployeeInterface> tempSet;
                tempSet = this.designationCodeWiseEmployeeMap.get(oldDesignationCode);
                tempSet.remove(dsEmployee);
                tempSet = this.designationCodeWiseEmployeeMap.get(dsEmployee.getDesignation().getCode());
                if (tempSet == null)
                {
                    tempSet = new TreeSet<>();
                    tempSet.add(employee);
                    this.designationCodeWiseEmployeeMap.put(designation.getCode(), tempSet);
                }
                else
                {
                    tempSet.add(employee);
                }
            }

            this.employeeIDWiseEmployeeMap.put(employeeID.toUpperCase(), dsEmployee);
            this.aadharNumberWiseEmployeeMap.put(aadharNumber.toUpperCase(), dsEmployee);
            this.panNumberWiseEmployeeMap.put(panNumber.toUpperCase(), dsEmployee);
            this.employeesSet.add(dsEmployee);
        }
        catch (DAOException daoe)
        {
            BLException bl = new BLException();
            bl.setGenericException(daoe.getMessage());
            throw bl;
        }
    }

    public void removeEmployee(String employeeID) throws BLException
    {
        BLException blException = new BLException();

        if (employeeID == null)
        {
            blException.addException(employeeID, "employeeID is null");
        }
        else
        {
            employeeID = employeeID.trim();
            if (employeeID.length() == 0)
                blException.addException(employeeID, "employeeID is null");
            else
                if (this.employeeIDWiseEmployeeMap.get(employeeID.toUpperCase()) == null)
                    blException.addException(employeeID, "employeeID not found!");
        }
        if (blException.hasExceptions())
            throw blException;
        
        try
        {
            EmployeeInterface dsEmployee;
            dsEmployee = this.employeeIDWiseEmployeeMap.get(employeeID.toUpperCase());
            String aadharNumber = dsEmployee.getAadharNumber();
            String panNumber = dsEmployee.getPanNumber();
            
            new EmployeeDAO().delete(dsEmployee.getEmployeeID());

            this.employeeIDWiseEmployeeMap.remove(employeeID.toUpperCase());
            this.aadharNumberWiseEmployeeMap.remove(aadharNumber.toUpperCase());
            this.panNumberWiseEmployeeMap.remove(panNumber.toUpperCase());
            this.employeesSet.remove(dsEmployee);
            
            Set<EmployeeInterface> tempSet;
            tempSet = this.designationCodeWiseEmployeeMap.get(dsEmployee.getDesignation().getCode());
            tempSet.remove(dsEmployee);
        }
        catch (DAOException daoe)
        {
            BLException bl = new BLException();
            bl.setGenericException(daoe.getMessage());
            throw bl;
        }
    }

    public Set<EmployeeInterface> getEmployees()
    {
        Set<EmployeeInterface> employees=new TreeSet<>();
        EmployeeInterface employee;
        DesignationInterface dsDesignation;
        DesignationInterface designation;

        for(EmployeeInterface dsEmployee:employeesSet)
        {
            employee=new Employee();
            employee.setEmployeeID(dsEmployee.getEmployeeID());
            employee.setName(dsEmployee.getName());

            dsDesignation=dsEmployee.getDesignation();
            designation=new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);

            employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
            employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPanNumber(dsEmployee.getPanNumber());
            employee.setAadharNumber(dsEmployee.getAadharNumber());
            
            employees.add(employee);
        }

        return employees;
    }
    
    public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException
    {
        DesignationManagerInterface designationManager;
        designationManager=DesignationManager.getDesignationManager();

        if(designationManager.designationCodeExists(designationCode)==false)
        {
            BLException blException=new BLException();
            blException.setGenericException("Invalid designation code : "+designationCode);
            throw blException;
        }

        Set<EmployeeInterface> employees=new TreeSet<>();
        Set<EmployeeInterface> ets;
        ets = this.designationCodeWiseEmployeeMap.get(designationCode);
        if(ets == null)
        {
            return employees;
        }

        EmployeeInterface employee;
        DesignationInterface dsDesignation;
        DesignationInterface designation;

        for(EmployeeInterface dsEmployee : ets)
        {
            employee=new Employee();
            employee.setEmployeeID(dsEmployee.getEmployeeID());
            employee.setName(dsEmployee.getName());
            dsDesignation=dsEmployee.getDesignation();
            designation=new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);
            employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
            employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPanNumber(dsEmployee.getPanNumber());
            employee.setAadharNumber(dsEmployee.getAadharNumber());
            employees.add(employee);
        }
        return employees;
    }

    public EmployeeInterface getEmployeeByEmployeeID(String employeeID) throws BLException
    {
        BLException blException = new BLException();
        
        if (employeeID == null)
        {
            blException.addException("employeeID", "employeeID is null");
            throw blException;
        }
        else
        {
            employeeID = employeeID.trim();

            if (employeeID.length() == 0)
            {
                blException.addException("employeeID", "employeeID is null");
                throw  blException;
            }
            if (this.employeeIDWiseEmployeeMap.containsKey(employeeID.toUpperCase()) == false)            
            {
                blException.addException("employeeID", "Invalid EmployeeID!");
                throw blException;
            }
        }

        EmployeeInterface dsEmployee = this.employeeIDWiseEmployeeMap.get(employeeID);

        EmployeeInterface employee = new Employee();
        employee.setEmployeeID(employeeID);
        employee.setName(dsEmployee.getName());

        DesignationInterface designation = new Designation();
        designation.setCode(dsEmployee.getDesignation().getCode());
        designation.setTitle(dsEmployee.getDesignation().getTitle());
        employee.setDesignation(designation);

        char gender = dsEmployee.getGender();
        Date DOB = dsEmployee.getDateOfBirth();
        boolean isIndian = dsEmployee.getIsIndian();
        String aadharNumber = dsEmployee.getAadharNumber();
        String panNumber = dsEmployee.getPanNumber();
        BigDecimal basicSalary = dsEmployee.getBasicSalary();

        employee.setGender((gender == 'M')?GENDER.MALE:GENDER.FEMALE);
        employee.setDateOfBirth(DOB);
        employee.setIsIndian(isIndian);
        employee.setAadharNumber(aadharNumber);
        employee.setPanNumber(panNumber);
        employee.setBasicSalary(basicSalary);

        return employee;
    }
    
    public EmployeeInterface getEmployeeByAadharNumber(String aadharNumber) throws BLException
    {
        EmployeeInterface dsEmployee = this.aadharNumberWiseEmployeeMap.get(aadharNumber.toUpperCase());

        if(dsEmployee == null)
        {
            BLException blException=new BLException();
            blException.addException("aadharNumber","Invalid Aadhar Number : " + aadharNumber);
            throw blException;
        }

        EmployeeInterface employee = new Employee();

        employee.setEmployeeID(dsEmployee.getEmployeeID());
        employee.setName(dsEmployee.getName());
        DesignationInterface dsDesignation = dsEmployee.getDesignation();
        DesignationInterface designation = new Designation();
        designation.setCode(dsDesignation.getCode());
        designation.setTitle(dsDesignation.getTitle());
        employee.setDesignation(designation);
        employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
        employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
        employee.setIsIndian(dsEmployee.getIsIndian());
        employee.setBasicSalary(dsEmployee.getBasicSalary());
        employee.setPanNumber(dsEmployee.getPanNumber());
        employee.setAadharNumber(dsEmployee.getAadharNumber());
        
        return employee;
    }
    public EmployeeInterface getEmployeeByPanNumber(String panNumber) throws BLException
    {
        EmployeeInterface dsEmployee = this.panNumberWiseEmployeeMap.get(panNumber.toUpperCase());

        if(dsEmployee==null)
        {
            BLException blException=new BLException();
            blException.addException("panNumber","Invalid Pan number : "+panNumber);
            throw blException;
        }
        EmployeeInterface employee=new Employee();
        employee.setEmployeeID(dsEmployee.getEmployeeID());
        employee.setName(dsEmployee.getName());
        DesignationInterface dsDesignation=dsEmployee.getDesignation();
        DesignationInterface designation=new Designation();
        designation.setCode(dsDesignation.getCode());
        designation.setTitle(dsDesignation.getTitle());
        employee.setDesignation(designation);
        employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
        employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
        employee.setIsIndian(dsEmployee.getIsIndian());
        employee.setBasicSalary(dsEmployee.getBasicSalary());
        employee.setPanNumber(dsEmployee.getPanNumber());
        employee.setAadharNumber(dsEmployee.getAadharNumber());
        return employee;
    }

    public boolean employeeIDExists(String employeeID)
    {
        return this.employeeIDWiseEmployeeMap.containsKey(employeeID);
    }
    public boolean employeeAadharNumberExists(String aadharNumber)
    {
        return this.aadharNumberWiseEmployeeMap.containsKey(aadharNumber);
    }
    public boolean employeePanNumberExists(String panNumber)
    {
        return this.panNumberWiseEmployeeMap.containsKey(panNumber);
    }
    public boolean isDesignationAlloted(int designationCode)
    {
        return this.designationCodeWiseEmployeeMap.containsKey(designationCode);
    }

    public int getEmployeeCount()
    {
        return this.employeesSet.size();
    }
    public int getEmployeeCountByDesignation(int designationCode)
    {
        Set<EmployeeInterface> ets;

        ets = this.designationCodeWiseEmployeeMap.get(designationCode);

        if (ets == null) return 0;
        else
        {
            return ets.size();
        }
    }
}