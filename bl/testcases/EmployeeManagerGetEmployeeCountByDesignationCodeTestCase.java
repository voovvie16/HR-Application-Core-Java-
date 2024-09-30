import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import java.util.*;

public class EmployeeManagerGetEmployeeCountByDesignationCodeTestCase 
{
    public static void main(String gg[]) 
    {
        int designationCode = Integer.parseInt(gg[0]);
        try 
        {
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            System.out.println("Employee count : " + employeeManager.getEmployeeCountByDesignation(designationCode));
        } 
        catch (BLException blException) 
        {
            if (blException.hasGenericException())
                System.out.println(blException.getGenericException());

            List<String> properties = blException.getProperties();
            properties.forEach((property) -> {
                System.out.println(blException.getException(property));
            });
        }
    }
}
