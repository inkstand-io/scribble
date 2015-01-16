package io.inkstand.scribble.rules.ldap;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.DnFactory;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Test Rule that provides an embedded LDAP server.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class Directory implements TestRule {

    private DirectoryService directoryService;

    private final TemporaryFolder folder;

    public Directory(final TemporaryFolder folder) {
        super();
        this.folder = folder;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                setupService();
                try {
                    base.evaluate();
                } finally {
                    tearDownService();
                }

            }

        };
    }

    protected void setupService() throws Exception {
        // Initialize the LDAP ds
        final DirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();
        factory.init("scribble");
        directoryService = factory.getDirectoryService();

        // Disable the ChangeLog system
        directoryService.getChangeLog().setEnabled(false);

        final String partitionId = "apache";
        final String suffix = "o=test";
        directoryService.getSchemaManager();
        final DnFactory dnFactory = directoryService.getDnFactory();
        final JdbmPartition partition = new JdbmPartition(directoryService.getSchemaManager(), dnFactory);
        partition.setId(partitionId);
        partition.setSuffixDn(dnFactory.create(suffix));
        partition.setCacheSize(1000);
        partition.setPartitionPath(folder.getRoot().toURI());

        // Create some indices (optional)
        partition.addIndex(new JdbmIndex<Entry>("objectClass", false));
        partition.addIndex(new JdbmIndex<Entry>("o", false));
        // Initialize the partition
        partition.initialize();

        // And start the ds
        directoryService.startup();

    }

    public DirectoryService getDirectoryService() {
        return directoryService;
    }

    protected void tearDownService() throws Exception {
        directoryService.shutdown();
    }

}
