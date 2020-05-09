package kl.springboot.demo.dao;

import java.util.List;
import kl.springboot.demo.entity.Sysuser;
import kl.springboot.demo.entity.SysuserExample;
import org.apache.ibatis.annotations.Param;

public interface SysuserMapper {
    int countByExample(SysuserExample example);

    int deleteByExample(SysuserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(Sysuser record);

    int insertSelective(Sysuser record);

    /**
     * 查询全部用户
     * @param example
     * @return
     */
    List<Sysuser> selectByExample(SysuserExample example);

    Sysuser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") Sysuser record, @Param("example") SysuserExample example);

    int updateByExample(@Param("record") Sysuser record, @Param("example") SysuserExample example);

    int updateByPrimaryKeySelective(Sysuser record);

    int updateByPrimaryKey(Sysuser record);
}