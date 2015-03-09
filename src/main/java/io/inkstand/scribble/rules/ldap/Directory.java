package io.inkstand.scribble.rules.ldap;

import org.apache.commons.io.IOUtils;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.core.api.CacheService;
import org.apache.directory.server.core.api.CoreSession;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.DnFactory;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.avl.AvlPartition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.xdbm.impl.avl.AvlIndex;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Test Rule that provides a directory service. If you need an LDAP directory server, you have to
 * embed this rule in the {@link io.inkstand.scribble.rules.ldap.DirectoryServer} rule.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class Directory implements TestRule {

    // TODO this class needs cleanup and refactoring

    private static final Logger LOG = getLogger(Directory.class);

    private final DirectoryService directoryService;

    private final TemporaryFolder folder;

    private boolean accessControlEnabled = false;
    private boolean anonymousAccessEnabled = true;

    public Directory(final TemporaryFolder folder) {
        super();
        this.folder = folder;
        this.directoryService = createDirectoryService();
    }

    /**
     * Creates a new DirectoryService instance for the test rule. Initialization of the service is done in the
     * appyStatement phase by invoking the setupService method.
     */
    private DirectoryService createDirectoryService() {

        final DirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();
        try {
            factory.init("scribble");
            return factory.getDirectoryService();
        } catch (Exception e) {

            throw new AssertionError("Unable to create directory service", e);
        }
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

    /**
     * Starts the DirectoryService up. AccessControl and AnonymousAccess are enabled as configured and the
     * cache service is initialized.
     *
     * @throws Exception
     *  if starting the directory service failed for any reason
     */
    protected void setupService() throws Exception { // NOSONAR

        final DirectoryService service = getDirectoryService();
        service.getChangeLog().setEnabled(false);
        service.setInstanceLayout(new InstanceLayout(this.folder.getRoot()));
        final CacheService cacheService = new CacheService();
        cacheService.initialize(service.getInstanceLayout());
        service.setCacheService(cacheService);

        service.setAccessControlEnabled(this.accessControlEnabled);
        service.setAllowAnonymousAccess(this.anonymousAccessEnabled);

        addPartition("scribble", "dc=scribble,dc=org");
        service.startup();
    }

    /**
     * Creates an AVL implementation based in-memory partition.
     * @param partitionId
     *  the id of the partition
     * @param suffix
     *  the suffix dn of all partition entries
     * @throws Exception
     *  if the partition could not be created
     */
    protected void addPartition(final String partitionId,
            final String suffix) throws Exception {

        //TODO candidate to be moved to a builder
        final DirectoryService service = getDirectoryService();

        final CacheService cacheService = service.getCacheService();
        final SchemaManager schemaManager = service.getSchemaManager();
        final DnFactory dnFactory = service.getDnFactory();

        final AvlPartition partition = new AvlPartition(schemaManager, dnFactory);
        partition.setId(partitionId);
        partition.setSuffixDn(dnFactory.create(suffix));
        partition.setCacheService(cacheService);
        partition.setSyncOnWrite(true);
        partition.setCacheSize(1000);
        partition.setPartitionPath(new File(service.getInstanceLayout().getPartitionsDirectory(), partitionId).toURI());
        partition.addIndex(new AvlIndex<Entry>("objectClass", false));
        partition.initialize();
        LOG.info("Created partition {} in {}", partitionId, this.folder.getRoot());
        service.addPartition(partition);
    }

    /**
     * Imports directory content that is defined in LDIF format and provided as input stream. The method
     * writes the stream content into a temporary file.
     * @param ldifData
     *  the ldif data to import as a stream
     * @throws IOException
     *  if the temporary file can not be created
     */
    public void importLdif(InputStream ldifData) throws IOException {

        final File ldifFile = this.folder.newFile("scribble_import.ldif");
        try (final FileWriter writer = new FileWriter(ldifFile)) {
            IOUtils.copy(ldifData, writer);
        }
        final String pathToLdifFile = ldifFile.getAbsolutePath();
        final CoreSession session = getDirectoryService().getAdminSession();
        final LdifFileLoader loader = new LdifFileLoader(session, pathToLdifFile);
        loader.execute();

    }

    protected void setupDirectoryContent(final DirectoryService service) throws IOException {

        final CoreSession session = service.getAdminSession();
        final String ldifFile = prepareLdifFile("LdapUserProfileServiceTest_testUsers.ldif");
        final LdifFileLoader loader = new LdifFileLoader(session, ldifFile);
        loader.execute();

    }

    protected String prepareLdifFile(final String ldifResource) throws IOException {

        final InputStream is = getClass().getResourceAsStream(ldifResource);
        final File ldifFile = this.folder.newFile("testUsers.ldif");
        try (final FileWriter writer = new FileWriter(ldifFile)) {
            IOUtils.copy(is, writer);
        }

        return ldifFile.getAbsolutePath();
    }

    /**
     * Adds a partition to the rule. Partitions can only be added before the rule is applied and the service
     * is started up. So this method is intended to be invoked by a builder.
     * @param partition
     *  the partition to be added to the service
     * @throws Exception
     *  if adding the partition failed for any reason
     */
    protected void addPartition(Partition partition) throws Exception {
        getDirectoryService().addPartition(partition);
    }

    /**
     * Enables access control on the directory service. Default is false.
     *
     * @param accessControlEnabled
     *  flag to indicate, if access control should be enabled
     */
    protected void setAccessControlEnabled(final boolean accessControlEnabled) {

        this.accessControlEnabled = accessControlEnabled;
    }

    /**
     * Enables anonymous access on the directory service. Default is true.
     * @param anonymousAccessEnabled
     *  flag to indicate, if anonymous access is allowed
     */
    protected void setAnonymousAccessEnabled(final boolean anonymousAccessEnabled) {

        this.anonymousAccessEnabled = anonymousAccessEnabled;
    }

    /**
     * The Apache DS Directory Service instance wrapped by this rule.
     * @return
     *  the {@link org.apache.directory.server.core.api.DirectoryService) instance of this rule
     */
    public DirectoryService getDirectoryService() {

        return directoryService;
    }

    /**
     * Shuts down the directory service
     * @throws Exception
     *  if the shutdown fails for any reason
     */
    protected void tearDownService() throws Exception { // NOSONAR

        directoryService.shutdown();

    }

}
