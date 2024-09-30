import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import java.util.*;

public class DesignationManagerDesignationCodeExistsTestCase 
{
    public static void main(String args[]) 
    {
        int code = Integer.parseInt(args[0]);
        try 
        {
            DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
            System.out.println(code + " exists : " + designationManager.designationCodeExists(code));
        } 
        catch (BLException blException) 
        {
            if (blException.hasGenericException())
                System.out.println(blException.getGenericException());
            List<String> properties = blException.getProperties();
            for (String property : properties) {
                System.out.println(blException.getException(property));
            }
        }
    }
}
