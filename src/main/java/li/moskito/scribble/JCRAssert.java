package li.moskito.scribble;

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
    public static void assertNodeExistByPath(Session session, String absPath) throws RepositoryException {
        try {
            session.getNode(absPath);
        } catch (PathNotFoundException e) {
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
    public static void assertNodeNotExistByPath(Session session, String absPath) throws RepositoryException {
        try {
            session.getNode(absPath);
            fail("Node " + absPath + " does not exist");
        } catch (PathNotFoundException e) {

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
    public static void assertNodeExist(Node rootNode, String relPath) throws RepositoryException {
        try {
            rootNode.getNode(relPath);
        } catch (PathNotFoundException e) {
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
    public static void assertPrimaryNodeType(Node node, String nodeType) throws RepositoryException {
        NodeType nt = node.getPrimaryNodeType();
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
    public static void assertMixinNodeType(Node node, String mixinType) throws RepositoryException {
        for (NodeType nt : node.getMixinNodeTypes()) {
            if (mixinType.equals(nt.getName())) {
                return;
            }
        }
        fail("Node " + node.getPath() + " has no mixin type " + mixinType);
    }
}
