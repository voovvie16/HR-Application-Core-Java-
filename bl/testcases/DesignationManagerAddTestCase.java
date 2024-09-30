import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

import java.util.*;

class DesignationManagerAddTestCase
{
    public static void main(String[] args)
    {
        DesignationInterface designation;
        designation = new Designation();

        designation.setTitle("CEO");

        try
        {
            DesignationManagerInterface designationManager;
            designationManager = DesignationManager.getDesignationManager();

            designationManager.addDesignation(designation);

            System.out.println("Designation : " + designation.getTitle() + " added with Code : " + designation.getCode());
        }
        catch (BLException blException)
        {
            if (blException.hasGenericException())
            {
                System.out.println(blException.getGenericException());
            }
            List<String> properties = blException.getProperties();

            for (String property: properties)
            {
                System.out.println(blException.getException(property));
            }
        }
    }
}