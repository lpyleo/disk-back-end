package com.disk.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.disk.file.common.RestResult;
import com.disk.file.dto.*;
import com.disk.file.model.Administrator;
import com.disk.file.model.Notice;
import com.disk.file.model.User;
import com.disk.file.service.AdminService;
import com.disk.file.service.NoticeService;
import com.disk.file.service.UserService;
import com.disk.file.util.DateUtil;
import com.disk.file.vo.NoticeInfoVO;
import com.disk.file.vo.NoticeVO;
import com.disk.file.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "notice", description = "该接口为系统通知接口，主要用来做一些系统通知的基本操作。")
@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    NoticeService noticeService;

    @Resource
    UserService userService;

    @Resource
    AdminService adminService;

    @Operation(summary = "获取通知列表", description = "用来做前台通知列表展示", tags = {"notice"})
    @GetMapping(value = "/getnoticelist")
    @ResponseBody
    public RestResult<NoticeVO> getNoticeList(NoticeListDTO noticeListDTO, @RequestHeader("token") String token){
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }

        List<NoticeVO> noticeList = noticeService.selectNotice(noticeListDTO.getCurrentPage(), noticeListDTO.getPageCount());
        Long total = noticeService.getCount();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", noticeList);

        return RestResult.success().data(map);

    }

    @Operation(summary = "获取系统通知信息", description = "根据系统通知的id获取详细信息", tags = {"notice"})
    @GetMapping(value = "/getnoticeinfo")
    @ResponseBody
    public RestResult<NoticeVO> getNoticeInfo(@RequestHeader("token") String token, Long noticeId) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }


        NoticeVO notice = noticeService.getNoticeInfoById(noticeId);
        return RestResult.success().data(notice);
    }

    @Operation(summary = "系统通知搜索", description = "模糊搜索系统通知", tags = {"notice"})
    @GetMapping(value = "/searchnotice")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> searchNotice(String noticeName, Long currentPage, Long pageCount, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }

        Map<String, Object> map = noticeService.searchNoticeByName(noticeName, currentPage, pageCount);
        return RestResult.success().data(map);

    }

    @Operation(summary = "获取详细的通知列表", description = "用来做后台通知列表展示", tags = {"notice"})
    @GetMapping(value = "/getnoticeinfolist")
    @ResponseBody
    public RestResult<NoticeInfoVO> getNoticeInfoList(@RequestHeader("token") String token,NoticeListDTO noticeListDTO){

        Administrator sessionAdmin = adminService.getAdminByToken(token);
        if (sessionAdmin == null) {
            return RestResult.fail().message("token验证失败");
        }
        List<NoticeInfoVO> noticeList = noticeService.selectNoticeInfo(noticeListDTO.getCurrentPage(), noticeListDTO.getPageCount());
        Long total = noticeService.getCount();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", noticeList);

        return RestResult.success().data(map);

    }

    @Operation(summary = "发布通知", description = "发布通知", tags = {"notice"})
    @PostMapping(value = "/publishnotice")
    @ResponseBody
    public RestResult<String> publishNotice(@RequestHeader("token") String token, @RequestBody NoticePublishDTO noticePublishDTO) {
        Administrator sessionAdmin = adminService.getAdminByToken(token);
        if (sessionAdmin == null) {
            return RestResult.fail().message("token验证失败");
        }
        Long adminId = sessionAdmin.getAdminId();
        RestResult<String> restResult = null;
        Notice notice = new Notice();
        notice.setSenderId(adminId);
        notice.setNoticeContent(noticePublishDTO.getNoticeContent());
        notice.setNoticeName(noticePublishDTO.getNoticeName());

        restResult = noticeService.addNotice(notice);

        return restResult;
    }

    @Operation(summary = "删除通知", description = "根据通知id删除通知", tags = {"notice"})
    @GetMapping(value = "/deletenotice")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> deleteNotice(Long noticeId, Long currentPage, Long pageCount) {

        Map<String, Object> map = noticeService.deleteNotice(noticeId, currentPage, pageCount);
        return RestResult.success().data(map);
    }

    @Operation(summary = "批量删除通知", description = "根据通知id批量删除通知", tags = {"notice"})
    @GetMapping(value = "/deletenoticebatch")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> deleteNoticeBatch(String noticeIdList, Long currentPage, Long pageCount) {

        String[] noticeIdArray = noticeIdList.split(",");
        List<Long> noticeIdLongList = new ArrayList<>();

        for (String noticeIdStr : noticeIdArray) {
            noticeIdLongList.add(Long.parseLong(noticeIdStr));
        }
        Map<String, Object> map = noticeService.deleteNoticeBatch(noticeIdLongList, currentPage, pageCount);
        return RestResult.success().data(map);
    }

    @Operation(summary = "模糊查询通知", description = "根据通知标题模糊查询通知", tags = {"notice"})
    @GetMapping(value = "/searchnoticeinfobyname")
    @ResponseBody
    public RestResult<NoticeInfoVO> searchNoticeInfoByName(@RequestHeader("token") String token, NoticeInfoListDTO noticeInfoListDTO){

        Administrator sessionAdmin = adminService.getAdminByToken(token);
        if (sessionAdmin == null) {
            return RestResult.fail().message("token验证失败");
        }
        Map<String, Object> map = noticeService.searchNoticeInfoByName(noticeInfoListDTO.getNoticeName(), noticeInfoListDTO.getCurrentPage(), noticeInfoListDTO.getPageCount());


        return RestResult.success().data(map);

    }

    @Operation(summary = "更新通知", description = "根据通知id修改通知信息", tags = {"notice"})
    @GetMapping(value = "/updatenotice")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> updateNotice(NoticeUpdateDTO noticeUpdateDTO, Long currentPage, Long pageCount) {

        NoticeInfoVO noticeInfoVO = new NoticeInfoVO();
        noticeInfoVO.setNoticeContent(noticeUpdateDTO.getNoticeContent());
        noticeInfoVO.setNoticeName(noticeUpdateDTO.getNoticeName());
        noticeInfoVO.setUpdateTime(DateUtil.getCurrentTime());
        Map<String, Object> map = noticeService.updateNotice(noticeUpdateDTO.getNoticeId(), noticeInfoVO, currentPage, pageCount);
        return RestResult.success().data(map);
    }
}
