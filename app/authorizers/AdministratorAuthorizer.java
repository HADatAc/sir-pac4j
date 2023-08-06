package authorizers;

//import org.apache.commons.lang.StringUtils;
import org.pac4j.core.authorization.authorizer.ProfileAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAllRolesAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.CommonProfile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minidev.json.JSONArray;

public class AdministratorAuthorizer extends ProfileAuthorizer<CommonProfile> {

    @Override
    public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
//        System.out.println("Is  DataOwnerRoleAuthorizer authorised was called:"+isAnyAuthorized(context, profiles));
        return isAnyAuthorized(context, profiles);
    }

    @Override
    public  boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
        if (profile == null) {
            return false;
        }
        Set<String> roles = profile.getRoles();
        JSONArray attributeRolesArray = profile.getAttribute("roles", JSONArray.class);
        String[] attributeRoles = null;
        if (attributeRolesArray != null) {
            attributeRoles = new String[attributeRolesArray.size()];
            for (int i = 0; i < attributeRolesArray.size(); i++) {
                attributeRoles[i] = (String) attributeRolesArray.get(i);
                //System.out.println("Role " + i + ": " + attributeRoles[i]);
            }
        }
        if (attributeRoles != null) {
            List<String> attributeRolesList = Arrays.asList(attributeRoles);
             return attributeRolesList.contains(Roles.ADMINISTRATOR);
        }
        return false;
    }

}
