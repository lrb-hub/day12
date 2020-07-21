package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.contentcategory;
import com.xiaoshu.entity.contentcategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface contentcategoryMapper extends BaseMapper<contentcategory> {
    long countByExample(contentcategoryExample example);

    int deleteByExample(contentcategoryExample example);

    List<contentcategory> selectByExample(contentcategoryExample example);

    int updateByExampleSelective(@Param("record") contentcategory record, @Param("example") contentcategoryExample example);

    int updateByExample(@Param("record") contentcategory record, @Param("example") contentcategoryExample example);
}