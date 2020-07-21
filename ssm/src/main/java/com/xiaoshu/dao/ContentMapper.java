package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.contentVo;

public interface ContentMapper extends BaseMapper<Content> {

	List<contentVo> findPageinfo(contentVo contentVo);
}