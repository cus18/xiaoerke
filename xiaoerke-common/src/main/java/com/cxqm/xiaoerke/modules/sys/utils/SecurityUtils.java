package com.cxqm.xiaoerke.modules.sys.utils;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SecurityUtils {
   static ThreadLocal<Subject> subjectThreadLocal =  new InheritableThreadLocal<Subject>();

    public static Subject getSubject() {
        if(subjectThreadLocal.get()==null) {
            Subject subject = new Subject() {

            	Principal principal = new Principal("dummy");
            	
                @Override
                public Principal getPrincipal() {
                    return principal;
                }


                public boolean isPermitted(String permission) {
                    return true;
                }


                public boolean[] isPermitted(String... permissions) {
                    return new boolean[]{true};
                }


                public boolean isPermittedAll(String... permissions) {
                    return true;
                }

            };
            subjectThreadLocal.set(subject);
        }
        return subjectThreadLocal.get();
    }

     public interface Subject {
        public Principal getPrincipal() ;


         public boolean isPermitted(String permission);


         public boolean[] isPermitted(String... permissions);


         public boolean isPermittedAll(String... permissions);
     }

    public static class Principal {
        String id;

        String loginName;

		String userId;
        
        Map<String, Object> cache = new HashMap(Collections.EMPTY_MAP);
        
        public Principal(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public Map<String, Object> getCacheMap() {
        	return cache;
        }
        
        public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
		
    }
}
