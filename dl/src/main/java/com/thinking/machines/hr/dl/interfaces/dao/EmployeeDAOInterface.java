package com.thinking.machines.hr.dl.interfaces.dao;

import java.util.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;

public interface EmployeeDAOInterface 
{
    public void add(EmployeeDTOInterface employeeDTO) throws DAOException;

    public void update(EmployeeDTOInterface designationDTO) throws DAOException;

    public void delete(String employeeID) throws DAOException;

    public Set<EmployeeDTOInterface> getAll() throws DAOException;

    public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException;
    public EmployeeDTOInterface getByEmployeeID(String EmployeeID) throws DAOException;
    public EmployeeDTOInterface getByAadharNumber(String aadharNumber) throws DAOException;
    public EmployeeDTOInterface getByPanNumber(String panNumber) throws DAOException;

    public boolean employeeIDExists(String employeeID) throws DAOException;
    public boolean aadharNumberExists(String aadharNumber) throws DAOException;
    public boolean panNumberExists(String panNumber) throws DAOException;
    public boolean isDesignationAlloted(int designationCode) throws DAOException;

    public int getCount() throws DAOException;
    public int getCountByDesignation(int designationCode) throws DAOException;
}