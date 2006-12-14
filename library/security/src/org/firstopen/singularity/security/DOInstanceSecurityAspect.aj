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


import net.sf.acegisecurity.intercept.method.aspectj.AspectJSecurityInterceptor;
import net.sf.acegisecurity.intercept.method.aspectj.AspectJCallback;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 */
    public aspect DOInstanceSecurityAspect implements InitializingBean {

    public aspect DomainObjectInstanceSecurityAspect 
    private AspectJSecurityInterceptor securityInterceptor;
    pointcut domainObjectInstanceExecution(): target(SingularityArtifact)
    && execution(public * *(..)) && !within(DomainObjectInstanceSecurityAspect);
    Object around(): domainObjectInstanceExecution() {
    if (this.securityInterceptor != null) {
    AspectJCallback callback = new AspectJCallback() {
    public Object proceedWithObject() {
    return proceed();
    }
    };
    return this.securityInterceptor.invoke(thisJoinPoint, callback);
    } else {
    return proceed();
    }
    }
    public AspectJSecurityInterceptor getSecurityInterceptor() {
    return securityInterceptor;
    }
    public void setSecurityInterceptor(AspectJSecurityInterceptor securityInterceptor) {
    this.securityInterceptor = securityInterceptor;
    }
    public void afterPropertiesSet() throws Exception {
    if (this.securityInterceptor == null)
    throw new IllegalArgumentException("securityInterceptor required");
    }
    }
}
