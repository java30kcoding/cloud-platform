package com.study.order.service;

import com.alibaba.fastjson.JSONObject;
import com.study.order.bean.Course;
import com.study.order.bean.Order;
import com.study.order.bean.UserStudy;
import com.study.order.client.CourseClient;
import com.study.order.common.OrderConstant;
import com.study.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by andy on 2019/1/24.
 */
@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CourseClient courseService;

    @Autowired
    private MsgSender sender;


    public int addOrder(Order Order) {
        return orderMapper.insert(Order);
    }

    public List<Order> getAllOrder() {
        return orderMapper.getAllOrder();
    }

    public List<Order> getOrdersByPage(Integer page, Integer size,String name) {
        int start = (page-1)*size;
        return orderMapper.getOrdersByPage(start,size,name);
    }


    public Long getCountByKey(String name) {
        return orderMapper.getCountByKey(name);
    }

    public Order getOrder(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }


    public int updateOrder(Order Order) {
        return orderMapper.updateByPrimaryKeySelective(Order);
    }

    public int deleteOrder(String id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    public int orderCourse(int userId, String courseId){
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderTime(new Date(System.currentTimeMillis()));
        Course course = courseService.getCourse(courseId);
        order.setState(OrderConstant.ORDER_UNPAID);
        order.setTotal(course.getPrice());
        order.setCourseId(courseId);
        return this.addOrder(order);
    }

    public  List<Order> getOrderByUserId(int userId){
        List<Order> orders = orderMapper.getOrderByUserId(userId);
        orders.stream().forEach(item ->{
            Course course = courseService.getCourse(item.getCourseId());
            if(course != null){
                item.setCourseName(course.getName());
            }
        });
        return orders;
    }


    public int payOrder(String orderId){
        Order orderInDb = this.getOrder(orderId);
        if(orderInDb.getState() == 0){
            Order newOrder = new Order();
            newOrder.setId(orderId);
            newOrder.setState(OrderConstant.ORDER_PAID);
            this.updateOrder(newOrder);
            UserStudy userStudy = new UserStudy();
            userStudy.setCourseId(orderInDb.getCourseId());
            userStudy.setUserId(orderInDb.getUserId());
            userStudy.setStartTime(new Date(System.currentTimeMillis()));
            courseService.addUserStudy(userStudy);
            return 1;
        }
        return 2;
    }


    public int payOrderUsingMq(String orderId){
        Order newOrder = new Order();
        newOrder.setId(orderId);
        newOrder.setState(OrderConstant.ORDER_PAID);
        this.updateOrder(newOrder);
        Order orderInDb = this.getOrder(orderId);
        UserStudy userStudy = new UserStudy();
        userStudy.setCourseId(orderInDb.getCourseId());
        userStudy.setUserId(orderInDb.getUserId());
        userStudy.setStartTime(new Date(System.currentTimeMillis()));
        String userStudyJsonStr = JSONObject.toJSONString(userStudy);
        sender.topicSend(userStudyJsonStr);
//        courseService.addUserStudy(userStudy);
        return 1;
    }

}
