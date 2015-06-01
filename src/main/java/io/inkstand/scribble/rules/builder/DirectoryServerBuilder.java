/*
 * Copyright 2015 Gerald Muecke, gerald.muecke@gmail.com
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

package io.inkstand.scribble.rules.builder;

import io.inkstand.scribble.rules.ldap.Directory;
import io.inkstand.scribble.rules.ldap.DirectoryServer;

/**
 * Builder for creating a {@link DirectoryServer} rule. The rule allows to start and stop an embedded ldap server
 * for test.
 * Created by Gerald on 29.05.2015.
 */
public class DirectoryServerBuilder extends Builder<DirectoryServer> {

    /**
     * The {@link Directory} rule that provides the ldap content for the ldap server
     */
    private final Directory directory;

    /**
     * The port the server should accept incoming connections
     */
    private int port = -1;

    /**
     * The listen address for the ldap server
     */
    private String listenAddress;

    /**
     * Flag to indicate the rule should find an available port on each rule application
     */
    private boolean findAvailablePortMode;

    public DirectoryServerBuilder(final Directory directory) {

        this.directory = directory;

    }

    @Override
    public DirectoryServer build() {

        final DirectoryServer ds = new DirectoryServer(directory);
        if (port != -1) {
            ds.setTcpPort(port);
        }
        if (listenAddress != null) {
            ds.setListenAddress(listenAddress);
        }
        ds.setAutoBind(this.findAvailablePortMode);
        return ds;
    }

    /**
     * Specifies a specific TCP port the ldap server created by the rule should accept incoming connections. If not
     * specified, the default port is 10389.
     *
     * @param port
     *         the port to use for servicing ldap request
     *
     * @return this builder
     */
    public DirectoryServerBuilder onPort(final int port) {

        this.port = port;

        return this;
    }

    /**
     * Specifies the listen address for the ldap server created by the rule. If not specified, localhost is the
     * default.
     *
     * @param address
     *         the listen address for the ldap server
     *
     * @return this builder
     */
    public DirectoryServerBuilder onListenAddress(final String address) {

        this.listenAddress = address;

        return this;
    }

    /**
     * Specifies that the rule should find an available port automatically on each application of the rule.
     *
     * @return this builder
     */
    public DirectoryServerBuilder onAvailablePort() {

        this.findAvailablePortMode = true;

        return this;
    }
}
