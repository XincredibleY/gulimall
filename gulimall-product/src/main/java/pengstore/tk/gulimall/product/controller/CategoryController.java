package pengstore.tk.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pengstore.tk.gulimall.product.entity.CategoryEntity;
import pengstore.tk.gulimall.product.service.CategoryService;
import pengstore.tk.common.utils.PageUtils;
import pengstore.tk.common.utils.R;



/**
 * 商品三级分类
 *
 * @author jiapeng
 * @email jiape123456@gmail.com
 * @date 2020-07-28 01:50:36
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表，显示所有商品的三级分类，并组装成树形图
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R list(){
        // 查询所有分类
        List<CategoryEntity> list = categoryService.listWithTree();
        //以树形结构组装分类
        return R.ok().put("data",list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 对应前端拖拽功能的批量修改
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){

        // 1. 查看删除的有没有外键依赖

        // categoryService.removeByIds(Arrays.asList(catIds));

        // 2. 逻辑删除
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
