package io.inkstand.scribble.rules.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.inkstand.scribble.rules.ldap.builder.DirectoryBuilder;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Gerald Muecke on 23.11.2015.
 */
public class DirectoryServerExample {


    public static TemporaryFolder folder = new TemporaryFolder();


    public static DirectoryServer ldap = new DirectoryBuilder(folder)
            .withPartition("scribble","dc=scribble")
            .importLdif(DirectoryServerExample.class.getResource("DirectoryExample.ldif"))
            .aroundDirectoryServer()
            .onAvailablePort().build();

    @ClassRule
    public static RuleChain chain = RuleChain.outerRule(folder).around(ldap);


    @Test
    public void testLookup() throws Exception {
        //prepare
        final LdapConnection connection = new LdapNetworkConnection("localhost", ldap.getTcpPort());

        //act
        try {
            connection.connect();
            connection.bind("uid=testuser,ou=users,dc=scribble", "Password1");
            final EntryCursor result = connection.search("ou=groups,dc=scribble","(uniqueMember=*)",
                                                         SearchScope.SUBTREE);
            //assert
            assertTrue("Search result expected",result.next());
            final Entry group = result.get();
            final Attribute uniqueMember = group.get("uniqueMember");
            assertNotNull(uniqueMember);
            assertEquals("uid=testuser,ou=users,dc=scribble", uniqueMember.get().getString());

        } finally {
            connection.close();
        }

    }

}
