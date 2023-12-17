package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.Notice;
import com.disk.file.vo.NoticeInfoVO;
import com.disk.file.vo.NoticeVO;
import com.disk.file.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper extends BaseMapper<Notice> {
    Long selectCount();
    List<NoticeVO> selectNotice(Long beginCount, Long pageCount);
    NoticeVO getNoticeInfo(@Param("noticeId") Long noticeId);
    List<NoticeVO> selectNoticeByName(String noticeName, Long beginCount, Long pageCount);
    Long selectCountByName(String noticeName);
    List<NoticeInfoVO> selectNoticeInfo(Long beginCount, Long pageCount);
    void insertNotice(Notice notice);
    void deleteNotice(Long noticeId);
    List<NoticeInfoVO> selectNoticeInfoByName(String noticeName, Long beginCount, Long pageCount);
    void updateNotice(Long noticeId, NoticeInfoVO noticeInfoVO);
}
