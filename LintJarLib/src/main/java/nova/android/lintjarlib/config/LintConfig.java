package nova.android.lintjarlib.config;

import com.android.tools.lint.detector.api.Context;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * nova.android.lintjarlib.
 *
 * @author Created by WXG on 2018/10/19 019 10:47.
 * @version V1.0
 */
public class LintConfig {

    private static final String LINT_CONFIG_NAME = "lint-config.json";
    private static final String LIBRARY_NAME = "lintaarlib";
    private Context mContext;
    private LintConfigBean mLintConfigBean;

    public LintConfig(Context mContext) {
        this.mContext = mContext;
        parseContext();
    }

    /**
     * get custom lint rules.
     *
     * @return {@link LintConfigBean.LintRulesBean}
     */
    public LintConfigBean.LintRulesBean getLintRules() {
        if (mLintConfigBean != null) {
            return mLintConfigBean.getLintRules();
        } else {
            return null;
        }
    }

    /**
     * get the deprecated apis.
     *
     * @return {@link LintConfigBean.LintRulesBean.DeprecatedApiBean}
     */
    public List<LintConfigBean.LintRulesBean.DeprecatedApiBean> getDeprecatedApis() {
        if (mLintConfigBean != null) {
            return mLintConfigBean.getLintRules().getDeprecatedApi();
        } else {
            return null;
        }
    }

    /**
     * get the handle exceptions.
     *
     * @return {@link LintConfigBean.LintRulesBean.HandleExceptionBean}
     */
    public List<LintConfigBean.LintRulesBean.HandleExceptionBean> getHandleExceptions() {
        if (mLintConfigBean != null) {
            return mLintConfigBean.getLintRules().getHandleException();
        } else {
            return null;
        }
    }

    /**
     * parse the config.
     */
    private void parseContext() {

        mContext.getProject().getDirectLibraries().forEach(project -> {
            for (File file : project.getAssetFolders()) {
                String path = file.getAbsolutePath();
                if (path.contains(LIBRARY_NAME) && file.isDirectory()) {
                    File configFile = new File(file.getAbsolutePath() + File.separator + LINT_CONFIG_NAME);
                    mLintConfigBean = new Gson().fromJson(readStringFromFile(configFile.getAbsolutePath
                            ()), LintConfigBean.class);
                    break;
                }
            }
        });

    }

    /**
     * Read content from file convert to String.
     *
     * @param path : the absolute path.
     * @return the content of file.
     */
    private String readStringFromFile(String path) {

        int len = 0;

        StringBuilder sb = new StringBuilder();

        File file = new File(path);

        if (!file.exists() || file.isDirectory()) {
            return null;
        }

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader in = null;

        try {

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            in = new BufferedReader(isr);

            String line;

            while ((line = in.readLine()) != null) {

                if (len != 0) {// 处理换行符的问题
                    sb.append(System.getProperty("line.separator")).append(line);
                } else {
                    sb.append(line);
                }

                len++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (fis != null) {
                    fis.close();
                }

                if (isr != null) {
                    isr.close();
                }

                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();

    }


}
