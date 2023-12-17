package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.Review;
import com.disk.file.vo.ReviewVO;

import java.util.List;

public interface ReviewMapper extends BaseMapper<Review> {
    void addReview(Review review);
    List<ReviewVO> selectAllReviewByFileId(Long fileId);
}
