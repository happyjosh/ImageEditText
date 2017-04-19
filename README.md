# 支持图片的编辑器

>* 支持本地图片
>* 支持网络图片
>* 支持附件（独立的样式）

使用方式

```java
compile 'com.jph:imageedittextlib:1.1.3'
```

```java
edt.insertNetImage(new NetPic(url));

edt.insertLocalImage(new LocalPic(imgPath));

edt.insertExtra(new Extra(id, "这是测试帖子" + id));

//插入多种数据
List list = new ArrayList();
String url = "http://xxx.jpg";
String imgPath = Environment.getExternalStorageDirectory().getPath() + "/test.jpg";
for (int i = 0; i < 10; i++) {
    list.add(new NetPic(url));
    list.add(new LocalPic(imgPath));
    list.add("普通文字普通文字");
    list.add(new Extra(i, "这是测试帖子" + i));
}
edt.setPatches(list);

```