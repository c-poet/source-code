package tk.mybatis.mapper.additional.update.force;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * @author qrqhuangcy
 * @Description: 通用Mapper接口, 非空字段强制更新
 * @date 2018-06-26
 */
@RegisterMapper
public interface UpdateByPrimaryKeySelectiveForceMapper<T> {

    /**
     * 根据主键更新属性不为null的值, 指定的属性(null值)会被强制更新
     *
     * @param record
     * @param forceUpdateProperties
     * @return
     */
    @UpdateProvider(type = UpdateByPrimaryKeySelectiveForceProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelectiveForce(@Param("record") T record, @Param("forceUpdateProperties") List<String> forceUpdateProperties);
}
