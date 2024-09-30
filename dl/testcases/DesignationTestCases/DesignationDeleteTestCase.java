import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;

public class DesignationDeleteTestCase
{
public static void main(String[] args)
{
int code = Integer.parseInt(args[0]);

try
{
DesignationDAOInterface designationDAO;
designationDAO = new DesignationDAO();

System.out.println(code);
designationDAO.delete(code);

System.out.println("Code : " + code + " deleted.");
}
catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}



}
}
