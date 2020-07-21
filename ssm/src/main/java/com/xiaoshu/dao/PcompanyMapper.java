package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Pcompany;
import com.xiaoshu.entity.PcompanyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PcompanyMapper extends BaseMapper<Pcompany> {
    long countByExample(PcompanyExample example);

    int deleteByExample(PcompanyExample example);

    List<Pcompany> selectByExample(PcompanyExample example);

    int updateByExampleSelective(@Param("record") Pcompany record, @Param("example") PcompanyExample example);

    int updateByExample(@Param("record") Pcompany record, @Param("example") PcompanyExample example);
}