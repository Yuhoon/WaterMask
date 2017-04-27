# WaterMask 
## 水印Demo 
### 本示例提供了水印工具类，支持拍照后添加水印，仅供参考
![](https://github.com/Yuhoon/WaterMask/blob/master/app/src/main/res/drawable/demo.gif)

### 工具类使用：
```
  //初始化水印工具
   waterMaskHelper = new WaterMaskHelper(this, this, this);
   
   //启用拍照
   waterMaskHelper.startCapture();
```

### 对外接口:
```
   //拍照后调用，返回照片uri
   void onChoose(ArrayList<String> photos);
   
   //拍照后调用，设置水印基本参数
   WaterMaskParam onDraw();
   
    @Override
    public void onChoose(ArrayList<String> photos) {
        uris = photos;
        Glide.with(MainActivity.this).load(photos.get(0)).placeholder(R.mipmap.ic_launcher).centerCrop().error(R.mipmap.ic_launcher)
                .crossFade().into(binding.img);
    }


    @Override
    public WaterMask.WaterMaskParam onDraw() {
        // param为null或txt为null时不绘制水印
        WaterMask.WaterMaskParam param = new WaterMask.WaterMaskParam();
        param.txt.add("我是一个小标题");
        param.txt.add(binding.edt.getText().toString().trim());
        param.location = maskLocation;
        param.itemCount = 30;
        return param;
    }

```

