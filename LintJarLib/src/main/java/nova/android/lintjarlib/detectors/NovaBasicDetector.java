package nova.android.lintjarlib.detectors;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nova.android.lintjarlib.config.LintConfig;
import nova.android.lintjarlib.config.LintConfigBean;

import static nova.android.lintjarlib.NovaCategory.CODING_CONVENTION;

/**
 * nova.android.lintjarlib.detectors.
 *
 * @author Created by WXG on 2018/10/19 019 14:28.
 * @version V1.0
 */
public class NovaBasicDetector extends Detector {

    static String LOG_ISSUE_ID = "LogUsage";
    static String OUT_ISSUE_ID = "OutUsage";

    private static String DEFAULT_BRIEF_DESCRIPTION = "NovaIssue, 注意报错，保证代码质量";
    private static String DEFAULT_EXPLANATION = "NovaIssue, 注意报错，保证代码质量";
    private static int DEFAULT_PRIORITY = 5;
    private static Severity DEFAULT_SEVERITY = Severity.WARNING;

    public static final Issue BASIC_ISSUE = Issue.create(
            "NovaBasicIssue",
            DEFAULT_BRIEF_DESCRIPTION,
            DEFAULT_EXPLANATION,
            CODING_CONVENTION,
            DEFAULT_PRIORITY,
            DEFAULT_SEVERITY,
            new Implementation(NovaBasicDetector.class, Scope.JAVA_FILE_SCOPE));

    private LintConfig mLintConfig;
    private Map<String, Issue> cachedIssues = new HashMap<>();
    private Map<String, LintConfigBean.LintRulesBean.DeprecatedApiBean> cachedApis = new HashMap<>();
    private Map<String, LintConfigBean.LintRulesBean.HandleExceptionBean> cachedExceptions = new HashMap<>();

    @Override
    public void beforeCheckEachProject(@NotNull Context context) {
        super.beforeCheckEachProject(context);
        mLintConfig = new LintConfig(context);
    }

    /**
     * get severity.
     *
     * @param severity like {@link Severity#ERROR}
     * @return {@link Severity}
     */
    private Severity getSeverity(String severity) {
        return Severity.fromName(severity);
    }

    /**
     * obtain issue by the specific id.
     *
     * @param id  issue id.
     * @param cls ths subclass of NovaBasicDetector.
     * @return {@link Issue}
     */
    protected Issue getIssue(String id, Class<? extends NovaBasicDetector> cls) {

        String briefDescription = DEFAULT_BRIEF_DESCRIPTION;
        String explanation = DEFAULT_EXPLANATION;
        int priority = DEFAULT_PRIORITY;
        Severity severity = DEFAULT_SEVERITY;
        Implementation implementation = new Implementation(cls, Scope.JAVA_FILE_SCOPE);

        if (mLintConfig != null) {

            //find the issue by id.
            boolean isFind = false;

            LintConfigBean.LintRulesBean.DeprecatedApiBean deprecatedApi = getDeprecatedApi(id);
            if (deprecatedApi != null) {
                isFind = true;
                briefDescription = deprecatedApi.getMessage();
                explanation = deprecatedApi.getMessage();
                priority = deprecatedApi.getPriority();
                severity = getSeverity(deprecatedApi.getSeverity());
            }

            if (!isFind) {
                LintConfigBean.LintRulesBean.HandleExceptionBean handleException = getHandleException(id);
                if (handleException != null) {
                    briefDescription = handleException.getMessage();
                    explanation = handleException.getMessage();
                    priority = handleException.getPriority();
                    severity = getSeverity(handleException.getSeverity());
                }
            }
        }

        Issue cachedIssue = this.cachedIssues.get(id);

        if (cachedIssue != null) {
            return cachedIssue;
        } else {
            Issue issue = Issue.create(id, briefDescription, explanation, CODING_CONVENTION, priority, severity,
                    implementation);

            this.cachedIssues.put(id, issue);

            return issue;
        }
    }

    /**
     * obtain {@link LintConfigBean.LintRulesBean.DeprecatedApiBean} by the specific id.
     *
     * @param id issue id.
     * @return {@link LintConfigBean.LintRulesBean.DeprecatedApiBean}
     */
    @Nullable
    protected LintConfigBean.LintRulesBean.DeprecatedApiBean getDeprecatedApi(String id) {

        LintConfigBean.LintRulesBean.DeprecatedApiBean cachedApi = cachedApis.get(id);
        if (cachedApi != null) {
            return cachedApi;
        }

        LintConfigBean.LintRulesBean.DeprecatedApiBean deprecatedApiBean = null;

        List<LintConfigBean.LintRulesBean.DeprecatedApiBean> deprecatedApis = mLintConfig.getDeprecatedApis();
        if (deprecatedApis != null) {
            for (LintConfigBean.LintRulesBean.DeprecatedApiBean deprecatedApi : deprecatedApis) {
                if (id.equals(deprecatedApi.getId())) {
                    deprecatedApiBean = deprecatedApi;
                    cachedApis.put(id, deprecatedApi);
                    break;
                }
            }
        }

        return deprecatedApiBean;
    }

    /**
     * obtain {@link LintConfigBean.LintRulesBean.HandleExceptionBean} by the specific id.
     *
     * @param id issue id.
     * @return {@link LintConfigBean.LintRulesBean.HandleExceptionBean}
     */
    @Nullable
    protected LintConfigBean.LintRulesBean.HandleExceptionBean getHandleException(String id) {

        LintConfigBean.LintRulesBean.HandleExceptionBean cachedException = cachedExceptions.get(id);
        if (cachedException != null) {
            return cachedException;
        }

        LintConfigBean.LintRulesBean.HandleExceptionBean handleExceptionBean = null;
        List<LintConfigBean.LintRulesBean.HandleExceptionBean> handleExceptions = mLintConfig.getHandleExceptions();
        if (handleExceptions != null) {
            for (LintConfigBean.LintRulesBean.HandleExceptionBean handleException : handleExceptions) {
                if (id.equals(handleException.getId())) {
                    handleExceptionBean = handleException;
                    cachedExceptions.put(id, handleException);
                    break;
                }
            }
        }

        return handleExceptionBean;
    }

}
