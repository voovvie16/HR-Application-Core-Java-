import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.enums.*;
import java.text.*;
import java.util.*;
import java.math.*;

public class EmployeeManagerAddEmployeeTestCase 
{
    public static void main(String gg[]) 
    {
        String name = "Priyanka Raut";
        DesignationInterface designation = new Designation();
        designation.setCode(1);
        char gender = 'F';
        boolean isIndian = false;
        Date dateOfBirth = new Date();
        BigDecimal basicSalary = new BigDecimal("199114");
        String panNumber = "PAN7545";
        String aadharCardNumber = "UID40210";
        try 
        {
            EmployeeInterface employee = new Employee();
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
            employeeManager.addEmployee(employee);

            System.out.println("Emloyee added with employee ID : " + employee.getEmployeeID());
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
