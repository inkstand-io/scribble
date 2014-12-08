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
    private Property property;

    @Test
    public void testAssertNodeExist_SessionString_PathFound_success() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenReturn(node);
        JCRAssert.assertNodeExist(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = AssertionError.class)
    public void testAssertNodeExist_SessionString_PathNotFound_fail() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(PathNotFoundException.class);
        JCRAssert.assertNodeExist(session, absPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeExist_SessionString_exception() throws Exception {
        String absPath = "root";
        when(session.getNode(absPath)).thenThrow(RepositoryException.class);
        JCRAssert.assertNodeExist(session, absPath);
    }

    @Test
    public void testAssertNodeExistNodeString_PathFound_success() throws Exception {
        String relPath = "child";
        when(node.getNode(relPath)).thenReturn(node);
        JCRAssert.assertNodeExist(node, relPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = AssertionError.class)
    public void testAssertNodeExistNodeString_PathNotFound_fail() throws Exception {
        String relPath = "child";
        when(node.getNode(relPath)).thenThrow(PathNotFoundException.class);
        JCRAssert.assertNodeExist(node, relPath);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertNodeExistNodeString_exception() throws Exception {
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
    public void testAssertItemNotExist_notExist_success() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(ItemNotFoundException.class);
        JCRAssert.assertItemNotExist(session, itemId);
    }

    @Test(expected = AssertionError.class)
    public void testAssertItemNotExist_exist_fail() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenReturn(node);
        JCRAssert.assertItemNotExist(session, itemId);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void testAssertItemNotExist_exception() throws Exception {
        String itemId = UUID.randomUUID().toString();
        when(session.getNodeByIdentifier(itemId)).thenThrow(RepositoryException.class);
        JCRAssert.assertItemNotExist(session, itemId);
    }

}
