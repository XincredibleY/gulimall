package pengstore.tk.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pengstore.tk.common.utils.PageUtils;
import pengstore.tk.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author jiapeng
 * @email jiape123456@gmail.com
 * @date 2020-07-28 13:30:46
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

