# WaterfallListview
cn.bitlove.waterfalllistview.widget.WaterfallListview 实现了瀑布流加载数据，他继承自ListView

提供了beforeRefresh,doRefresh两个接口供数据处理用

注意：**调用完doRefresh 函数后，记得调用completeRefresh函数来清空刷新状态**
