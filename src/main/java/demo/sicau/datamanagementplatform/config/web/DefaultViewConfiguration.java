package demo.sicau.datamanagementplatform.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 23:30 2018/11/7
 * @Description:
 */
@Configuration
public class DefaultViewConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 存放图片的路径
     */
    @Value("${file.img.location}")
    private String imgLocation;
    /**
     * 存放资源文件的路径
     */
    @Value("${file.resource.location")
    private String resourceLocation;

    /**
     * 配置默认首页
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大允许
//        factory.setMaxFileSize("3MB");
//        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }
}
