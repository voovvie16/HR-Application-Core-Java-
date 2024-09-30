package com.thinking.machines.hr.dl.dao;

import java.math.*;
import java.util.*;
import java.util.zip.DataFormatException;
import java.text.*;
import java.io.*;

import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;

public class EmployeeDAO implements EmployeeDAOInterface 
{
    private static String FILE_NAME = "Employee.data";

    public void add(EmployeeDTOInterface employeeDTO) throws DAOException 
    {
        if (employeeDTO == null)
            throw new DAOException("employeeDTO is null");

        String employeeID;

        String name = employeeDTO.getName();
        if (name == null)
            throw new DAOException("Name is null");
        name = name.trim();
        if (name.length() == 0)
            throw new DAOException("Length of name is 0");

        int designationCode = employeeDTO.getDesignationCode();
        if (designationCode <= 0)
            throw new DAOException("Invalid Designation Code : " + designationCode);
        DesignationDAO designationDAO;
        designationDAO = new DesignationDAO();
        if (!(designationDAO.codeExists(designationCode)))
            throw new DAOException("Invalid Designation Code : " + designationCode);

        Date DOB = employeeDTO.getDateOfBirth();
        if (DOB == null)
            throw new DAOException("Date of Birth is null");

        char gender = employeeDTO.getGender();

        boolean isIndian = employeeDTO.getIsIndian();

        BigDecimal basicSalary = employeeDTO.getBasicSalary();
        if (basicSalary == null)
            throw new DAOException("basic Salary is null");
        if (basicSalary.signum() == -1)
            throw new DAOException("basic Salary can not be negative");

        String pan = employeeDTO.getPanNumber();
        if (pan == null)
            throw new DAOException("Pan Number is null");
        pan = pan.trim();
        if (pan.length() == 0)
            throw new DAOException("Invalid Pan Number");

        String aadhar = employeeDTO.getAadharNumber();
        if (aadhar == null)
            throw new DAOException("Aadhar Number is null");
        aadhar = aadhar.trim();
        if (aadhar.length() == 0)
            throw new DAOException("Invalid Aadhar Number");

        try 
        {
            int lastGeneratedEmployeeId = 10000000;
            String lastGeneratedEmployeeIdString = "";
            int recordCount = 0;
            String recordCountString = "";

            File file = new File(FILE_NAME);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            if (raf.length() == 0) 
            {
                lastGeneratedEmployeeIdString = String.format("%10s", "10000000");
                raf.writeBytes(lastGeneratedEmployeeIdString + "\n");

                recordCountString = String.format("%-10s", "0");
                raf.writeBytes(recordCountString + "\n");
            } 
            else 
            {
                lastGeneratedEmployeeId = Integer.parseInt(raf.readLine().trim());
                recordCount = Integer.parseInt(raf.readLine().trim());
            }

            boolean panExist = false, aadharExist = false;

            String fpan, faadhar;
            while (raf.getFilePointer() < raf.length()) 
            {
                for (int i = 1; i <= 7; i++)
                    raf.readLine();

                fpan = raf.readLine();
                faadhar = raf.readLine();

                if (panExist == false && fpan.equalsIgnoreCase(pan))
                    panExist = true;

                if (aadharExist == false && faadhar.equalsIgnoreCase(aadhar))
                    aadharExist = true;

                if (panExist && aadharExist)
                    break;
            }

            if (panExist && aadharExist) 
            {
                raf.close();
                throw new DAOException("Pan Number : " + pan + " Exists,\nAadhar Number : " + aadhar + " Exists");
            }
            if (panExist) 
            {
                raf.close();
                throw new DAOException("Pan Number : " + pan + " Exists");
            }
            if (aadharExist) 
            {
                raf.close();
                throw new DAOException("Aadhar Number : " + aadhar + " Exists");
            }

            lastGeneratedEmployeeId++;
            employeeID = "A" + lastGeneratedEmployeeId;
            raf.writeBytes(employeeID + "\n");
            recordCount++;

            raf.writeBytes(name + "\n");

            raf.writeBytes(designationCode + "\n");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            raf.writeBytes(simpleDateFormat.format(DOB) + "\n");

            raf.writeBytes(gender + "\n");

            raf.writeBytes(isIndian + "\n");

            raf.writeBytes(basicSalary.toPlainString() + "\n");

            raf.writeBytes(pan + "\n");

            raf.writeBytes(aadhar + "\n");

            raf.seek(0);
            lastGeneratedEmployeeIdString = String.format("%-10d", lastGeneratedEmployeeId);
            recordCountString = String.format("%-10d", recordCount);
            raf.writeBytes(lastGeneratedEmployeeIdString + "\n");
            raf.writeBytes(recordCountString + "\n");
            raf.close();

            employeeDTO.setEmployeeID(employeeID);
        } catch (IOException io) {
            throw new DAOException(io.getMessage());
        }
    }

    public void update(EmployeeDTOInterface employeeDTO) throws DAOException 
    {
        if (employeeDTO == null)
            throw new DAOException("EmployeeDTO is null");
        
        String employeeID = employeeDTO.getEmployeeID();
        if (employeeID == null)
            throw new DAOException("EmployeeID is null");
        if (employeeID.length() == 0)
            throw new DAOException("Length of EmployeeID is zero");
        
        String name = employeeDTO.getName();
        if (name == null)
            throw new DAOException("name is null");
        name = name.trim();
        if (name.length() == 0)
            throw new DAOException("Length of Name is zero");

        int designationCode = employeeDTO.getDesignationCode();
        if (designationCode <= 0)
            throw new DAOException("Designation Code is less than zero");
        DesignationDAOInterface designationDAO;
        designationDAO = new DesignationDAO();
        if (!designationDAO.codeExists(designationCode))
            throw new DAOException("Designation Code does not exist");

        Date DOB = employeeDTO.getDateOfBirth();
        if (DOB == null)
            throw new DAOException("Date of Birth is null");

        char gender = employeeDTO.getGender();
        if (gender == ' ')
            throw new DAOException("Gender not set to Male/Female");

        boolean isIndian = employeeDTO.getIsIndian();
        
        BigDecimal basicSalary = employeeDTO.getBasicSalary();
        if (basicSalary == null)
            throw new DAOException("Basic Salary is null");
        if (basicSalary.signum() == -1)
            throw new DAOException("Basic Salary is negative");
        
        String pan = employeeDTO.getPanNumber();
        if (pan == null)
            throw new DAOException("Pan Number is null");
        pan = pan.trim();
        if (pan.length() == 0)
            throw new DAOException("Length of Pan Number is zero");

        String aadhar = employeeDTO.getAadharNumber();
        if (aadhar == null)
            throw new DAOException("Aadhar Number is null");
        aadhar = aadhar.trim();
        if (aadhar.length() == 0)
            throw new DAOException("Length of Aadhar Number is zero");

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                throw new DAOException("Invalid Employee ID : " + employeeID);    
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0)
            {
                raf.close();
                throw new DAOException("Invalid Employee ID : " + employeeID);
            }

            raf.readLine(); raf.readLine();

            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("dd/MM/yyyy");

            String fEmployeeID;
            String fname;
            int fDesignationCode;           
            Date fDateOfBirth;
            char fGender;
            boolean fIsIndian;
            BigDecimal fBasicSalary;
            String fPanNumber;
            String fAadharNumber;

            long foundAt = 0;
            boolean employeeIDFound = false;
            boolean panFound = false;
            boolean aadharFound = false;

            String panFoundAgainstEmployeeID = "";
            String aadharFoundAgainstEmployeeID = "";

            int x;

            while (raf.getFilePointer() < raf.length())
            {
                if (employeeIDFound == false) foundAt = raf.getFilePointer();

                fEmployeeID = raf.readLine();

                for (x = 1; x <= 6; x++)
                    raf.readLine();
                
                fPanNumber = raf.readLine();
                fAadharNumber = raf.readLine();

                if (employeeIDFound == false && fEmployeeID.equalsIgnoreCase(employeeID))
                    employeeIDFound = true;

                if (panFound == false && fPanNumber.equalsIgnoreCase(pan))
                {
                    panFound = true;
                    panFoundAgainstEmployeeID = fEmployeeID;
                }
                if (aadharFound == false && fAadharNumber.equalsIgnoreCase(aadhar))
                {
                    aadharFound = true;
                    aadharFoundAgainstEmployeeID = fEmployeeID;
                }

                if (employeeIDFound && aadharFound && panFound)
                    break;
            }

            if (employeeIDFound == false)
            {
                raf.close();
                throw new DAOException("Invalid Employee ID : " + employeeID);
            }

            boolean panExist = false, aadharExist = false;
            if (panFound && panFoundAgainstEmployeeID.equalsIgnoreCase(employeeID) == false)
                panExist = true;
            
            if (aadharFound && aadharFoundAgainstEmployeeID.equalsIgnoreCase(employeeID) == false)
                aadharExist = true;

            if (aadharExist && panExist)
            {
                raf.close();
                throw new DAOException("Aadhar Card : " + aadhar + " Exists\n" + "Pan Number : " + pan + " Exists");
            }
            if (panExist)
            {
                raf.close();
                throw new DAOException("Pan Number : " + pan + " Exists");
            }
            if (aadharExist)
            {
                raf.close();
                throw new DAOException("Aadhar Card : " + aadhar + " Exists");
            }

            raf.seek(foundAt);
            for (x = 1; x <= 9; x++)
                raf.readLine();

            File tmpFile = new File("tmpEmployee.data");
            RandomAccessFile traf = new RandomAccessFile(tmpFile, "rw");
            
            while (raf.getFilePointer() < raf.length())
            {
                traf.writeBytes(raf.readLine() + "\n");
            }
            raf.seek(foundAt);

            raf.writeBytes(employeeID + "\n");
            raf.writeBytes(name + "\n");
            raf.writeBytes(designationCode + "\n");
            raf.writeBytes(sdf.format(DOB) + "\n");
            raf.writeBytes(gender + "\n");
            raf.writeBytes(isIndian + "\n");
            raf.writeBytes(basicSalary.toPlainString() + "\n");
            raf.writeBytes(pan + "\n");
            raf.writeBytes(aadhar + "\n");

            traf.seek(0);
            while (traf.getFilePointer() < traf.length())
            {
                raf.writeBytes(traf.readLine() + "\n");
            }

            raf.setLength(raf.getFilePointer());
            raf.close();
            traf.setLength(0);
            raf.close();
            traf.close();
        }
        catch (IOException io)
        {
            throw new DAOException("Nahi chlra bhai Update");
        }
    }

    public void delete(String employeeID) throws DAOException 
    {        
        if (employeeID == null)
            throw new DAOException("EmployeeID is null");
        if (employeeID.length() == 0)
            throw new DAOException("Length of EmployeeID is zero");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                throw new DAOException("Invalid Employee ID : " + employeeID);    
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0)
            {
                raf.close();
                throw new DAOException("Invalid Employee ID : " + employeeID);
            }

            raf.readLine(); 
            int recordCount = Integer.parseInt(raf.readLine().trim());

            long foundAt = 0;
            boolean employeeIDFound = false;
            
            int x;
            String fEmployeeID;
            while (raf.getFilePointer() < raf.length())
            {
                foundAt = raf.getFilePointer();

                fEmployeeID = raf.readLine();

                for (x = 1; x <= 8; x++)
                    raf.readLine();

                if (fEmployeeID.equalsIgnoreCase(employeeID))
                {
                    employeeIDFound = true;
                    break;
                }
            }

            if (employeeIDFound == false)
            {
                raf.close();
                throw new DAOException("Invalid Employee ID : " + employeeID);
            }

            File tmpFile = new File("tmpEmployee.data");
            RandomAccessFile traf = new RandomAccessFile(tmpFile, "rw");
            
            while (raf.getFilePointer() < raf.length())
            {
                traf.writeBytes(raf.readLine() + "\n");
            }
            raf.seek(foundAt);

            traf.seek(0);
            while (traf.getFilePointer() < traf.length())
            {
                raf.writeBytes(traf.readLine() + "\n");
            }

            raf.setLength(raf.getFilePointer());
            recordCount--;
            
            String recordCountString = String.format("%-10d", recordCount);
            raf.seek(0);
            raf.readLine();
            raf.writeBytes(recordCountString + "\n");

            raf.close();
            traf.setLength(0);
            raf.close();
            traf.close();
        }
        catch (IOException io)
        {
            throw new DAOException("Nahi chlra bhai Update");
        }
    }

    public Set<EmployeeDTOInterface> getAll() throws DAOException 
    {
        Set<EmployeeDTOInterface> employees;
        employees = new TreeSet<>();

        try 
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                return employees;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return employees;
            }

            raf.readLine();
            raf.readLine();

            EmployeeDTOInterface employeeDTO;

            char fgender;

            while (raf.getFilePointer() < raf.length()) 
            {
                employeeDTO = new EmployeeDTO();

                employeeDTO.setEmployeeID(raf.readLine().trim());
                employeeDTO.setName(raf.readLine());
                employeeDTO.setDesignationCode(Integer.parseInt(raf.readLine()));

                String dateString;
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try 
                {
                    dateString = raf.readLine();
                    d = sdf.parse(dateString);
                } 
                catch (ParseException pe) 
                {
                    //do nothing
                }
                employeeDTO.setDateOfBirth(d);

                fgender = raf.readLine().charAt(0);
                if (fgender == 'M') employeeDTO.setGender(GENDER.MALE);
                else if (fgender == 'F') employeeDTO.setGender(GENDER.FEMALE);
                
                employeeDTO.setIsIndian(Boolean.parseBoolean(raf.readLine()));
                employeeDTO.setBasicSalary(new BigDecimal(raf.readLine()));
                employeeDTO.setPanNumber(raf.readLine());
                employeeDTO.setAadharNumber(raf.readLine());

                employees.add(employeeDTO);
            }

            raf.close();
            return employees;
        } 
        catch (IOException io) 
        {
            throw new DAOException(io.getMessage());
        }
    }

    public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
    {
        if (designationCode <= 0) throw new DAOException("Invalid Desigation Code");
        
        if (!(new DesignationDAO().codeExists(designationCode)))
            throw new DAOException("Invalid Designation Code : " + designationCode);

        Set<EmployeeDTOInterface> employees;
        employees = new TreeSet<>();

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists()) return employees;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return employees;
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;
            String EmployeeID, name;
            int fdesignationCode = 0;
            char fgender;
            
            while (raf.getFilePointer() < raf.length())
            {
                EmployeeID = raf.readLine();
                name = raf.readLine();
                fdesignationCode = Integer.parseInt(raf.readLine());

                if (fdesignationCode == designationCode)
                {
                    employeeDTO = new EmployeeDTO();
                    employeeDTO.setEmployeeID(EmployeeID);
                    employeeDTO.setName(name);
                    employeeDTO.setDesignationCode(designationCode);

                    String dateString;
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {
                        dateString = raf.readLine();
                        d = sdf.parse(dateString);
                    }
                    catch (ParseException pe)
                    {
                        //do nothing
                    }
                    employeeDTO.setDateOfBirth(d);

                    fgender = raf.readLine().charAt(0);
                    if (fgender == 'M') employeeDTO.setGender(GENDER.MALE);
                    else if (fgender == 'F') employeeDTO.setGender(GENDER.FEMALE);
                    employeeDTO.setIsIndian(Boolean.parseBoolean(raf.readLine()));
                    employeeDTO.setBasicSalary(new BigDecimal(raf.readLine()));
                    employeeDTO.setPanNumber(raf.readLine());
                    employeeDTO.setAadharNumber(raf.readLine());

                    employees.add(employeeDTO);
                }
                else for (int i = 0; i < 6; i++) raf.readLine();
            }

            raf.close();
            return employees;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public EmployeeDTOInterface getByAadharNumber(String aadharNumber) throws DAOException 
    {
        if (aadharNumber == null) throw new DAOException("Invalid Aadhar Number is Null");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                throw new DAOException("Invalid Aadhar Number");

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                throw new DAOException("Invalid Aadhar Number");
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;

            String EmployeeID, name;
            int designationCode = 0;
            char gender;
            boolean isIndian;
            BigDecimal basicSalary;
            String fpan;
            String faadhar;
            char fgender;
            
            while (raf.getFilePointer() < raf.length())
            {
                EmployeeID = raf.readLine();
                name = raf.readLine();
                designationCode = Integer.parseInt(raf.readLine());
                String DOB;
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try
                {
                    DOB = raf.readLine();
                    d = sdf.parse(DOB);
                }
                catch (ParseException pe)
                {
                    //do nothing
                }
                fgender = raf.readLine().charAt(0);
                isIndian = Boolean.parseBoolean(raf.readLine());
                basicSalary = new BigDecimal(raf.readLine());
                fpan = raf.readLine();
                faadhar = raf.readLine();

                if (faadhar.equalsIgnoreCase(aadharNumber))
                {
                    employeeDTO = new EmployeeDTO();

                    employeeDTO.setEmployeeID(EmployeeID);
                    employeeDTO.setName(name);
                    employeeDTO.setDesignationCode(designationCode);
                    employeeDTO.setDateOfBirth(d);
                    if (fgender == 'M') employeeDTO.setGender(GENDER.MALE);
                    else if (fgender == 'F') employeeDTO.setGender(GENDER.FEMALE);
                    employeeDTO.setIsIndian(isIndian);
                    employeeDTO.setBasicSalary(basicSalary);
                    employeeDTO.setPanNumber(fpan);
                    employeeDTO.setAadharNumber(faadhar);

                    return employeeDTO;
                }
            }

            raf.close();
            throw new DAOException("Invalid Aadhar Number");
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public EmployeeDTOInterface getByPanNumber(String panNumber) throws DAOException 
    {
        if (panNumber == null) throw new DAOException("Invalid Pan Number is Null");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                throw new DAOException("Invalid Pan Number");

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                throw new DAOException("Invalid Pan Number");
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;

            String EmployeeID, name;
            int designationCode = 0;
            char fgender;
            boolean isIndian;
            BigDecimal basicSalary;
            String fpan;
            String faadhar;
            
            while (raf.getFilePointer() < raf.length())
            {
                EmployeeID = raf.readLine();
                name = raf.readLine();
                designationCode = Integer.parseInt(raf.readLine());
                String DOB;
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try
                {
                    DOB = raf.readLine();
                    d = sdf.parse(DOB);
                }
                catch (ParseException pe)
                {
                    //do nothing
                }
                fgender = raf.readLine().charAt(0);
                isIndian = Boolean.parseBoolean(raf.readLine());
                basicSalary = new BigDecimal(raf.readLine());
                fpan = raf.readLine();
                faadhar = raf.readLine();

                if (fpan.equalsIgnoreCase(panNumber))
                {
                    employeeDTO = new EmployeeDTO();

                    employeeDTO.setEmployeeID(EmployeeID);
                    employeeDTO.setName(name);
                    employeeDTO.setDesignationCode(designationCode);
                    employeeDTO.setDateOfBirth(d);
                    if (fgender == 'M') employeeDTO.setGender(GENDER.MALE);
                    else if (fgender == 'F') employeeDTO.setGender(GENDER.FEMALE);
                    employeeDTO.setIsIndian(isIndian);
                    employeeDTO.setBasicSalary(basicSalary);
                    employeeDTO.setPanNumber(fpan);
                    employeeDTO.setAadharNumber(faadhar);

                    return employeeDTO;
                }
            }

            raf.close();
            throw new DAOException("Invalid Pan Number");
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public boolean isDesignationAlloted(int designationCode) throws DAOException 
    {
        if (designationCode <= 0) throw new DAOException("Invalid Desigation Code");
        
        if (!(new DesignationDAO().codeExists(designationCode)))
            throw new DAOException("Invalid Designation Code : " + designationCode);

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists()) return false;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return false;
            }

            raf.readLine(); raf.readLine();

            int fdesignationCode;
            
            while (raf.getFilePointer() < raf.length())
            {
                raf.readLine(); raf.readLine();
                fdesignationCode = Integer.parseInt(raf.readLine());

                if (fdesignationCode == designationCode)
                {
                    raf.close();
                    return true;
                }
                else for (int i = 0; i < 6; i++) raf.readLine();
            }

            raf.close();
            return false;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public boolean employeeIDExists(String employeeID) throws DAOException 
    {
        if (employeeID == null) throw new DAOException("EmployeeID is Null");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                return false;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return false;
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;

            String fEmployeeID;
            
            while (raf.getFilePointer() < raf.length())
            {
                fEmployeeID = raf.readLine().trim();
                if (fEmployeeID.equalsIgnoreCase(employeeID))
                {
                    raf.close();
                    return true;
                }
                for (int i = 1; i < 9; i++) raf.readLine();
            }

            raf.close();
            return false;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public boolean aadharNumberExists(String aadharNumber) throws DAOException 
    {
        if (aadharNumber == null) throw new DAOException("AadharCard Number is Null");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                return false;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return false;
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;

            String fAadhar;
            
            while (raf.getFilePointer() < raf.length())
            {
                for (int i = 1; i < 9; i++) raf.readLine();
                fAadhar = raf.readLine();
                if (fAadhar.equalsIgnoreCase(aadharNumber))
                {
                    raf.close();
                    return true;
                }
            }

            raf.close();
            return false;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public boolean panNumberExists(String panNumber) throws DAOException 
    {
        if (panNumber == null) throw new DAOException("Pan Number is Null");
        
        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists())
                return false;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return false;
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;

            String fPan;
            
            while (raf.getFilePointer() < raf.length())
            {
                for (int i = 1; i < 8; i++) raf.readLine();
                fPan = raf.readLine();
                if (fPan.equalsIgnoreCase(panNumber))
                {
                    raf.close();
                    return true;
                }
                raf.readLine();
            }

            raf.close();
            return false;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public int getCount() throws DAOException 
    {
        int recordCount = 0;

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists()) return 0;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return 0;
            }

            raf.readLine();
            recordCount = Integer.parseInt(raf.readLine().trim()); 
            
            raf.close();
            return recordCount;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public int getCountByDesignation(int designationCode) throws DAOException 
    {
        if (designationCode <= 0) throw new DAOException("Invalid Desigation Code");
        
        if (!(new DesignationDAO().codeExists(designationCode)))
            throw new DAOException("Invalid Designation Code : " + designationCode);

        int designationCount = 0;

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists()) return 0;

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0) 
            {
                raf.close();
                return 0;
            }

            raf.readLine(); raf.readLine();

            EmployeeDTOInterface employeeDTO;
            String EmployeeID, name;
            int fdesignationCode = 0;
            
            while (raf.getFilePointer() < raf.length())
            {
                raf.readLine(); raf.readLine();
                fdesignationCode = Integer.parseInt(raf.readLine());

                if (fdesignationCode == designationCode)
                    designationCount++;
                for (int i = 0; i < 6; i++) raf.readLine();
            }

            raf.close();
            return designationCount;
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

    public EmployeeDTOInterface getByEmployeeID(String EmployeeID) throws DAOException
    {
        if (EmployeeID == null) throw new DAOException("EmployeeID is null");

        try
        {
            File file = new File(FILE_NAME);
            if (!file.exists()) throw new DAOException("EmployeeID is null");

            RandomAccessFile raf;
            raf = new RandomAccessFile(file, "rw");
            if (raf.length() == 0)
                throw new DAOException("EmployeeID is null");

            raf.readLine(); raf.readLine();
            
            String fEmployeeID;
            EmployeeDTOInterface employeeDTO;

            while (raf.getFilePointer() < raf.length())
            {
                fEmployeeID = raf.readLine().trim();

                if (fEmployeeID.equalsIgnoreCase(EmployeeID))
                {
                    String name = raf.readLine();
                    int designationCode = Integer.parseInt(raf.readLine());
                    String DOB = raf.readLine();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {
                        d = sdf.parse(DOB);
                    }
                    catch (ParseException pe)
                    {
                        // do nothing
                    }
                    char fgender = raf.readLine().charAt(0);
                    boolean isIndian = Boolean.parseBoolean(raf.readLine());
                    BigDecimal basicSalary = new BigDecimal(raf.readLine());
                    String pan = raf.readLine();
                    String aadhar = raf.readLine();

                    employeeDTO = new EmployeeDTO();

                    employeeDTO.setEmployeeID(EmployeeID);
                    employeeDTO.setName(name);
                    employeeDTO.setDesignationCode(designationCode);
                    employeeDTO.setDateOfBirth(d);
                    if (fgender == 'M') employeeDTO.setGender(GENDER.MALE);
                    else if (fgender == 'F') employeeDTO.setGender(GENDER.FEMALE);
                    employeeDTO.setBasicSalary(basicSalary);
                    employeeDTO.setIsIndian(isIndian);
                    employeeDTO.setAadharNumber(aadhar);
                    employeeDTO.setPanNumber(pan);
                    return employeeDTO;
                }
                else
                    for (int i = 0; i < 8; i++) raf.readLine();
            }

            raf.close();
            throw new DAOException("Invalid EmployeeID");
        }
        catch (IOException io)
        {
            throw new DAOException(io.getMessage());
        }
    }

}