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

import java.util.Objects;
import javax.inject.Inject;

import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;

public abstract class AbstractCompatibleVersionRule extends AbstractEnforcerRule
{
    private final MavenProject project;

    @Inject
    public AbstractCompatibleVersionRule(MavenProject project)
    {
        this.project = Objects.requireNonNull(project);
    }

    @Override
    public void execute() throws EnforcerRuleException
    {
        String packaging = project.getPackaging();
        if ("pom".equals(packaging))
        {
            // Skip pom packaging (not a deployed artifact)
            return;
        }
        String version = project.getVersion();
        ensureCompatibleVersion(version);
    }

    protected abstract void ensureCompatibleVersion(String version) throws EnforcerRuleException;
}
