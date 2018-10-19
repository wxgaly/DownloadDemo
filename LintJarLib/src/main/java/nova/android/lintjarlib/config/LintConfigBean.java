package nova.android.lintjarlib.config;

import java.util.List;

/**
 * nova.android.lintjarlib.
 *
 * @author Created by WXG on 2018/10/19 019 11:27.
 * @version V1.0
 */
public class LintConfigBean {


    /**
     * lintRules : {"deprecatedApi":[{"id":"OutUsage","className":"java.io.PrintStream","methods":["println"],
     * "message":"请勿直接使用System.out，应该使用Logger","priority":6,"severity":"Error"},{"id":"LogUsage","className":"android
     * .util.Log","methods":["v","d","i","w","e","wtf"],"message":"请勿直接使用Log，应该使用Logger","priority":6,
     * "severity":"Error"},{"id":"NewThread","className":"java.lang.Thread","methods":["Thread"],
     * "message":"避免单独创建Thread执行后台任务，存在性能问题，建议使用ThreadPool，并建议使用构造方法，不建议使用Executors","priority":5,
     * "severity":"Warning"}],"handleException":[{"id":"ColorException","className":"android.graphics.Color",
     * "methods":["parseColor"],"exception":"java.lang.IllegalArgumentException","message":"Color
     * .parseColor需要加try-catch处理IllegalArgumentException异常","priority":8,"severity":"Error"}]}
     */

    private LintRulesBean lintRules;

    public LintRulesBean getLintRules() {
        return lintRules;
    }

    public void setLintRules(LintRulesBean lintRules) {
        this.lintRules = lintRules;
    }

    public static class LintRulesBean {
        private List<DeprecatedApiBean> deprecatedApi;
        private List<HandleExceptionBean> handleException;

        public List<DeprecatedApiBean> getDeprecatedApi() {
            return deprecatedApi;
        }

        public void setDeprecatedApi(List<DeprecatedApiBean> deprecatedApi) {
            this.deprecatedApi = deprecatedApi;
        }

        public List<HandleExceptionBean> getHandleException() {
            return handleException;
        }

        public void setHandleException(List<HandleExceptionBean> handleException) {
            this.handleException = handleException;
        }

        public static class DeprecatedApiBean {
            /**
             * id : OutUsage
             * className : java.io.PrintStream
             * methods : ["println"]
             * message : 请勿直接使用System.out，应该使用Logger
             * priority : 6
             * severity : Error
             */

            private String id;
            private String className;
            private String message;
            private int priority;
            private String severity;
            private List<String> methods;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getSeverity() {
                return severity;
            }

            public void setSeverity(String severity) {
                this.severity = severity;
            }

            public List<String> getMethods() {
                return methods;
            }

            public void setMethods(List<String> methods) {
                this.methods = methods;
            }
        }

        public static class HandleExceptionBean {
            /**
             * id : ColorException
             * className : android.graphics.Color
             * methods : ["parseColor"]
             * exception : java.lang.IllegalArgumentException
             * message : Color.parseColor需要加try-catch处理IllegalArgumentException异常
             * priority : 8
             * severity : Error
             */

            private String id;
            private String className;
            private String exception;
            private String message;
            private int priority;
            private String severity;
            private List<String> methods;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getException() {
                return exception;
            }

            public void setException(String exception) {
                this.exception = exception;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getSeverity() {
                return severity;
            }

            public void setSeverity(String severity) {
                this.severity = severity;
            }

            public List<String> getMethods() {
                return methods;
            }

            public void setMethods(List<String> methods) {
                this.methods = methods;
            }
        }
    }
}
