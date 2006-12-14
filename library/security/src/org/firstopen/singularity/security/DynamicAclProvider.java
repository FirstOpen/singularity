/*
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.acl.AclEntry;
import net.sf.acegisecurity.acl.basic.BasicAclProvider;
import net.sf.acegisecurity.acl.basic.SimpleAclEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * 
 * <P>
 * DynamicAclProvider - Source for ACLs created dynamically based on bean shell
 * script bound with objects at runtime.
 * </P>
 * 
 * @author Tom Rose  (tom.rose@i-konect.com)
 * 
 * @version $Id: DynamicAclProvider.java 1242 2006-01-14 03:34:08Z TomRose $
 * @since 1.0
 */

public class DynamicAclProvider extends BasicAclProvider {

    Log log = LogFactory.getLog(this.getClass());

    @Override
    public AclEntry[] getAcls(Object domainInstance,
            Authentication authentication) {
        AclEntry[] acls = super.getAcls(domainInstance, authentication);
        AclEntry[] addacls = createAcls(acls, domainInstance);
        return addacls;
    }

    @Override
    public AclEntry[] getAcls(Object domainInstance) {
        AclEntry[] acls = super.getAcls(domainInstance);
        AclEntry[] addacls = createAcls(acls, domainInstance);
        return addacls;
    }

    protected AclEntry[] createAcls(AclEntry[] acls, Object domainInstance) {

        AclEntry[] aclEntries = new AclEntry[1];

        SimpleAclEntry acl = new SimpleAclEntry();

        Interpreter i = new Interpreter(); // Construct an interpreter
        try {
            i.set("acl", new SimpleAclEntry());

            i.set("obj", domainInstance);
  
            
            Date date = (Date) i.get("date"); // retrieve a variable

            // Eval a statement and get the result
            i.eval("acl.addPermission(1)");
            i.source("somefile.bsh");
            acl = (SimpleAclEntry) i.get("acl");
            aclEntries[0] = acl;
            // Source an external script file
            // i.source("somefile.bsh");

        } catch (EvalError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
       
        int size = 0;
        if (acls != null)
            size = acls.length;
        if (aclEntries != null)
            size += aclEntries.length;

        AclEntry[] finalacls = new AclEntry[size];
        int start = 0;
        if (acls != null) {
            System.arraycopy(acls, 0, finalacls, 0, acls.length);
            start = acls.length;
        }
        if (aclEntries != null)
            System.arraycopy(aclEntries, 0, finalacls, start, aclEntries.length);
        return aclEntries;
    }
}
