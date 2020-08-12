package pengstore.tk.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pengstore.tk.common.utils.PageUtils;
import pengstore.tk.common.utils.Query;

import pengstore.tk.gulimall.product.dao.CategoryDao;
import pengstore.tk.gulimall.product.entity.CategoryEntity;
import pengstore.tk.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 找出分类父子结构
        List<CategoryEntity> level1Menus = categoryEntities.stream().
                filter(categoryEntity -> categoryEntity.getParentCid() == 0).
                map(menu -> {
                    menu.setChildren(getChildrens(menu,categoryEntities));
                    return menu;
                }).
                sorted(Comparator.comparingInt(CategoryEntity::getSort)).
                collect(Collectors.toList());
        // 找到所有一级分类
        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查当前要删除的菜单是否和其他表单相关联

        // 逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    // 在所有CategoryEntity寻找root的children
    public List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> childrenEntities = all.stream().
                filter(entity -> entity.getParentCid() == root.getCatId()).
                map(menu -> {
                    menu.setChildren(getChildrens(menu,all));
                    return menu;
                }).
                sorted(Comparator.comparingInt(CategoryEntity::getSort)).
                collect(Collectors.toList());
        return childrenEntities;
    }

}