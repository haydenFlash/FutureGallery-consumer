package com.futuregallery.web.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.futuregallery.model.DicValue;
import com.futuregallery.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DicListener implements ServletContextListener {
    @Reference(interfaceClass = DicService.class, version = "1.0.0")
    private DicService dicService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("开始加入数据字典");
        ServletContext servletContext = sce.getServletContext();

        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> set = map.keySet();

        for (String s : set) {
            List<DicValue> list = map.get(s);
            servletContext.setAttribute(s + "List", list);
        }
        System.out.println("完成加入数据字典");

    }
}

//@Component
//public class DicListener implements ApplicationListener<ContextRefreshedEvent> {
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        System.out.println("开始加入数据字典");
//        ApplicationContext applicationContext = event.getApplicationContext();
//        DicService dicService = applicationContext.getBean(DicService.class);
//        ServletContext application = applicationContext.getBean(ServletContext.class);
//
//        Map<String, List<DicValue>> map = dicService.getAll();
//        Set<String> set = map.keySet();
//
//        for (String s : set) {
//            List<DicValue> list = map.get(s);
//            application.setAttribute("s" + list, list);
//        }
//        System.out.println("完成加入数据字典");
//    }
//}
