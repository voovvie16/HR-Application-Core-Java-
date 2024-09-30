import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import java.util.*;

public class DesignationManagerGetDesignationsTestCase 
{
    public static void main(String gg[]) 
    {
        try 
        {
            DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
            Set<DesignationInterface> designations;
            designations = designationManager.getDesignations();
            for (DesignationInterface designation : designations) {
                System.out.println("Code : " + designation.getCode() + " Title : " + designation.getTitle());
            }
        } 
        catch (BLException blException) 
        {
            if (blException.hasGenericException())
                System.out.println(blException.getGenericException());
            List<String> properties = blException.getProperties();
            for (String property : properties) 
            {
                System.out.println(blException.getException(property));
            }
        }
    }
}
