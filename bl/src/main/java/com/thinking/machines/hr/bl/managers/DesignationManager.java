package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;

import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;

import java.util.*;

public class DesignationManager implements DesignationManagerInterface 
{
    private Map<Integer, DesignationInterface> codeWiseDesignationMap;
    private Map<String, DesignationInterface> titleWiseDesignationMap;
    private Set<DesignationInterface> designationsSet;

    private static DesignationManager designationManager = null;

    // private constructor
    private DesignationManager() throws BLException
    {
        populateDataStructures();
    }

    private void populateDataStructures() throws BLException
    {
        this.codeWiseDesignationMap = new HashMap<>();
        this.titleWiseDesignationMap = new HashMap<>();
        this.designationsSet = new TreeSet<>();

        try
        {
            DesignationDAOInterface eg = new DesignationDAO();
            Set<DesignationDTOInterface> dlDesignations = eg.getAll();
    
            DesignationInterface designation;
    
            for (DesignationDTOInterface dlDesignation: dlDesignations)
            {
                designation = new Designation();
                designation.setCode(dlDesignation.getCode());
                designation.setTitle(dlDesignation.getTitle());
    
                this.codeWiseDesignationMap.put(designation.getCode(), designation);
                this.titleWiseDesignationMap.put(designation.getTitle().toUpperCase(), designation);
                this.designationsSet.add(designation);
            }
        }
        catch (DAOException daoException)
        {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    public static DesignationManagerInterface getDesignationManager() throws BLException
    {
        if (designationManager == null) designationManager = new DesignationManager();
        return designationManager;
    }

    public void addDesignation(DesignationInterface designation) throws BLException
    {
        BLException blException = new BLException();
        if (designation == null)
        {
            blException.setGenericException("Designation required");
            throw blException;
        }
        String title = designation.getTitle();
        int code = designation.getCode();

        if (code != 0)
        {
            blException.addException("code", "Code should be zero");
        }
        if (title == null)
        {
            blException.addException("title", "Title required");
            title = "";
        }
        else
        {
            title = title.trim();
            if (title.length() == 0)
            {
                blException.addException("title", "Title required");
            }
        }
        if (title.length() > 0)
        {
            if (this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
            {
                blException.addException("title", "Designation : " + title + " already exists.");
            }
        }

        if (blException.hasExceptions())
            throw blException;
        
        try
        {
            DesignationDTOInterface designationDTO;
            designationDTO = new DesignationDTO();
            designationDTO.setTitle(title);

            DesignationDAOInterface designationDAO;
            designationDAO = new DesignationDAO();

            designationDAO.add(designationDTO);

            code = designationDTO.getCode();
            designation.setCode(code);

            DesignationInterface dsDesignation;
            dsDesignation = new Designation();
            dsDesignation.setCode(designation.getCode());
            dsDesignation.setTitle(title);

            codeWiseDesignationMap.put(code, dsDesignation);
            titleWiseDesignationMap.put(title.toUpperCase(), dsDesignation);
            designationsSet.add(dsDesignation);

        }
        catch (DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }
    }   

    public void updateDesignation(DesignationInterface designation) throws BLException
    {
        BLException blException = new BLException();
        if (designation == null)
        {
            blException.setGenericException("Designation required");
            throw blException;
        }
        String title = designation.getTitle();
        int code = designation.getCode();

        if (code <= 0)
        {
            blException.addException("code", "Invalid Code : " + code);
        }
        if (code > 0)
        {
            if(!this.codeWiseDesignationMap.containsKey(code))
            {
                blException.addException("code", "Invalid Code : " + code);
                throw blException;
            }
        }
        if (title == null)
        {
            blException.addException("title", "Title required");
            title = "";
        }
        else
        {
            title = title.trim();
            if (title.length() == 0)
            {
                blException.addException("title", "Title required");
            }
        }
        if (title.length() > 0)
        {
            DesignationInterface d;
            d = this.titleWiseDesignationMap.get(title.toUpperCase());
            
            if (d != null && d.getCode() != code)
            {
                blException.addException("title", "Designation : " + title + " exists");
            }
        }

        if (blException.hasExceptions())
            throw blException;
        
        try
        {
            DesignationInterface dsDesignation;
            dsDesignation = this.codeWiseDesignationMap.get(code);
            
            DesignationDTOInterface dlDesignation;
            dlDesignation = new DesignationDTO();
            dlDesignation.setCode(code);
            dlDesignation.setTitle(title);
                        
            new DesignationDAO().update(dlDesignation);

            // removing the old designation from the data structures
            this.codeWiseDesignationMap.remove(code);
            this.titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
            this.designationsSet.remove(dsDesignation);

            // updating the title
            dsDesignation.setTitle(title);

            //inserting the new object
            codeWiseDesignationMap.put(code, dsDesignation);
            titleWiseDesignationMap.put(title.toUpperCase(), dsDesignation);
            designationsSet.add(dsDesignation);
        }
        catch (DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }
    }
    
    public void removeDesignation(int code) throws BLException
    {
        BLException blException = new BLException();

        if (code <= 0)
        {
            blException.addException("code", "Invalid Code : " + code);
            throw blException;
        }
        if (code > 0)
        {
            if(!this.codeWiseDesignationMap.containsKey(code))
             {
                blException.addException("code", "Invalid Code : " + code);
                throw blException;
            }
        }

        try
        {
            DesignationInterface dsDesignation;
            dsDesignation = this.codeWiseDesignationMap.get(code);
                                  
            new DesignationDAO().delete(code);

            // removing the old designation from the data structures
            this.codeWiseDesignationMap.remove(code);
            this.titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
            this.designationsSet.remove(dsDesignation);
        }
        catch (DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
        }
    }

    public DesignationInterface getDesignationByCode(int code) throws BLException
    {
        DesignationInterface designation;
        designation = this.codeWiseDesignationMap.get(code);

        if (designation == null)
        {
            BLException bl = new BLException();
            bl.addException("code", "Invalid Code : "+ code);
            throw bl;
        }
        DesignationInterface d = new Designation();
        d.setCode(code);
        d.setTitle(designation.getTitle());
        return d;
    }
    
    public DesignationInterface getDesignationByTitle(String title) throws BLException
    { 
        DesignationInterface designation = this.titleWiseDesignationMap.get(title.toUpperCase());
        
        if (designation == null)
        {
            BLException blException = new BLException();
            blException.addException("title", "Invalid Title : "+ title);
            throw blException;
        }

        DesignationInterface d = new Designation();
        d.setCode(designation.getCode());
        d.setTitle(designation.getTitle());
        return d;
    }

    public int getDesignationCount() throws BLException
    {
        return this.designationsSet.size();
    }

    public boolean designationCodeExists(int code) throws BLException
    {
        return this.codeWiseDesignationMap.containsKey(code);
    }

    public boolean designationTitleExists(String title) throws BLException
    {
        return this.titleWiseDesignationMap.containsKey(title);
    }

    public Set<DesignationInterface> getDesignations() throws BLException
    {
        Set<DesignationInterface> designations;
        designations = new TreeSet<>();

        designationsSet.forEach((designation) -> {
            DesignationInterface di;
            di = new Designation();
            di.setCode(designation.getCode());
            di.setTitle(designation.getTitle());

            designations.add(di);
        });

        return designations;
    }

    public DesignationInterface getDSDesignationByCode(int code) throws BLException
    {
        DesignationInterface designation;
        designation = this.codeWiseDesignationMap.get(code);
        return designation;
    }
}

