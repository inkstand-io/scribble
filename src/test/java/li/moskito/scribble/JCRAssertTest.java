package li.moskito.scribble;

import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JCRAssertTest {

    @Mock
    private Session session;

    @Mock
    private Node node;

    @Mock
    private NodeType nodeType;

    @Mock
    private Property property;

    @Test
    public void testAssertNodeExistByPath_SessionString_PathFound_success() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenReturn(node);
        JCRAssert.assertNodeExistByPath(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = AssertionError.class)
    public void testAssertNodeExistByPath_SessionString_PathNotFound_fail() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(PathNotFoundException.class);
        JCRAssert.assertNodeExistByPath(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeExistByPath_SessionString_exception() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeExistByPath(session, absPath);
    }

    @Test(expected = AssertionError.class)
    public void testAssertNodeNotExistByPath_SessionString_PathFound_fail() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenReturn(node);
        JCRAssert.assertNodeNotExistByPath(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAssertNodeNotExistByPath_SessionString_PathNotFound_success() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(PathNotFoundException.class);
        JCRAssert.assertNodeNotExistByPath(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeNotExistByPath_SessionString_exception() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeNotExistByPath(session, absPath);
    }

    @Test
    public void testAssertNodeExistByPathNodeString_PathFound_success() throws Exception {
        String relPath = "child";
        when(node.getNode(relPath)).thenReturn(node);
        JCRAssert.assertNodeExist(node, relPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = AssertionError.class)
    public void testAssertNodeExistByPathNodeString_PathNotFound_fail() throws Exception {
        String relPath = "child";
        when(node.getNode(relPath)).thenThrow(PathNotFoundException.class);
        JCRAssert.assertNodeExist(node, relPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeExistByPathNodeString_exception() throws Exception {
        String relPath = "child";
        when(node.getNode(relPath)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeExist(node, relPath);
    }

    @Test
    public void testAssertStringPropertyEquals_equals_sucess() throws Exception {
        // prepare
        String propertyName = "property";
        String propertyValue = "value";
        when(node.getPath()).thenReturn("root");
        when(node.hasProperty(propertyName)).thenReturn(true);
        when(node.getProperty(propertyName)).thenReturn(property);
        when(property.getType()).thenReturn(PropertyType.STRING);
        when(property.getString()).thenReturn(propertyValue);
        // act
        JCRAssert.assertStringPropertyEquals(node, propertyName, "value");
    }

    @Test(expected = AssertionError.class)
    public void testAssertStringPropertyEquals_notEquals() throws Exception {
        // prepare
        String propertyName = "property";
        String propertyValue = "value";
        when(node.getPath()).thenReturn("root");
        when(node.hasProperty(propertyName)).thenReturn(true);
        when(node.getProperty(propertyName)).thenReturn(property);
        when(property.getType()).thenReturn(PropertyType.STRING);
        when(property.getString()).thenReturn(propertyValue);
        // act
        JCRAssert.assertStringPropertyEquals(node, propertyName, "anotherValue");
    }

    @Test(expected = AssertionError.class)
    public void testAssertStringPropertyEquals_wrongType_fail() throws Exception {
        // prepare
        String propertyName = "property";
        String propertyValue = "value";
        when(node.getPath()).thenReturn("root");
        when(node.hasProperty(propertyName)).thenReturn(true);
        when(node.getProperty(propertyName)).thenReturn(property);
        when(property.getType()).thenReturn(PropertyType.LONG);
        when(property.getString()).thenReturn(propertyValue);
        // act
        JCRAssert.assertStringPropertyEquals(node, propertyName, "value");
    }

    @Test(expected = AssertionError.class)
    public void testAssertStringPropertyEquals_noSuchProperty() throws Exception {
        // prepare
        String propertyName = "property";
        String propertyValue = "value";
        when(node.getPath()).thenReturn("root");
        when(node.hasProperty(propertyName)).thenReturn(false);
        when(node.getProperty(propertyName)).thenReturn(property);
        when(property.getType()).thenReturn(PropertyType.STRING);
        when(property.getString()).thenReturn(propertyValue);
        // act
        JCRAssert.assertStringPropertyEquals(node, propertyName, "value");
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertStringPropertyEquals_exception() throws Exception {
        // prepare
        String propertyName = "property";
        String propertyValue = "value";
        when(node.getPath()).thenReturn("root");
        when(node.hasProperty(propertyName)).thenThrow(RepositoryException.class);
        when(node.getProperty(propertyName)).thenReturn(property);
        when(property.getType()).thenReturn(PropertyType.STRING);
        when(property.getString()).thenReturn(propertyValue);
        // act
        JCRAssert.assertStringPropertyEquals(node, propertyName, "value");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAssertNodeNotExistById_notExist_success() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(ItemNotFoundException.class);
        JCRAssert.assertNodeNotExistById(session, itemId);
    }

    @Test(expected = AssertionError.class)
    public void testAssertNodeNotExistById_exist_fail() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenReturn(node);
        JCRAssert.assertNodeNotExistById(session, itemId);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeNotExistById_exception() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeNotExistById(session, itemId);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = AssertionError.class)
    public void testAssertNodeExistById_notExist_fail() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(ItemNotFoundException.class);
        JCRAssert.assertNodeExistById(session, itemId);
    }

    @Test
    public void testAssertNodeExistById_exist_success() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenReturn(node);
        JCRAssert.assertNodeExistById(session, itemId);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeExistById_exception() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeExistById(session, itemId);
    }

    @Test
    public void testAssertPrimaryNodeType_matching_success() throws Exception {
        String nodeTypeName = "nt:unstructured";
        when(node.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.getName()).thenReturn(nodeTypeName);
        JCRAssert.assertPrimaryNodeType(node, nodeTypeName);
    }

    @Test(expected = AssertionError.class)
    public void testAssertPrimaryNodeType_notMatching_fail() throws Exception {
        String nodeTypeName = "nt:unstructured";
        when(node.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.getName()).thenReturn("nt:resource");
        JCRAssert.assertPrimaryNodeType(node, nodeTypeName);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertPrimaryNodeType_exception() throws Exception {
        String nodeTypeName = "nt:unstructured";
        when(node.getPrimaryNodeType()).thenThrow(RepositoryException.class);
        JCRAssert.assertPrimaryNodeType(node, nodeTypeName);
    }

    @Test
    public void testAssertMixinNodeType_match_success() throws Exception {
        String nodeTypeName = "mix:title";
        NodeType[] mixinTypes = new NodeType[] {
            nodeType
        };
        when(node.getMixinNodeTypes()).thenReturn(mixinTypes);
        when(nodeType.getName()).thenReturn(nodeTypeName);
        JCRAssert.assertMixinNodeType(node, nodeTypeName);
    }

    @Test(expected = AssertionError.class)
    public void testAssertMixinNodeType_notMatch_fail() throws Exception {
        String nodeTypeName = "mix:title";
        NodeType[] mixinTypes = new NodeType[] {
            nodeType
        };
        when(node.getMixinNodeTypes()).thenReturn(mixinTypes);
        when(nodeType.getName()).thenReturn("mix:versionable");
        JCRAssert.assertMixinNodeType(node, nodeTypeName);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertMixinNodeType_exception() throws Exception {
        String nodeTypeName = "mix:title";
        when(node.getMixinNodeTypes()).thenThrow(RepositoryException.class);
        JCRAssert.assertMixinNodeType(node, nodeTypeName);
    }

}
