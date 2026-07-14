//
//  ========================================================================
//  Copyright (c) 1995-2024 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.toolchain.enforcer.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RequireDebianCompatibleVersionRuleTest
{
    @ParameterizedTest
    @ValueSource(strings = {
        // Basic ones
        "1.0.0",
        "7.0.2.RC0",
        // Skipped SNAPSHOTs
        "1.0.0-SNAPSHOT",
        "7.0.2.SNAPSHOT",
    })
    public void testGoodVersions(String version)
        throws EnforcerRuleException
    {
        RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule(new MavenProject());
        rule.ensureCompatibleVersion( version );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Dash not allowed
        "7.0.2-RC0",
        // Underscore not allowed
        "7.0.2_RC0",
        // reserved word "alpha"
        "8.1.1.alpha3"
    })
    public void testBadVersion(String version)
    {
        Assertions.assertThrows( EnforcerRuleException.class, () -> {
            RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule(new MavenProject());
            rule.ensureCompatibleVersion( version );
        } );
    }
}
