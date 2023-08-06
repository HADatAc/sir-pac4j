package controllers;

//import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import model.JsonContent;
import modules.SecurityModule;
//import org.pac4j.cas.profile.CasProxyProfile;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.Pac4jConstants;
//import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.PlayWebContext;
//import org.pac4j.play.context.PlayFrameworkParameters;
import org.pac4j.play.http.PlayHttpActionAdapter;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlayCacheSessionStore;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;
import authorizers.Roles;
import util.Utils;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RoleList;

public class Application extends Controller {

    @Inject
    private Config config;
 
    @Inject
    private SessionStore playSessionStore;

    
    private UserProfile getProfile(Http.Request request) {
        PlayWebContext webContext = new PlayWebContext(request);
        ProfileManager profileManager = new ProfileManager(webContext, playSessionStore);
        Optional profile = profileManager.getProfile();
        if (profile.isPresent()) {
            return (UserProfile)profile.get();
        } 
        return null; 
        //final PLayFrameworkParameters parameters = new PLayFrameworkParameters(request);
        //final org.pac4j.core.context.WebContext context = config.getWebContextFactory().newContext(request);
        //final org.pac4j.core.context.session.SessionStore sessionStore = config.getSessionStoreFactory().newSessionStore(request);
        //final ProfileManager profileManager = config.getProfileManagerFactory().apply(context, sessionStore);
        //return profileManager.getProfiles();
    }
 
    @Secure(clients = "AnonymousClient")
    public Result index(Http.Request request) throws Exception {
        return ok(views.html.index.render(getProfile(request), null, null));
    }

    private Result protectedIndexView(Http.Request request) {
        // profiles
        return ok(views.html.protectedIndex.render(getProfile(request)));
    }

    /* 
    @Secure(clients = "FacebookClient", matchers = "excludedPath")
    public Result facebookIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    private Result notProtectedIndexView(Http.Request request) {
        // profiles
        return ok(views.html.notprotectedIndex.render(getProfile(request)));
    }

    @Secure(clients = "HeaderClient")
    public Result signedGenericIndex(Http.Request request) {
        return notProtectedIndexView(request);
    }

    /* 
    public Result facebookNotProtectedIndex(Http.Request request) {
        return notProtectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "FacebookClient", authorizers = "admin")
    public Result facebookAdminIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "FacebookClient", authorizers = "custom")
    public Result facebookCustomIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "TwitterClient,FacebookClient")
    public Result twitterIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    @Secure
    public Result protectedIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "FormClient")
    //f@SubjectPresent(handlerKey = "FormClient", forceBeforeAuthCheck = true)
    public Result formIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    // Setting the isAjax parameter is no longer necessary as AJAX requests are automatically detected:
    // a 401 error response will be returned instead of a redirection to the login url.
    @Secure(clients = "FormClient")
    public Result formIndexJson(Http.Request request) {
        Content content = views.html.protectedIndex.render(getProfiles(request));
        JsonContent jsonContent = new JsonContent(content.body());
        return ok(jsonContent);
    }
    */

    /* 
    @Secure(clients = "IndirectBasicAuthClient")
    public Result basicauthIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    //@Secure(clients = "DirectBasicAuthClient,ParameterClient,DirectFormClient")
    @Secure(clients = "DirectBasicAuthClient,ParameterClient")
    public Result dbaIndex(Http.Request request) {

        Utils.block();

        return protectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "CasClient")
    public Result casIndex(Http.Request request) {
        final CommonProfile profile = getProfiles(request).get(0);
        final String service = "http://localhost:8080/proxiedService";
        String proxyTicket = null;
        if (profile instanceof CasProxyProfile) {
            final CasProxyProfile proxyProfile = (CasProxyProfile) profile;
            proxyTicket = proxyProfile.getProxyTicketFor(service);
        }
        return ok(views.html.casProtectedIndex.render(profile, service, proxyTicket));
    }
    */

    /* 
    @Secure(clients = "SAML2Client")
    public Result samlIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    /* 
    @Secure(clients = "OidcClient")
    public Result oidcIndex(Http.Request request) {
        return protectedIndexView(request);
    }
    */

    @Secure(clients = "HeaderClient", authorizers = Roles.AUTHENTICATED)
    public Result signedAuthenticatedIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "HeaderClient", authorizers = Roles.ADMINISTRATOR)
    public Result signedAdministratorIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "HeaderClient", authorizers = Roles.AUTHENTICATED_ADMINISTRATOR)
    public Result signedAuthenticatedAdministratorIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    /* 
    //@Secure(clients = "AnonymousClient", authorizers = "csrfCheck")
    public Result csrfIndex(Http.Request request) {
        return ok(views.html.csrf.render(getProfiles(request)));
    }
    */

    /* 
    public Result loginForm() throws TechnicalException {
        final FormClient formClient = (FormClient) config.getClients().findClient("FormClient").get();
        return ok(views.html.loginForm.render(formClient.getCallbackUrl()));
    }
    */

    /* 
    public Result jwt(Http.Request request) {
        final List<CommonProfile> profiles = getProfiles(request);
        final JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(SecurityModule.JWT_SALT));
        String token = "";
        if (CommonHelper.isNotEmpty(profiles)) {
            token = generator.generate(profiles.get(0));
        }
        return ok(views.html.jwt.render(token));
    }
    */

    /* 
    public Result forceLogin(Http.Request request) {
        final PlayWebContext context = new PlayWebContext(request, playSessionStore);
        final Client client = config.getClients().findClient(context.getRequestParameter(Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER).get()).get();
        try {
            final HttpAction action = (HttpAction) client.getRedirectionAction(context).get();
            return (Result) PlayHttpActionAdapter.INSTANCE.adapt(action, context);
        } catch (final HttpAction e) {
            throw new TechnicalException(e);
        }
    }
    */
}
