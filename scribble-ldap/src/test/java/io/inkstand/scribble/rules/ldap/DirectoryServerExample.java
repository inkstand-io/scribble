/*
 * Copyright 2015-2016 DevCon5 GmbH, info@devcon5.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.rules.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import io.inkstand.scribble.rules.ldap.builder.DirectoryBuilder;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

/**
 * This example shows how to setup and connect to the ldap server. The suite contains two tests simply to prove,
 * the rule is re-usable as classrule without having to restart.
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
    public void testLookup_one() throws Exception {

        doTest();

    }

    @Test
    public void testLookup_two() throws Exception {
        //prepare
        doTest();

    }

    private void doTest()
            throws LdapException, org.apache.directory.api.ldap.model.cursor.CursorException, IOException {

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
