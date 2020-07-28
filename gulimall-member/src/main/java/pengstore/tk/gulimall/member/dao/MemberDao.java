package pengstore.tk.gulimall.member.dao;

import pengstore.tk.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author jiapeng
 * @email jiape123456@gmail.com
 * @date 2020-07-28 13:43:52
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
