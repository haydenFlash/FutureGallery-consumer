package com.futuregallery.workbench.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.futuregallery.model.Student;
import com.futuregallery.model.User;
import com.futuregallery.service.StudentService;
import com.futuregallery.utils.DateTimeUtil;
import com.futuregallery.utils.UUIDUtil;
import com.futuregallery.vo.PaginationVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/contacts")
public class StudentController {

    @Reference(interfaceClass = StudentService.class, version = "1.0.0", check = false)
    private StudentService studentService;

    @RequestMapping("/index")
    public String getContactsIndex() {
        return "workbench/contacts/index";
    }

    @RequestMapping("/getUserList")
    @ResponseBody
    public List<User> getUserList() {

        List<User> userList = studentService.getAllUser();

        return userList;
    }

    @RequestMapping("/addStudent")
    @ResponseBody
    public Boolean addStudent(Student student, HttpServletRequest request) {
        student.setId(UUIDUtil.getUUID());
        student.setCreateTime(DateTimeUtil.getSysTime());
        student.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = studentService.addStudent(student);

        return flag;
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO pageList(String pageNo, String pageSizeStr, Student student) {
        int pageSize = Integer.parseInt(pageSizeStr);
        int startIndex = pageSize * (Integer.parseInt(pageNo) - 1);

        Map<String, Object> map = new HashMap<>();
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        map.put("owner", student.getOwner());
        map.put("name", student.getName());
        map.put("createBy", student.getCreateBy());
        map.put("source", student.getSource());
        map.put("birth", student.getBirth());

        PaginationVO vo = studentService.pageList(map);

        return vo;
    }

    @RequestMapping("/deleteStudent")
    @ResponseBody
    public Boolean deleteStudent(HttpServletRequest request) {
        String[] ids = request.getParameterValues("id");
        return studentService.deleteStudent(ids);
    }

    @RequestMapping("/editStudent")
    @ResponseBody
    public Map<String, Object> editStudent(String id) {
        return studentService.editStudent(id);
    }

    @RequestMapping("/updateStudent")
    @ResponseBody
    public Boolean updateStudent(Student student) {
        return studentService.updateStudent(student);
    }
}
