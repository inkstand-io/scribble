package io.inkstand.scribble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;

/**
 * Assertion for writing unit tests against a JCR repository.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public final class JCRAssert {

    private JCRAssert() {
    }

    /**
     * Asserts the equality of a property value of a node with an expected value
     *
     * @param node
     *            the node containing the property to be verified
     * @param propertyName
     *            the property name to be verified
     * @param actualValue
     *            the actual value that should be compared to the propert node
     * @throws RepositoryException
     */
    public static void assertStringPropertyEquals(final Node node, final String propertyName, final String actualValue)
            throws RepositoryException {
        assertTrue("Node " + node.getPath() + " has no property " + propertyName, node.hasProperty(propertyName));
        final Property prop = node.getProperty(propertyName);
        assertEquals("Property type is not STRING ", PropertyType.STRING, prop.getType());
        assertEquals(actualValue, prop.getString());
    }

    /**
     * Asserts that a specific node with the given absolute path exists in the session
     *
     * @param session
     *            the session to search for the node
     * @param absPath
     *            the absolute path to look for a node
     * @throws RepositoryException
     *             if the repository access failed
     */
    public static void assertNodeExistByPath(final Session session, final String absPath) throws RepositoryException {
        try {
            session.getNode(absPath);
        } catch (final PathNotFoundException e) {
            fail("Node " + absPath + " does not exist");
        }
    }

    /**
     * Asserts that a specific node with the given absolute path does not exist in the session
     *
     * @param session
     *            the session to search for the node
     * @param absPath
     *            the absolute path to look for a node
     * @throws RepositoryException
     *             if the repository access failed
     */
    public static void assertNodeNotExistByPath(final Session session, final String absPath) throws RepositoryException {
        try {
            session.getNode(absPath);
            fail("Node " + absPath + " does not exist");
        } catch (final PathNotFoundException e) { // NOSONAR
            // the exception is expected
        }
    }

    /**
     * Asserts that an item, identified by it's unique id, is found in the repository session.
     *
     * @param session
     *            the session to be searched
     * @param itemId
     *            the item expected to be found
     * @throws RepositoryException
     */
    public static void assertNodeExistById(final Session session, final String itemId) throws RepositoryException {
        try {
            session.getNodeByIdentifier(itemId);
        } catch (final ItemNotFoundException e) {
            fail("ItemNotFoundException expected");
        }
    }

    /**
     * Asserts that an item, identified by it's unique id, is not found in the repository session.
     *
     * @param session
     *            the session to be searched
     * @param itemId
     *            the item expected not to be found
     * @throws RepositoryException
     */
    public static void assertNodeNotExistById(final Session session, final String itemId) throws RepositoryException {
        try {
            session.getNodeByIdentifier(itemId);
            fail("ItemNotFoundException expected");
        } catch (final ItemNotFoundException e) {
            // this was expected
        }
    }

    /**
     * Asserts that a specific node exists under the root node, where the specific node is specified using its relative
     * path
     *
     * @param rootNode
     *            the root Node to start the search
     * @param relPath
     *            the relative path of the node that is asserted to exist
     * @throws RepositoryException
     *             if the repository access failed
     */
    public static void assertNodeExist(final Node rootNode, final String relPath) throws RepositoryException {
        try {
            rootNode.getNode(relPath);
        } catch (final PathNotFoundException e) {
            fail("Node " + relPath + " does not exist under " + rootNode.getPath());
        }
    }

    /**
     * Asserts the primary node type of the node
     *
     * @param node
     *            the node whose primary node type should be checked
     * @param nodeType
     *            the nodetype that is asserted to be the node type of the node
     * @throws RepositoryException
     */
    public static void assertPrimaryNodeType(final Node node, final String nodeType) throws RepositoryException {
        final NodeType nt = node.getPrimaryNodeType();
        assertEquals(nodeType, nt.getName());
    }

    /**
     * Asserts one of the node's mixin type equals the specified nodetype
     *
     * @param node
     *            the node whose mixin types should be checked
     * @param mixinType
     *            the node type that is asserted to be one of the mixin types of the node
     * @throws RepositoryException
     */
    public static void assertMixinNodeType(final Node node, final String mixinType) throws RepositoryException {
        for (final NodeType nt : node.getMixinNodeTypes()) {
            if (mixinType.equals(nt.getName())) {
                return;
            }
        }
        fail("Node " + node.getPath() + " has no mixin type " + mixinType);
    }

    /**
     * Asserts that a specific node type is registered in the workspace of the session.
     *
     * @param session
     *            the session to perform the lookup
     * @param nodeTypeName
     *            the name of the nodetype that is asserted to exist
     * @throws RepositoryException
     *             if an error occurs
     */
    public static void assertNodeTypeExists(final Session session, final String nodeTypeName)
            throws RepositoryException {

        final NodeTypeManager ntm = session.getWorkspace().getNodeTypeManager();
        assertTrue("NodeType " + nodeTypeName + " does not exist", ntm.hasNodeType(nodeTypeName));
    }
}
