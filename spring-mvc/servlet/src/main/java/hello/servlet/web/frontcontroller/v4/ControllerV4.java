package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

/**
 *
 *
 * @Param paramMap
 * @Param model
 * @return viewName
 *
 */
public interface ControllerV4 {
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
