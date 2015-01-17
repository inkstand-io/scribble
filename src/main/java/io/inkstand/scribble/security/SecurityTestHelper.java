package io.inkstand.scribble.security;

import java.lang.reflect.Method;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class SecurityTestHelper {

    /**
     * SLF4J Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(SecurityTestHelper.class);

    private SecurityTestHelper() {
    }

    /**
     * Creates a subject for a user with the given name
     *
     * @param userName
     *            name of the user
     * @return a subject for the user
     */
    public static Subject subjectForUser(final String userName) {

        final SimpleUserPrincipal principal = new SimpleUserPrincipal(userName);
        return subjectForUser(principal);

    }

    /**
     * Creates a subject for the given user principal
     *
     * @param userPrincipal
     *            the user principal to be added to the subject
     * @return the subject for the user
     */
    public static Subject subjectForUser(final Principal userPrincipal) {

        final Set<Principal> principals = new HashSet<>();
        principals.add(userPrincipal);

        return new Subject(false, principals, Collections.emptySet(), Collections.emptySet());
    }

    /**
     * Invokes the specified method on the target in the JAAS Context of the given subject
     *
     * @param jaasSubject
     *            the subject in whose context the method should be invoked
     * @param target
     *            the target object of the method invocation or <code>null</code> if it is a static method
     * @param method
     *            the method to be invoked
     * @param params
     *            the parameters passed to the method
     * @return the result of the method invocation
     * @throws Throwable
     *             if the invocation failed for any reason
     */
    public static <T> T invokeAs(final Subject jaasSubject, final Object target, final Method method,
            final Object... params) throws Throwable { // NOSONAR

        try {
            return Subject.doAs(jaasSubject, new PrivilegedExceptionAction<T>() {

                @SuppressWarnings("unchecked")
                @Override
                public T run() throws Exception {
                    return (T) method.invoke(target, params);
                }
            });
        } catch (final PrivilegedActionException e) {
            LOG.debug("Exception in privileged action", e);
            throw e.getCause();
        }

    }

    /**
     * Invokes the specified method on the target in the JAAS Context of the given user
     *
     * @param user
     *            the user in whose context the method should be invoked
     * @param target
     *            the target object of the method invocation or <code>null</code> if it is a static method
     * @param method
     *            the method to be invoked
     * @param params
     *            the parameters passed to the method
     * @return the result of the method invocation
     * @throws Throwable
     *             if the invokation failed for any reason
     */
    public static <T> T invokeAs(final String user, final Object target, final Method method, final Object... params)
            throws Throwable { // NOSONAR
        return invokeAs(subjectForUser(user), target, method, params);
    }

}
