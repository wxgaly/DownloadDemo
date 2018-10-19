package nova.android.lintjarlib.detectors;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UCallExpression;

import java.util.ArrayList;
import java.util.List;

import nova.android.lintjarlib.config.LintConfigBean;

import static nova.android.lintjarlib.NovaCategory.CODING_CONVENTION;

/**
 * nova.android.lintlib.
 *
 * @author Created by WXG on 2018/10/17 017 18:38.
 * @version V1.0
 */
public class LogDetector extends NovaBasicDetector implements SourceCodeScanner {

    public static final Issue ISSUE = Issue.create(
            LOG_ISSUE_ID,
            "避免使用Log/System.out.println",
            "使用Logger，将日志记录在文件中方便查找问题",
            CODING_CONVENTION, 6, Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE));

    private LintConfigBean.LintRulesBean.DeprecatedApiBean mLogDeprecatedApi;
    private LintConfigBean.LintRulesBean.DeprecatedApiBean mOutDeprecatedApi;

    @Override
    public List<String> getApplicableMethodNames() {
        mLogDeprecatedApi = getDeprecatedApi(LOG_ISSUE_ID);
        mOutDeprecatedApi = getDeprecatedApi(OUT_ISSUE_ID);

        List<String> methodNames = new ArrayList<>();

        methodNames.addAll(mLogDeprecatedApi.getMethods());
        methodNames.addAll(mOutDeprecatedApi.getMethods());

        return methodNames;
    }

    @Override
    public void visitMethod(@NotNull JavaContext context,
                            @NotNull UCallExpression call,
                            @NotNull PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, mLogDeprecatedApi.getClassName())) {
            context.report(getIssue(LOG_ISSUE_ID, getClass()), call, context.getLocation(call),
                    mLogDeprecatedApi.getMessage());
        }

        if (context.getEvaluator().isMemberInClass(method, mOutDeprecatedApi.getClassName())) {
            context.report(getIssue(OUT_ISSUE_ID, getClass()), call, context.getLocation(call),
                    mOutDeprecatedApi.getMessage());
        }
    }

}
