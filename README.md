# ** AvatarLabelView **

一个可配置的迷你版 Label 辅助类，支持多种配置效果，具体不同配置展示效果如下图。

<div><img src=".picture/demo.png" width="350"></div>

# ** 说明文档 **

** 使用样例 **

```xml

```

** 已实现类说明 **

|类别|类名|说明|
|--|--|--|
|library|LabelViewHelper|标签辅助核心实现类|
|library|LabelView|基于 LabelViewHelper 实现的一个纯标签 View，可嵌套在 ViewGroup 中使用等|
|demo|LabelImageView|基于 LabelViewHelper 实现的一个具备标签的 ImageView，可属性配置等|
|demo|LabelLinearLayout|基于 LabelViewHelper 实现的一个具备标签的 LinearLayout，可属性配置等|
|customer|XxxView|类比上面 demo 中基于 LabelViewHelper 实现自己的 Label View|

** 属性说明 **

** 拓展为自己 View 使用方式 **

1. 在自己的自定义 View 构造方法创建 LabelViewHelper 对象。
2. 在自己的自定义 View 相关绘制方法（onDraw、dispatchDraw 等）中调用 LabelViewHelper 的 drawLabel 方法。
3. 至此你的自定义 View 就支持可配置的 Label 效果了，如需别的拓展可以参考 demo 或者查看 LabelViewHelper 其他方法。

# ** License 声明 **

MIT License

Copyright (c) 2016 yanbo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.