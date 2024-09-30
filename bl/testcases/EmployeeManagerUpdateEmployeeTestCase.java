import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.enums.*;
import java.text.*;
import java.util.*;
import java.math.*;

public class EmployeeManagerUpdateEmployeeTestCase 
{
    public static void main(String gg[]) 
    {
        String employeeId = "A10000004";
        String name = "Neha Singh";
        DesignationInterface designation = new Designation();
        designation.setCode(2);
        Date dateOfBirth = new Date();
        char gender = 'F';
        boolean isIndian = true;
        BigDecimal basicSalary = new BigDecimal("11111");
        String panNumber = "PAN0181";
        String aadharCardNumber = "UID7011";
        
        try 
        {
            EmployeeInterface employee = new Employee();
            employee.setEmployeeID(employeeId);
            employee.setName(name);
            employee.setDesignation(designation);
            employee.setDateOfBirth(dateOfBirth);
            if (gender == 'M')
                employee.setGender(GENDER.MALE);
            if (gender == 'F')
                employee.setGender(GENDER.FEMALE);
            employee.setIsIndian(isIndian);
            employee.setBasicSalary(basicSalary);
            employee.setPanNumber(panNumber);
            employee.setAadharNumber(aadharCardNumber);
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            employeeManager.updateEmployee(employee);
            System.out.println("Emloyee updated");
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