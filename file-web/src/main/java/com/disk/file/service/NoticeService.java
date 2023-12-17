package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.common.RestResult;
import com.disk.file.model.Notice;
import com.disk.file.vo.NoticeInfoVO;
import com.disk.file.vo.NoticeVO;

import java.util.List;
import java.util.Map;

public interface NoticeService extends IService<Notice> {
    List<NoticeVO> selectNotice(Long currentPage, Long pageCount);
    NoticeVO getNoticeInfoById(Long noticeId);
    Long getCount();
    Map<String, Object> searchNoticeByName(String noticeName, Long currentPage, Long pageCount);
    List<NoticeInfoVO> selectNoticeInfo(Long currentPage, Long pageCount);
    RestResult<String> addNotice(Notice notice);
    Map<String, Object> deleteNotice(Long noticeId, Long currentPage, Long pageCount);
    Map<String, Object> deleteNoticeBatch(List<Long> noticeIdList, Long currentPage, Long pageCount);
    Map<String, Object> searchNoticeInfoByName(String noticeName, Long currentPage, Long pageCount);
    Map<String, Object> updateNotice(Long noticeId, NoticeInfoVO noticeInfoVO, Long currentPage, Long pageCount);
}
