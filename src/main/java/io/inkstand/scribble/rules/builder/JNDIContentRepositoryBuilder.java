package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.jcr.JNDIContentRepository;

import java.util.Properties;

import javax.jcr.Repository;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.AssertionFailedError;

/**
 * A {@link Builder} for creating a {@link JNDIContentRepository}. The builder allows to override the default lookup
 * name as well setting a {@link Context} or configuring the context to create. If no context property is specified the
 * default {@link Context} will be used.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class JNDIContentRepositoryBuilder extends ContentRepositoryBuilder<JNDIContentRepository> {
    private final JNDIContentRepository contentRepository;
    /**
     * Properties to use for creating the context
     */
    private Properties contextProperties = new Properties();
    /**
     * A Naming context that is used if specified.
     */
    private Context context;

    public JNDIContentRepositoryBuilder() {
        contentRepository = new JNDIContentRepository();
    }

    @Override
    public JNDIContentRepository build() {
        final Context ctx;
        if (context != null) {
            ctx = context;
        } else {
            try {
                if (contextProperties.isEmpty()) {
                    // create default initial context
                    ctx = new InitialContext();
                } else {
                    ctx = new InitialContext(contextProperties);
                }
            } catch (final NamingException e) {
                throw new AssertionFailedError("Rule building failed: " + e.getMessage());
            }
        }
        contentRepository.setContext(ctx);
        return contentRepository;
    }

    /**
     * Sets the context to be used for the JNDI lookup. If no context is set or <code>null</code> is passed, as new
     * initial context will be created before the lookup.
     *
     * @param context
     *            the context to use
     * @return this builder
     */
    public JNDIContentRepositoryBuilder usingContext(final Context context) {
        this.context = context;
        return this;
    }

    /**
     * Set the lookup name to use for the {@link Repository} lookup. This will override the default value.
     *
     * @param lookupName
     *            the new lookup name to use
     * @return this test rule
     * @see JNDIContentRepository
     */
    public JNDIContentRepositoryBuilder withLookup(final String lookupName) {
        contentRepository.setLookupName(lookupName);
        return this;
    }

    /**
     * Uses the given properties as environment for the lookup context. Replaces the current context environment.
     *
     * @param properties
     *            the properties to use for the environment for the context initialization
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withContextProperties(final Properties properties) {

        contextProperties = (Properties) properties.clone();
        return this;
    }

    /**
     * Adds a new context property to the environment for the JNDI lookup context
     *
     * @param name
     *            the name of the environment variable
     * @param value
     *            the value to assign to the variable
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withContextProperty(final String name, final Object value) {

        contextProperties.put(name, value);
        return this;
    }

    /**
     * Uses the specified contextFactory as initial context factory. The method is a convenience for setting the
     * INITIAL_CONTEXT_FACTORY property on the environment.
     *
     * @param contextFactory
     *            the context factory class to use to create the context
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withInitialContextFactory(final Class<?> contextFactory) {

        return withInitialContextFactory(contextFactory.getName());
    }

    /**
     * Uses the specified contextFactory as initial context factory. The method is a convenience for setting the
     * INITIAL_CONTEXT_FACTORY property on the environment.
     *
     * @param contextFactory
     *            the context factory class name to use to create the context
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withInitialContextFactory(final String contextFactory) {

        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        return this;
    }

    /**
     * Uses the specified naming service provider. The method is a convenience for setting the PROVIDER_URL property on
     * the environment.
     *
     * @param providerUrl
     *            the provider URL to use for naming services. For JBoss the URL is like
     *            <code>remote://localhost:4447</code>. In case JBoss Remote naming should be used, the context property
     *            <code>jboss.naming.client.ejb.context</code> has to be set to <code>true</code>
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withProviderURL(final String providerUrl) {

        contextProperties.put(Context.PROVIDER_URL, providerUrl);
        return this;
    }

    /**
     * Sets the context properties for SECURITY_PRINCIPAL and SECURITY_CREDENTIAL to perform the lookup. This method is
     * a convenience for setting the properties SECURITY_PRINCIPAL and SECURITY_CREDENTIAL on the environment.
     *
     * @param principalName
     *            the principal name to use to perform the lookup
     * @param credentials
     *            the credentials used to authenticate the principal
     * @return this test rule
     */
    public JNDIContentRepositoryBuilder withSecurityPrincipal(final String principalName, final String credentials) {

        contextProperties.put(Context.SECURITY_PRINCIPAL, principalName);
        contextProperties.put(Context.SECURITY_CREDENTIALS, credentials);
        return this;
    }

}
