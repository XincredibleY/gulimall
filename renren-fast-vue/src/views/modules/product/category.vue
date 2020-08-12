<template>
  <div>
    <el-tree
      :data="menu"
      :props="defaultProps"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandcatId"
      :expand-on-click-node="false"
      draggable
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button v-if="node.level<3" type="text" size="mini" @click="append(data)">Append</el-button>
          <el-button
            v-if="node.childNodes.length==0"
            type="text"
            size="mini"
            @click="remove(node, data)"
          >Delete</el-button>
          <el-button type="text" size="mini" @click="edit(data)">Edit</el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="category">
        <el-form-item label="分类名称" :label-width="formLabelWidth">
          <el-input v-model="category.name" autocomplete="off"></el-input>  
        </el-form-item>
        <el-form-item label="图标" :label-width="formLabelWidth">
          <el-input v-model="category.icon" autocomplete="off"></el-input>  
        </el-form-item>
        <el-form-item label="货品单位" :label-width="formLabelWidth">
          <el-input v-model="category.productUnit" autocomplete="off"></el-input>  
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="chooseMethod()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片等）
// 例如：import 《组件名称》from'《组件路径》';

export default {
  // import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data () {
    // 这里存放数据
    return {
      menu: [],
      title: '',
      appendoredit: '',
      expandcatId: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogFormVisible: false,
      category: {
        catId: null,
        name: '',
        parentCid: 0,
        catLevel: 1,
        showStatus: 1,
        sort: 0,
        icon: '',
        productUnit: ''
      },
      formLabelWidth: '100px',
      maxLevel: 0,
      pCid: [],
      updateNodes: []
    }
  },
  // 计算属性 类似于data概念
  compute: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    getMenu () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(
        ({data}) => {
          console.log('读取菜单数据。。。');
          console.log(data.data);
          this.menu = data.data;
        }
      )
    },
    chooseMethod () {
      if (this.appendoredit === 'append') {
        this.addcategory();
      }
      if (this.appendoredit === 'edit') {
        this.editcategory();
      }
    },
    append (data) {
      this.title = '新增分类';
      this.appendoredit = 'append';
      this.category.name = '';
      this.category.icon = '';
      this.category.productUnit = '';
      this.dialogFormVisible = true;
      this.category.catId = null;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;
      this.category.showStatus = 1;
      this.category.sort = 0;
    },
    addcategory () {
      this.dialogFormVisible = false;
      // console.log(this.category);
      if (this.category.name !== '') {
        this.$http({
          url: this.$http.adornUrl('/product/category/save'),
          method: 'post',
          data: this.$http.adornData(this.category, false)
        }).then(() => {
          this.$message({
            type: 'success',
            message: '添加分类保存成功'
          });
          this.getMenu();
          this.expandcatId = [this.category.parentCid];
        }
        );
      }
    },
    remove (node, data) {
      var ids = [data.catId];
      this.$confirm(`删除分类【${data.name}】?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.getMenu();
          this.expandcatId = [node.parent.data.catId];
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '删除取消!'
        });
      });
      console.log('remove', data, node);
    },
    edit (data) {
      this.title = '编辑分类';
      this.appendoredit = 'edit';
      this.category.catId = data.catId;
      this.dialogFormVisible = true;
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${this.category.catId}`),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        console.log(this.category);
        this.category.name = data.category.name;
        this.category.icon = data.category.icon;
        this.category.productUnit = data.category.productUnit;
        this.category.parentCid = data.category.parentCid;
      })
    },
    editcategory () {
      this.dialogFormVisible = false;
      // 防止data.category中的其他默认值覆盖数据库中的真实值，所以只取这四个需要的值
      var {catId, name, icon, productUnit} = this.category;
      if (this.category.name !== '') {
        this.$http({
          url: this.$http.adornUrl('/product/category/update'),
          method: 'post',
          data: this.$http.adornData({catId, name, icon, productUnit}, false)
        }).then(() => {
          this.$message({
            type: 'success',
            message: '修改分类保存成功'
          });
          this.getMenu();
          this.expandcatId = [this.category.parentCid];
        }
        );
      }
    },
    // 判断节点可否被dropped
    allowDrop (draggingNode, dropNode, type) {
      this.maxLevel = 0;
      // 1. 计算draggingNode下所有子节点中最大层级-draggingNode层级
      this.readDeepestLvl(draggingNode.data);
      let deep = this.maxLevel - draggingNode.data.catLevel;
      // 2. 如果type=next or prev 计算dropNode的level + 第1步结果
      // 3. 如果type=inner 计算dropNode的level + 1 + 第1步结果
      if (type === 'inner') {
        return deep + dropNode.data.catLevel + 1 <= 3;
      } else {
        return deep + dropNode.data.catLevel <= 3;
      }
    },
    // 判断节点树中最深节点的lvl
    readDeepestLvl (node) {
      if (node.children != null && node.children.length > 0) {
        for (let i = 0; i < node.children.length; i++) {
          if (node.children[i].catLevel > this.maxLevel) {
            this.maxLevel = node.children[i].catLevel;
          }
          this.readDeepestLvl(node.children[i]);
        }
      } else {
        if (node.catLevel > this.maxLevel) { this.maxLevel = node.catLevel; }
      }
    },
    // 拖拽成功后的函数
    handleDrop (draggingNode, dropNode, dropType, ev) {
      console.log(draggingNode, dropNode, dropType, ev);
      // 1、当前节点最新的父节点id sibling
      let siblings = null;
      let pCid = 0;
      if (dropType === 'inner') {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      } else {
        pCid = dropNode.data.parentCid;
        siblings = dropNode.parent.childNodes;
      }
      this.pCid.push(pCid);

      // 2、当前拖拽节点的最新顺序，还有sort, 还有lvl
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId === draggingNode.data.catId) {
          let catLevel = draggingNode.level;
          if (siblings[i].data.catLevel !== siblings[i].level) {
            catLevel = siblings[i].level;
            this.changeNodesLvl(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel
          });
        } else {
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i
          });
        }
      }

      this.$http({
        url: this.$http.adornUrl('/product/category/update/sort'),
        method: 'post',
        data: this.$http.adornData(this.updateNodes, false)
      }).then(() => {
        this.$message({
          type: 'success',
          message: '拖拽数据保存成功'
        });
      });
      this.getMenu();
      this.expandcatId = this.pCid;
      this.pCid = [];
      this.updateNodes = [];
    },
    // 改变节点树所有节点的lvl
    changeNodesLvl (node) {
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          this.updateNodes.push({
            catId: node.childNodes[i].data.catId,
            catLevel: node.childNodes[i].level
          })
          this.changeNodesLvl(node.childNodes[i]);
        }
      }
    }
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created () {
    this.getMenu()
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted () {},
  beforeCreate () {}, // 生命周期 - 创建之前
  beforeMount () {}, // 生命周期 - 挂载之前
  beforeUpdate () {}, // 生命周期 - 更新之前
  updated () {}, // 生命周期 - 更新之后
  beforeDestory () {}, // 生命周期 - 销毁之前
  destoryed () {}, // 生命周期 - 销毁完成
  activated () {} // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>
</style>