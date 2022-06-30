<template>
  <div class="move-dialog">
    <el-skeleton :rows="3" animated v-if="skeletonFlag" />
    <el-tree
      :data="folderList"
      :props="defaultProps"
      :default-expanded-keys="['-1']"
      node-key="id"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
      v-else
    >
      <template #default="{ node }">
        <span class="custom-tree-node">
          <svg style="width: 20px; height: 20px">
            <use xlink:href="#icon-wenjianjia"></use>
          </svg>
          <div class="text">{{ node.label }}</div>
        </span>
      </template>
    </el-tree>

    <div class="btn">
      <el-button @click="commit" size="small">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { getTree } from "@/api/request";
import { notice } from "@/utils/notice";
import { updateParentIdAndLevel } from "@/api/request";
export default defineComponent({
  props: {
    moveItemList: {
      type: Array,
    },
  },
  emits: ["moveResult"],
  setup(props, context) {
    const folderList = ref<any[]>([
      {
        id: "-1",
        name: "user",
        level: -1,
        children: [],
      },
    ]);

    const defaultProps = {
      label: "name",
      children: "children",
    };
    const skeletonFlag = ref(true);

    const currentNode = ref<any>();
    const files = computed(() => {
      const list: string[] = [];
      props.moveItemList?.forEach((item: any) => {
        if (!item.folder) {
          list.push(item.id as string);
        }
      });
      return list;
    });

    const folders = computed(() => {
      const list: string[] = [];
      props.moveItemList?.forEach((item: any) => {
        if (item.folder) {
          list.push(item.id as string);
        }
      });
      return list;
    });

    const handleNodeClick = (TreeNode: any) => {
      currentNode.value = TreeNode;
    };

    const commit = async () => {
      if (isChildren(currentNode.value.id)) {
        notice("warning", "警告", "目标文件夹是源文件夹的子文件夹");
        return;
      }
      if ((props.moveItemList as any[])[0].parent_id === currentNode.value.id) {
        return;
      }
      const jsonData = {
        parentId: currentNode.value.id as string,
        levelFrom: (props.moveItemList as any[])[0].level as number,
        levelTo: (currentNode.value.level as number) + 1,
        files: files.value,
        folders: folders.value,
      };
      const data = await updateParentIdAndLevel(jsonData);
      if (data != null) {
        if ((data as any).code === 0) {
          context.emit("moveResult", "success");
        } else {
          context.emit("moveResult", "error");
        }
      }
    };

    const isChildren = (selectId: string) => {
      let flag = false;
      function handle(child: string, parent: string) {
        const queue = [];
        queue.push(folderList.value[0]);
        let parentItem: any;
        if (child === parent) {
          flag = true;
          return;
        }
        while (queue.length > 0) {
          for (let i = 0; i < queue[0].children.length; i++) {
            const item: any = queue[0].children[i];
            if (item.id === parent) {
              parentItem = item;
              break;
            } else if (item.id === child) {
              flag = false;
              return;
            }
            queue.push(item);
          }
          if (parentItem != undefined && parentItem != null) {
            break;
          }
          queue.splice(0, 1);
        }
        if (parentItem != undefined && parentItem != null) {
          const queueParent = [];
          queueParent.push(parentItem);
          while (queueParent.length > 0) {
            for (let i = 0; i < queueParent[0].children.length; i++) {
              if (queueParent[0].children[i].id === child) {
                flag = true;
                return;
              }
              queueParent.push(queueParent[0].children[i]);
            }
            queueParent.splice(0, 1);
          }
        }
      }
      for (let i = 0; i < folders.value.length; i++) {
        handle(selectId, folders.value[i]);
      }
      return flag;
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      const data = await getTree();
      if (data != null) {
        if ((data as any).code === 0) {
          console.log(data.data);
          folderList.value[0].children = data.data;
        }
      }
      skeletonFlag.value = false;
    });

    return {
      folderList,
      defaultProps,
      commit,
      handleNodeClick,
      skeletonFlag,
    };
  },
});
</script>


<style lang="scss" scoped>
.move-dialog {
  padding: 10px;
  min-height: 200px;
  position: relative;
  .el-tree {
    margin-bottom: 25px;
    .custom-tree-node {
      display: flex;
      .text {
        margin-left: 5px;
      }
    }
  }

  .btn {
    position: absolute;
    bottom: 10px;
    left: 216px;
  }
}
</style>