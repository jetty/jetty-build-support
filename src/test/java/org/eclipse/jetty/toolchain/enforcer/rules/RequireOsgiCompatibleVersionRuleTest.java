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

public class RequireOsgiCompatibleVersionRuleTest
{
    @ParameterizedTest
    @ValueSource(strings = {
        // Basic ones
        "1.0.0",
        "1.2.3.4",
        // Long form classifier
        "12.0.0.v20100511-114423",
        // SNAPSHOT version
        "4.0.2.SNAPSHOT",
        "7.0.2-SNAPSHOT",
        // Versions seen in the wild.
        "3.4.2.r342_v20081028-0800",
        "3.4.3.R34x_v20081215-1030",
        "3.0.5.v20090218-1800-e3x",
        "1.1.1.M20080827-0800b",
        "0.1.37.v200803061811"
    })
    public void testGoodOsgiVersion(String version)
        throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule(new MavenProject());
        rule.ensureCompatibleVersion("1.0.0");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // Too long
        "1.2.3.4.5",
        // Too short
        "1.2",
        // Characters not allows in first 3 segments
        "a.0.2",
        "1a.0.2",
        "1.b.3",
        "1.2b.3",
        "1.2.c",
        "1.2.3c",
        // Alpha / Beta
        "1.beta.2",
        "1.0.alpha-2",
        // Invalid Qualifier
        "1.0.2.2009:05:12"
    })
    public void testBadOsgiVersion(String version)
    {
        Assertions.assertThrows(EnforcerRuleException.class, () ->
        {
            RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule(new MavenProject());
            rule.ensureCompatibleVersion(version);
        });
    }
}
