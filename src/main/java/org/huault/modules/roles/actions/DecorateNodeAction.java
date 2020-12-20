package org.huault.modules.roles.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * This is the minimum required setup to declare a Jahia Action using OSGi
 */
@Component(service = Action.class)
public class DecorateNodeAction extends Action {

	private static final Logger LOG = LoggerFactory.getLogger(DecorateNodeAction.class);
	
    @Activate
    public void activate() {
        setName("decorateNode");
        setRequireAuthenticatedUser(false);
        setRequiredMethods("GET,POST");
    }

    @Override
    public ActionResult doExecute(final HttpServletRequest req,final RenderContext renderContext,final Resource resource,final JCRSessionWrapper session,final Map<String, List<String>> parameters,final URLResolver urlResolver) throws Exception {
    	
    	LOG.debug("Calling DecorateNodeAction ");
    	System.out.println("DEBUG 1");
    	
    	JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(renderContext.getUser(), renderContext.getWorkspace(), resource.getLocale(), new JCRCallback<Boolean>() {
			@Override
			public Boolean doInJCR(JCRSessionWrapper systemSession) throws RepositoryException {
				
				JCRNodeWrapper node = systemSession.getNodeByIdentifier(resource.getNode().getIdentifier());
				
				if (!node.isNodeType("comix:decorated")) {
					node.addMixin("comix:decorated");
					systemSession.save();
				}
				
				String bgColor = getParameter(parameters, "bgColor", "");
				
				node.setProperty("bgColor", bgColor);
				systemSession.save();
				
				return true;
			}
		});
    	
    	JSONObject response = new JSONObject();
        response.put("message", "Success");
        
        return new ActionResult(HttpServletResponse.SC_OK, null, response);
    
    }
}