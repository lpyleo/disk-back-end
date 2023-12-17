package com.disk.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.common.RestResult;
import com.disk.file.mapper.NoticeMapper;
import com.disk.file.model.Notice;
import com.disk.file.service.NoticeService;
import com.disk.file.util.DateUtil;
import com.disk.file.vo.NoticeInfoVO;
import com.disk.file.vo.NoticeVO;
import com.disk.file.vo.UserDeptInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    @Override
    public List<NoticeVO> selectNotice(Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeVO> noticeList = noticeMapper.selectNotice(beginCount, pageCount);
        return noticeList;
    }

    @Override
    public NoticeVO getNoticeInfoById(Long noticeId){
        NoticeVO notice = noticeMapper.getNoticeInfo(noticeId);
        return notice;
    }

    @Override
    public Long getCount(){
        return noticeMapper.selectCount();
    }

    @Override
    public Map<String, Object> searchNoticeByName(String noticeName, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeVO> noticeList;
        Long total;
        noticeList = noticeMapper.selectNoticeByName(noticeName, beginCount, pageCount);
        total = noticeMapper.selectCountByName(noticeName);
        Map<String, Object> map = new HashMap<>();
        map.put("list", noticeList);
        map.put("total", total);
        return map;
    }

    @Override
    public List<NoticeInfoVO> selectNoticeInfo(Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeInfoVO> noticeList = noticeMapper.selectNoticeInfo(beginCount, pageCount);
        return noticeList;
    }

    @Override
    public RestResult<String> addNotice(Notice notice){
        String noticeContent = notice.getNoticeContent();
        String noticeName = notice.getNoticeName();

        if (!StringUtils.hasLength(noticeContent)){
            return RestResult.fail().message("通知内容不能为空！");
        }
        if (!StringUtils.hasLength(noticeName)){
            return RestResult.fail().message("通知标题不能为空！");
        }

        notice.setDeleteFlag(0);
        notice.setSendTime(DateUtil.getCurrentTime());
        notice.setUpdateTime(DateUtil.getCurrentTime());
        int result = noticeMapper.insert(notice);

        if (result == 1) {
            return RestResult.success();
        } else {
            return RestResult.fail().message("发布通知失败，请检查输入信息！");
        }
    }

    @Override
    public Map<String, Object> deleteNotice(Long noticeId, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeInfoVO> list;
        Long total;

        noticeMapper.deleteNotice(noticeId);
        list = noticeMapper.selectNoticeInfo(beginCount, pageCount);
        total = noticeMapper.selectCount();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> deleteNoticeBatch(List<Long> noticeIdList, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeInfoVO> list;
        Long total;

        for(int i = 0; i < noticeIdList.size(); i++){
            noticeMapper.deleteNotice(noticeIdList.get(i));
        }

        list = noticeMapper.selectNoticeInfo(beginCount, pageCount);
        total = noticeMapper.selectCount();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> searchNoticeInfoByName(String noticeName, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeInfoVO> noticeList;
        Long total;
        noticeList = noticeMapper.selectNoticeInfoByName(noticeName, beginCount, pageCount);
        total = noticeMapper.selectCountByName(noticeName);
        Map<String, Object> map = new HashMap<>();
        map.put("list", noticeList);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> updateNotice(Long noticeId, NoticeInfoVO noticeInfoVO, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<NoticeInfoVO> list;
        Long total;

        noticeMapper.updateNotice(noticeId, noticeInfoVO);
        list = noticeMapper.selectNoticeInfo(beginCount, pageCount);
        total = noticeMapper.selectCount();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }
}
