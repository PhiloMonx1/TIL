package tobyspring_eh13.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
          "tobyspring_eh13.config.autoconfig.DispatcherServletConfig",
          "tobyspring_eh13.config.autoconfig.TomcatWebServerConfig",
        };
    }
}
