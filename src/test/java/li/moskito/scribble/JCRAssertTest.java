package li.moskito.scribble;

import static org.mockito.Mockito.when;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
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

}
