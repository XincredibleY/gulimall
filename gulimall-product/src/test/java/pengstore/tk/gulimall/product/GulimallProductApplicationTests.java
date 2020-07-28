package pengstore.tk.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pengstore.tk.gulimall.product.entity.BrandEntity;
import pengstore.tk.gulimall.product.service.BrandService;

import java.util.List;
import java.util.Map;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setName("改之");
//        brandEntity.setName("huawei华为");
//        brandService.save(brandEntity);
//        brandService.updateById(brandEntity);
//        System.out.println("保存成功！! ");
//        BrandEntity brandEntity1 = new BrandEntity();
//        brandEntity1.setName("iphone");
//        brandService.save(brandEntity1);
//        List<BrandEntity> brand_id_list = brandService.list(new QueryWrapper<BrandEntity>());
//        brand_id_list.forEach((item)->{
//            System.out.println(item);
//        });
        List<Map<String, Object>> maps = brandService.listMaps(new QueryWrapper<BrandEntity>());
        maps.forEach((item)->{
            System.out.println(item);
        });
    }

}
