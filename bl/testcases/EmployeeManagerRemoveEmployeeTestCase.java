import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import java.util.*;

public class EmployeeManagerRemoveEmployeeTestCase 
{
    public static void main(String args[]) 
    {
        String employeeId = args[0];
        try 
        {
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            employeeManager.removeEmployee(employeeId);
            System.out.println("Employee removed");
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
