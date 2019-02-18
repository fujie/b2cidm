package jp.co.ctcg.b2c.snsauth.authn.impl;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.idp.authn.AbstractExtractionAction;
import net.shibboleth.idp.authn.AuthnEventIds;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.authn.context.UsernamePasswordContext;
import net.shibboleth.idp.session.context.SessionContext;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.security.auth.Subject;

/**
 * An action that extracts a token code from an HTTP form, creates a
 * {@link TokenUserContext}, and attaches it to the
 * {@link AuthenticationContext}.
 * 
 * @author N.Fujie
 */
@SuppressWarnings("rawtypes")
public class PushNotification extends AbstractExtractionAction {

	/** Class logger. */
	@Nonnull
	private final Logger log = LoggerFactory.getLogger(PushNotification.class);

	/**
	 * Constructor.
	 */
	public PushNotification() {
		super();
	}

	@Override
	protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext,
			@Nonnull final AuthenticationContext authenticationContext) {

		final HttpServletRequest request = getHttpServletRequest();

		if (request == null) {
			log.debug("{} Empty HttpServletRequest", getLogPrefix());
			ActionSupport.buildEvent(profileRequestContext, AuthnEventIds.NO_CREDENTIALS);
			return;
		}

		try {
			UsernamePasswordContext upCtx = authenticationContext.getSubcontext(UsernamePasswordContext.class, true);
			String userName = upCtx.getUsername();
			log.info("user : {}", userName);

			// Todo : use messagingAPI

			// http client
			CloseableHttpClient httpclient = HttpClients.createDefault();
			// HttpGet myrequest = new HttpGet("https://b2cotp-php.azurewebsites.net/send.php?line_id=U33f595897fe185eb89c4b105d6f44dcb");
			HttpGet myrequest = new HttpGet("https://b2cotp-php.azurewebsites.net/send.php?student_id=" + userName);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(myrequest);
				int status = response.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK){
					log.info("approved");
				}else{
					log.info("denied");
                                	ActionSupport.buildEvent(profileRequestContext, AuthnEventIds.INVALID_CREDENTIALS);
                                	return;
				}
			} catch(Exception e) {

			}
		} catch (Exception e) {
			log.warn("{} Login by {} produced exception", getLogPrefix(),  e);
		}
	}

}
