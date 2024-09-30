import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;

public class DesignationAddTestCase
{
public static void main(String[] args)
{
String title = args[0];

try 
{
DesignationDTOInterface designationDTO;
designationDTO = new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO;
designationDAO = new DesignationDAO();
designationDAO.add(designationDTO);
System.out.println("Designation with title : " + title + " added with code : " + designationDTO.getCode());
}
catch(DAOException daoE)
{
System.out.println(daoE.getMessage());
}
}
}
