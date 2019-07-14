package com.study.order.client;

import com.study.order.bean.Course;
import com.study.order.bean.RespBean;
import com.study.order.bean.UserStudy;
import com.study.order.client.hystrix.CourseClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zhongxin on 2017/9/17.
 */

@FeignClient(value = "cloud-classroom-course",fallback = CourseClientHystrix.class )
public interface CourseClient {

    @GetMapping(value = "/cloud/classroom/course/{id}")
    Course getCourse(@PathVariable("id") String id);

    @PostMapping(value = "/cloud/classroom/user_study")
    RespBean addUserStudy(UserStudy userStudy);


    @GetMapping(value = "/cloud/classroom/course/names")
    Map<String, String> getCourseNames();
}



