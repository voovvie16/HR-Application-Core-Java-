import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;


public class DesignationCodeExistsTestCase
{
public static void main(String[] args)
{
try
{
int code = Integer.parseInt(args[0]);

DesignationDAOInterface designationDAO;
designationDAO = new DesignationDAO();

System.out.println("Code : " + code + "exists : " + designationDAO.codeExists(code));
}
catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}
}
}