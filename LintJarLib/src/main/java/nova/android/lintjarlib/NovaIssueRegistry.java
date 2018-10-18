package nova.android.lintjarlib;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * nova.android.lintlib.
 *
 * @author Created by WXG on 2018/10/16 016 17:16.
 * @version V1.0
 */
public class NovaIssueRegistry extends IssueRegistry {
    @NotNull
    @Override
    public List<Issue> getIssues() {
        System.out.println("***************************************************");
        System.out.println("****************nova lint is starting *****************");
        System.out.println("***************************************************");
        return Arrays.asList(LogDetector.ISSUE);
    }

    @Override public int getApi() { return com.android.tools.lint.detector.api.ApiKt.CURRENT_API; }
}
