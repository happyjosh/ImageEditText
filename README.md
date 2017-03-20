# 支持图片的编辑器

>* 支持图片
>* 支持附件（独立的样式）

使用方式

```java
compile 'com.jph:imageedittextlib:1.0.0' 
```

```java
edt.insertLocalImage(new LocalPic(imgPath));

edt.insertExtra(new Extra(id, "这是测试帖子" + id));
```