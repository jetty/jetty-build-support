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

public class RequireRedhatCompatibleVersionRuleTest
{
    @ParameterizedTest
    @ValueSource(strings = {
        // Basic ones
        "1.0.0",
        "7.0.2.RC0",
        // SNAPSHOTs
        "1.0.0-SNAPSHOT",
        "7.0.2.SNAPSHOT",
    })
    public void testGoodVersions(String version)
        throws EnforcerRuleException
    {
        RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule(new MavenProject());
        rule.ensureCompatibleVersion( version );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Dash not allowed here
        "7.0.2-RC0",
        // "alpha" is a reserved word
        "9.1.1.alpha3"
    })
    public void testBadVersion(String version)
    {
        Assertions.assertThrows( EnforcerRuleException.class, () -> {
            RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule(new MavenProject());
            rule.ensureCompatibleVersion( version );
        } );
    }
}
