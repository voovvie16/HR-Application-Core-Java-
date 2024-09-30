import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;


public class DesignationDAOTitleExistsTestCase
{
public static void main(String[] args)
{
try
{
String title = args[0];

DesignationDAOInterface designationDAO;
designationDAO = new DesignationDAO();

System.out.println("Title : " + title + " exists : " + designationDAO.titleExists(title));
}
catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}
}
}