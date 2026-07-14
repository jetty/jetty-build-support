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

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;

@Named("requireCompatibleVersionDebian")
public class RequireDebianCompatibleVersionRule extends AbstractCompatibleVersionRule
{
    @Inject
    public RequireDebianCompatibleVersionRule(MavenProject project)
    {
        super(project);
    }

    @Override
    protected void ensureCompatibleVersion(String version) throws EnforcerRuleException
    {
        if (version.endsWith("SNAPSHOT"))
        {
            // Skip check on SNAPSHOT versions.
            return;
        }

        if (version.contains("-"))
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                + "It can't have the '-' character as it has a special meaning reserved for the DEB process (package build #).");
        }

        if (version.contains("_"))
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                + "It can't have the '_' character as it has a special meaning reserved for the DEB process "
                + "(indicates split between package name, version, and architecture on filename).");
        }

        // Architecture IDs 
        String reserved[] =
            {
                "all", "i386", "i486", "i586", "i686", "pentium", "athlon", "ia64", "x86_64", "amd64", "ia32", "alpha", "sparc",
                "m68k", "ppc", "hppa", "arm"
            };

        String lversion = version.toLowerCase();
        for (String id : reserved)
        {
            if (lversion.contains(id))
            {
                throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                    + "It can't use the DEB reserved word \"" + id + "\".");
            }
        }
    }

    public String getCacheId()
    {
        return "version-compatible-debian";
    }
}
